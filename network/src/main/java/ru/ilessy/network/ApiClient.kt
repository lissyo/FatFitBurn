package ru.ilessy.network

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

class ApiClient {

    private val client: OkHttpClient = OkHttpClient.Builder().build()

    operator fun invoke(request: Request, callback: Callback): Call {
        val call = client.newCall(request)
        call.enqueue(callback)
        return call
    }

}