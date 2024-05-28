package com.tubewiki.home.router

import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.utils.TagConstant

object WikiRouter {

    /**
     * 医院列表
     */
    const val WIKI_LIST_HOSPITAL = "/wiki/hospitalList"

    /**
     * 医院医生搜索页面
     */
    const val WIKI_SEARCH_HOSPITAL_DOCTOR = "/wiki/searchHospitalDoctor"

    /**
     * 所有医院列表
     */
    const val WIKI_SEARCH_All_HOSPITAL = "/wiki/searchAllHospital"

    /**
     * 搜索医院
     */
    const val WIKI_SEARCH_HOSPITAL = "/wiki/searchHospital"

    /** 医生列表 */
    const val DOCTOR_LIST = "/wiki/doctorList"

    /** 医生搜索 */
    const val DOCTOR_SEARCH = "/wiki/doctorSearch"

    /**
     * 跳转到医院搜索页面
     */
    fun toSearchTubeHospital() {
        ARouter.getInstance().build(WIKI_SEARCH_HOSPITAL)
            .navigation()
    }

    /**
     * 跳转到所有医院列表页面
     */
    fun toAllHospital(){
        ARouter.getInstance().build(WIKI_LIST_HOSPITAL)
            .navigation()
    }

    /**
     * 跳转到所有医生列表
     */
    fun toAllDoctor(){
        ARouter.getInstance().build(DOCTOR_LIST)
            .navigation()
    }

    fun toSearchHospitalDoctor(){
        ARouter.getInstance().build(WIKI_SEARCH_HOSPITAL_DOCTOR)
            .navigation()
    }

    /**
     * 跳转到所有医院搜索页面
     */
    fun toSearchAllHospital(){
        ARouter.getInstance().build(WIKI_SEARCH_All_HOSPITAL)
            .navigation()
    }

    /**
     * 跳转到医院搜索页面
     */
    fun toSearchDoctor() {
        ARouter.getInstance().build(DOCTOR_SEARCH).navigation()
    }

    /**
     * 跳转到医生详情页面
     * @date 2023/7/19 15:35
     * @param
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
}