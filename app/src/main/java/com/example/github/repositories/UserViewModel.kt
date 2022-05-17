package com.example.github.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    retrofit: Retrofit
) : ViewModel() {

    private val service: GitHubEndpoints = retrofit.create(GitHubEndpoints::class.java)

    val user = MutableLiveData<UserDTO>()
    val repositories = MutableLiveData<List<RepositoryDTO>>()

    fun fetchUser(username: String) {
        // FIXME Use the proper scope
        viewModelScope.launch(Dispatchers.IO) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            val response = service.getUser(username).execute()
            user.postValue(response.body()!!)
        }
    }

    fun fetchRepositories(reposUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            val response = service.getUserRepositories(reposUrl).execute()
            repositories.postValue(response.body()!!)
        }
    }
}