package com.example.github.repositories.presentation.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.data.remotemodel.UserDTO
import com.example.github.repositories.shared.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getUserRepositoriesUseCase: GetUserRepositoriesUseCase,
) : ViewModel() {

    private val _user = MutableLiveData<UserDTO>()
    val user: LiveData<UserDTO> = _user

    private val _repositories = MutableLiveData<List<RepositoryDTO>>()
    val repositories: LiveData<List<RepositoryDTO>> = _repositories

    val errorFetchingData = SingleLiveEvent<Unit>()
    val viewModelInitComplete = SingleLiveEvent<Unit>()

    init {
        viewModelInitComplete.call()
    }

    fun fetchUser(username: String) {
        viewModelScope.launch {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = getUserUseCase.invoke(username)
                _user.value = response
            } catch (e: Throwable) {
                errorFetchingData.call()
            }
        }
    }

    fun fetchRepositories(reposUrl: String) {
        viewModelScope.launch {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = getUserRepositoriesUseCase.invoke(reposUrl)
                _repositories.value = response
            } catch (e: Throwable) {
                errorFetchingData.call()
            }
        }
    }
}