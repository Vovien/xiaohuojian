package com.jmbon.minitools.tubetest.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.jmbon.minitools.tubetest.api.TubeApi

class TubeViewModel : BaseViewModel() {

    fun getBaseInfo() = launchWithFlow({
        TubeApi.getBaseInfo()
    }, handleError = false)

    fun getToolInfo(tool_id:String) = launchWithFlow({
        TubeApi.getToolInfo(tool_id)
    }, handleError = false)

    fun startForecast(
        base_info: String,
        problems: String,
        sperms: String,
        ovarys: String,
        uteruss: String,
        fallopians: String,
        pregnancy_number: String,
        birth_number: String,
        tubetest_number: String,
        tubetest_type: String,
        programme: String,
        demands: String,
        city_id: String,
        consider_tube: String,
        tube_city: String,
        tube_cost: String,
        other_problem: String,
        other_ovarys: String,
        other_uteruss: String,
        other_fallopians: String,
        other_demands: String,
    ) = launchWithFlow({
        TubeApi.startForecast(
            base_info,
            problems,
            sperms,
            ovarys,
            uteruss,
            fallopians,
            pregnancy_number,
            birth_number,
            tubetest_number,
            tubetest_type,
            programme,
            demands,
            city_id,
            consider_tube,
            tube_city,
            tube_cost,
            other_problem,
            other_ovarys,
            other_uteruss,
            other_fallopians,
            other_demands
        )
    }, handleError = false)


}