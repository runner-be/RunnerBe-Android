package com.applemango.presentation.ui.mapperImpl

import com.applemango.domain.entity.AddressEntity
import com.applemango.presentation.ui.mapper.AddressMapper
import com.applemango.presentation.ui.model.AddressModel
import javax.inject.Inject

class AddressMapperImpl @Inject constructor(): AddressMapper {
    override fun mapToDomain(input: AddressModel): AddressEntity {
        return AddressEntity(
            placeName = input.placeName,
            roadAddress = input.roadAddress,
            latitude = input.latitude,
            longitude = input.longitude,
            pageNumber = input.pageNumber,
        )
    }

    override fun mapToPresentation(input: AddressEntity): AddressModel {
        return AddressModel(
            placeName = input.placeName,
            roadAddress = input.roadAddress,
            latitude = input.latitude,
            longitude = input.longitude,
            pageNumber = input.pageNumber,
        )
    }
}