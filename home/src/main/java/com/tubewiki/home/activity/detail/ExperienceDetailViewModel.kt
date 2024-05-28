package com.tubewiki.home.activity.detail

import com.apkdv.mvvmfast.bean.ResultTwoData
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.home.api.HomeApi
import com.tubewiki.home.bean.ExperienceDetailData
import kotlinx.coroutines.flow.MutableStateFlow

/**
 *
 * @author MilkCoder
 * @date 2023/11/24
 * @copyright All copyrights reserved to ManTang.
 */
class ExperienceDetailViewModel : CommonViewModel() {

    val firstShowFlow = MutableStateFlow(true)

    //    private val _experienceDetailFlow = MutableStateFlow<ExperienceDetailData?>(null)
    val experienceDetailFlow = MutableStateFlow<ExperienceDetailData?>(null)

    /**
     * 获取经验详情
     * @date 2023/11/24 10:02
     * @param experienceId 经验Id
     */
    fun getExperienceDetail(experienceId: Int, topicId: Int) = launchWithFlow({
        val data = HomeApi.getExperienceDetail(experienceId, topicId)
        experienceDetailFlow.value = data
        data
    })
}