package kr.devkyu.data.network.api

import com.applemango.runnerbe.BuildConfig
import kr.devkyu.data.dto.KakaoAddressDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface GetAddressResultListApi {

    @GET("https://dapi.kakao.com/v2/local/search/keyword.json")
    suspend fun getAddressList(
        @Header("Authorization") authorization: String? = "KakaoAK ${BuildConfig.REST_API_KEY}",
        @Query("query") query: String,
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 15,
        @Query("sort") sort: String? = "accuracy"
    ) : Response<kr.devkyu.data.dto.KakaoAddressDto>
}