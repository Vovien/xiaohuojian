package com.jmbon.video.api

import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.bean.VideoAlbum
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpBusiness
import com.jmbon.video.bean.VideoListDetail
import com.jmbon.video.bean.VideoNewDetail
import rxhttp.wrapper.param.toResponse

object VideoApi {


    suspend fun getVideoList(): VideoListDetail {
        return HttpBusiness.post("rand_video_list")
            .toResponse<VideoListDetail>().await()
    }

    suspend fun getVideoDetail(videoId: Int): VideoNewDetail {
        return HttpBusiness.post("video_detail")
            .add("video_id", videoId)
            .toResponse<VideoNewDetail>().await()
    }

    suspend fun videoCollect(
        operateType: String,
        videoId: Int,
    ): EmptyData {
        return HttpBusiness.post("item_collect")
            .add("item_type", 1)
            .add("operate_type", operateType)
            .add("item_id", videoId)
            .toResponse<EmptyData>().await()
    }


}