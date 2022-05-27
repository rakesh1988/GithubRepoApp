package com.example.github.repositories.data

import com.example.github.repositories.data.remotemodel.RepositoryDTO

object BookmarkDataStore {
    private val bookmarks = hashSetOf<Int>() // storing just int is enough as repository DTO is big

    fun bookmarkRepo(repoId: Int, shouldBookmark: Boolean) {
        if (shouldBookmark)
            bookmarks.add(repoId)
        else
            bookmarks.remove(repoId)
    }

    fun isRepoBookmarked(repoId: Int): Boolean {
        return bookmarks.contains(repoId)
    }
}

@Deprecated("this has a better implementation as [BookmarkDataStore]")
class LocalDataStore private constructor() {

    companion object {
        val instance = LocalDataStore()
    }

    private val bookmarks = mutableMapOf<Int, RepositoryDTO>()

    fun bookmarkRepo(repositoryDTO: RepositoryDTO, bookmarked: Boolean) {
        if (bookmarked)
            bookmarks[repositoryDTO.id] = repositoryDTO
        else
            bookmarks.remove(repositoryDTO.id)
    }

    fun getBookmarks() = bookmarks.values
}