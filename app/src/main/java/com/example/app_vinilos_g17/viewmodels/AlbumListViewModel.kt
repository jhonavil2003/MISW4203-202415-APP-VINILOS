package com.example.app_vinilos_g17.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_vinilos_g17.models.AlbumList
import com.example.app_vinilos_g17.repositories.AlbumListRepository
import kotlinx.coroutines.launch

class AlbumListViewModel(application: Application)  : AndroidViewModel(application) {

    companion object {
        private const val TAG = "AlbumListViewModel"
    }

    private val albumListRepository = AlbumListRepository(application)

    private val _albums = MutableLiveData<List<AlbumList>>()
    private val _isLoading = MutableLiveData(true)

    val albums: LiveData<List<AlbumList>>
        get() = _albums

    val isLoading: LiveData<Boolean> get() = _isLoading


    private var _eventNetworkError = MutableLiveData(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val fetchedAlbumList = albumListRepository.getAlbumList()
                _albums.postValue(fetchedAlbumList)

                _eventNetworkError.value = false
                _isLoading.value = false
            }
            catch (error: Exception) {
                Log.d(TAG, error.toString())
                _eventNetworkError.value = true
            }
        }
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AlbumListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AlbumListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}