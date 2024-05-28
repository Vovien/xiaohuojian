package com.tubewiki.home.hospital.adpater

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.blankj.utilcode.util.SpanUtils
import com.jmbon.middleware.utils.Color
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.HospitalDoctorKeyWork
import com.tubewiki.home.databinding.ItemHospitalSearchHistoryBinding

class HospitalDoctorSearchHotAdapter : BindingQuickAdapter<HospitalDoctorKeyWork.KeyWord, ItemHospitalSearchHistoryBinding>() {
    override fun convert(holder: BaseBindingHolder, item: HospitalDoctorKeyWork.KeyWord) {
        holder.getViewBinding<ItemHospitalSearchHistoryBinding>().apply {
            imageType.gone()
            imageDelete.gone()
            SpanUtils.with(textSearchKey).append("${holder.layoutPosition+1}.").setForegroundColor(getColorByPosition(holder.layoutPosition))
                .append(item.keyword).create()
        }
    }

    private fun getColorByPosition(position: Int): Int {
        return when (position) {
            0 -> R.color.color_FF5A5F.Color
            1 -> R.color.color_FA8346.Color
            2 -> R.color.color_F5C045.Color
            else -> R.color.color_BFBFBF.Color
        }
    }
}