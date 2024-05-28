package com.jmbon.middleware.arouter

import android.app.Activity
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.R
import com.jmbon.middleware.bean.ArticleList
import com.jmbon.middleware.bean.TubeArticleDetail
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action

/**
 * @author : leimg
 * time   : 2022/5/23
 * desc   :
 * version: 1.0
 */
object ArouterUtils {
    /**
     * 跳转到经验详情页面
     * @date 2023/11/24 10:38
     * @param
     */
    fun toExperienceDetailsActivity(experienceId: Int, topicId: Int=0) {
        ARouter.getInstance().build("/home/experience/details")
            .withInt(TagConstant.EXPERIENCE_ID, experienceId)
            .withInt(TagConstant.TOPIC_ID, topicId)
            .navigation()
    }

    /**
     * 跳转文章详情页
     */
    fun toArticleDetailsActivity(articleId: Int) {

        ARouter.getInstance().build("/home/article/details")
            .withInt(TagConstant.ARTICLE_ID, articleId)
            .navigation()
    }

    /**
     * 跳转到视频详情页面
     * @date 2023/11/15 11:53
     * @param videoId
     */
    fun toVideoDetailActivity(videoId: Int) {
        Action {
            ARouter.getInstance().build("/video/details/activity")
                .withInt(TagConstant.VIDEO_ID, videoId)
                .navigation()
        }.logInToIntercept()
    }

    /**
     * 跳转医生详情页
     */
    fun toDoctorDetailsActivity(doctorId: Int) {

        ARouter.getInstance().build("/hospital/activity/doctor_detail")
            .withInt(TagConstant.DOCTOR_ID, doctorId)
            .navigation()
    }

    /**
     * 跳转医院详情页
     */
    fun toHospitalDetailsActivity(hospitalId: Int) {
        ARouter.getInstance().build("/hospital/activity/hospital_detail")
            .withInt(TagConstant.HOSPITAL_ID, hospitalId)
            .navigation()
    }

    /**
     * 跳转文章详情页
     */
    fun toArticleDetailsActivity(item: TubeArticleDetail) {

        ARouter.getInstance().build("/home/article/details")
            .withInt(TagConstant.ARTICLE_ID, item.id)
            .withParcelable(TagConstant.ARTICLE_CONTENT, item)
            .withTransition(
                R.anim.activity_bottom_in,
                R.anim.activity_background
            )
            .navigation()

    }

    /**
     * 跳转文章专栏详情页
     */
    fun toArticleColumnDetailActivity(topicId: Int) {

        ARouter.getInstance().build("/home/activity/column_detail")
            .withInt(TagConstant.TOPIC_ID, topicId)
            .withTransition(
                R.anim.activity_bottom_in,
                R.anim.activity_background
            )
            .navigation()

    }

    /**
     * 跳转文章详情页 点击评论
     */
    fun toArticleDetailsActivityWithComment(articleId: Int, commentId: Int) {

        ARouter.getInstance().build("/question/activity/answer_detail")
            .withInt(TagConstant.ARTICLE_ID, articleId)
            .withBoolean(TagConstant.SHOW_COMMENT, true)
            .withInt(TagConstant.NEED_TOP_ID, commentId)
            .navigation()

    }

    /**
     * 跳转文章详情页
     */
    fun toArticleDetailsActivity(item: ArticleList) {

        ARouter.getInstance().build("/question/activity/answer_detail")
            .withInt(TagConstant.ARTICLE_ID, item.id)
            .withParcelable(TagConstant.ARTICLE_CONTENT, item)
            .withTransition(
                R.anim.activity_bottom_in,
                R.anim.activity_background
            )
            .navigation()

    }

    /**
     * 跳转隐私协议页面
     */
    fun topPrivacyAgreeActivity(activity: Activity) {

        ARouter.getInstance().build("/app/privacy_agree")
            .withTransition(
                R.anim.xhj_fade_in,
                R.anim.xhj_fade_out
            )
            .navigation(activity, object : NavCallback() {
                override fun onArrival(postcard: Postcard?) {
                    activity.finish()
                }
            })
    }

}