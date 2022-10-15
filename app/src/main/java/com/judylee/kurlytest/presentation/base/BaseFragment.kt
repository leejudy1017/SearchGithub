package com.judylee.kurlytest.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


abstract class BaseFragment<T : ViewDataBinding>(
    @LayoutRes private val contentLayoutId: Int
) : Fragment() {

    private var _binding: T? = null
    protected val binding: T get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, contentLayoutId, container, false)

        extraSetupBinding()
        setup()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onSubscribe()
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


    override fun onDestroyView() {
        release()
        _binding = null
        super.onDestroyView()
    }

    /** Fragment가 onDestroyView 될 때, 리소스를 해제할 때 사용한다 */
    protected abstract fun release()

    // fragment 이동
    open fun replaceFragment(fragmentLayoutId: Int, fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .apply {
                replace(fragmentLayoutId, fragment)
                addToBackStack(null)
                commit()
            }
    }
}