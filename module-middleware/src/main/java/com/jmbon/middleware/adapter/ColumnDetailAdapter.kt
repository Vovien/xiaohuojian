package com.jmbon.middleware.adapter

import android.graphics.Typeface
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.utils.coverToTenThousandPlus
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.jmbon.middleware.bean.ColumnArticles
import com.jmbon.middleware.databinding.ItemArticleNormalLayoutBinding


class ColumnDetailAdapter :
    BindingQuickAdapter<ColumnArticles, ItemArticleNormalLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: ColumnArticles) {
        holder.getViewBinding<ItemArticleNormalLayoutBinding>().apply {
            tvTitle.text = item.title
            tvDesc.text = item.introduction

            ivCover.loadRadius(
                item.cover,
                8f.dp(),
                R.drawable.icon_tube_placeholder
            )

            if (item.readNum >= 10000) {
                tvNum.text = "${item.readNum.coverToTenThousandPlus()}阅读"
                tvNum.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
                tvNum.setTextColor(ColorUtils.getColor(R.color.color_FFAB8F))
            } else {
                tvNum.text = "${item.readNum}阅读"
                tvNum.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
                tvNum.setTextColor(ColorUtils.getColor(R.color.color_BFBFBF))
            }

        }
    }
}