package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.nasa.gov/"
private const val KEY = "VCFPeljtVL2u3tpHOQviVpO95ve7UQQ3N3tVed7j"


private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


interface AsteroidApiService {
    @GET("neo/rest/v1/feed")
    fun getNetworkAsteroids(
        @Query ("START_DATE") start: String = "2015-09-07",
        @Query ("END_DATE") end: String = "2015-09-08",
        @Query ("API_KEY") key: String = "DEMO_KEY"
    ): Deferred<String>
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