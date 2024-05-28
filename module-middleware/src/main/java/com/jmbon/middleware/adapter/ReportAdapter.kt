package com.jmbon.middleware.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.ReportTypeData
import com.jmbon.middleware.databinding.ReportItemLayoutBinding

/**
 * @author : leimg
 * time   : 2021/4/15
 * desc   :举报类型
 * version: 1.0
 */
class ReportAdapter :
    BindingQuickAdapter<ReportTypeData.Report, ReportItemLayoutBinding>() {
    override fun convert(holder: BaseBindingHolder, item: ReportTypeData.Report) {
        holder.getViewBinding<ReportItemLayoutBinding>().apply {
            tvTitle.text = item.reason
            if (holder.adapterPosition == selectedPos) {
                ivChoice.setImageResource(R.drawable.icon_report_choice)
            } else {
                ivChoice.setImageResource(R.drawable.icon_report_default)
            }
        }
    }

    private var selectedPos = -1

    fun setSelectedPos(pos: Int) {
        selectedPos = pos
        notifyDataSetChanged()
    }

    fun getSelectedPos(): Int {
        return selectedPos
    }
}