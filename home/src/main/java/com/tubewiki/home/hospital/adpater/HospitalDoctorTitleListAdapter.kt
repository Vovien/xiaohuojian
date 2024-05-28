package com.tubewiki.home.hospital.adpater

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.jmbon.middleware.bean.WebScrollOffset
import com.tubewiki.home.R
import com.tubewiki.home.databinding.ItemHospitalDoctorTitleListLayoutBinding

/**
 * 医生医院目录导航adapter
 */
class HospitalDoctorTitleListAdapter :
    BindingQuickAdapter<WebScrollOffset, ItemHospitalDoctorTitleListLayoutBinding>() {

    var selectedPos = -1

    override fun convert(holder: BaseBindingHolder, item: WebScrollOffset) {
        holder.getViewBinding<ItemHospitalDoctorTitleListLayoutBinding>().apply {
            tvTitle.text = item.title


            if (holder.adapterPosition == selectedPos) {
                viewDot.visible()
                tvTitle.setTextColor(context.resources.getColor(R.color.color_currency))
            } else {
                viewDot.gone()
                tvTitle.setTextColor(context.resources.getColor(R.color.color_7F7F7F))

            }

        }
    }
}