package com.example.app_vinilos_g17.ui.albums

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AlbumsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Albums Fragment"
    }
    val text: LiveData<String> = _text
}