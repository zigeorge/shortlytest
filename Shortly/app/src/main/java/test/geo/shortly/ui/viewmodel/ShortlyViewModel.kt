package test.geo.shortly.ui.viewmodel

import androidx.lifecycle.*
import test.geo.shortly.other.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import test.geo.shortly.other.NetworkConnection
import test.geo.shortly.other.Resource
import test.geo.shortly.other.ShortlyUtil
import test.geo.shortly.repositories.ShortlyRepository
import test.geo.shortly.ui.LinkError
import javax.inject.Inject

@HiltViewModel
class ShortlyViewModel @Inject constructor(
    private val repository: ShortlyRepository,
    private val networkConnection: NetworkConnection
) : ViewModel() {

    /* getting the flow of shortLink from repository */
    private val _allLinks = repository.getLinkHistory()

    /* observing the flow of shortLink as live data*/
    val linkHistory = _allLinks.asLiveData()

    /* Defined _addShortLinkStatus to observe the status when user tries to shorten a link */
    private val _addShortLinkStatus = MutableLiveData<Event<Resource<ShortLinkResponse>>>()
    val addShortLinkStatus: LiveData<Event<Resource<ShortLinkResponse>>> = _addShortLinkStatus

    /*
    * Tries to add a shortLink and updates the status of the process to the view using livedata and
    * viewModel scopes
    * */
    fun addShortLink(link: String?) {
        if (!networkConnection.hasConnection) {
            _addShortLinkStatus.postValue(Event(Resource.error(LinkError.NO_INTERNET.msg, null)))
            return
        }
        if (link.isNullOrEmpty()) {
            _addShortLinkStatus.postValue(Event(Resource.error(LinkError.EMPTY_LINK.msg, null)))
            return
        }
        if (ShortlyUtil.isInvalidLink(link)) {
            _addShortLinkStatus.postValue(Event(Resource.error(LinkError.INVALID_LINK.msg, null)))
            return
        }

        _addShortLinkStatus.postValue(Event(Resource.loading(null)))

        viewModelScope.launch {
            val response = repository.insertLink(link.trim())

            if (response.ok) {
                repository.insertLink(
                    ShortLink(
                        shortLink = response.result!!.full_short_link,
                        origin = response.result.original_link
                    )
                )
                _addShortLinkStatus.postValue(Event(Resource.success(response)))
            } else {
                _addShortLinkStatus.postValue(Event(Resource.error(response.error, null)))
            }
        }
    }

    /* Delete a shortened link
    * @param shortLink: ShortLink is the link user intends to delete
    * */
    fun deleteLnk(shortLink: ShortLink) = viewModelScope.launch {
        repository.deleteLink(shortLink)
    }

}