package com.example.share_music_movil.ui.fav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class favViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is fav Fragment"
    }
    val text: LiveData<String> = _text
}