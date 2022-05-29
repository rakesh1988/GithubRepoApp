package com.example.github.repositories.presentation.landing

import com.example.github.repositories.data.remotemodel.Response
import com.example.github.repositories.utils.TestUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RepositoryTransformerTest {

    private val sut: RepositoryTransformer = RepositoryTransformer()

    private val sampleRepositoryDTO: Response by lazy {
        TestUtil.readJson(
            jsonFileName = "search-repositories-response.json",
            resultClass = Response::class.java
        )
    }

    @Test
    fun `title must be shown appropriately with position details`() {
        val randomIndex = (0 until sampleRepositoryDTO.items.size).random()
        val expectedTitle =
            "# ${randomIndex + 1}: ${sampleRepositoryDTO.items[randomIndex].full_name}".uppercase()

        // when
        val result = sut.transformRepoListToRepoItemDto(
            sampleRepositoryDTO.items[randomIndex],
            randomIndex
        )

        // then
        assertEquals(expectedTitle, result.title)
    }

    @Test
    fun `description must show only 150 characters`() {
        val randomIndex = (0 until sampleRepositoryDTO.items.size).random()
        val descriptionText = sampleRepositoryDTO.items[randomIndex].description.orEmpty()

        val expectedDescription = if (descriptionText.length > 150) {
            descriptionText.take(150).plus("...")
        } else descriptionText

        // when
        val result = sut.transformRepoListToRepoItemDto(
            sampleRepositoryDTO.items[randomIndex],
            randomIndex
        )

        // then
        assertEquals(expectedDescription, result.description)

    }

}