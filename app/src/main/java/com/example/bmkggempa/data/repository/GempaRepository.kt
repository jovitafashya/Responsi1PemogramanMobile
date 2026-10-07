package com.example.bmkggempa.data.repository

import com.example.bmkggempa.data.model.Gempa
import com.example.bmkggempa.data.remote.BmkgApiService

class GempaRepository(private val api: BmkgApiService) {
    suspend fun getGempaTerkini(): List<Gempa> = api.getGempaTerkini().infoGempa.gempa
}
