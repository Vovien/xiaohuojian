package com.jmbon.video.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.video.api.VideoApi
import kotlinx.coroutines.flow.map

class VideoDetailsViewModel : BaseViewModel() {

    /**
     * 获取视频列表
     * @date 2023/9/11 16:51
     */
    fun getAllVideoList() = launchWithFlow({
        VideoApi.getVideoList()
    }, handleError = true)

    /**
     * 获取视频详情
     * @date 2023/11/14 16:54
     * @param videoId
     */
    fun getVideoDetail(videoId: Int) = launchWithFlow({
        VideoApi.getVideoDetail(videoId)
    }, handleError = true)

    /**
     * 收藏视频
     * @date 2023/11/14 18:07
     */
    fun collect(operateType: String, videoId: Int) = launchWithFlow({
        VideoApi.videoCollect(operateType, videoId)
    }, handleError = false)

}