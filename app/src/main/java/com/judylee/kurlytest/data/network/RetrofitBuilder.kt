package com.judylee.kurlytest.data.network

import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {

    companion object {

        private const val API_URL = "http://api.github.com"
        private val contentType = MediaType.parse("application/json")

        private val client = OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .build()

            chain.proceed(newRequest)
        }

        /**
         * Retrofit 설정
         */
        private val retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client.build())
            .build()

        private fun<T> buildService(service: Class<T>): T{
            return retrofit.create(service)
        }

        /**
         * Service 등록하는 곳, 해당 Service를 Repository에서 불러서 활용한다.
         */

        // apiService 등록
        val apiService = buildService(ApiService::class.java)

    }

}