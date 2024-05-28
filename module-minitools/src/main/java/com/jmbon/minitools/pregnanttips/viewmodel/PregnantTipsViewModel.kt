package com.jmbon.minitools.tubetest.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.jmbon.minitools.pregnanttips.api.PregnantTipsApi
import com.jmbon.minitools.tubetest.api.TubeApi

class PregnantTipsViewModel : BaseViewModel() {

    fun getToolInfo(tool_id:String) = launchWithFlow({
        PregnantTipsApi.getToolInfo(tool_id)
    }, handleError = false)


    fun getAllWeeks() = launchWithFlow({
        PregnantTipsApi.getAllWeeks()
    }, handleError = false)

    fun getWeekData(week: String) = launchWithFlow({
        PregnantTipsApi.getWeekData(week)
    }, handleError = false)


    fun setPreProduction(date: String) = launchWithFlow({
        PregnantTipsApi.setPreProduction(date)
    }, handleError = false)


}