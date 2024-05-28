package com.jmbon.minitools.router

import android.os.Parcelable
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.bean.Form
import com.jmbon.minitools.report.bean.FertilityAbilityTestResultBean
import com.jmbon.minitools.report.bean.TemplateTypeEnum
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.bean.ForecastResultBean

object MiniToolRouter {

    /**
     * 获取方案结果页
     */
    const val MINI_TOOL_GET_SCHEME_RESULT = "/miniTool/getSchemeResult"

    /**
     * 生育力测试结果页
     */
    const val MINI_TOOL_FERTILITY_ABILITY_TEST_RESULT = "/miniTool/fertilityAbilityTestResult"

    /**
     * 公共模板
     */
    const val MINI_TOOL_PUBLIC_TEMPLATE = "/miniTool/publicTemplate"

    /**
     * 跳转到公共模板
     */
    fun toPublicTemplate(
        type: TemplateTypeEnum,
        data: Parcelable?,
        formList: ArrayList<Form>? = null,
        source: String? = null
    ) {
        ARouter.getInstance().build(MINI_TOOL_PUBLIC_TEMPLATE)
            .withInt("type", type.value)
            .withParcelable("data", data)
            .withParcelableArrayList("formList", formList)
            .withString("source", source)
            .navigation()
    }

    fun selfTest(formList: ArrayList<Form>?, source: String?) {
        ARouter.getInstance().build(TubeArouterConstant.MINI_TOOL_SELF_TEST)
            .withParcelableArrayList("formList", formList)
            .withString("source", source)
            .navigation()
    }


    /**
     * 跳转到生育力测试结果页
     */
    fun toFertilityAbilityTestResult(fertilityAbilityTestResultBean: FertilityAbilityTestResultBean?) {
        ARouter.getInstance().build(MINI_TOOL_FERTILITY_ABILITY_TEST_RESULT)
            .withParcelable("fertilityAbilityTestResultBean", fertilityAbilityTestResultBean)
            .navigation()
    }

    /**
     * 跳转方案结果页面
     */
    fun toGetSchemeResult(resultInfo: ForecastResultBean?) {
        ARouter.getInstance().build(MINI_TOOL_GET_SCHEME_RESULT)
            .withParcelable("resultInfo", resultInfo)
            .navigation()
    }
}