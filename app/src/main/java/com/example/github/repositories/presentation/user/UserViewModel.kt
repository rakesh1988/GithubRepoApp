package com.example.github.repositories.presentation.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.data.remotemodel.UserDTO
import com.example.github.repositories.repository.GitHubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val gitHubRepo: GitHubRepo
) : ViewModel() {

    val user = MutableLiveData<UserDTO>()
    val repositories = MutableLiveData<List<RepositoryDTO>>()

    fun fetchUser(username: String) {
        // FIXME Use the proper scope
        viewModelScope.launch {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = gitHubRepo.getUser(username)
                user.value = response
            } catch (e: Throwable) {
                // TODO: handle error
            }
        }
    }

    fun fetchRepositories(reposUrl: String) {
        viewModelScope.launch {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = gitHubRepo.getUserRepositories(reposUrl)
                repositories.value = response
            } catch (e: Throwable) {
                // TODO: handle error
            }
        }
    }
}