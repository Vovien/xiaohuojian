package com.jmbon.minitools.pregnanttips.api

import com.jmbon.middleware.config.network.HttpV2
import com.jmbon.minitools.tubetest.bean.BaseInfoBean
import com.jmbon.minitools.tubetest.bean.ForecastResultBean
import com.jmbon.minitools.base.bean.ToolInfoBean
import com.jmbon.minitools.pregnanttips.bean.WeekBean
import com.jmbon.minitools.pregnanttips.bean.WeekDetailBean

import rxhttp.wrapper.param.toResponse

object PregnantTipsApi {
    /**
     * 获取小工具基本信息
     */
    suspend fun getToolInfo(tool_id:String): ToolInfoBean {
        return HttpV2.post("common/get_tool_info")
            .add("tool_id",tool_id)
            .toResponse<ToolInfoBean>().await()
    }

    /**
     * 获取所有怀孕周
     */
    suspend fun getAllWeeks(): WeekBean {
        return HttpV2.post("applet/pregnancy_tips/get_weeks").toResponse<WeekBean>().await()
    }

    /**
     * 获取孕期日历周详情
     */
    suspend fun getWeekData(week: String): WeekDetailBean {
        return HttpV2.post("applet/pregnancy_tips/get_week_data").add("week", week)
            .toResponse<WeekDetailBean>().await()
    }

    /**
     * 设置孕期日历
     * date :2023-02-15
     */
    suspend fun setPreProduction(date: String): WeekDetailBean {
        return HttpV2.post("applet/pregnancy_tips/set_pre_production").add("date", date)
            .toResponse<WeekDetailBean>().await()
    }


}