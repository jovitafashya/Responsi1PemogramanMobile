package com.example.bmkggempa.data.model

import com.google.gson.annotations.SerializedName

data class GempaResponse(@SerializedName("Infogempa") val infoGempa: InfoGempa)
data class InfoGempa(val gempa: List<Gempa>)
data class Gempa(
    @SerializedName("Tanggal") val tanggal: String,
    @SerializedName("Jam") val jam: String,
    @SerializedName("Coordinates") val coordinates: String,
    @SerializedName("Magnitude") val magnitude: String,
    @SerializedName("Kedalaman") val kedalaman: String,
    @SerializedName("Wilayah") val wilayah: String,
    @SerializedName("Potensi") val potensi: String
)
