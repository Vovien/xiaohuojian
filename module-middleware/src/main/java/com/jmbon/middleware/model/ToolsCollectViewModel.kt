package com.jmbon.middleware.model

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.api.PayAPI
import com.jmbon.middleware.api.ShareApi

class ToolsCollectViewModel : BaseViewModel() {


    /**
     * 收藏小程序
     *     * 【0：取消收藏，1：收藏】
     */
    fun toolCollect(
        miniId: String, status: Int
    ) = launchWithFlow({
        ShareApi.toolCollect(miniId, status)
    }, handleError = false)
    /**
     * 收藏小程序
     *     * 【0：取消收藏，1：收藏】
     */
    fun toolIsCollect(
        miniId: String
    ) = launchWithFlow({
        ShareApi.toolIsCollect(miniId)
    }, handleError = false)

}