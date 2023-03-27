package com.example.dotametrics.presentation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dotametrics.data.model.search.SearchResult
import com.example.dotametrics.data.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _results = MutableLiveData<List<SearchResult>>()
    val results: LiveData<List<SearchResult>>
        get() = _results

    private val _error = MutableLiveData<String>()
    val error: LiveData<String>
        get() = _error

    private val retrofit = RetrofitInstance.getService()

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

}