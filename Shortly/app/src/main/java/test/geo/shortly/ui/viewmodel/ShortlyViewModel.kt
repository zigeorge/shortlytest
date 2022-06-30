package test.geo.shortly.ui.viewmodel

import androidx.lifecycle.*
import com.androiddevs.shoppinglisttestingyt.other.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import test.geo.shortly.other.NetworkConnection
import test.geo.shortly.other.Resource
import test.geo.shortly.other.ShortlyUtil
import test.geo.shortly.repositories.ShortlyRepository
import test.geo.shortly.ui.LinkError
import test.geo.shortly.ui.model.LinkHistoryListItem
import javax.inject.Inject

@HiltViewModel
class ShortlyViewModel @Inject constructor(
    private val repository: ShortlyRepository,
    private val networkConnection: NetworkConnection
) : ViewModel() {

    val allLink = repository.getLinkHistory().asLiveData()
    val linkHistory = MutableLiveData<List<LinkHistoryListItem>>()

    private var copiedLink: LinkHistoryListItem? = null

    private val _addShortLinkStatus = MutableLiveData<Event<Resource<ShortLinkResponse>>>()
    val addShortLinkStatus: LiveData<Event<Resource<ShortLinkResponse>>> = _addShortLinkStatus

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
            val response = repository.insertLink(link)

            if (response.ok) {
                _addShortLinkStatus.postValue(Event(Resource.success(response)))
            } else {
                _addShortLinkStatus.postValue(Event(Resource.error(response.error, null)))
            }
        }
    }

    fun deleteLnk(shortLink: ShortLink) = viewModelScope.launch {
        repository.deleteLink(shortLink)
    }

    fun linkHistoryUpdated(copiedLink: LinkHistoryListItem? = null) {
        if (copiedLink != null)
            this.copiedLink = copiedLink
        val allLink = allLink.value
        if (allLink.isNullOrEmpty()) return
        val linkHistory = LinkHistoryListItem.fromAllLinks(allLink, this.copiedLink)
        this.linkHistory.postValue(linkHistory)
    }

}