package com.jmbon.minitools.tubetest.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.R
import com.jmbon.minitools.databinding.ItemProvinceLayoutBinding
import com.jmbon.minitools.tubetest.bean.BaseInfoBean


/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   :
 * version: 1.0
 */
class ProvinceItemAdapter :
    BindingQuickAdapter<BaseInfoBean.City, ItemProvinceLayoutBinding>() {

    var selectedId = -1

    override fun convert(holder: BaseBindingHolder, item: BaseInfoBean.City) {
        holder.getViewBinding<ItemProvinceLayoutBinding>().apply {

            if (selectedId == holder.adapterPosition) {
                viewFlag.visible()
                tvProvince.setTextColor(ColorUtils.getColor(R.color.color_currency))
                tvProvince.paint.isFakeBoldText = true
                root.setBackgroundColor(ColorUtils.getColor(R.color.white))
            } else {
                viewFlag.gone()
                tvProvince.setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
                tvProvince.paint.isFakeBoldText = false
                root.setBackgroundColor(ColorUtils.getColor(R.color.ColorFAFA))
            }
            tvProvince.text = item.title

        }
    }

}