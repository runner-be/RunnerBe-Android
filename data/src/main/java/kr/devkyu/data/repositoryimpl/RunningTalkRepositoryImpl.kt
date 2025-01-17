package kr.devkyu.data.repositoryimpl

import kr.devkyu.data.mapper.CommonMapper
import kr.devkyu.data.mapper.RunningTalkMessageMapper
import kr.devkyu.data.mapper.RunningTalkRoomMapper
import com.applemango.runnerbe.data.network.api.GetRunningTalkMessagesApi
import com.applemango.runnerbe.data.network.api.GetRunningTalkRoomsApi
import com.applemango.runnerbe.data.network.api.PostMessageReportApi
import com.applemango.runnerbe.data.network.api.PostMessageApi
import com.applemango.runnerbe.data.network.request.MessageReportRequest
import com.applemango.runnerbe.data.network.request.SendMessageRequest
import com.applemango.runnerbe.entity.CommonEntity
import com.applemango.runnerbe.entity.RunningTalkMessagesEntity
import com.applemango.runnerbe.entity.RunningTalkRoomEntity
import com.applemango.runnerbe.repository.RunningTalkRepository
import retrofit2.HttpException
import javax.inject.Inject

class RunningTalkRepositoryImpl @Inject constructor(
    private val getRunningTalkRoomsApi: GetRunningTalkRoomsApi,
    private val getRunningTalkMessagesApi: GetRunningTalkMessagesApi,
    private val sendMessagesApi: PostMessageApi,
    private val postMessageReportApi: PostMessageReportApi,
    private val commonMapper: CommonMapper,
    private val runningTalkRoomMapper: RunningTalkRoomMapper,
    private val runningTalkMessageMapper: RunningTalkMessageMapper,
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

    override suspend fun getRunningTalkRooms(): List<RunningTalkRoomEntity> {
        val response = getRunningTalkRoomsApi.getRunningTalkRooms()
        if (response.isSuccessful) {
            val body = response.body()
            if (body?.isSuccess == true) {
                return body.result.map {
                    runningTalkRoomMapper.mapToDomain(it)
                }
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
                return RunningTalkMessagesEntity(
                    body.roomInfo,
                    body.messages
                )
            } else {
                throw IllegalStateException("Business logic failed: ${body?.message}")
            }
        } else {
            throw HttpException(response)
        }
    }
}