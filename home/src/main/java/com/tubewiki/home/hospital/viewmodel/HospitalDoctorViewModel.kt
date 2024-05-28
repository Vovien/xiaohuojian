package com.tubewiki.home.hospital.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.tubewiki.home.api.WikiApi

/**
 * @author : leimg
 * time   : 2022/7/27
 * desc   :
 * version: 1.0
 */
class HospitalDoctorViewModel : BaseViewModel() {
    /**
     * 医院详情
     * hasLocal 1定位，0未定位
     */
    fun hospitalDetail(
        hospitalId: Int,

        ) = launchWithFlow({
        WikiApi.hospitalDetail(
            hospitalId
        )
    }, handleError = false)

    /**
     * 医院详情
     * hasLocal 1定位，0未定位
     */
    fun doctorDetail(
        doctorId: Int,

        ) = launchWithFlow({
        WikiApi.doctorDetail(
            doctorId
        )
    }, handleError = false)

    fun doctorAttention(
        doctorId: Int,
        focus: String,
        success: () -> Unit
    ) = launchOnlyResult({
        WikiApi.doctorAttention(doctorId, focus)
    }, {
        success()
    }, handleError = false)
}