package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.Constants.KEY
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getNetworkAsteroids(
        @Query ("API_KEY") key: String = "DEMO_KEY"
    ): Deferred<String>

    @GET("planetary/apod")
    fun getPicture(
        @Query("api_key") key: String = KEY
    ): Deferred<PictureOfDay>
}

object AsteroidApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val retrofitService = retrofit.create(AsteroidApiService::class.java)

}