package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.home.R
import com.tubewiki.home.databinding.ItemAllColumnLayoutBinding
import com.tubewiki.home.databinding.ItemHomePregentHelpLayoutBinding
import com.tubewiki.home.databinding.ItemHomeRecommendKnowledgeLayoutBinding

/**
 * 首页推荐知识adapter
 */
class AllColumnAdapter :
    BindingQuickAdapter<TubeArticleDetail, ItemAllColumnLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: TubeArticleDetail) {
        holder.getViewBinding<ItemAllColumnLayoutBinding>().apply {

            ivCover.load(
                item.indexCover,
                R.drawable.icon_tube_placeholder
            )

            tvTitle.text = item.customTitle.ifEmpty { item.topicName }
            tvDesc.text = item.customDescription.ifEmpty { item.description }

        }
    }
}