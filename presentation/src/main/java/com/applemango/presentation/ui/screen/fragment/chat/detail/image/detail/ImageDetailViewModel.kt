package com.applemango.presentation.ui.screen.fragment.chat.detail.image.detail

import android.net.Uri
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModel @Inject constructor(): ViewModel() {
    private val _imageList: MutableStateFlow<List<Uri>> = MutableStateFlow(emptyList())
    val imageList: StateFlow<List<Uri>> get() = _imageList.asStateFlow()

    fun updateImageList(list: List<Uri>) {
        _imageList.value = list
    }
}