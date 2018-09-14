package com.gpetuhov.android.samplemoshi.model

import com.squareup.moshi.Json

// In models, that will be serialized / deserialized,
// it is advisable to always provide properties with their JSON names,
// even if they are the same as the names of the properties.
data class User(
        @Json(name = "name") val name: String,
        @Json(name = "age") val age: Int
)