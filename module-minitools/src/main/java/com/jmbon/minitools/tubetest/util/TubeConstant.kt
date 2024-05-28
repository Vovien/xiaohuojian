package com.jmbon.minitools.tubetest.util

import android.app.Activity
import com.jmbon.minitools.base.bean.ToolInfoBean
import com.jmbon.minitools.tubetest.bean.BaseInfoBean
import com.jmbon.minitools.tubetest.bean.UserInfoBean

/**
 * @author : leimg
 * time   : 2022/12/7
 * desc   :
 * version: 1.0
 */
object TubeConstant {

    var lastActivity: Class<Activity>? = null

    var problemList: MutableList<Int> = mutableListOf()

    var userInfoBean = UserInfoBean(UserInfoBean.UserInfo(30, 160, 50.0f))
    var baseInfoBean = BaseInfoBean()
    var miniApp = ToolInfoBean.Tool()

    /** 结果类型判定 */
    var resultType = ""

    fun clearUserInfoBean() {
        userInfoBean = UserInfoBean(UserInfoBean.UserInfo(30, 160, 50.0f))
    }

}