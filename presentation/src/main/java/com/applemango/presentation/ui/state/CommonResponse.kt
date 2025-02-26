package com.applemango.presentation.ui.state

sealed class CommonResponse {
    data class Success<T>(val code :Int, val body : T) : CommonResponse()
    data class Failed(val code : Int, val message : String) : CommonResponse() {
        companion object {
            private const val CODE_DEFAULT_FAILED = 999
            private const val MESSAGE_DEFAULT_FAILED = "Unknown Error"

            fun getDefaultFailed(message: String?): Failed {
                return Failed(
                    CODE_DEFAULT_FAILED,
                    message ?: MESSAGE_DEFAULT_FAILED
                )
            }
        }
    }
    object Loading : CommonResponse()
    object Empty : CommonResponse()
}