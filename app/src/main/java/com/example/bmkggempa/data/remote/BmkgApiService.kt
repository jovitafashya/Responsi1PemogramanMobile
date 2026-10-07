package com.example.bmkggempa.data.remote

import com.example.bmkggempa.data.model.GempaResponse
import retrofit2.http.GET

interface BmkgApiService {
    @GET("DataMKG/TEWS/gempaterkini.json")
    suspend fun getGempaTerkini(): GempaResponse
}
