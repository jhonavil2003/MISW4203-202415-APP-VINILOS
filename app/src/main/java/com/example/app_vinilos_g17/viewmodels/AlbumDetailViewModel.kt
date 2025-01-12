package com.example.app_vinilos_g17.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.app_vinilos_g17.models.Album
import com.example.app_vinilos_g17.repositories.AlbumDetailRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlbumDetailViewModel(application: Application, albumId: Int) : AndroidViewModel(application) {

    private val albumRepository = AlbumDetailRepository(application)

    private val _album = MutableLiveData<Album>()
    val album: LiveData<Album> get() = _album

    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData(false)

    init {
        fetchAlbumDetails(albumId)
    }

    private fun fetchAlbumDetails(albumId: Int) {
        // Usamos viewModelScope para ejecutar la tarea en una corutina
        viewModelScope.launch {
            try {
                // Llamamos a la función suspensiva en el repositorio para obtener el detalle del álbum
                val fetchedAlbum = albumRepository.getAlbumDetail(albumId)
                // Formateamos la fecha
                val formattedAlbum = fetchedAlbum.copy(releaseDate = formatReleaseDate(fetchedAlbum.releaseDate))
                // Actualizamos el LiveData con los datos
                _album.postValue(formattedAlbum)
                // Indicamos que no hubo error
                _eventNetworkError.value = false
            } catch (error: Exception) {
                // En caso de error, mostramos un mensaje y actualizamos el estado de error
                Log.d("NetworkError", error.toString())
                _eventNetworkError.value = true
            }
        }
    }

    private fun formatReleaseDate(dateString: String): String {
        val originalFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val desiredFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return try {
            val date: Date = originalFormat.parse(dateString)!!
            desiredFormat.format(date)
        } catch (e: Exception) {
            dateString // Devuelve la fecha original si hay un error
        }
    }

    fun setTrack(albumId: Int, album: Map<String, String>) {
        viewModelScope.launch {
            try {
                val albumCreated =  albumRepository.setTrackAlbum(albumId, album)
                _eventNetworkError.value = false
            } catch (e: Exception) {
                Log.e("Track", "Error adding album", e)
                _eventNetworkError.value = true
            }
        }
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
