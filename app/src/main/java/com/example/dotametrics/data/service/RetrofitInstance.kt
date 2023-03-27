package com.example.dotametrics.data.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private var retrofit: Retrofit? = null

    private var BASE_URL = "https://api.opendota.com/api/"

    fun getService(): DotaService {

        if (retrofit == null)
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        return retrofit!!.create(DotaService::class.java)
    }
}