package com.example.github.repositories.presentation.user

import com.example.github.repositories.data.remotemodel.UserDTO
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
class GetUserUseCaseTest {
    private lateinit var sut: GetUserUseCase

    @RelaxedMockK
    lateinit var gitHubRepo: GitHubRepo

    companion object {
        private val getUserSuccessResponse: UserDTO = TestUtil.readJson(
            GetUserUseCaseTest::class.java.classLoader,
            "users-repositories-response.json",
            UserDTO::class.java
        )
    }

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        sut = GetUserUseCase(gitHubRepo)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getUser is successful, response with UserDTO is returned`() = runTest {
        //given
        val expectedResult = getUserSuccessResponse
        coEvery {
            gitHubRepo.getUser("")
        } returns getUserSuccessResponse

        val actualResult = sut.invoke("")
        //then
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `when getUser API throws exception, exception will be thrown by use case`() =
        runTest {
            val expectedException = IOException()
            //given
            coEvery {
                gitHubRepo.getUser("")
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