package com.applemango.presentation.util

object NumberUtil {

    //시작부터 끝까지 step 단위로 값을 가지는 리스트 생성
    fun getUnitNumber(start: Int, end: Int, step: Int) : List<String> =
        (start..end step step).map { if(it in 0..9) "0$it" else it.toString()}

    fun getRange(start: Int, end: Int) : List<String> =
        IntRange(start, end).map { it.toString() }
}