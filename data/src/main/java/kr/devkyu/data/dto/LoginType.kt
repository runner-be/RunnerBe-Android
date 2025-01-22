package kr.devkyu.data.dto

enum class LoginType(val value: Int) {
    KAKAO(1),
    NAVER(2);

    companion object {
        fun findByValue(value: Int) : LoginType? {
            val list = values().filter { it.value == value }
            return if(list.isNotEmpty()) list[0]
            else null
        }
    }
}