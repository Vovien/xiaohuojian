package com.jmbon.minitools.base.util

import android.app.Activity
import android.content.Intent
import com.alibaba.android.arouter.launcher.ARouter
import com.jmbon.minitools.base.bean.ToolInfoBean
import com.jmbon.minitools.base.dialog.MiniAppMenuDialog
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.lxj.xpopup.XPopup

/**
 * @author : leimg
 * time   : 2022/12/13
 * desc   :
 * version: 1.0
 */
object MiniMenuUtils {

    fun showMenuDialog(
        context: Activity, toolInfo: ToolInfoBean.Tool, type: Int = 0
    ) {
        XPopup.Builder(context)
            .enableDrag(false)
            .moveUpToKeyboard(false)
            .dismissOnTouchOutside(true)
            .isDestroyOnDismiss(true)
            .asCustom(
                MiniAppMenuDialog(context, toolInfo.toolId, toolInfo.name, toolInfo.icon,
                    {
                        //重试
                        context.finish()
                        if (type == 0) {
                            //试管自测重试
                            ARouter.getInstance().build(TubeArouterConstant.TUBE_INDEX)
                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).navigation()
                        }
                    },
                    {
                    })
            )
            .show()
    }


}