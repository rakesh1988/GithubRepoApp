package com.example.github.repositories.data.api

import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.data.remotemodel.Response
import com.example.github.repositories.data.remotemodel.UserDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface GitHubEndpoints {
    @GET("search/repositories")
    suspend fun searchRepositories(
        @Query("q") q: String,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Response

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): UserDTO

    @GET
    suspend fun getUserRepositories(
        @Url userRepo: String
    ): MutableList<RepositoryDTO>
}