package com.applemango.runnerbe.data.repositoryimpl

import com.applemango.runnerbe.data.network.api.GetRunningTalkDetailApi
import com.applemango.runnerbe.domain.repository.RunningTalkRepository
import com.applemango.runnerbe.data.network.api.GetRunningTalkMessagesApi
import com.applemango.runnerbe.data.network.api.MessageSendApi
import com.applemango.runnerbe.data.network.request.SendMessageRequest
import com.applemango.runnerbe.presentation.state.CommonResponse
import javax.inject.Inject

class RunningTalkRepositoryImpl @Inject constructor(
    private val getRunningTalkMessagesApi: GetRunningTalkMessagesApi,
    private val getRunningTalkDetailApi: GetRunningTalkDetailApi,
    private val sendMessagesApi: MessageSendApi
) : RunningTalkRepository {
    override suspend fun getRunningTalks(): CommonResponse {
        return try {
            val response = getRunningTalkMessagesApi.getMessages()
            if(response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(response.body()?.code?:response.code(),response.body()?.message?:response.message())
            }
        }catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999,e.message?:"error")
        }
    }

    override suspend fun getRunningTalkDetail(roomId: Int): CommonResponse {
        return try {
            val response = getRunningTalkDetailApi.getRunningTalkDetail(roomId)
            if(response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(response.body()?.code?:response.code(),response.body()?.message?:response.message())
            }
        }catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999,e.message?:"error")
        }
    }

    override suspend fun sendMessage(roomId: Int, content: String): CommonResponse {
        return try {
            val response = sendMessagesApi.sendMessage(roomId, SendMessageRequest(content))
            if(response.isSuccessful && response.body() != null && response.body()!!.isSuccess) {
                CommonResponse.Success(response.body()!!.code, response.body()!!)
            } else {
                CommonResponse.Failed(response.body()?.code?:response.code(),response.body()?.message?:response.message())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            CommonResponse.Failed(999,e.message?:"error")
        }
    }
}