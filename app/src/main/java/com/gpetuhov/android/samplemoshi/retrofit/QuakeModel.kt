package com.gpetuhov.android.samplemoshi.retrofit

import com.squareup.moshi.Json


data class QuakeModel(
    @Json(name = "properties") val quakeProperties: QuakeProperties
)