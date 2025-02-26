package com.applemango.presentation.ui.screen.fragment.map.address

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.applemango.domain.usecaseImpl.post.SearchAddressByKeywordUseCase
import com.applemango.presentation.ui.mapper.AddressMapper
import com.applemango.presentation.ui.model.AddressModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunningAddressSearchViewModel @Inject constructor(
    private val searchAddressByKeywordUseCase: SearchAddressByKeywordUseCase,
    private val addressMapper: AddressMapper
) : ViewModel() {
    private val _addressResultFlow = MutableStateFlow<PagingData<AddressModel>>(PagingData.empty())
    val addressResultFlow : Flow<PagingData<AddressModel>> = _addressResultFlow
        .asStateFlow()
        .cachedIn(viewModelScope)

    fun getAddressSearchResultList(keyword: String) {
        viewModelScope.launch {
            searchAddressByKeywordUseCase(keyword)
                .collectLatest { addressResult ->
                    val result = addressResult.map { addressMapper.mapToPresentation(it) }
                    _addressResultFlow.emit(result)
                }
        }
    }
}