package com.tubewiki.home.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.utils.*
import com.tubewiki.home.R
import com.tubewiki.home.databinding.ItemAllColumnLayoutBinding
import com.tubewiki.home.databinding.ItemArticleDoctorLayoutBinding
import com.tubewiki.home.databinding.ItemHomePregentHelpLayoutBinding
import com.tubewiki.home.databinding.ItemHomeRecommendKnowledgeLayoutBinding

/**
 * 文章医生adapter
 */
class ArticleDoctorAdapter :
    BindingQuickAdapter<TubeArticleDetail.Doctor, ItemArticleDoctorLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: TubeArticleDetail.Doctor) {
        holder.getViewBinding<ItemArticleDoctorLayoutBinding>().apply {

            ivCover.loadCircle(
                item.avatar,
                R.drawable.icon_tube_placeholder
            )

            tvTitle.text = item.name
            tvPosition.text = item.jobTitle
            tvHospital.text = item.hospital

        }
    }
}