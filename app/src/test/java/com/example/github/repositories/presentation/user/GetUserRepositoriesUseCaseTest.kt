package com.example.github.repositories.presentation.user

import com.example.github.repositories.data.remotemodel.RepositoryDTO
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
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.io.IOException

@ExperimentalCoroutinesApi
@ExtendWith(value = [MockKExtension::class, InstantExecutorExtension::class])
class GetUserRepositoriesUseCaseTest {
    private lateinit var sut: GetUserRepositoriesUseCase

    @RelaxedMockK
    lateinit var gitHubRepo: GitHubRepo

    companion object {
        private val getUserRepositoriesSuccessResponse: Array<RepositoryDTO> = TestUtil.readJson(
            GetUserRepositoriesUseCaseTest::class.java.classLoader,
            "users-repo-repositories-response.json",
            Array<RepositoryDTO>::class.java
        )
    }

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        sut = GetUserRepositoriesUseCase(gitHubRepo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when getUserRepositories is successful, response with items is returned`() = runTest {
        //given
        val expectedResult = getUserRepositoriesSuccessResponse.toList()
        coEvery {
            gitHubRepo.getUserRepositories("")
        } returns getUserRepositoriesSuccessResponse.toList()

        val actualResult = sut.invoke("")
        //then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getUserRepositories API throws exception, exception will be thrown by use case`() =
        runTest {
            val expectedException = IOException()
            //given
            coEvery {
                gitHubRepo.getUserRepositories("")
            } throws expectedException

            // when
            runCatching {
                sut.invoke("")
            }.onSuccess {
                fail()
            }.onFailure {
                assertEquals(expectedException, it)
            }
            Unit
        }

}