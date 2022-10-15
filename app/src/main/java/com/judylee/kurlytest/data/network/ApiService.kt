package com.judylee.kurlytest.data.network

import com.judylee.kurlytest.data.network.dto.SearchGithubResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search/repositories")
    suspend fun getGitHubRepositories(@Query("q") query: String) : Response<SearchGithubResponse>

}