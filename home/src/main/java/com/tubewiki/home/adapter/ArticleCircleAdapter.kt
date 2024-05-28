package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.ArticleDetailBean
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.home.R
import com.tubewiki.home.databinding.ItemAllColumnLayoutBinding
import com.tubewiki.home.databinding.ItemArticleCircleLayoutBinding
import com.tubewiki.home.databinding.ItemHomePregentHelpLayoutBinding
import com.tubewiki.home.databinding.ItemHomeRecommendKnowledgeLayoutBinding

/**
 * 文章详情页推荐圈子adapter
 */
class ArticleCircleAdapter :
    BindingQuickAdapter<ArticleDetailBean.Circle, ItemArticleCircleLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: ArticleDetailBean.Circle) {
        holder.getViewBinding<ItemArticleCircleLayoutBinding>().apply {

            ivCover.loadRadius(
                item.cover, 8f.dp(),
                R.drawable.icon_tube_placeholder
            )

            tvTitle.text = item.customTitle
            tvDesc.text = "${item.memberCount}位姐妹正在热聊"

        }
    }
}