package com.jmbon.middleware.model

import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.event.SingleLiveEvent
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.network.entity.EmptyData
import com.jmbon.middleware.api.API
import com.jmbon.middleware.bean.ReportTypeData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf

class ReportModel : BaseViewModel() {

    val getReportResult = SingleLiveEvent<MutableList<ReportTypeData.Report>>()

    /**
     * 获取举报分类
     */
    fun getReportType() {
        val list = mutableListOf<ReportTypeData.Report>()
        list.add(ReportTypeData.Report(1, "存在违反国家法律法规的内容"))
        list.add(ReportTypeData.Report(2, "存在色情、淫秽、低俗等不适内容"))
        list.add(ReportTypeData.Report(3, "存在辱骂、中伤等不友善内容"))
        list.add(ReportTypeData.Report(4, "存在广告、营销内容"))
        list.add(ReportTypeData.Report(5, "存在内容质量、安全性问题"))
        getReportResult.postValue(list)
        defLayout.showContent.call()
//        launchOnlyResult({
//            API.getReportType()
//        }, {
//            getReportResult.postValue(it)
//            defLayout.showContent.call()
//        }, {
//            it.message.showToast()
//            defLayout.showError.call()
//        }, {}, false)
    }


    /**
     * 群消息举报
     */
    fun groupMessageReport(
        reasonId: Int,
        number: String,
        targetContent: String,
        targetId: String,
        type: String, reason: String = "",
        reportedUid: String = "0"
    ) =
        launchWithFlow({
            delay(200)
            EmptyData()
//            API.groupMessageReport(
//                reasonId,
//                number,
//                type,
//                targetContent,
//                targetId,
//                reason,
//                reportedUid
//            )
        }, {
            it.message.showToast()
        }, handleError = false)
}

