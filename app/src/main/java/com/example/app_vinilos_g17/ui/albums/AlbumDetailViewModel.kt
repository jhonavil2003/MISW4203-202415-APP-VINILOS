package com.example.app_vinilos_g17.ui.albums

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.repositories.AlbumDetailRepository

class AlbumDetailViewModel(application: Application, albumId: Int) : AndroidViewModel(application) {

    private val albumRepository = AlbumDetailRepository(application)

    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album> get() = _album

    private val _eventNetworkError = MutableLiveData<Boolean>(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean> get() = _isNetworkErrorShown

    init {
        fetchAlbumDetails(albumId)
    }

    private fun fetchAlbumDetails(albumId: Int) {
        albumRepository.getAlbumDetail(albumId, { fetchedAlbum ->
            _album.postValue(fetchedAlbum)
            _eventNetworkError.value = false
        }, { error ->
            Log.d("NetworkError", error.toString())
            _eventNetworkError.value = true
        })
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(private val app: Application, private val albumId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumDetailViewModel(app, albumId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
