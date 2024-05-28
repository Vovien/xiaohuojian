package com.tubewiki.home.bean.hospital.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * 医院列表过滤数据
 * @author MilkCoder
 * @date 2023/7/20
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class HospitalFilterPayload(
    // 搜索关键字
    var keyword: String = "",
    // 1.综合排序，2.医院等级排序，3.入驻医生数排序,默认综合排序
    val type: Int = 1,
    // 医院等级id，多个用英文逗号隔开
    val level_ids: String = "",
    val lng: Double = 0.0,
    val lat: Double = 0.0,
    val cityName: String = "",
    // 城市拼音
    val pinyin: String = "",
    // 是否定位：0：未定位，1：已定位
    val has_local: Int = -1
) : Parcelable {

    val cityStr: String
        get() = cityName.ifEmpty { "获取定位" }

    val tabList: List<String>
        get() = if (cityName.isNotEmpty()) listOf("本地医生", "本地医院") else listOf(
            "推荐医生",
            "推荐医院"
        )

    val typeStr: String
        get() = when (type) {
            1 -> "综合排序"
            2 -> "医院等级"
            3 -> "专家最多"
            else -> "综合排序"
        }
}
