package com.jmbon.middleware.arouter.service

import android.app.Activity
import android.content.Context
import android.os.Parcelable
import com.alibaba.android.arouter.facade.template.IProvider
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.middleware.bean.Form

/******************************************************************************
 * Description: 小工具模块Provider
 *
 * Author: jhg
 *
 * Date: 2023/4/24
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
interface IMiniToolProvider : IProvider {

    override fun init(context: Context?) {

    }

    /**
     * 跳转到选择首页
     */
    fun toChooseHome(activity: Activity? = null, withFinish: Boolean = false)

    /**
     * 跳转到咨询求助页面
     * @param advisoryId: 咨询id
     */
    fun toAdvisoryHelp(advisoryId: Int = 0, questionId: Int = 0, question: String = "")

    /**
     * 跳转试管自测页面
     * @date 2023/11/6 17:48
     * @param resultType 怀孕结果
     */
    fun toTestTube(resultType: String? = null)

    /**
     * 跳转到自测页面
     */
    fun toSelfTest(formList: ArrayList<Form>?, source: String? = "")
}