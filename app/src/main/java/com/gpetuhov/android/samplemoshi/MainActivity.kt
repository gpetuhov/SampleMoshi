package com.gpetuhov.android.samplemoshi

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.gpetuhov.android.samplemoshi.model.User
import kotlinx.android.synthetic.main.activity_main.*
import com.squareup.moshi.Moshi
import org.jetbrains.anko.toast


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
    }
}
