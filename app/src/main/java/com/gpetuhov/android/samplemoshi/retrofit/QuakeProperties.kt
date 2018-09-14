package com.gpetuhov.android.samplemoshi.retrofit

import com.squareup.moshi.Json

data class QuakeProperties(
    @Json(name = "place") val location: String,
    @Json(name = "mag") val magnitude: Double
)