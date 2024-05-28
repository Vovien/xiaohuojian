package com.yxbabe.xiaohuojian.router

import com.alibaba.android.arouter.launcher.ARouter

/******************************************************************************
 * Description: 路由定义
 *
 * Author: jhg
 *
 * Date: 2023/9/20
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object AppRouter {

    const val APP_HELP_GROUP = "/app/helpGroup"

    /**
     * 跳转到助孕圈子页面
     * @param title: 助孕圈子标题
     * @param type: 助孕圈子类型
     */
    fun toHelpGroup(title: String, type: Int) {
        ARouter.getInstance()
            .build(APP_HELP_GROUP)
            .withString("title", title)
            .withInt("type", type)
            .navigation()
    }
}