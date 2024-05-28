package com.yxbabe.xiaohuojian.viewmodel

import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.common.CommonViewModel
import com.yxbabe.xiaohuojian.api.MainApi
import com.jmbon.middleware.config.MMKVKey
import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel : CommonViewModel() {

    val firstShowFlow = MutableStateFlow(true)


    private fun activeDevice(result: () -> Unit) {
        UMConfigure.getOaid(Utils.getApp()) { oaid ->
            viewScope().launch {
                //获取oaid
                val ip = "" //NetworkUtils.getIPAddress(true) wifi是获取局域网，ip让后台服务器获取
                var sysVersion = DeviceUtils.getSDKVersionName()
                var deviceModel = DeviceUtils.getModel()
                launchWithFlow({
                    MainApi.syncDevice(
                        oaid,
                        ip,
                        sysVersion,
                        deviceModel
                    )
                }).next {
                    true.saveToMMKV(MMKVKey.ACTIVATE_DEVICE)
                    result()
                }
            }
        };

    }
}