package com.jmbon.middleware.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.*
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.Utils
import com.jmbon.middleware.bean.LocationInfoBean
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.location.DLocationTools
import com.jmbon.widget.GeneralDialog
import java.util.*

/******************************************************************************
 * Description: 位置信息辅助类
 *
 * Author: jhg
 *
 * Date: 2023/4/19
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class LocationHelper {

    companion object {

        /**
         * 获取位置坐标
         */
        @SuppressLint("MissingPermission")
        fun getLocationCoordinate(
            context: Context,
            reason: String = "为了展示更准确的医院信息, 需要获取您的位置权限",
            listener: (longitude: Double, latitude: Double) -> Unit
        ) {
            if (PermissionUtils.checkPermission(
                    context, mutableListOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                ) || reason.isNullOrBlank()
            ) {
                realGetLocationCoordinate(context, listener)
                return
            }

            if (!reason.isNullOrBlank()) {
                GeneralDialog.showDialog(
                    ActivityUtils.getTopActivity(),
                    title = "申请权限说明",
                    content = reason,
                    confirmBtnText = "好的",
                    isHideCancel = true,
                    dismissOnTouchOutside = false,
                    confirmListener = {
                        realGetLocationCoordinate(context, listener)
                    })
            }
        }

        /**
         * 获取位置坐标
         */
        @SuppressLint("MissingPermission")
        fun realGetLocationCoordinate(
            context: Context,
            listener: (longitude: Double, latitude: Double) -> Unit
        ) {
            PermissionUtils.requestPermission(context, mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), {
                val locationManager =
                    Utils.getApp().getSystemService(Context.LOCATION_SERVICE) as? LocationManager
                        ?: kotlin.run {
                            listener(0.0, 0.0)
                            return@requestPermission
                        }

                val providerList = locationManager.getProviders(true)
                // 寻找最精确的位置
                var bestLocation: Location? = null
                for (it in providerList) {
                    val currentLocation = locationManager.getLastKnownLocation(it) ?: continue
                    if (bestLocation == null || currentLocation.accuracy < bestLocation.accuracy) {
                        bestLocation = currentLocation
                    }
                }

                bestLocation?.let {
                    if (CommonViewModel.locationLD.value?.citySimpleName.isNullOrEmpty()) {
                        val city = DLocationTools.getLocality(
                            context,
                            it.latitude,
                            it.longitude
                        )
                        CommonViewModel.locationLD.value?.citySimpleName = LocationInfoBean.getSimpleCityName(city)
                    }
                    CommonViewModel.locationLD.value?.longitude = it.longitude
                    CommonViewModel.locationLD.value?.latitude = it.latitude
                }
                listener(bestLocation?.longitude ?: 0.0, bestLocation?.latitude ?: 0.0)
            }) {
                listener(0.0, 0.0)
            }
        }
    }
}