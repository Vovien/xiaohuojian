package com.jmbon.middleware.bury.event

/**
 * 用于埋点的事件
 * @author MilkCoder
 * @date 2023/9/22 14:28
 * @copyright all rights reserved by ManTang
 */
enum class ClickEventEnum(val value: String) {

    /**
     * 点击视频广告详情页广告入口
     */
    EVENT_CLICK_VIDEO_AD_DETAIL_AD("EventVideoAdDetailCommonAd"),

    /**
     * 点击首页通用轮播banner
     */
    EVENT_CLICK_COMMON_BANNER("EventHomeCommonCarouselBanner"),

    /**
     * 点击我的页窄条图片
     */
    EVENT_CLICK_MINE_BANNER("EventMyNarrowBanner"),

    /**
     * 点击注册转化页底部自定义按钮
     */
    EVENT_CLICK_REGISTER_TRANSFORM_BOTTOM_BUTTON("EventRegistrationConversionBottomCustomButton"),

    /**
     * 点击常驻问医生悬浮按钮
     */
    EVENT_PERMANENT_ASK_DOCTOR_FIX_BUTTON("EventPermanentAskDoctorFixButton"),

    /**
     * 点击全部专栏通用轮播banner
     */
    EVENT_ALL_TOPIC_COMMON_CAROUSEL_BANNER("EventAllTopicCommonCarouselBanner"),

    /**
     * 点击试管流程页窄条图片
     */
    EVENT_TEST_TUBE_PROCESS_NARROW_BANNER("EventTestTubeProcessNarrowBanner"),

    /**
     * 点击医院首页窄条图片
     */
    EVENT_HOSPITAL_HOME_NARROW_BANNER("EventHospitalHomeNarrowBanner"),

    /**
     * 点击普通文章通用轮播banner
     */
    EVENT_ORDINARY_ARTICLE_COMMON_CAROUSEL_BANNER("EventOrdinaryArticleCommonCarouselBanner"),

    /**
     * 点击普通文章详情页内容中广告
     */
    EVENT_ORDINARY_ARTICLE_DETAIL_CONTENT_AD("EventOrdinaryArticleDetailContentAd"),

    /**
     * 点击普通文章详情页案例广告
     */
    EVENT_ORDINARY_ARTICLE_DETAIL_CASE_AD("EventOrdinaryArticleDetailCaseAd"),

    /**
     * 点击普通文章详情页群聊
     */
    EVENT_ORDINARY_ARTICLE_DETAIL_GROUP("EventOrdinaryArticleDetailGroup"),

    /**
     * 点击普通文章详情页底部定制方案
     */
    EVENT_ORDINARY_ARTICLE_DETAIL_BOTTOM_PLAN("EventOrdinaryArticleDetailBottomPlan"),

    /**
     * 点击百科文章通用轮播banner
     */
    EVENT_ENCYCLOPEDIA_ARTICLE_COMMON_CAROUSEL_BANNER("EventEncyclopediaArticleCommonCarouselBanner"),

    /**
     * 点击百科文章详情页内容中广告
     */
    EVENT_ENCYCLOPEDIA_ARTICLE_DETAIL_CONTENT_AD("EventEncyclopediaArticleDetailContentAd"),

    /**
     * 点击百科文章详情页案例广告
     */
    EVENT_ENCYCLOPEDIA_ARTICLE_DETAIL_CASE_AD("EventEncyclopediaArticleDetailCaseAd"),

    /**
     * 点击百科文章详情页群聊
     */
    EVENT_ENCYCLOPEDIA_ARTICLE_DETAIL_GROUP("EventEncyclopediaArticleDetailGroup"),

    /**
     * 点击百科文章详情页底部定制方案
     */
    EVENT_ENCYCLOPEDIA_ARTICLE_DETAIL_BOTTOM_PLAN("EventEncyclopediaArticleDetailBottomPlan"),

    /**
     * 点击常见问题页案例广告
     */
    EVENT_COMMON_PROBLEM_CASE_AD("EventCommonProblemCaseAd"),

    /**
     * 点击助孕指南详情页案例广告
     */
    EVENT_PREGNANCY_GUIDE_DETAIL_CASE_AD("EventPregnancyGuideDetailCaseAd"),

    /**
     * 点击助孕指南详情页底部微信群聊
     */
    EVENT_PREGNANCY_GUIDE_DETAIL_BOTTOM_WECHAT_GROUP("EventPregnancyGuideDetailBottomWeChatGroup"),

    /**
     * 点击助孕指南详情页底部方案
     */
    EVENT_PREGNANCY_GUIDE_DETAIL_BOTTOM_PLAN("EventPregnancyGuideDetailBottomPlan"),

    /**
     * 点击首页好孕群聊
     */
    EVENT_HOME_GOOD_PREGNANCY_GROUP("EnterHomeGoodPregnancyGroup"),

    /**
     * 点击视频详情页悬浮标签栏
     */
    EVENT_VIDEO_DETAIL_FIX_TAG("EventVideoDetailFixTag"),

    /**
     * 点击医生详情页底部预约医生
     */
    EVENT_DOCTOR_DETAIL_BOTTOM_RESERVE_DOCTOR("EventDoctorDetailBottomReserveDoctor"),

    /**
     * 点击医院详情页底部预约医院
     */
    EVENT_HOSPITAL_DETAIL_BOTTOM_RESERVE_HOSPITAL("EventHospitalDetailBottomReserveHospital")

}