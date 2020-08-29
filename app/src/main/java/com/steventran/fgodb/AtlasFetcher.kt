package com.steventran.fgodb

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.steventran.fgodb.api.AtlasApi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "AtlasFetcher"

class AtlasFetcher {

    private val atlasApi: AtlasApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://api.atlasacademy.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        atlasApi = retrofit.create(AtlasApi::class.java)
    }

    fun fetchServants(): LiveData<List<Servant>> {
        val responseLiveData: MutableLiveData<List<Servant>> = MutableLiveData()
        val atlasRequest: Call<List<Servant>> = atlasApi.getServants()

        atlasRequest.enqueue(object: Callback<List<Servant>> {
            override fun onFailure(call: Call<List<Servant>>, t: Throwable) {
                Log.e(TAG, "Failure to fetch servants", t)
            }

            override fun onResponse(
                call: Call<List<Servant>>,
                response: Response<List<Servant>>
            ) {
                Log.d(TAG, "Fetched servants")
                var servantResponse: List<Servant>? = response.body()
                servantResponse = servantResponse?.filter { servant ->
                    listOf("heroine", "normal").contains(servant.type)
                }
                responseLiveData.value = servantResponse
            }

        })

        return responseLiveData
    }

    @WorkerThread
    fun fetchServantPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = atlasApi.getUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream().use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }
}