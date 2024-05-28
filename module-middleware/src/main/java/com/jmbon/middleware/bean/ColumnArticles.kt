package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 *
 * @author MilkCoder
 * @date 2023/10/23
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class ColumnArticles(
    @SerializedName("cover")
    val cover: String = "",
    @SerializedName("content")
    val introduction: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("article_id", alternate = ["id"])
    val id: Int = 0,
    @SerializedName("read_num")
    val readNum: Int = 0,
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("identity")
    val identity: String = ""
) : Parcelable
