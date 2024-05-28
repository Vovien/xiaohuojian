package com.tubewiki.home.doctor.adapter

import android.view.LayoutInflater
import android.widget.LinearLayout
import com.apkdv.mvvmfast.ktx.DataBindingQuickAdapter
import com.jmbon.middleware.utils.dp
import com.tubewiki.home.databinding.ItemHospitalDoctorTagLayoutBinding
import com.tubewiki.home.databinding.ItemLocalDoctorLayoutBinding
import com.tubewiki.home.doctor.bean.LocalDoctor

/**
 * 本地医生适配器
 * @author MilkCoder
 * @date 2023/7/19
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
class LocalDoctorAdapter : DataBindingQuickAdapter<LocalDoctor, ItemLocalDoctorLayoutBinding>() {
    override fun convert(
        holder: AutomaticDataBindingHolder<ItemLocalDoctorLayoutBinding>,
        item: LocalDoctor
    ) {
        holder.dataBinding?.apply {
            this.doctor = item
            this.executePendingBindings()
            llTag.removeAllViews()
            item.labels.forEach {
                val textView =
                    ItemHospitalDoctorTagLayoutBinding.inflate(LayoutInflater.from(context))
                textView.tvTag.text = it.label
                llTag.addView(textView.root)
                val layoutParams = textView.root.layoutParams as LinearLayout.LayoutParams
                layoutParams.marginEnd = 12f.dp()
                textView.root.layoutParams = layoutParams
            }
        }
    }

}