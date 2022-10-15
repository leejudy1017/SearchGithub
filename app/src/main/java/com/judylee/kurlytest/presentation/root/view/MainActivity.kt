package com.judylee.kurlytest.presentation.root.view

import android.content.Context
import android.util.Log
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.judylee.kurlytest.R
import com.judylee.kurlytest.data.network.RetrofitApi
import com.judylee.kurlytest.data.network.dto.SearchGithubResponse
import com.judylee.kurlytest.databinding.ActivityMainBinding
import com.judylee.kurlytest.presentation.base.BaseActivity
import com.judylee.kurlytest.presentation.root.view.adapter.RepositoryAdapter
import com.judylee.kurlytest.presentation.root.viewModel.MainViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainVM: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun extraSetupBinding() {
        binding.vm = mainVM
    }

    override fun setup() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    override fun setupViews() {

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                // 키보드 내리고 searchView 포커싱 없애기
                hideKeyboard()
                binding.searchView.clearFocus()
                if (text != null) {
                    if (text.isNotEmpty()) {
                        Log.i("text", text)
                        //Todo: 네트워크 확인
                        //Todo: 로딩스피너
                        val call: Call<SearchGithubResponse> =
                            RetrofitApi.apiService.getGitHubRepositories(text)
                        call.enqueue(object : Callback<SearchGithubResponse> {
                            override fun onResponse(
                                call: Call<SearchGithubResponse>,
                                response: Response<SearchGithubResponse>
                            ) {
                                Log.i("success", response.body()?.items.toString())
                                Log.i("success", response.body()?.totalCount.toString())

                                if (response.body()?.totalCount == 0) {
                                    // 안내문구 (검색 정보가 없을 때)
                                    Toast.makeText(applicationContext, "검색된 리포지토리가 없습니다.", Toast.LENGTH_SHORT).show()
                                } else {
                                    var listManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
                                    var listAdapter = response.body()?.let { RepositoryAdapter(applicationContext, text, it) }
                                    var recyclerList = binding.recyclerView.apply {
                                        setHasFixedSize(true)
                                        layoutManager = listManager
                                        adapter = listAdapter
                                    }
                                }
                            }

                            override fun onFailure(call: Call<SearchGithubResponse>, t: Throwable) {
                                Log.i("fail", t.toString())
                            }

                        })
                    }
                    else {
                        // 안내문구 (키보드 입력) : length = 0
                        Toast.makeText(applicationContext, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }
                }
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                return true
            }

        })

    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

    fun hideKeyboard() {
        val imm = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

}