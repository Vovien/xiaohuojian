package com.jmbon.minitools.tubetest.adapter

import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.R
import com.jmbon.minitools.databinding.ItemCityLayoutBinding
import com.jmbon.minitools.tubetest.bean.BaseInfoBean


/**
 * @author : leimg
 * time   : 2021/9/14
 * desc   :
 * version: 1.0
 */
class CityItemAdapter :
    BindingQuickAdapter<BaseInfoBean.City, ItemCityLayoutBinding>() {

    var selectedId = -1

    override fun convert(holder: BaseBindingHolder, item: BaseInfoBean.City) {
        holder.getViewBinding<ItemCityLayoutBinding>().apply {

            if (selectedId == holder.adapterPosition) {
                ivChoice.visible()
                tvProvince.setTextColor(ColorUtils.getColor(R.color.color_currency))
                tvProvince.paint.isFakeBoldText = true
            } else {
                ivChoice.gone()
                tvProvince.setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
                tvProvince.paint.isFakeBoldText = false

            }
            tvProvince.text = item.title

        }
    }

}