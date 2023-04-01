package com.pluu.diffrxcoroutinesample.data

import logcat.logcat
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object DataSource {
    private val client = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor { message ->
                logcat { message }
            }.apply {
                this.setLevel(HttpLoggingInterceptor.Level.BASIC)
            }
        )
        .build();

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.github.com/")
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: GitHubService by lazy {
        retrofit.create(GitHubService::class.java)
    }
}