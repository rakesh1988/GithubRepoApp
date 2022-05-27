package com.example.github.repositories.presentation.landing

import com.example.github.repositories.data.remotemodel.RepositoryDTO

class RepositoryTransformer {
    fun transformRepoListToRepoItemDto(repositoryDTO: RepositoryDTO, position: Int): RepoItemDto =
        RepoItemDto(
            title = "# ${position + 1}: ${repositoryDTO.full_name}".uppercase(),
            description = beautifyDescription(repositoryDTO.description.orEmpty()),
            author = repositoryDTO.owner?.login.orEmpty(),
        )


    private fun beautifyDescription(description: String): String =
        if (description.length > 150) {
            description.take(150).plus("...")
        } else description

}