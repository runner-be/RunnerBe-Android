package com.applemango.runnerbe.presentation.model

enum class RunnerDiligence(val value : String) {
    ERROR_RUNNER("불량러너"),
    SINCERITY_RUNNER("성실러너"),
    EFFORT_RUNNER("노력러너"),
    BEGINNER_RUNNER("초보러너")
}

fun getDiligenceFromString(text: String?): RunnerDiligence =
    when(text) {
        "성실러너" -> RunnerDiligence.ERROR_RUNNER
        "초보러너" -> RunnerDiligence.ERROR_RUNNER
        "노력러너" -> RunnerDiligence.ERROR_RUNNER
        else -> RunnerDiligence.ERROR_RUNNER
    }