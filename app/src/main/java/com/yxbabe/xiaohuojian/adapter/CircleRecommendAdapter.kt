package com.yxbabe.xiaohuojian.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.SizeUtils
import com.jmbon.middleware.bean.CircleData
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.utils.*
import com.yxbabe.xiaohuojian.R
import com.yxbabe.xiaohuojian.databinding.ItemRecommendFindCircleBinding

class CircleRecommendAdapter :
    BindingQuickAdapter<CircleData, ItemRecommendFindCircleBinding>() {
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseBindingHolder, item: CircleData) {
        holder.getViewBinding<ItemRecommendFindCircleBinding>().apply {
            imageCircle.loadRadius(item.covers, 25f.dp())
            textCircleName.text = item.name
            textDescription.text = item.description
            textDiscuss.text = "${item.memberCount}姐妹在讨论"
            if (item.memberList.isNotNullEmpty()) {
                floatUser.visible()
                floatUser.removeAllViews()
                floatUser.setChildHorizontalSpacing((-8f).dp())
                item.memberList.take(5).forEach {
                    floatUser.addView(buildImage(this.root.context, it.avatarFile))
                }
            } else {
                floatUser.gone()
            }

            root.setOnSingleClickListener({
                BannerHelper.onClick(
                    CommonBanner(
                        item_type = item.item_type,
                        identity = item.identity
                    )
                )
            })
        }
    }

    private fun buildImage(context: Context, avatarFile: String): ImageView {
        val image = ImageView(context)
        val params = ViewGroup.LayoutParams(SizeUtils.dp2px(24f), SizeUtils.dp2px(24f))
        image.layoutParams = params
        image.setBackgroundResource(R.drawable.shape_image_white_border)
        image.loadCircle(avatarFile)
        return image
    }
}