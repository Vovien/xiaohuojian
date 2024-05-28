package com.jmbon.middleware.utils

import android.os.Bundle
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.GsonUtils
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.arouter.service.*
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.ItemTypeEnum
import com.jmbon.middleware.bean.WxIdentity
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.valid.action.Action

/******************************************************************************
 * Description: Banner通用的跳转逻辑
 *
 * Author: jhg
 *
 * Date: 2023/3/29
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object BannerHelper {

    /**
     * Banner的跳转逻辑
     * @param bannerInfo: 当前所点击的Banner信息
     */
    fun onClick(bannerInfo: CommonBanner) {
        when (bannerInfo.item_type) {
            ItemTypeEnum.ITEM_TYPE_COLUMN.value -> {
                ArouterUtils.toArticleColumnDetailActivity(bannerInfo.item_id)
            }

            ItemTypeEnum.ITEM_TYPE_ARTICLE.value -> {
                ArouterUtils.toArticleDetailsActivity(bannerInfo.item_id)
            }

            ItemTypeEnum.ITEM_TYPE_EXPERIENCE.value -> {
                ArouterUtils.toExperienceDetailsActivity(bannerInfo.item_id, bannerInfo.topicId)
            }

            ItemTypeEnum.ITEM_TYPE_VIDEO.value -> {
                Action {
                    ARouter.getInstance().build("/video/details")
                        .withInt(TagConstant.VIDEO_ID, bannerInfo.item_id)
                        .navigation()
                }.logInToIntercept()
            }

            ItemTypeEnum.ITEM_TYPE_QUESTION.value -> {
                Action {
                    ARouter.getInstance().build("/question/activity/ask_detail")
                        .withInt(TagConstant.QUESTION_ID, bannerInfo.item_id)
                        .navigation()
                }.logInToIntercept()
            }

            ItemTypeEnum.ITEM_AI_TYPE_HELP.value -> {
                Action {
                    ARouter.getInstance().build("/help/detail/activity")
                        .withInt("id", bannerInfo.item_id)
                        .navigation()
                }.logInToIntercept()
            }

            ItemTypeEnum.ITEM_TYPE_HELP.value -> {
                Action {
                    ARouter.getInstance().build("/circle/ask/details")
                        .withInt(TagConstant.QUESTION_ID, bannerInfo.item_id)
                        .navigation()
                }.logInToIntercept()
            }

            ItemTypeEnum.ITEM_TYPE_GROUP_CHAT.value -> {
                Action {
                    ARouter.getInstance().build("/chat/group/info")
                        .withString(TagConstant.GROUP_NUMBER, bannerInfo.identity)
                        .navigation()
                }.logInToIntercept()
            }

            ItemTypeEnum.ITEM_TYPE_SEARCH_SUBJECT.value -> {
                ARouter.getInstance().build("/middleware/search/activity")
                    .withString(TagConstant.SEARCH_CONTENT, "/search/result/fragment")
                    .withBoolean(TagConstant.DIRECT_SEARCH, true)
                    .withString(TagConstant.SEARCH_KEY, bannerInfo.identity)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_WEB.value -> {
                ARouter.getInstance().build("/webview/activity")
                    .withString(
                        "url",
                        bannerInfo.url
                    )
                    .withString("title", bannerInfo.origin_title)
                    .navigation()
            }

            ItemTypeEnum.ITEM_TYPE_TOAST.value -> {
                if (bannerInfo.identity.isNotEmpty()) {
                    bannerInfo.identity.showToast()
                }
            }

            ItemTypeEnum.ITEM_TYPE_WECHAT_PAGE.value -> {
                try {
                    if (bannerInfo.identity.contains("-|-")) {
                        val arr = bannerInfo.identity.split("-|-")
                        toWxMiniProgram(
                            ActivityUtils.getTopActivity(),
                            type = arr[0],
                            groupName = arr[1]
                        )
                        return
                    }
                    if (JsonUtils.isValidJson(bannerInfo.identity)) {
                        val wxIdentity =
                            GsonUtils.fromJson(bannerInfo.identity, WxIdentity::class.java)
                        val pregnancyType =
                            if (wxIdentity.need_pre_status == 1) Constant.getDefaultPregnantIntStatus() else 0
                        toWxMiniProgram(
                            ActivityUtils.getTopActivity(),
                            type = wxIdentity.page_type,
                            groupName = wxIdentity.group_name,
                            pregnancyType = pregnancyType
                        )
                        return
                    }
                    toWxMiniProgram(
                        ActivityUtils.getTopActivity(),
                        type = bannerInfo.identity
                    )
                } catch (_: Exception) {
                    "数据异常,请稍后重试~".showToast()
                }
            }

            else -> {
                "数据异常,请稍后重试~".showToast()
            }
        }
    }

    /**
     * 医生列表（原小程序）
     * @date 2023/7/25 9:47
     * @param
     */
    fun toDoctorList() {
        ARouter.getInstance().navigation(IHomeProvider::class.java).toDoctorList()
    }

    /**
     * 医院列表（原小程序）
     * @date 2023/7/25 9:48
     */
    fun toHospitalList() {
        ARouter.getInstance().navigation(IHomeProvider::class.java).toHospitalList()
    }
}