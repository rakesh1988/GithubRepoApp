package com.example.github.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    retrofit: Retrofit
) : ViewModel() {

    private val service: GitHubEndpoints = retrofit.create(GitHubEndpoints::class.java)

    val repositories = MutableLiveData<List<RepositoryDTO>>()

    fun fetchItems() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: Response?
            withContext(Dispatchers.IO) {
                response = service.searchRepositories(QUERY, SORT, ORDER).execute().body()
            }
            repositories.value = response?.items
        }
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.Main) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            var response: Response?
            withContext(Dispatchers.IO) {
                response = service.searchRepositories(QUERY, SORT, ORDER).execute().body()
            }
            repositories.value = response?.items
        }
    }
}