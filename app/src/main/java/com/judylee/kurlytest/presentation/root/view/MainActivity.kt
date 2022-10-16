package com.judylee.kurlytest.presentation.root.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.judylee.kurlytest.R
import com.judylee.kurlytest.data.network.RetrofitApi
import com.judylee.kurlytest.data.network.dto.GithubRepositoryResponse
import com.judylee.kurlytest.data.network.dto.SearchGithubResponse
import com.judylee.kurlytest.databinding.ActivityMainBinding
import com.judylee.kurlytest.presentation.base.BaseActivity
import com.judylee.kurlytest.presentation.root.view.adapter.RepositoryAdapter
import com.judylee.kurlytest.presentation.root.viewModel.MainViewModel
import com.judylee.kurlytest.util.LoadingDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    var currentPage = 1
    var totalCount = 0
    var searchText = ""
    var githubRepositoryResponse = mutableListOf<GithubRepositoryResponse>()
    lateinit var listAdapter : RepositoryAdapter

    private val mainVM: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun extraSetupBinding() {
        binding.vm = mainVM
    }

    override fun setup() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        listAdapter = RepositoryAdapter(applicationContext, searchText, githubRepositoryResponse, totalCount, currentPage)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun setupViews() {

        binding.recyclerView.adapter = listAdapter
        var listManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = listManager
            adapter = listAdapter
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {

                // 키보드 내리고 searchView 포커싱 없애기
                hideKeyboard()
                binding.searchView.clearFocus()

                if (text != null) {
                    if (text.isNotEmpty()) {
                        searchText = text
                        currentPage = 1
                        githubRepositoryResponse.clear()
                        listAdapter.notifyDataSetChanged()
                        getGitHubRepositoriesApi()
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

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                //스크롤이 필요한 경우
                if(totalCount > 30 * currentPage){
                    val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    val totalItemViewCount = recyclerView.adapter!!.itemCount-1

                    // 키보드 내리고 searchView 포커싱 없애기
                    hideKeyboard()
                    binding.searchView.clearFocus()

                    if(newState == 2 && !recyclerView.canScrollVertically(1) && lastVisibleItemPosition == totalItemViewCount){
                        currentPage += 1
                        getGitHubRepositoriesApi()
                    }
                }

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

    fun getGitHubRepositoriesApi(){

        // 서버 호출하는 동안 로딩 다이얼로그 띄우도록 함
        var dialog = LoadingDialog()

        val call: Call<SearchGithubResponse> = RetrofitApi.apiService.getGitHubRepositories(searchText,currentPage)

        dialog.show(supportFragmentManager,"")
        call.enqueue(object : Callback<SearchGithubResponse> {
            override fun onResponse(
                call: Call<SearchGithubResponse>,
                response: Response<SearchGithubResponse>
            ) {
                if (response.isSuccessful) {

                    totalCount = response.body()?.totalCount!!

                    dialog.dismiss()
                    if (response.body()?.totalCount == 0) {
                        // 안내문구 (검색 정보가 없을 때)
                        Toast.makeText(applicationContext, "검색된 리포지토리가 없습니다.", Toast.LENGTH_SHORT).show()
                    } else {

                        for (items in response.body()?.items!!) {
                            githubRepositoryResponse.add(items)
                        }

                        listAdapter.notifyDataSetChanged()
                        listAdapter?.setItemClickListener(object :
                            RepositoryAdapter.ItemClickListener {
                            override fun onClick(view: View, position: Int) {
                                try {
                                    // item 클릭 시 해당 리포지토리 링크 연결
                                    val webIntent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(response.body()?.items?.get(position)?.htmlUrl)
                                    )
                                    startActivity(webIntent)
                                } catch (e: java.lang.Exception) {
                                    e.printStackTrace()
                                    // 안내문구 (링크 연결이 불가능 할 때)
                                    Toast.makeText(
                                        applicationContext,
                                        "해당 리포지토리를 열 수 없습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })

                    }
                }
                else{

                }
            }

            override fun onFailure(call: Call<SearchGithubResponse>, t: Throwable) {
                Log.i("fail", t.toString())
                dialog.dismiss()
                // 안내문구 (데이터를 받아오지 못했을 때)
                Toast.makeText(applicationContext, "서버에서 데이터를 가져오지 못했습니다.\n네트워크를 확인해주세요.", Toast.LENGTH_SHORT).show()
            }

        })

    }

}