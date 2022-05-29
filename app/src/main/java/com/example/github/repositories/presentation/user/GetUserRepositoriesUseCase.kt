package com.example.github.repositories.presentation.user

import com.example.github.repositories.repository.GitHubRepo
import javax.inject.Inject

class GetUserRepositoriesUseCase @Inject constructor(
    private val gitHubRepo: GitHubRepo
) {
    suspend operator fun invoke(reposUrl: String) =
        gitHubRepo.getUserRepositories(reposUrl)
}
