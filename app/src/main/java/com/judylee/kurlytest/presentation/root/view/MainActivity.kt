package com.judylee.kurlytest.presentation.root.view

import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.judylee.kurlytest.R
import com.judylee.kurlytest.databinding.ActivityMainBinding
import com.judylee.kurlytest.presentation.base.BaseActivity
import com.judylee.kurlytest.presentation.root.viewModel.MainViewModel

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


    }

    override fun onSubscribe() {
    }

    override fun release() {
    }

}