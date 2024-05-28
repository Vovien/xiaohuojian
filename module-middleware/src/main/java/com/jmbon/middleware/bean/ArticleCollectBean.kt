package com.jmbon.middleware.bean
import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 *
 * @author MilkCoder
 * @date 2023/11/15
 * @copyright All copyrights reserved to ManTang.
 */
@Keep
@Parcelize
data class ArticleCollectBean(
    @SerializedName("collect_list")
    val collectList: MutableList<ColumnArticles>? = mutableListOf()
) : Parcelable
