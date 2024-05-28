package com.jmbon.middleware.config.network

import com.blankj.utilcode.util.ActivityUtils
import com.tencent.mmkv.MMKV


/******************************************************************************
 * Description: 缓存管理器
 *
 * Author: jhg
 *
 * Date: 2023/7/11
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
object HttpCacheManager {

    private const val KEY = "httpCache"

    init {
        ActivityUtils.getTopActivity()?.applicationContext?.let {
            MMKV.initialize(it, "${it.cacheDir}/http_cache")
        }
    }

    fun put(url: String, value: String) {
        MMKV.mmkvWithID(KEY).putString(url, value)
    }

    fun get (url: String): String {
        return MMKV.mmkvWithID(KEY).getString(url, "") ?: ""
    }
}