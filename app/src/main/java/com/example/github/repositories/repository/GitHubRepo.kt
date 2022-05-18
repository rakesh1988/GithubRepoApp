package com.example.github.repositories.repository

import com.example.github.repositories.data.api.GitHubEndpoints
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.data.remotemodel.Response
import com.example.github.repositories.data.remotemodel.UserDTO
import retrofit2.Retrofit
import javax.inject.Inject

class GitHubRepo @Inject constructor(
    retrofit: Retrofit
) {
    private val service: GitHubEndpoints = retrofit.create(GitHubEndpoints::class.java)

    suspend fun searchRepositories(query: String, sort: String, order: String): Response {
        return service.searchRepositories(query, sort, order)
    }

    suspend fun getUser(username: String): UserDTO {
        return service.getUser(username)
    }

    suspend fun getUserRepositories(reposUrl: String): List<RepositoryDTO> {
        return service.getUserRepositories(reposUrl)
    }
}