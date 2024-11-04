package com.example.app_vinilos_g17.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ArtistsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Artists Fragment"
    }
    val text: LiveData<String> = _text
}