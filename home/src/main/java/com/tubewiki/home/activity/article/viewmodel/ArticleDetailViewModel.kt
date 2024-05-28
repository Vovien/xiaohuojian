package com.tubewiki.home.activity.article.viewmodel

import com.jmbon.middleware.common.CommonViewModel
import com.tubewiki.home.api.HomeApi
import kotlinx.coroutines.flow.MutableStateFlow

class ArticleDetailViewModel : CommonViewModel() {

    val firstShowFlow = MutableStateFlow(true)

    fun getDetails(articleId: Int) = launchWithFlow({
        HomeApi.getArticleDetail(articleId)
    })

    /**
     * 文章收藏和取消收藏
     * @date 2023/10/26 10:11
     * @param
     */
    fun articleCollect(articleId: Int, operateType: String) = launchWithFlow({
        HomeApi.collect(articleId, operateType)
    }, handleError = false)

    /**
     * 反馈
     */
    fun feedback(itemId: Int, type: Int) = launchWithFlow({
        HomeApi.feedback(itemId, type)
    }, handleError = false)

    /**
     * 反馈有无帮助
     */
    fun feedbackHelp(itemId: Int, itemTitle: String, isHelpful: Int) = launchWithFlow({
        HomeApi.feedbackHelp(itemId, itemTitle, isHelpful, 1)
    }, handleError = false)

}