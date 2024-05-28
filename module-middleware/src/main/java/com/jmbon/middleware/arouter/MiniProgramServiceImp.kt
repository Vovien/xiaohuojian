package com.jmbon.middleware.arouter

import android.content.Context
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action


@Route(path = "/miniprogram/start/service", name = "启动小程序 dialog")
class MiniProgramServiceImp : MiniProgramService {

    override fun startMiniProgram(programId: String, miniType: Int, param: Bundle?) {
        Action {
            if (programId == "-100") {//miniType默认是1，音乐是2
                //跳转本地音乐模块
                ARouter.getInstance().build("/music/category/list").navigation()
            } else if (programId == "-200") {
                //跳转到怀孕自测
                ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                    .toTestTube("pregnancy")
            } else if (programId == "2021102900000003") {// 2021102900000003是试管自测 miniType默认是1，音乐是2
                //跳转本地试管自测模块
                ARouter.getInstance().build("/minitools/tubetest/tube_index").navigation()
            } else if (programId == "2022040600000001") {// 2022040600000001是孕期提醒 miniType默认是1，音乐是2
                //跳转本地孕期提醒模块
                ARouter.getInstance().build("/minitools/pregnant_tips/detail").navigation()
            } else if (programId == "2022040100000001") {// 2022040100000001 能不能吃
                val router = param?.getString("page")
                if (router.isNotNullEmpty() && router!!.contains("pages/foodDetail/foodDetail")) {
                    val id = router!!.split("=")[1].toInt()
                    ARouter.getInstance()
                        .build("/minitools/food_wiki/food_detail")
                        .withInt("params", id).navigation()
                } else {
                    //跳转本地能不能吃模块
                    ARouter.getInstance().build("/minitools/food_wiki/index")
                        .navigation()
                }
            } else if (programId == "2021102900000004") {
                //原小程序医生调整
                BannerHelper.toDoctorList()
            } else if (programId == "2021102900000005") {
                //原小程序医院调整
                BannerHelper.toHospitalList()
            }else {
                startMiniProgram(programId, param)
            }
        }.logInToIntercept()
    }


    override fun startMiniProgram(programId: String, param: Bundle?) {
        Action {
            if (programId == "-100") {//-100是胎教音乐  miniType默认是1，音乐是2
                //跳转本地音乐模块
                ARouter.getInstance().build("/music/category/list").navigation()
                return@Action
            } else if (programId == "-200") {
                //跳转到怀孕自测
                ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                    .toTestTube("pregnancy")
                return@Action
            } else if (programId == "2021102900000003") {// 2021102900000003是试管自测 miniType默认是1，音乐是2
                //跳转本地试管自测模块
                ARouter.getInstance().build("/minitools/tubetest/tube_index").navigation()
                return@Action
            } else if (programId == "2022040600000001") {// 2022040600000001是孕期提醒 miniType默认是1，音乐是2
                //跳转本地孕期提醒模块
                ARouter.getInstance().build("/minitools/pregnant_tips/detail").navigation()
                return@Action
            } else if (programId == "2022040100000001") {// 2022040100000001 能不能吃
                //跳转本地能不能吃模块
                val router = param?.getString("page")
                if (router.isNotNullEmpty() && router!!.contains("pages/foodDetail/foodDetail")) {
                    val id = router!!.split("=")[1].toInt()
                    ARouter.getInstance()
                        .build("/minitools/food_wiki/food_detail")
                        .withInt("params", id).navigation()
                } else {
                    //跳转本地能不能吃模块
                    ARouter.getInstance().build("/minitools/food_wiki/index")
                        .navigation()
                }
                return@Action
            }else if (programId == "2021102900000004") {
                //原小程序医生调整
                BannerHelper.toDoctorList()
            } else if (programId == "2021102900000005") {
                //原小程序医院调整
                BannerHelper.toHospitalList()
            }
            "小程序跳转异常".showToast()
        }.logInToIntercept()
    }

    override fun init(context: Context?) {

    }


}