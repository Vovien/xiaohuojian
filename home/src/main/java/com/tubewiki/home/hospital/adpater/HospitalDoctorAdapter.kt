package com.tubewiki.home.hospital.adpater

import android.view.LayoutInflater
import android.widget.LinearLayout
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.loadCircle
import com.tubewiki.home.bean.hospital.bean.HospitalDetailBean
import com.tubewiki.home.databinding.ItemHospitalDoctorLayoutBinding
import com.tubewiki.home.databinding.ItemHospitalDoctorTagLayoutBinding

/**
 * 医院详情图片adapter
 */
class HospitalDoctorAdapter :
    BindingQuickAdapter<HospitalDetailBean.Doctor, ItemHospitalDoctorLayoutBinding>() {

    override fun convert(holder: BaseBindingHolder, item: HospitalDetailBean.Doctor) {
        holder.getViewBinding<ItemHospitalDoctorLayoutBinding>().apply {

            ivCover.loadCircle(item.avatarFile)

            tvTitle.text = item.name
            tvOffice.text = item.position
            tvScore.text = "${item.star.toDouble()}"
            tvHospitalName.text = item.hospitalName
            tvHospitalOffice.text = item.department
            tvAskNum.text = "咨询量${item.consultCount}"

            var stringBuilder = StringBuilder("擅长：")

            item.columns.forEachIndexed { index, column ->
                stringBuilder.append(column.columnName)
                if (index != item.columns.size - 1) {
                    stringBuilder.append("、")
                }
            }
            tvSpecial.text = stringBuilder

            llTag.removeAllViews()
            item.labels.forEach {
                var textView =
                    ItemHospitalDoctorTagLayoutBinding.inflate(LayoutInflater.from(context))
                textView.tvTag.text = it.label
                llTag.addView(textView.root)
                var layoutParams = textView.root.layoutParams as LinearLayout.LayoutParams
                layoutParams.marginEnd = 12f.dp()
                textView.root.layoutParams = layoutParams
            }
        }
    }
}