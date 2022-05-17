package com.example.github.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.ORDER
import com.example.github.repositories.data.QUERY
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.SORT
import com.example.github.repositories.repository.GitHubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gitHubRepo: GitHubRepo
) : ViewModel() {
    private val TAG = "MainViewModel"
    val repositories = MutableLiveData<List<RepositoryDTO>>()

    fun fetchItems() {
        viewModelScope.launch() {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = gitHubRepo.searchRepositories(QUERY, SORT, ORDER)
                repositories.value = response.items
            } catch (e: Throwable) {
                Log.e(TAG, "error", e)
                // TODO: handle error
            }
        }
    }

    fun refresh() {
        fetchItems()
    }
}