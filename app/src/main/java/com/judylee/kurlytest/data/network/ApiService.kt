package com.judylee.kurlytest.data.network

import com.judylee.kurlytest.data.network.dto.SearchGithubResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/repositories")
    fun getGitHubRepositories(@Query("q") query: String, @Query("page") page : Int) : Call<SearchGithubResponse>

}