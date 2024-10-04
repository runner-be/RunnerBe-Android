package com.applemango.runnerbe.data.network.request

import com.google.gson.annotations.SerializedName

data class PatchUserPaceRegisterRequest(@SerializedName("pace") val pace: String)
