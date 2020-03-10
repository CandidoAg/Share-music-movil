package com.example.share_music_movil.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class playlistModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is playlist Fragment"
    }
    val text: LiveData<String> = _text
}