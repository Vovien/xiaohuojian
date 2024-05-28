package com.tubewiki.home.hospital.adpater

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadRadius
import com.tubewiki.home.bean.hospital.bean.HospitalDetailBean
import com.tubewiki.home.databinding.ItemHospitalLeaderLayoutBinding

/**
 * 医院详情图片adapter
 */
class HospitalLeaderAdapter :
    BindingQuickAdapter<HospitalDetailBean.Hospital.Leader, ItemHospitalLeaderLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: HospitalDetailBean.Hospital.Leader) {
        holder.getViewBinding<ItemHospitalLeaderLayoutBinding>().apply {

            tvTitle.text = item.name
            ivCover.loadRadius(item.avatarFile, 8f.dp())
            tvOffice.text = item.doctorDescript
            if (item.isDean > 0) {
                ivTag.visible()
            } else {
                ivTag.gone()
            }

        }
    }
}