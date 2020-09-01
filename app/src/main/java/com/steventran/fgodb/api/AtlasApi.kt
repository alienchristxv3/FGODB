package com.steventran.fgodb.api

import com.steventran.fgodb.Servant
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface AtlasApi {
    @GET("export/NA/basic_servant.json")
    fun getServants(): Call<List<Servant>>

    @GET("nice/NA/servant/{servantID}")
    fun getDetailedServant(@Path("servantID") servantCollectionNo: Int)
}