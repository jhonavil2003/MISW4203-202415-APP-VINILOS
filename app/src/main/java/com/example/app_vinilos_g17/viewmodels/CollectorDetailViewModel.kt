package com.example.app_vinilos_g17.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.models.CollectorAlbum
import com.example.app_vinilos_g17.repositories.CollectorsRepository
import com.example.app_vinilos_g17.repositories.AlbumDetailRepository
import kotlinx.coroutines.launch

class CollectorDetailViewModel(application: Application, collectorId: Int) : AndroidViewModel(application) {

    private val collectorsRepository = CollectorsRepository(application)
    private val albumDetailRepository = AlbumDetailRepository(application)

    private val _collector = MutableLiveData<Collector>()
    val collector: LiveData<Collector> get() = _collector

    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData(false)

    init {
        fetchCollectorDetail(collectorId)
    }

    private fun fetchCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            try {
                val collectorDetail = collectorsRepository.getCollectorDetail(collectorId)
                collectorDetail.collectorAlbums = fetchAlbumNames(collectorDetail.collectorAlbums)
                _collector.postValue(collectorDetail)

                _eventNetworkError.value = false
            } catch (e: Exception) {
                Log.e("NetworkError", "Error al obtener el detalle del coleccionista: ${e.message}")
                _eventNetworkError.value = true
            }
        }
    }

    private suspend fun fetchAlbumNames(collectorAlbums: List<CollectorAlbum>): List<CollectorAlbum> {
        collectorAlbums.forEach { album ->
            try {
                val albumDetail = albumDetailRepository.getAlbumDetail(album.id)
                album.albumName = albumDetail.name
            } catch (e: Exception) {
                Log.e("NetworkError", "Error al obtener el nombre del álbum: ${e.message}")
            }
        }
        return collectorAlbums
    }

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(private val app: Application, private val collectorId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CollectorDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CollectorDetailViewModel(app, collectorId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
