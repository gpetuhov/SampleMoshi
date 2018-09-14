package com.gpetuhov.android.samplemoshi.retrofit

import com.squareup.moshi.Json


class QuakeResult(
    @Json(name = "features") val quakeList: List<QuakeModel>
)