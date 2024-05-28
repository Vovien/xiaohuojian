package com.jmbon.minitools.report.adapter

import android.app.Activity
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.glide.GlideUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.R
import com.jmbon.minitools.report.bean.FertilityGroupChatBean

class FertilityGroupChatAdapter: BaseQuickAdapter<FertilityGroupChatBean, BaseViewHolder>(R.layout.item_fertility_group_chat_layout) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            BannerHelper.onClick(
                CommonBanner(
                    item_type = data[position].itemType,
                    identity = data[position].identity
                )
            )
            val type = (recyclerView.context as? Activity)?.intent?.getIntExtra("type", 0)
        }
    }

    override fun convert(holder: BaseViewHolder, item: FertilityGroupChatBean) {
        holder.getView<ShapeableImageView>(R.id.iv_group_chat_icon).apply {
            shapeAppearanceModel = ShapeAppearanceModel().withCornerSize(12.dp.toFloat())
            GlideUtil.getInstance().loadUrl(this, item.cover, R.drawable.icon_circle_placeholder)
        }

        holder.setText(R.id.tv_group_chat_title, item.name)
        holder.setText(R.id.tv_group_chat_desc, item.description)
        holder.setGone(R.id.tv_group_chat_desc, item.description.isNullOrBlank())
        holder.setText(R.id.tv_online, item.txt)
        holder.setGone(R.id.ll_online, item.txt.isNullOrBlank())
    }
}