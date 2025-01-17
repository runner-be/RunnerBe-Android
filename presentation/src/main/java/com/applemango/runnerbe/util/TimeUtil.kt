package com.applemango.runnerbe.util

object TimeUtil {

    fun getAMAndPMTo24(isAm: Boolean, hour: Int) : Int {
        return if(!isAm) {
            if(hour >= 12) hour else hour + 12
        } else hour
    }

}