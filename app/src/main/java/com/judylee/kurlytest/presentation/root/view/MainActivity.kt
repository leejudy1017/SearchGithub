package com.judylee.kurlytest.presentation.root.view

import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.judylee.kurlytest.R
import com.judylee.kurlytest.data.network.ApiService
import com.judylee.kurlytest.data.network.RetrofitApi
import com.judylee.kurlytest.data.network.dto.SearchGithubResponse
import com.judylee.kurlytest.databinding.ActivityMainBinding
import com.judylee.kurlytest.presentation.base.BaseActivity
import com.judylee.kurlytest.presentation.root.viewModel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainVM : MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun extraSetupBinding() {
        binding.vm = mainVM
    }

    override fun setup() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun setupViews() {

        val call : Call<SearchGithubResponse> = RetrofitApi.apiService.getGitHubRepositories("android")
        call.enqueue(object : Callback<SearchGithubResponse> {
            override fun onResponse(
                call: Call<SearchGithubResponse>,
                response: Response<SearchGithubResponse>
            ) {
                Log.i("success",response.body()?.items.toString())
                Log.i("success",response.body()?.totalCount.toString())
            }

            override fun onFailure(call: Call<SearchGithubResponse>, t: Throwable) {
                Log.i("fail",t.toString())
            }

        })


    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

}