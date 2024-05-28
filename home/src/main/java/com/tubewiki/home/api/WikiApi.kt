package com.tubewiki.home.api

import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.tubewiki.home.bean.hospital.bean.AllHospitalBean
import com.tubewiki.home.bean.hospital.bean.CityPy
import com.tubewiki.home.bean.hospital.bean.DoctorDetailBean
import com.tubewiki.home.bean.hospital.bean.HospitalDetailBean
import com.tubewiki.home.bean.hospital.bean.HospitalDoctorKeyWork
import com.tubewiki.home.bean.hospital.bean.HospitalLevelBean
import rxhttp.wrapper.param.toResponse

object WikiApi {

    /**
     * 获取城市对应拼音
     * @date 2023/7/20 14:58
     */
    suspend fun getCityPinyin(
        city_name: String
    ): CityPy {
        return Http.post("h5/common/get_pinyin")
            .add("city_name", city_name)
            .toResponse<CityPy>().await()
    }

    /**
     * 搜索医院
     * @date 2023/7/21 15:09
     */
    suspend fun getHospitalList(
        keyword: String = "",
        type: Int,
        levelIds: String,
        hasLocal: Int,
        cityPinyin: String,
        page: Int,
        lng: Double,
        lat: Double,
        pageSize: Int = Constant.PAGE_SIZE
    ): AllHospitalBean {
        return Http.post("h5/hospital/search")
            .add("keyword", keyword)
            .add("type", type)
            .add("level_ids", levelIds)
            .add("has_local", hasLocal)
            .add("pinyin", cityPinyin)
            .add("page", page)
            .add("pageSize", pageSize)
            .add("lng", lng)
            .add("lat", lat)
            .toResponse<AllHospitalBean>().await()
    }

    /**
     * 获取医院等级
     */
    suspend fun getLocalHospitalLevel(): HospitalLevelBean {
        return Http.post("hospital/levels")
            .toResponse<HospitalLevelBean>().await()
    }
    /**
     * 医院详情
     */
    suspend fun hospitalDetail(
        hospitalId: Int,
    ): HospitalDetailBean {
        return Http.post("hospital/detail")
            .add("hospital_id", hospitalId)
            .toResponse<HospitalDetailBean>().await()
    }

    /**
     * 医生详情
     */
    suspend fun doctorDetail(
        doctorId: Int,
    ): DoctorDetailBean {
        return Http.post("doctor/detail")
            .add("doctor_id", doctorId)
            .toResponse<DoctorDetailBean>().await()
    }

    suspend fun doctorAttention(
        doctorId: Int,
        focus: String,
    ): EmptyData {
        return Http.post("doctor/focus")
            .add("doctor_id", doctorId)
            .add("type", focus)
            .toResponse<EmptyData>().await()
    }

    suspend fun searchHot(): HospitalDoctorKeyWork {
        return Http.post("h5/common/hots")
            .toResponse<HospitalDoctorKeyWork>().await()
    }

}