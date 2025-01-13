package com.applemango.runnerbe.data.mapperImpl

import com.applemango.runnerbe.data.mapper.WriteRunningMapper
import com.applemango.runnerbe.data.network.request.WriteRunningRequest
import com.applemango.runnerbe.usecaseImpl.post.WritePostUseCase.WriteRunningParam

class WriteRunningMapperImpl: WriteRunningMapper {
    override fun mapToData(input: WriteRunningParam): WriteRunningRequest {
        return WriteRunningRequest(
            runningTitle = input.runningTitle,
            gatheringTime = input.gatheringTime,
            runningTime = input.runningTime,
            latitude = input.latitude,
            longitude = input.longitude,
            placeName = input.placeName,
            placeAddress = input.placeAddress,
            placeExplain = input.placeExplain,
            runningTag = input.runningTag,
            minAge = input.minAge,
            maxAge = input.maxAge,
            numberOfRunner = input.numberOfRunner,
            contents = input.contents,
            gender = input.gender,
            paceGrade = input.paceGrade,
            isAfterParty = input.isAfterParty
        )
    }

    override fun mapToDomain(input: WriteRunningRequest): WriteRunningParam {
        return WriteRunningParam(
            runningTitle = input.runningTitle,
            gatheringTime = input.gatheringTime,
            runningTime = input.runningTime,
            latitude = input.latitude,
            longitude = input.longitude,
            placeName = input.placeName,
            placeAddress = input.placeAddress,
            placeExplain = input.placeExplain,
            runningTag = input.runningTag,
            minAge = input.minAge,
            maxAge = input.maxAge,
            numberOfRunner = input.numberOfRunner,
            contents = input.contents,
            gender = input.gender,
            paceGrade = input.paceGrade,
            isAfterParty = input.isAfterParty
        )
    }
}