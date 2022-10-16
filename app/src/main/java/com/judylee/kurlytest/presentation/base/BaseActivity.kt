package com.judylee.kurlytest.presentation.base

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity<T : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int
) : AppCompatActivity() {

    private var _binding: T? = null
    val binding: T
        get() = _binding!!

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("TAG", "onCreate")

        _binding = DataBindingUtil.setContentView(this, contentLayoutId)

        // 액션바 숨기기
        supportActionBar?.hide()

        // 상태바 텍스트 색 변경(상태바 배경색에 따라 흰/검)
        // API 30 기준으로 구분
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsControllerCompat(window, LayoutInflater.from(this).inflate(contentLayoutId, null)).isAppearanceLightStatusBars = true
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        onSubscribe()
        extraSetupBinding()
    }

    override fun onResume() {
        super.onResume()
        setup()
        setupViews()
    }

    /** dataBinding 객체에 추가적인 작업을 할 때 사용한다 */
    protected abstract fun extraSetupBinding()

    /** ui와 관련 없는 요소를 초기화 할 때 사용한다 */
    protected abstract fun setup()

    /** ui를 초기화 할 때 사용한다 */
    protected abstract fun setupViews()

    /** livedata에 subscribe할 때 사용한다 */
    protected abstract fun onSubscribe()


    override fun onDestroy() {
        release()
        _binding = null
        super.onDestroy()
    }

    /** Activity가 onDestroy() 될 때 리소스를 해제할 때 사용한다 */
    protected abstract fun release()


}