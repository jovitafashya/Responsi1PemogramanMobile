package com.example.bmkggempa.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: BmkgApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://data.bmkg.go.id/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BmkgApiService::class.java)
    }
}
