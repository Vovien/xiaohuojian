package com.jmbon.minitools.pregnancy.api

import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.network.Http
import com.jmbon.minitools.pregnancy.bean.PregnancyResultBean
import rxhttp.wrapper.param.toResponse

object PregnancyApi {

    /**
     * 怀孕自测提交
     */
    suspend fun pregnancySubmit(
        base_info: String,
        problems: String,
        sperms: String,
        ovarys: String,
        uteruss: String,
        fallopians: String,
        other_problem: String,
        other_ovarys: String,
        other_uteruss: String,
        other_fallopians: String,
    ): PregnancyResultBean {
        return Http.post("app/v15/pregnant/test_submit")
            .add("base_info", base_info)
            .add("problems", problems)
            .add("sperms", sperms)
            .add("ovarys", ovarys)
            .add("uteruss", uteruss)
            .add("fallopians", fallopians)
            .add("other_problem", other_problem)
            .add("other_ovarys", other_ovarys)
            .add("other_uteruss", other_uteruss)
            .add("other_fallopians", other_fallopians)
            .add("pregnancy_type", Constant.getDefaultPregnantIntStatus())
            .toResponse<PregnancyResultBean>().await()
    }
}