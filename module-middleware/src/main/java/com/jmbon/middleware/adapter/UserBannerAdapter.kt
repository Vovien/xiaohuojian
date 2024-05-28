package com.jmbon.middleware.adapter

import com.apkdv.mvvmfast.ktx.DataBindingBannerAdapter
import com.jmbon.middleware.bean.UserList
import com.jmbon.middleware.databinding.ItemUserBannerBinding

class UserBannerAdapter :
    DataBindingBannerAdapter<UserList, ItemUserBannerBinding>() {
    override fun onBindView(
        holder: AutomaticDataBindingHolder<ItemUserBannerBinding>,
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