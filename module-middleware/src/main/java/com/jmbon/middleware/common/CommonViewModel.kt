package com.jmbon.middleware.common

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.launchWithFlow
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.utils.getString
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.jmbon.middleware.api.API
import com.jmbon.middleware.bean.GeneralConfBean
import com.jmbon.middleware.bean.LocationInfoBean
import com.jmbon.middleware.bean.PopupImageBean
import com.jmbon.middleware.bean.VersionInfo
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.config.MMKVKey
import com.jmbon.middleware.config.network.Http
import com.jmbon.widget.download_progress.DownloadProgressEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import rxhttp.toDownload


/******************************************************************************
 * Description: 通用ViewModule, 解決业务层面的通用问题，如定位等
 *
 * Author: jhg
 *
 * Date: 2023/3/10
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
open class CommonViewModel : BaseViewModel() {

    /**
     * 当前的页码
     */
    var currentPage = 1

    /**
     * 分页大小
     */
    var pageSize = 12

    /**
     * 当前位置的定位信息
     */
    val currentLocation = MutableLiveData<LocationInfoBean?>()

    protected val _requestFinishLD = MutableLiveData<Boolean>()
    val requestFinishLD: LiveData<Boolean> = _requestFinishLD

    /**
     * 获取公共banner
     * @date 2023/8/4 14:38
     */
    fun getBannerList() = launchWithFlow({
        API.getCommonBanner()
    }, handleError = false)


    /**
     * 开始定位
     */
    fun startLocation() {
        // 如果用户曾经选择了城市, 则默认返回选择的城市信息
        val locationInfo = MMKVKey.LAST_LOCATION.getString()
        if (!locationInfo.isNullOrBlank()) {
            GsonUtils.getGson().fromJson(locationInfo, LocationInfoBean::class.java)?.let {
                locationLD.postValue(it)
                return
            }
        }

        viewModelScope.launch {
            launchWithFlow(
                {
                    API.getAddressInfoByIP()
                }, {
                    locationLD.postValue(null)
                }
            ).next {
                address.citySimpleName = LocationInfoBean.getSimpleCityName(address.cityName)
                locationLD.postValue(address)
            }
        }
    }

    /**
     * 开始真实位置定位
     */
    fun startRealLocation() {
        viewModelScope.launch {
            launchWithFlow(
                {
                    API.getAddressInfoByIP()
                }, {
                    currentLocation.postValue(null)
                }
            ).next {
                address.citySimpleName = LocationInfoBean.getSimpleCityName(address.cityName)
                currentLocation.postValue(address)
            }
        }
    }

    fun generalConf() = launchWithFlow({ API.generalConf() })

    fun downloadApp(version: VersionInfo.Version, context: Context) {
        val path = "${context.externalCacheDir}/${version.versionCode}.apk"
        launchOnlyResult({
            Http.get(version.download)
                .toDownload(
                    path
                ) {
                    // 下载进度
                    EventBus.getDefault().post(
                        DownloadProgressEvent(
                            it.progress,
                            if (it.progress >= 100) path else ""
                        )
                    )
                    LogUtils.i(it)
                }.await()
        }, {
            LogUtils.i(it)
            AppUtils.installApp(path)
        }, isShowDialog = false, handleError = false)

    }

    fun getPopupImage() {
        viewModelScope.launch {
            launchWithFlow({ API.getPopupImage() }, {
                popupImageFlow.value = null
            }, handleError = false).next {
                popupImageFlow.value = this
            }
        }
    }


    companion object {
        val popupImageFlow = MutableStateFlow<PopupImageBean?>(null)

        /**
         * 定位结果
         */
        val locationLD = MutableLiveData<LocationInfoBean?>()

        /**
         * 版本更新
         */
        val versionUpdate = MutableStateFlow<VersionInfo.Version?>(null)

        /**
         * 公共配置数据
         */
        val configFlow = MutableStateFlow<GeneralConfBean?>(null)
            get() {
                if (field.value == null) {
                    field.value = getConfigInfo()
                }
                return field
            }

        /**
         * 读取配置信息
         */
        private fun getConfigInfo(): GeneralConfBean? {
            try {
                return GsonUtils.fromJson(
                    MMKVKey.COMMON_CONFIG.getString(),
                    GeneralConfBean::class.java
                )
            } catch (e: Exception) {
                e.printStackTrace(System.out)
            }

            return null
        }

        fun updateLocation(locationInfo: LocationInfoBean?) {
            if (locationInfo == null) {
                return
            }

            locationInfo.citySimpleName = LocationInfoBean.getSimpleCityName(locationInfo.cityName)
            if (locationInfo != locationLD.value) {
                locationLD.postValue(locationInfo)
            }
            GsonUtils.getGson().toJson(locationInfo).saveToMMKV(MMKVKey.LAST_LOCATION)
        }

        /**
         * 激活设备
         */
        fun activeDevice(oaid: String?) {
            GlobalScope.launch {
                val sysVersion = DeviceUtils.getSDKVersionName()
                val deviceModel = DeviceUtils.getModel()
                launchWithFlow({
                    API.oaidReport(sysVersion, deviceModel, oaid)
                    API.activeApp(oaid)
                }).next {}
            }
        }
    }
}