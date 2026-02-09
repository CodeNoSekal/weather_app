package com.polyhub.weather

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface Api{

    @GET("/data/2.5/weather")
    suspend fun getWeather(
        @Query("lat")
        latitude: Double,
        @Query("lon")
        longitude: Double,
        @Query("units")
        units: String = "metric",
        @Query("lang")
        lang: String = "ru"
    ): ApiResponse

}

object RetrofitClient{
    private const val BASE_URL = "https://api.openweathermap.org"
    const val API_KEY = ""

    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(ApiKeyInterceptor())
        .addInterceptor(logging)
        .build()

    val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val api: Api = retrofit.create(Api::class.java)
}

