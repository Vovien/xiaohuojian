package com.tubewiki.home.hospital.adpater

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.tubewiki.home.databinding.ItemHospitalImageLayoutBinding

/**
 * 医院详情图片adapter
 */
class HospitalImageAdapter :
    BindingQuickAdapter<String, ItemHospitalImageLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: String) {
        holder.getViewBinding<ItemHospitalImageLayoutBinding>().apply {

            ivCover.loadRadius(item, 8f.dp())

        }
    }
}