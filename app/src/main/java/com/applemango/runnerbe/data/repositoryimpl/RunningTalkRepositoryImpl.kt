package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.data.mapper.CommonMapper
import com.applemango.runnerbe.data.mapper.RunningTalkMessagesMapper
import com.applemango.runnerbe.data.mapper.RunningTalkRoomsMapper
import com.applemango.runnerbe.data.network.api.GetRunningTalkMessagesApi
import com.applemango.runnerbe.data.network.api.GetRunningTalkRoomsApi
import com.applemango.runnerbe.data.network.api.PostMessageReportApi
import com.applemango.runnerbe.data.network.api.PostMessageApi
import com.applemango.runnerbe.data.network.request.MessageReportRequest
import com.applemango.runnerbe.data.network.request.SendMessageRequest
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.RunningTalkMessagesEntity
import com.applemango.runnerbe.entity.RunningTalkRoomsEntity
import com.applemango.runnerbe.repository.RunningTalkRepository
import retrofit2.HttpException
import javax.inject.Inject

class RunningTalkRepositoryImpl @Inject constructor(
    private val getRunningTalkRoomsApi: GetRunningTalkRoomsApi,
    private val getRunningTalkMessagesApi: GetRunningTalkMessagesApi,
    private val sendMessagesApi: PostMessageApi,
    private val postMessageReportApi: PostMessageReportApi,
    private val commonMapper: CommonMapper,
    private val runningTalkRoomsMapper: RunningTalkRoomsMapper,
    private val runningTalkMessagesMapper: RunningTalkMessagesMapper,
) : BaseRepository(), RunningTalkRepository {

    override suspend fun sendMessage(roomId: Int, content: String?, url: String?): CommonEntity {
        return handleApiCall(
            apiCall = {
                sendMessagesApi.sendMessage(roomId, SendMessageRequest(content, url))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun reportMessage(messageIdList: List<Int>): CommonEntity {
        return handleApiCall(
            apiCall = {
                postMessageReportApi.messageReport(MessageReportRequest(
                    messageIdList.joinToString(",")
                ))
            },
            mapResponse = { body ->
                commonMapper.mapToDomain(body)
            }
        )
    }

    override suspend fun getRunningTalkRooms(): RunningTalkRoomsEntity {
        val response = getRunningTalkRoomsApi.getRunningTalkRooms()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return runningTalkRoomsMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }

    override suspend fun getRunningTalkMessages(roomId: Int): RunningTalkMessagesEntity {
        val response = getRunningTalkMessagesApi.getRunningTalkMessages(roomId)
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return runningTalkMessagesMapper.mapToDomain(body)
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}