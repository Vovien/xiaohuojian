package com.jmbon.middleware.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.apkdv.mvvmfast.ktx.launchWithFlow
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.api.API
import com.jmbon.middleware.bean.MiniParam
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

enum class MiniTypeEnum(val value: String) {
    /**
     * 邀您入群
     */
    MINI_TYPE_INVITE_GROUP("invite_group"),
    
    /** 享受专业生育服务 */
    MINI_TYPE_FERTILITY_SERVICE("fertility_service"),
}

@OptIn(DelicateCoroutinesApi::class)
fun toWxMiniProgram(
    context: Context, type: String = "", groupName: String = "", pregnancyType: Int = 0
) {
    GlobalScope.launch {
        launchWithFlow({ API.getMiniParameter() }).netCatch {
            toMiniProgram(context, type, groupName, pregnancyType, null)
        }.next {
            toMiniProgram(context, type, groupName, pregnancyType, this)
        }
    }
}

private fun toMiniProgram(
    context: Context,
    type: String = "",
    groupName: String = "",
    pregnancyType: Int = 0,
    miniProgram: MiniParam? // 如果为空则使用默认参数
) {
    val basePath = if (miniProgram != null && miniProgram.path.isNotEmpty()) {
        miniProgram.path
    } else {
        "pages/conversion/conversion"
    }
    val mimiId = if (miniProgram != null && miniProgram.miniProgramId.isNotEmpty()) {
        miniProgram.miniProgramId
    } else {
        BuildConfig.WECHAT_MINI_PROGRAM_ID
    }
    val newGroupName = groupName.addLastChar("群")
    val uriString = if (newGroupName.isNotEmpty()) Uri.encode(newGroupName) else ""
    val path =
        "${basePath}?app_type=9&pregnancy_type=${pregnancyType}&type=${type}&group_name=${uriString}"
    val req = WXLaunchMiniProgram.Req()
    req.userName = mimiId  // 填小程序原始id
    Log.e("MiniProgramPath", path)
    req.path = path //拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
    req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE // 可选打开 开发版，体验版和正式版
    WxMiniProgram.createWx(context)?.sendReq(req)
}

fun String.addLastChar(newStr: String): String {
    return if (this.isNotEmpty()) {
        if (this.last().toString() == newStr) {
            this
        } else {
            this + newStr
        }
    } else ""
}


object WxMiniProgram {
    private var wxAPI: IWXAPI? = null
    fun createWx(context: Context): IWXAPI? {
        if (wxAPI != null) {
            return wxAPI!!
        }
        val createWXAPI = WXAPIFactory.createWXAPI(context, BuildConfig.WECHAT_APP_ID)
        return if (createWXAPI.isWXAppInstalled) {
            wxAPI = createWXAPI
            wxAPI
        } else {
            "请您先安装微信".showToast()
            null
        }
    }
}