package com.example.dotametrics.presentation.view.main

import androidx.lifecycle.*
import com.example.dotametrics.App
import com.example.dotametrics.data.local.repository.PlayerRepository
import com.example.dotametrics.data.remote.model.search.SearchResult
import com.example.dotametrics.data.remote.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val app: App) : ViewModel() {

    private val _results = MutableLiveData<List<SearchResult>>()
    val results: LiveData<List<SearchResult>>
        get() = _results

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val retrofit = RetrofitInstance.getService()

    private val repository = PlayerRepository(app.db)

    val players = repository.getPlayers().asLiveData()

    fun search(user: String) {
        if (user.isNotBlank()) {
            retrofit.getSearchResults(user).enqueue(object : Callback<List<SearchResult>> {
                override fun onResponse(
                    call: Call<List<SearchResult>>,
                    response: Response<List<SearchResult>>
                ) {
                    _results.value = response.body()
                }

                override fun onFailure(call: Call<List<SearchResult>>, t: Throwable) {
                    _error.value = t.message.toString()
                }
            })
        }
    }

    class MainViewModelFactory(private val app: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}