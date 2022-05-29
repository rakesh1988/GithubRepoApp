package com.example.github.repositories.presentation.landing

import com.example.github.repositories.data.remotemodel.Response
import com.example.github.repositories.repository.GitHubRepo
import com.example.github.repositories.utils.InstantExecutorExtension
import com.example.github.repositories.utils.TestUtil
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExperimentalCoroutinesApi
@ExtendWith(value = [MockKExtension::class, InstantExecutorExtension::class])
class SearchRepositoryUseCaseTest {
    private lateinit var sut: SearchRepositoryUseCase

    @RelaxedMockK
    lateinit var gitHubRepo: GitHubRepo

    companion object {
        private val searchRepositoriesSuccessResponse: Response = TestUtil.readJson(
            SearchRepositoryUseCaseTest::class.java.classLoader,
            "search-repositories-response.json",
            Response::class.java
        )
    }

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        sut = SearchRepositoryUseCase(gitHubRepo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when searchRepositories is successful, response with items is returned`() = runTest {
        //given
        val expectedResult = searchRepositoriesSuccessResponse.items
        coEvery {
            gitHubRepo.searchRepositories("", "", "")
        } returns searchRepositoriesSuccessResponse
        val actualResult = sut.invoke("", "", "").items
        //then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when searchRepositories API throws exception, exception will be thrown by use case`() =
        runTest {
            val expectedException = IOException()
            //given
            coEvery {
                gitHubRepo.searchRepositories("", "", "")
            } throws expectedException

            // when
            runCatching {
                sut.invoke("", "", "")
            }.onSuccess {
                fail()
            }.onFailure {
                assertEquals(expectedException, it)
            }
            Unit
        }

}