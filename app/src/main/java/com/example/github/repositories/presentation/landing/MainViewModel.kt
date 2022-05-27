package com.example.github.repositories.presentation.landing

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.ORDER
import com.example.github.repositories.data.QUERY
import com.example.github.repositories.data.SORT
import com.example.github.repositories.data.remotemodel.RepositoryDTO
import com.example.github.repositories.repository.GitHubRepo
import com.example.github.repositories.shared.SingleLiveEvent
import com.example.github.repositories.shared.getLogTag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val gitHubRepo: GitHubRepo
) : ViewModel() {
    private val TAG = getLogTag()

    private val _repositories = MutableLiveData<List<RepositoryDTO>>()
    val repositories = _repositories

    val errorFetchingData = SingleLiveEvent<Unit>()

    init {
        fetchItems()
    }

    fun fetchItems() {
        viewModelScope.launch {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = gitHubRepo.searchRepositories(QUERY, SORT, ORDER)
                _repositories.value = response.items
            } catch (e: Throwable) {
                Log.e(TAG, "error", e)
                errorFetchingData.call()
            }
        }
    }

    fun refresh() {
        fetchItems()
    }
}