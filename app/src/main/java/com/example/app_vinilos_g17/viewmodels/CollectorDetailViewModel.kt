package com.example.app_vinilos_g17.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_vinilos_g17.models.Collector
import com.example.app_vinilos_g17.repositories.CollectorsRepository
import kotlinx.coroutines.launch

class CollectorDetailViewModel(application: Application, collectorId: Int) : AndroidViewModel(application) {

    private val collectorsRepository = CollectorsRepository(application)

    private val _collectorDetail = MutableLiveData<Collector>()
    val collectorDetail: LiveData<Collector> get() = _collectorDetail

    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    init {
        fetchCollectorDetail(collectorId)
    }

    private fun fetchCollectorDetail(collectorId: Int) {
        viewModelScope.launch {
            try {
                // Llamada al repositorio para obtener el detalle del coleccionista
                val collectorDetail = collectorsRepository.getCollectorDetail(collectorId)
                Log.d("CollectorDetailViewModel", "Coleccionista obtenido: ${collectorDetail.name}")
                _collectorDetail.postValue(collectorDetail)
                _eventNetworkError.value = false
            } catch (e: Exception) {
                Log.e("NetworkError", "Error al obtener el detalle del coleccionista: ${e.message}")
                _eventNetworkError.value = true
            }
        }
    }

    // Función para resetear el estado de error después de mostrarlo
    fun onNetworkErrorShown() {
        _eventNetworkError.value = false
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
