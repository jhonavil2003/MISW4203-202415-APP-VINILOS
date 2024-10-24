package com.example.app_vinilos_g17.data.broker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://blackvynils-827885dbcaa3.herokuapp.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface RetrofitApiService {
    @GET("albums")
    fun getProperties():
            Call<String>
}

object RetrofitApi {
    val retrofitService : RetrofitApiService by lazy {
        retrofit.create(RetrofitApiService::class.java) }
}