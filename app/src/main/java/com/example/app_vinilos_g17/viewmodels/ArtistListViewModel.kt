package com.example.app_vinilos_g17.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_vinilos_g17.models.Artist
import com.example.app_vinilos_g17.repositories.ArtistRepository
import kotlinx.coroutines.launch

class ArtistListViewModel(application: Application): AndroidViewModel(application) {

    companion object {
        private const val TAG = "ArtistListViewModel"
    }

    private val artistRepository = ArtistRepository(application)

    private val _artists = MutableLiveData<List<Artist>>()
    private val _isLoading = MutableLiveData(true)

    val artists: LiveData<List<Artist>>
        get() = _artists

    val isLoading: LiveData<Boolean> get() = _isLoading


    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val fetchedArtistList = artistRepository.getArtists()
                _artists.postValue(fetchedArtistList)

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
            if (modelClass.isAssignableFrom(ArtistListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistListViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}