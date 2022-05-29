package com.example.github.repositories.presentation.user

import com.example.github.repositories.repository.GitHubRepo
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val gitHubRepo: GitHubRepo
) {
    suspend operator fun invoke(username: String) =
        gitHubRepo.getUser(username)
}
