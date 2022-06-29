package test.geo.shortly.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androiddevs.shoppinglisttestingyt.other.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import test.geo.shortly.data.local.ShortLink
import test.geo.shortly.data.remote.responses.ShortLinkResponse
import test.geo.shortly.other.NetworkConnection
import test.geo.shortly.other.ShortlyUtil
import test.geo.shortly.other.Resource
import test.geo.shortly.repositories.ShortlyRepository
import javax.inject.Inject

@HiltViewModel
class ShortlyViewModel @Inject constructor(
    private val repository: ShortlyRepository,
    private val networkConnection: NetworkConnection
): ViewModel() {

    val linkHistory = repository.getLinkHistory()

    private val _addShortLinkStatus = MutableLiveData<Event<Resource<ShortLinkResponse>>>()
    val addShortLinkStatus:LiveData<Event<Resource<ShortLinkResponse>>> = _addShortLinkStatus

    fun addShortLink(link: String?) {
        if (!networkConnection.hasConnection) {
            _addShortLinkStatus.postValue(Event(Resource.error("No Internet Connection", null)))
            return
        }
        if(link.isNullOrEmpty()) {
            _addShortLinkStatus.postValue(Event(Resource.error("Empty Link", null)))
            return
        }
        if(ShortlyUtil.isInvalidLink(link)) {
            _addShortLinkStatus.postValue(Event(Resource.error("Invalid Link", null)))
            return
        }

        _addShortLinkStatus.postValue(Event(Resource.loading(null)))

        viewModelScope.launch {
            val response = repository.insertLink(link)

            if(response.ok) {
                _addShortLinkStatus.postValue(Event(Resource.success(response)))
            } else {
                _addShortLinkStatus.postValue(Event(Resource.error(response.error, null)))
            }
        }
    }

    fun deleteLnk(shortLink: ShortLink) = viewModelScope.launch {
        repository.deleteLink(shortLink)
    }

}