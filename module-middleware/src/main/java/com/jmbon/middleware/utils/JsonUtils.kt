package com.jmbon.middleware.utils

import org.json.JSONObject

/**
 * Json 工具
 * @author MilkCoder
 * @date 2023/11/2
 * @copyright All copyrights reserved to ManTang.
 */
object JsonUtils {
    fun isValidJson(jsonString: String?): Boolean {
        jsonString ?: return false
        return try {
            val jsonObject = JSONObject(jsonString)
            true
        } catch (e: Exception) {
            false
        }
    }
}
