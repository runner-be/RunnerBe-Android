package com.applemango.runnerbe.presentation.screen.fragment.chat.detail.image.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageDetailViewModelImpl @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), ImageDetailViewModel {

    private val _title = MutableStateFlow(savedStateHandle.get<String>("title")?:"")
    override val title: StateFlow<String> = _title.asStateFlow()

    private val _imageList = MutableStateFlow<List<ImageDetailUiState>>(listOf())
    override val imageList: StateFlow<List<ImageDetailUiState>> = _imageList.asStateFlow()

    private val _currentPageNumber = MutableStateFlow(savedStateHandle.get<Int>("pageNumber")?:0)
    override val currentPageNumber: StateFlow<Int> =_currentPageNumber.asStateFlow()

    private val _actions = MutableSharedFlow<ImageDetailAction>()
    override val actions: SharedFlow<ImageDetailAction> = _actions.asSharedFlow()

    init {
        savedStateHandle.get<Array<String>>("images")?.let {
            viewModelScope.launch {
                _imageList.emit(it.map { image -> ImageDetailUiState(image) })
            }
        }
    }

    override fun backClicked() {
        viewModelScope.launch {
            _actions.emit(ImageDetailAction.MoveToBack)
        }
    }

    override fun leftMoveClicked() {
        val page = currentPageNumber.value
        val listSize = imageList.value.size
        viewModelScope.launch {
            _currentPageNumber.emit(if(page == 0) listSize - 1 else page - 1)
        }
    }

    override fun rightMoveClicked() {
        val page = currentPageNumber.value
        val listSize = imageList.value.size
        viewModelScope.launch {
            _currentPageNumber.emit(if(page == listSize -1) 0 else page + 1)
        }
    }
}