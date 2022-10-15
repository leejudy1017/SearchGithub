package com.judylee.kurlytest.data.network.dto

import com.google.gson.annotations.SerializedName

data class GithubRepositoryOwnerResponse(
    @SerializedName("avatar_url")
    val avatarUrl: String
)