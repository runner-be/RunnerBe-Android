package com.applemango.runnerbe.util

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import java.io.IOException
import java.util.*

object AddressUtil {
    // 좌표 -> 주소 변환
    fun getAddress(context: Context, lat: Double, lng: Double): String {
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addressResult = "검색되지 않는 지역이에요."
        try {
            //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
            //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geoCoder.getFromLocation(lat, lng, 2) { addressList ->
                    if (addressList.size > 0) {
                        val currentLocationAddress =
                            "${if (addressList[0].locality == null) addressList[0].subLocality
                            else addressList[0].locality?:""} ${addressList[0].thoroughfare?:""}"
                        addressResult = currentLocationAddress
                    }
                }
            } else {
                val addressList: List<Address> = geoCoder.getFromLocation(lat, lng, 2) as List<Address>
                if (addressList.isNotEmpty()) {
                    val currentLocationAddress =
                        "${if (addressList[0].locality == null) addressList[0].subLocality
                        else addressList[0].locality?:""} ${addressList[0].thoroughfare?:""}"
                    addressResult = currentLocationAddress
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressResult
    }

    fun getAddressSimpleLine(context: Context, lat: Double, lng: Double): String {
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addressResult = "검색되지 않는 지역이에요."
        runCatching {
            val address = geoCoder.getFromLocation(lat, lng, 2) as ArrayList<Address>
            addressResult = address[0].getAddressLine(0)
        }.onFailure {
            it.printStackTrace()
        }

        return addressResult
    }
}