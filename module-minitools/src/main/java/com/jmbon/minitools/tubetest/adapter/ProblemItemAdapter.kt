package com.jmbon.minitools.tubetest.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.R
import com.jmbon.minitools.databinding.ItemLayoutProblemBinding
import com.jmbon.minitools.tubetest.bean.BaseInfoBean


/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   :
 * version: 1.0
 */
class ProblemItemAdapter :
    BindingQuickAdapter<BaseInfoBean.SelectBean, ItemLayoutProblemBinding>() {

    var selectedIds = mutableListOf<Int>()

    override fun convert(holder: BaseBindingHolder, item: BaseInfoBean.SelectBean) {
        holder.getViewBinding<ItemLayoutProblemBinding>().apply {

            if (selectedIds.contains(item.id)) {
                tvItem.setTextColor(ColorUtils.getColor(R.color.white))
                tvItem.setShapeSolidColor(ColorUtils.getColor(R.color.color_currency))
            } else {
                tvItem.setTextColor(ColorUtils.getColor(R.color.color_262626))
                tvItem.setShapeSolidColor(ColorUtils.getColor(R.color.colorF9F9F9))
            }
            tvItem.setUseShape()
            tvItem.text = item.title

        }
    }

}