package com.gpetuhov.android.samplemoshi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gpetuhov.android.samplemoshi.model.User
import kotlinx.android.synthetic.main.activity_main.*
import com.squareup.moshi.Moshi
import org.jetbrains.anko.toast
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.gpetuhov.android.samplemoshi.retrofit.QuakeService
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.android.Main
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // To JSON
        textView.setOnClickListener {
            // Create new user
            val user = User("Bob", 25)

            // Get Moshi adapter
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<User>(User::class.java)

            // Convert user to Json
            val json = jsonAdapter.toJson(user)

            textView.text = json
        }

        // From JSON
        textView.setOnLongClickListener {
            val json = textView.text.toString()

            // Get Moshi adapter
            val moshi = Moshi.Builder().build()
            val jsonAdapter = moshi.adapter<User>(User::class.java)

            val user2: User?

            // Moshi will throw exception in case of malformed JSON
            // (if we try to get user from "Hello, world!" string)
            user2 = try {
                jsonAdapter.fromJson(json)
            } catch (e: Exception) {
                null
            }

            toast(user2?.name ?: "null")

            true
        }

        button.setOnClickListener {
            val okHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor(
                            HttpLoggingInterceptor().setLevel(
                                    HttpLoggingInterceptor.Level.BASIC
                            )
                    )
                    .build()

            // To use Moshi converter with Retrofit in Kotlin, we have 2 options:
            // 1. Use reflection
            // In this case add converter like this:
            // .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
            // 2. Use code generation
            // In this case add converter simply like this
            // .addConverterFactory(MoshiConverterFactory.create())
            // And also add @JsonClass(generateAdapter = true) to Kotlin classes.
            // In this example we will use the second option.
            val retrofit = Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl("https://earthquake.usgs.gov/fdsnws/event/1/")
                    .addConverterFactory(MoshiConverterFactory.create())
                    .build()

            val quakeService = retrofit.create(QuakeService::class.java)

            // In Kotlin 1.3 we should start coroutines like this
            GlobalScope.launch(Dispatchers.Default, CoroutineStart.DEFAULT, null, {
                val result = quakeService.getQuakes("geojson", "10").execute()

                launch(Dispatchers.Main) {
                    val quakeResult = result.body()
                    val recentQuakeLocation = quakeResult?.quakeList?.firstOrNull()?.quakeProperties?.location

                    toast(recentQuakeLocation ?: "Error downloading quakes")
                }
            })
        }
    }
}
