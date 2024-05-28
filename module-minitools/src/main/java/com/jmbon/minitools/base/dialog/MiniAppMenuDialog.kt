package com.jmbon.minitools.base.dialog

import android.content.Context
import com.apkdv.mvvmfast.base.dialog.BaseBottomDialog
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.minitools.databinding.LayoutMiniAppMenuBinding

/**
 * @author : leimg
 * time   : 2021/4/23
 * desc   :小程序菜单dialog
 * version: 1.0
 */
class MiniAppMenuDialog(
    var mContext: Context,
    var appId: String,
    var title: String,
    var cover: String,
    var reload: () -> Unit,
    var dismissResult: () -> Unit,
) :
    BaseBottomDialog<LayoutMiniAppMenuBinding>(mContext) {

    var isCollect = false //是否已经收藏

    override fun onCreate() {
        super.onCreate()


        //收藏小工具
//        val collectViewModel = CollectViewModel()
//        collectViewModel.toolIsCollect(appId) {
//            isCollect = it
//            if (it) {
//                binding.ivCollect.setImageResource(com.jmbon.middleware.R.drawable.icon_collention_pressed)
//                binding.tvCollect.text = "已收藏"
//
//            } else {
//                binding.ivCollect.setImageResource(com.jmbon.middleware.R.drawable.icon_mini_tool_collect)
//                binding.tvCollect.text = "收藏小工具"
//
//
//            }
//        }

        binding.tvTitle.text = title
        binding.ivCover.loadCircle(cover)
        binding.tvSure.setOnSingleClickListener({
            dismiss()
        })

        binding.llReload.setOnSingleClickListener({
            dismiss()
            reload()
        })

    }


    override fun onDismiss() {
        super.onDismiss()
        dismissResult()
    }


}