package com.tubewiki.home.doctor.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * 医生列表过滤数据
 * @author MilkCoder
 * @date 2023/7/20
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class DoctorFilterPayload(
    // 搜索关键字
    var keyword: String = "",
    // 排序类型【1：综合排序，2：评分最高，3：问诊量，4：回复速度最快】
    val type: Int = 1,
    // 擅长领域id
    val column_ids: String = "",
    val cityName: String = "",
    // 城市拼音
    val pinyin: String = "",
    // 是否定位：0：未定位，1：已定位
    val has_local: Int = -1
) : Parcelable {

    val cityStr: String
        get() = cityName.ifEmpty { "未定位" }

    val typeStr: String
        get() = when (type) {
            1 -> "综合排序"
            2 -> "评分最高"
            3 -> "问诊量"
            4 -> "回复速度最快"
            else -> "综合排序"
        }
}
