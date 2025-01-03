package com.applemango.runnerbe.presentation.screen.fragment.map.address

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.applemango.runnerbe.domain.usecase.post.SearchAddressByKeywordUseCase
import com.applemango.runnerbe.presentation.state.CommonResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningAddressSearchViewModel @Inject constructor(
    private val searchAddressByKeywordUseCase: SearchAddressByKeywordUseCase
) : ViewModel() {
    private val _addressResultFlow = MutableStateFlow<PagingData<AddressResult>>(PagingData.empty())
    val addressResultFlow : Flow<PagingData<AddressResult>> = _addressResultFlow
        .asStateFlow()
        .cachedIn(viewModelScope)

    fun getAddressSearchResultList(keyword: String) {
        viewModelScope.launch {
            searchAddressByKeywordUseCase(keyword)
                .collectLatest { addressResult ->
                    if (addressResult is CommonResponse.Success<*> && addressResult.body is PagingData<*>) {
                        Log.e("Address search", " : addressResult is CommonResponse.Success")
                        _addressResultFlow.emit(addressResult.body as PagingData<AddressResult>)
                    }
                }
        }
    }
}