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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ArtistDetailViewModel (application: Application, artistId: Int) : AndroidViewModel(application) {
    private val artistRepository = ArtistRepository(application)

    private val _artist = MutableLiveData<Artist>()
    val artist: LiveData<Artist> get() = _artist

    private val _eventNetworkError = MutableLiveData(false)
    val eventNetworkError: LiveData<Boolean> get() = _eventNetworkError

    private val _isNetworkErrorShown = MutableLiveData(false)

    init {
        fetchArtistDetail(artistId)
    }

    private fun fetchArtistDetail(artistId: Int) {
        // Usamos viewModelScope para ejecutar la tarea en una corutina
        viewModelScope.launch {
            try {
                // Llamamos a la función suspensiva en el repositorio para obtener el detalle del álbum
                val fetchedArtist = artistRepository.getArtistDetail(artistId)
                // Formateamos la fecha
                val formattedAlbum = fetchedArtist.copy(birthDate = formatReleaseDate(fetchedArtist.birthDate))
                // Actualizamos el LiveData con los datos
                _artist.postValue(formattedAlbum)
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

    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    class Factory(private val app: Application, private val artistId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArtistDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArtistDetailViewModel(app, artistId) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}