package com.jmbon.middleware.search

import com.apkdv.mvvmfast.utils.getString
import com.apkdv.mvvmfast.utils.mmkv
import com.apkdv.mvvmfast.utils.saveToMMKV
import com.blankj.utilcode.util.DeviceUtils
import com.blankj.utilcode.util.GsonUtils
import com.google.gson.reflect.TypeToken
import com.jmbon.middleware.utils.isNotNullEmpty

object SavedSearchHistoryUtils {
    // KEY
    private val SEARCH_HISTORY = "search_history"
     val BANNER_SEARCH_HISTORY = "banner_search_history"
     val HOSPITAL_SEARCH_HISTORY = "banner_search_history"
     val HOSPITAL_DOCTOR_SEARCH_HISTORY = "banner_search_history_doctor"
     val DOCTOR_SEARCH_HISTORY = "banner_search_doctor"

    fun saveHistory(item: String, key: String = SEARCH_HISTORY) {
        val list = arrayListOf<String>()
        val key = DeviceUtils.getAndroidID() + key
        val saveData = key.getString()
        list.add(item)
        if (saveData.isNotNullEmpty()) {
            //去重
            val oldData = GsonUtils.fromJson<ArrayList<String>>(
                saveData,
                object : TypeToken<ArrayList<String>>() {}.type
            )

            list.addAll(
                oldData.filterNot { it == item }
            )
        }
        GsonUtils.toJson(list.take(20), ArrayList::class.java).saveToMMKV(key)
    }

    fun getHistory(key: String = SEARCH_HISTORY): ArrayList<String> {
        val list = arrayListOf<String>()
        val key = DeviceUtils.getAndroidID() + key
        val saveData = key.getString()
        if (saveData.isNotNullEmpty()) {
            list.addAll(
                GsonUtils.fromJson<ArrayList<String>>(
                    saveData,
                    object : TypeToken<ArrayList<String>>() {}.type
                )
            )
        }
        return list
    }

    fun deleteHistory(item: String, key: String = SEARCH_HISTORY) {
        val key = DeviceUtils.getAndroidID() + key
        val saveData = key.getString()

        val list = GsonUtils.fromJson<ArrayList<String>>(
            saveData,
            object : TypeToken<ArrayList<String>>() {}.type
        )
        list.remove(item)
        GsonUtils.toJson(list, ArrayList::class.java).saveToMMKV(key)
    }

    fun clean(key: String = SEARCH_HISTORY) {
        val key = DeviceUtils.getAndroidID() + key
        mmkv.remove(key)
    }
}