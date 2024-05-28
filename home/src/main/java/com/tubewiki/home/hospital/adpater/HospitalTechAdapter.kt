package com.tubewiki.home.hospital.adpater

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.tubewiki.home.bean.hospital.bean.HospitalDetailBean
import com.tubewiki.home.databinding.ItemHospitalTechLayoutBinding

/**
 * 医院详情图片adapter
 */
class HospitalTechAdapter :
    BindingQuickAdapter<HospitalDetailBean.Hospital.TechnologyName, ItemHospitalTechLayoutBinding>() {

    override fun convert(
        holder: BaseBindingHolder,
        item: HospitalDetailBean.Hospital.TechnologyName
    ) {
        holder.getViewBinding<ItemHospitalTechLayoutBinding>().apply {

            tvTitle.text = item.technology

        }
    }
}