package com.example.github.repositories.presentation.landing

import com.example.github.repositories.repository.GitHubRepo
import javax.inject.Inject

class SearchRepositoryUseCase @Inject constructor(
    private val gitHubRepo: GitHubRepo
) {
    suspend operator fun invoke(query: String, sort: String, order: String) =
        gitHubRepo.searchRepositories(query, sort, order)
}
