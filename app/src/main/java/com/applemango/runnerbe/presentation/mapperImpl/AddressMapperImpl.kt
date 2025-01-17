package com.applemango.runnerbe.presentation.mapperImpl

import com.applemango.runnerbe.entity.AddressEntity
import com.applemango.runnerbe.presentation.mapper.AddressMapper
import com.applemango.runnerbe.presentation.model.AddressModel
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