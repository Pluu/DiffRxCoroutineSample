package com.pluu.diffrxcoroutinesample.data

import com.pluu.diffrxcoroutinesample.data.model.User
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface GitHubService {
    @GET("/users/Pluu")
    fun getUser(): Single<User>

    @GET("/error")
    fun tryNetworkError(): Maybe<Any?>

    @GET("/users/Pluu")
    suspend fun suspendGetUser(): User

    @GET("/error")
    suspend fun suspendTryNetworkError(): Any?
}
