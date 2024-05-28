package com.jmbon.middleware.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.PermissionUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.jmbon.widget.GeneralDialog

/**
 * @author : leimg
 * time   : 2021/5/13
 * desc   :
 * version: 1.0
 */
object PermissionUtils {

    /**
     * 单权限请求判断
     *  action:获取权限成功执行的操作
     *  deniedTips：权限名称
     */
    fun doNeedPermissionAction(
        context: Context,
        permissionStr: String,
        action: () -> Unit,
        functionStr: String,
        permissionName: String
    ) {

        if (PermissionUtils.isGranted(permissionStr)) {
            action()
            return
        }

        PermissionUtils.permission(Manifest.permission.CALL_PHONE)
            .explain { activity, denied, shouldRequest ->

                GeneralDialog.showNormalDialog(
                    activity,
                    "${permissionName}权限申请",
                    "${functionStr}功能需要${permissionName}权限,是否开启权限？",
                    confirmListener = {
                        shouldRequest.start(true)
                    },
                    cancelListener = {
                        shouldRequest.start(false)
                    })

            }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    action()
                    return@callback
                }
                if (deniedForever.isNotEmpty()) {
                    "请手动开启${permissionName}权限".showToast()
                    PermissionPageUtils(context).jumpPermissionPage()
                } else {
                    "${permissionName}权限已拒绝".showToast()
                }
            }
            .request()

    }

    /**
     * 多权限请求判断
     *  action:获取权限成功执行的操作
     *  deniedTips：拒绝后提示的操作
     */
    fun doNeedPermissionAction(
        context: Context,
        permissionStrList: MutableList<String>,
        action: () -> Unit,
        functionStr: String,
        permissionName: String
    ) {

        if (PermissionUtils.isGranted(*permissionStrList.toTypedArray())) {
            action()
            return
        }
        PermissionUtils.permission(*permissionStrList.toTypedArray())
            .explain { activity, denied, shouldRequest ->

                GeneralDialog.showNormalDialog(
                    activity,
                    "${permissionName}权限申请",
                    "${functionStr}功能需要${permissionName}权限,是否开启权限？",
                    confirmListener = {
                        shouldRequest.start(true)
                    },
                    cancelListener = {
                        shouldRequest.start(false)
                    })

            }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    action()
                    return@callback
                }
                if (deniedForever.isNotEmpty()) {
                    "请手动开启${permissionName}权限".showToast()
                    PermissionPageUtils(context).jumpPermissionPage()
                } else {
                    "${permissionName}权限已拒绝".showToast()
                }
            }
            .request()
    }


    /**
     * 多权限请求判断 -不需要解释
     *  action:获取权限成功执行的操作
     *  deniedTips：拒绝后提示的操作
     */
    fun doNeedPermissionAction2(
        context: Context,
        permissionStrList: MutableList<String>,
        action: () -> Unit,
        deny: () -> Unit,
        functionStr: String,
        permissionName: String,
        isShowTipsDialog: Boolean
    ) {

        if (PermissionUtils.isGranted(*permissionStrList.toTypedArray())) {
            action()
            return
        }
        PermissionUtils.permission(*permissionStrList.toTypedArray())
            .explain { activity, denied, shouldRequest ->

                shouldRequest.start(true)
            }
            .callback { isAllGranted, granted, deniedForever, denied ->
                if (isAllGranted) {
                    action()
                    return@callback
                }

                if (isShowTipsDialog) {
                    if (deniedForever.isNotEmpty()) {
                        // "请手动开启${permissionName}权限".showToast()
                        GeneralDialog.showNormalDialog(
                            context,
                            "${permissionName}权限申请",
                            functionStr,
                            //"${functionStr}功能需要${permissionName}权限,是否开启权限？",
                            confirmBtnText = "开启权限",
                            confirmListener = {
                                PermissionPageUtils(context).jumpPermissionPage()
                            },
                            cancelListener = {
                               // "${permissionName}权限已拒绝".showToast()
                            })

                    } else {
                      //  "${permissionName}权限已拒绝".showToast()
                    }
                }

                deny()
            }
            .request()
    }

    /**
     * 请求权限
     * @param context: 要申请权限的页面Context
     * @param permissionList: 需要申请权限的列表
     * @param onGranted: 获取权限后的回调逻辑
     * @param onDenied: 拒绝授权的回调逻辑
     */
    fun requestPermission(context: Context, permissionList: List<String>, onGranted: () -> Unit, onDenied: (fullyDenied: Boolean) -> Unit = {})  {
        val realContext = if (context is Activity) context else ActivityUtils.getTopActivity()
        if (realContext == null || realContext.isFinishing) {
            return
        }

        XXPermissions.with(realContext)
            .permission(permissionList)
            .request(object : OnPermissionCallback {

                override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                    onGranted.invoke()
                }

                override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                    onDenied(doNotAskAgain)
                }
            })
    }

    /**
     * 检查指定权限是否已获取
     * @param context: 要申请权限的页面Context
     * @param permissionList: 需要申请权限的列表
     */
    fun checkPermission(context: Context, permissionList: List<String>): Boolean  {
        return XXPermissions.isGranted(context, permissionList)
    }
}