package com.applemango.runnerbe.presentation.model

import com.applemango.runnerbe.R

enum class CreatorImageAndPosition {
    EUNSEO,
    JEONG,
    DURU,
    NIAKA,
    JUDY,
    SHEAVE,
    JOY,
    CHANHO;

    val creatorName get() = when(this) {
        EUNSEO -> R.string.eunseo
        JEONG -> R.string.jeong
        DURU -> R.string.duru
        NIAKA -> R.string.niaka
        JUDY -> R.string.judy
        SHEAVE -> R.string.sheave
        JOY -> R.string.joy
        CHANHO -> R.string.chanho
    }

    val position get() = when(this) {
        EUNSEO -> "Plan"
        JEONG -> "Design"
        DURU, NIAKA, JUDY -> "AOS"
        SHEAVE, JOY -> "iOS"
        CHANHO -> "Server"
    }

    val imageResource get() = when(this) {
        EUNSEO -> R.drawable.ic_eunseo
        JEONG -> R.drawable.ic_jeong
        DURU -> R.drawable.ic_duru
        NIAKA -> R.drawable.ic_niaka
        JUDY -> R.drawable.ic_judy
        SHEAVE -> R.drawable.ic_sheave
        JOY -> R.drawable.ic_joy
        CHANHO -> R.drawable.ic_chanho
    }
}