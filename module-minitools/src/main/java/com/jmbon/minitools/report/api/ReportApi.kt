package com.jmbon.minitools.report.api

import com.jmbon.middleware.config.network.Http
import com.jmbon.minitools.report.bean.FertilityAbilityTestResultBean
import com.jmbon.minitools.report.bean.FertilityGroupChatListBean
import com.jmbon.minitools.report.bean.TestResultPictureBean
import rxhttp.wrapper.param.toResponse

object ReportApi {

    /**
     * 获取生育自测结果
     * @param type: 对应分类id
     * @param values: 选择对应的值, 采用逗号分隔
     */
    suspend fun getFertilityAbilityTestResult(type: Int, values: String): FertilityAbilityTestResultBean {
        return Http.post("app/v12/procreate/test")
            .add("type", type)
            .add("vals", values)
            .toResponse<FertilityAbilityTestResultBean>().await()
    }

    suspend fun getGroupChatList(count: Int): FertilityGroupChatListBean {
        return Http.post("app/v12/common/get_group_list")
            //.add("num", count)
            .toResponse<FertilityGroupChatListBean>().await()
    }

    /**
     * 获取测试结果图片
     * @param id: 测试结果id
     */
    suspend fun getTestResultPicture(id: Int): TestResultPictureBean {
        return Http.post("app/v12/procreate/get_result_image")
            .add("id", id)
            .toResponse<TestResultPictureBean>().await()
    }
}