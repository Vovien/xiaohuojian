package com.tubewiki.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.ktx.DataBindingQuickAdapter
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.utils.BannerHelper
import com.tubewiki.home.bean.Screen
import com.tubewiki.home.databinding.ItemCommonProblemBinding

class CommonProblemAdapter : DataBindingQuickAdapter<Screen, ItemCommonProblemBinding>() {
    override fun convert(
        holder: AutomaticDataBindingHolder<ItemCommonProblemBinding>,
        item: Screen
    ) {
        holder.dataBinding?.apply {
            this.screen = item
            executePendingBindings()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            data[position].apply {
                BannerHelper.onClick(
                    CommonBanner(
                        item_id = identity.toIntOrNull() ?: 0,
                        item_type = itemType,
                        identity = identity
                    )
                )
            }
        }
    }

}