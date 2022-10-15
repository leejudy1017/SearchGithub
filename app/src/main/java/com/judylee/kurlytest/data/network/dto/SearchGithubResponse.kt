package com.judylee.kurlytest.data.network.dto

import com.google.gson.annotations.SerializedName

// search github repository
data class SearchGithubResponse(
    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("items")
    val items: List<GithubRepositoryResponse>
)


