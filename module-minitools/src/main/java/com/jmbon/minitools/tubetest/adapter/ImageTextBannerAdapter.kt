package com.jmbon.minitools.tubetest.adapter

import com.apkdv.mvvmfast.ktx.DataBindingBannerAdapter
import com.jmbon.minitools.databinding.ItemImageTextBinding
import com.jmbon.minitools.tubetest.bean.UserList

class ImageTextBannerAdapter :
    DataBindingBannerAdapter<UserList, ItemImageTextBinding>() {
    override fun onBindView(
        holder: AutomaticDataBindingHolder<ItemImageTextBinding>,
        data: UserList?,
        position: Int,
        size: Int
    ) {
        holder.dataBinding?.apply {
            this.userList = data
            this.executePendingBindings()
        }
    }


}