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
data class ColumnArticlesBean(
    @SerializedName("content_list", alternate = ["collect_list"])
    val articleList: MutableList<ColumnArticles>? = mutableListOf()
) : Parcelable
