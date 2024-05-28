package com.jmbon.minitools.report.fragment

import android.view.View
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.jmbon.middleware.extention.loadUrl
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.databinding.FragmentDefaultHeaderLayoutBinding
import com.jmbon.minitools.report.viewmodel.GroupChatViewModel
import com.qmuiteam.qmui.kotlin.onClick

class DefaultHeaderFragment :
    ViewModelFragment<GroupChatViewModel, FragmentDefaultHeaderLayoutBinding>() {

    override fun initView(view: View) {
        binding.apply {
            ivHead.loadUrl(
                "",
                bottomLeftRadius = 36.dp,
                bottomRightRadius = 36.dp
            )
            ivHead.onClick {

            }
        }
    }
}