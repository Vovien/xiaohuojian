package com.tubewiki.home.hospital.adpater

import com.apkdv.mvvmfast.ktx.DataBindingQuickAdapter
import com.tubewiki.home.bean.hospital.bean.AllHospital
import com.tubewiki.home.databinding.ItemAllHospitalLayoutBinding

class AllHospitalAdapter :
    DataBindingQuickAdapter<AllHospital, ItemAllHospitalLayoutBinding>() {
    override fun convert(
        holder: AutomaticDataBindingHolder<ItemAllHospitalLayoutBinding>,
        item: AllHospital
    ) {
        holder.dataBinding?.apply {
            this.hospital = item
            this.executePendingBindings()
        }
    }

}