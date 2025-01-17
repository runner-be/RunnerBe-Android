package com.applemango.runnerbe.presentation.model.type

import com.applemango.runnerbe.R

enum class CreatorImageAndPosition {
    EUNSEO,
    SEUNGWAN,
    JEONG,
    JUHEE,
    NIAKA,
    LOKI,
    JOY,
    CHANGGYU;

    val creatorName get() = when(this) {
        EUNSEO -> R.string.eunseo
        SEUNGWAN -> R.string.seungwan
        JEONG -> R.string.jeong
        JUHEE -> R.string.juhee
        NIAKA -> R.string.niaka
        LOKI -> R.string.loki
        JOY -> R.string.joy
        CHANGGYU -> R.string.changgyu
    }

    val position get() = when(this) {
        EUNSEO -> R.string.position_plan
        SEUNGWAN -> R.string.position_server
        JEONG, JUHEE -> R.string.position_design
        NIAKA, LOKI -> R.string.position_android
        JOY, CHANGGYU -> R.string.position_apple
    }

    val imageResource get() = when(this) {
        EUNSEO -> R.drawable.ic_eunseo
        SEUNGWAN -> R.drawable.ic_wanni
        JEONG -> R.drawable.ic_jeong
        JUHEE -> R.drawable.ic_juhee
        NIAKA -> R.drawable.ic_niaka
        LOKI -> R.drawable.ic_loki
        JOY -> R.drawable.ic_joy
        CHANGGYU -> R.drawable.ic_gogochang
    }
}