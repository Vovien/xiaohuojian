package com.jmbon.minitools.tubetest.api

import com.blankj.utilcode.util.ActivityUtils
import com.jmbon.middleware.config.network.HttpV2
import com.jmbon.minitools.base.bean.ToolInfoBean
import com.jmbon.minitools.tubetest.bean.BaseInfoBean
import com.jmbon.minitools.tubetest.bean.ForecastResultBean
import rxhttp.wrapper.param.toResponse

object TubeApi {

    /**
     * 获取基本信息
     */
    suspend fun getBaseInfo(): BaseInfoBean {
        return HttpV2.post("applet/tubetest/base_info")
            .toResponse<BaseInfoBean>().await()
    }
  /**
     * 获取小工具基本信息
     */
    suspend fun getToolInfo(tool_id:String): ToolInfoBean {
        return HttpV2.post("common/get_tool_info")
            .add("tool_id",tool_id)
            .toResponse<ToolInfoBean>().await()
    }


    /**
     * 试管自测提交
     * base_info 1,35,96 是	Text 基本信息，年龄 身高，体重对应id用英文逗号隔开
     * problems 166,169  对应备孕难题id，多个用英文逗号隔开
     * sperms 对应精子问题id，多个用英文逗号隔开
     * ovarys 对应卵巢问题id，多个用英文逗号隔开
     * uteruss 对应子宫问题id，多个用英文逗号隔开
     * fallopians 对应输卵管问题id，多个用英文逗号隔开
     * pregnancy_number 对应怀孕次数id
     * birth_number  对应生育次数id
     * tubetest_number 对应做试管次数id
     * tubetest_type 对应选择的试管类型id
     * programme 对应选择的促排方案id
     * demands 对应选择的试管需求id,多个用英文逗号隔开
     * city_id 对应选择的城市id
     * consider_tube 对应是否有意愿做试管,1：有意愿，2：没有意愿，3：考虑中
     * tube_city 对应选择愿意时的城市id, 0:未选择，1：省内，2：国内，3：海外
     * tube_cost 对应选择愿意时的的费用选择，0：没有费用，1:5万以下，2:10万以下，3:20万以下，4:30万以下，5:30万以上
     * other_problem 其它怀孕难题
     * other_ovarys 其它卵巢情况
     * other_uteruss 其它子宫情况
     * other_fallopians 其它输卵管情况
     * other_demands 其它试管需求

     */
    suspend fun startForecast(base_info: String,
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
    ): ForecastResultBean {
        return HttpV2.post("applet/tubetest/test")
            .add("base_info", base_info)
            .add("problems", problems)
            .add("sperms", sperms)
            .add("ovarys", ovarys)
            .add("uteruss", uteruss)
            .add("fallopians", fallopians)
            .add("pregnancy_number", pregnancy_number)
            .add("birth_number", birth_number)
            .add("tubetest_number", tubetest_number)
            .add("tubetest_type", tubetest_type)
            .add("programme", programme)
            .add("demands", demands)
            .add("city_id", city_id)
            .add("consider_tube", consider_tube)
            .add("tube_city", tube_city)
            .add("tube_cost", tube_cost)
            .add("other_problem", other_problem)
            .add("other_ovarys", other_ovarys)
            .add("other_uteruss", other_uteruss)
            .add("other_fallopians", other_fallopians)
            .add("other_demands", other_demands)
            .apply {
                // 区分试管流程, inlet_type=2表示定位方案入口进入, 1表示其他入口进入
                if (ActivityUtils.getActivityList()?.find { it1 -> it1.localClassName.contains("GetSchemeDetailActivity") } != null) {
                    add("inlet_type", 2)
                } else {
                    add("inlet_type", 1)
                }
            }
            .toResponse<ForecastResultBean>().await()
    }



}