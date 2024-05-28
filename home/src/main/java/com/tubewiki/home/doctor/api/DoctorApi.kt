package com.tubewiki.home.doctor.api

import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.tubewiki.home.doctor.bean.ColumnBean
import com.tubewiki.home.doctor.bean.DoctorBean
import com.tubewiki.home.bean.hospital.bean.HospitalDetailBean
import rxhttp.wrapper.param.toResponse

/**
 * 医生相关Api
 * @author MilkCoder
 * @date 2023/6/12
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
object DoctorApi {
    /**
     * 搜索医生
     * @date 2023/7/20 9:08
     */
    suspend fun getDoctorList(
        keyword: String = "",
        type: Int,
        columnIds: String,
        hasLocal: Int,
        cityPinyin: String,
        page: Int,
        pageSize: Int = Constant.PAGE_SIZE
    ): DoctorBean {
        return Http.post("h5/doctor/search")
            .add("keyword", keyword)
            .add("type", type)
            .add("column_ids", columnIds)
            .add("has_local", hasLocal)
            .add("pinyin", cityPinyin)
            .add("page", page)
            .add("pageSize", pageSize)
            .toResponse<DoctorBean>().await()
    }

    /**
     * 同院医生
     */
    suspend fun hospitalDoctorList(
        hospitalId: Int,
        page: Int,
    ): HospitalDetailBean {
        return Http.post("/hospital/doctors")
            .add("hospital_id", hospitalId)
            .add("page", page)
            .add("pageSize", Constant.PAGE_SIZE)
            .toResponse<HospitalDetailBean>().await()
    }

    /**
     * 获取擅长列表
     * @date 2023/7/20 16:00
     */
    suspend fun getColumnList() =
        Http.post("h5/doctor/territory")
            .toResponse<ColumnBean>().await()

}