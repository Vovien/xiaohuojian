package com.yxbabe.xiaohuojian.api

import androidx.annotation.Keep
import com.apkdv.mvvmfast.BuildConfig
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.blankj.utilcode.util.GsonUtils
import com.jmbon.middleware.bean.CircleConfigBean
import com.jmbon.middleware.config.network.Http
import com.jmbon.middleware.config.network.HttpV2
import com.jmbon.middleware.bean.HelpGroupBean
import com.jmbon.middleware.bean.KnowledgeBean
import com.jmbon.middleware.bean.RecommendCircle
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.HttpBusiness
import com.jmbon.middleware.config.network.HttpCacheManager
import com.tubewiki.home.bean.LaunchAdv
import rxhttp.wrapper.param.toResponse

@Keep
object MainApi {

    /**
     * https://api.jmbom.com/api/v1/app/v2/report/oaid
     * 同步设备激活接口
     */
    suspend fun syncDevice(
        oaid: String,
        ip: String,
        sysVersion: String,
        deviceModel: String
    ): EmptyData {
        return HttpV2.post("report/oaid")
            .add("ip", ip)
            .add("sys_version", sysVersion)
            .add("device_model", deviceModel)
            .toResponse<EmptyData>().await()
    }

    /**
     * 获取首页头部配置信息
     */
    suspend fun getKnowledgeInfo(): KnowledgeBean {
        return HttpBusiness.post("tab/knowledge")
            .toResponse<KnowledgeBean>().await()
    }

    /**
     * 获取开屏广告
     * @date 2023/6/25 14:42
     */
    suspend fun getScreenAdv(): LaunchAdv {
        val path = "app/v13/common/get_screen_adv"
        // 优先获取缓存数据, 如果有缓存数据, 则直接返回, 同时请求接口更新缓存数据
        val cacheData = HttpCacheManager.get(BuildConfig.BASE_URL + path)
        val adInfo = GsonUtils.fromJson(cacheData, LaunchAdv::class.java)
        if (adInfo != null) {
            // 使用try-catch避免更新数据时出现异常打断正常的缓存展示逻辑
            try {
                realGetScreenAdv(path)
            } catch (e: Exception) {

            }
            return adInfo
        }

        return realGetScreenAdv(path)
    }

    /**
     * 从网络获取开屏广告
     * @param path: 开屏广告路径
     */
    private suspend fun realGetScreenAdv(path: String): LaunchAdv {
        return Http.post(path)
            .connectTimeout(3 * 1000)
            .readTimeout(3 * 1000)
            .toResponse<LaunchAdv>().await().apply {
                // 请求成功后缓存数据
                HttpCacheManager.put(BuildConfig.BASE_URL + path, GsonUtils.toJson(this))
            }
    }

    //推荐圈子
    suspend fun getRecommendCircle(
        page: Int,
        pageSize: Int = Constant.PAGE_SIZE
    ): RecommendCircle {
        return Http.post("circle/recommend_circle_v2")
            .add("page", page)
            .add("page_size", pageSize)
            .toResponse<RecommendCircle>().await()
    }

    /**
     * 获取指定类型的互助群列表
     * @param type: 互助群的类型
     */
    suspend fun getHelperGroupList(type: Int, page: Int, pageSize: Int = 10): HelpGroupBean {
        return HttpBusiness.post("category_group_list")
            .add("cate_id", type)
//            .add("page", page)
//            .add("page_size", pageSize)
            .toResponse<HelpGroupBean>().await()
    }

    /**
     * 获取圈子配置信息
     */
    suspend fun getCircleConfig(): CircleConfigBean {
        return HttpBusiness.post("tab/join_group")
            .toResponse<CircleConfigBean>().await()
    }

}