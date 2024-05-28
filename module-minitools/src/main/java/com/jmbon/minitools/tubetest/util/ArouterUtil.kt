package com.jmbon.minitools.tubetest.util

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.minitools.pregnancy.util.PregnancyRouterConstant.TUBE_PREGNANCY_RESULT

/**
 * @author : leimg
 * time   : 2022/12/8
 * desc   :
 * version: 1.0
 */
object ArouterUtil {

    fun toActivity(path: String) {
        ARouter.getInstance().build(path).navigation()
    }

    /**
     * 怀孕结果
     * @date 2023/11/6 18:06
     */
    fun toPregnancyForResult() {
        ARouter.getInstance().build(TUBE_PREGNANCY_RESULT).navigation()
    }

    fun toActivityForResult(context: AppCompatActivity, requestCode: Int, path: String) {
        ARouter.getInstance().build(path).navigation(context, requestCode)
    }

    fun toProblemActivity(index: Int) {
        val problemList = TubeConstant.problemList
        if (index >= 0 && problemList.isNotEmpty() && problemList.size > index) {
            when (problemList[index]) {
                167 -> ARouter.getInstance().build(TubeArouterConstant.TUBE_SPERMS_PREG_PROBLEM)
                    .navigation() //男方问题
                168 -> ARouter.getInstance().build(TubeArouterConstant.TUBE_ZG_PREG_PROBLEM)
                    .navigation() //子宫问题
                170 -> {
                    //染色体异常
                    problemList.removeAt(index)
                    toProblemActivity(0)
                }
                169 -> ARouter.getInstance().build(TubeArouterConstant.TUBE_LC_PREG_PROBLEM)
                    .navigation() //卵巢问题
                171 -> ARouter.getInstance().build(TubeArouterConstant.TUBE_SLG_PREG_PROBLEM)
                    .navigation() //输卵管异常
                else -> if (TubeConstant.resultType == "pregnancy") {
                    toPregnancyForResult()
                } else {
                    ARouter.getInstance().build(TubeArouterConstant.TUBE_PREG_NUM_PROBLEM)
                        .navigation() //其他原因
                }
            }
        } else {
            if (TubeConstant.resultType == "pregnancy") {
                toPregnancyForResult()
            } else {
                ARouter.getInstance().build(TubeArouterConstant.TUBE_PREG_NUM_PROBLEM)
                    .navigation() //其他原因
            }

        }

    }

}