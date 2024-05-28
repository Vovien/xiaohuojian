package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.getViewBinding
import com.apkdv.mvvmfast.ktx.invisible
import com.apkdv.mvvmfast.ktx.visible

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.QuestionMultiData
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.event.CircleChangedEvent
import com.jmbon.middleware.databinding.ItemQuestionListBinding
import com.jmbon.middleware.provider.BindingBaseItemProvider
import com.jmbon.middleware.utils.HtmlTools
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.tubewiki.home.databinding.ItemArticleImageLayoutBinding

/**
 * @author : leimg
 * time   : 2022/5/10
 * desc   :
 * version: 1.0
 */
class ArticleImageProvider :
    BindingBaseItemProvider<CircleChangedEvent, TubeArticleDetail, ItemArticleImageLayoutBinding>() {
    override val itemViewType: Int
        get() = 2 //1 普通， 2百科

    override fun convert(helper: BaseViewHolder, item: TubeArticleDetail) {
        helper.getViewBinding<ItemArticleImageLayoutBinding>().apply {
            tvTitle.text = item.customTitle.ifEmpty { item.title }
            tvDesc.text = item.customDescription.ifEmpty { HtmlTools.delHTMLTag(item.content) }

            ivCover.loadRadius(item.indexCover.ifEmpty { item.cover }, 8f.dp())

        }
    }

    override fun setEventData(
        it: CircleChangedEvent,
        item: TubeArticleDetail,
        viewBinding: ItemArticleImageLayoutBinding
    ) {
    }

    override fun onViewRecycled(holder: BaseViewHolder) {
    }


}