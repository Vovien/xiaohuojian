package com.tubewiki.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.tubewiki.home.bean.HomeBean
import com.tubewiki.home.databinding.ItemHomeTubeStepLayoutBinding
import com.tubewiki.home.router.HomeRouter

class HomeTubeStepAdapter :
    BindingQuickAdapter<HomeBean.Cate, ItemHomeTubeStepLayoutBinding>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            HomeRouter.toQuestionCategory(data[position].id)
        }
    }

    override fun convert(holder: BaseBindingHolder, item: HomeBean.Cate) {
        holder.getViewBinding<ItemHomeTubeStepLayoutBinding>().apply {
            GlideUtil.getInstance().loadCircleImg(
                ivBg,
                item.icon,
                com.jmbon.middleware.R.drawable.icon_topic_placeholder
            )

            tvTitle.text = item.title

        }
    }
}