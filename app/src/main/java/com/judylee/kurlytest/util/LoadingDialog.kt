package com.judylee.kurlytest.util

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.judylee.kurlytest.R
import com.judylee.kurlytest.databinding.DialogLoadingBinding

class LoadingDialog() : DialogFragment() {

    var _binding: DialogLoadingBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogLoadingBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    @SuppressLint("ResourceType")
    override fun onResume() {
        super.onResume()
        // full Screen code
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        // 레이아웃 배경설정
        dialog?.window?.setBackgroundDrawableResource(R.color.dim)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}