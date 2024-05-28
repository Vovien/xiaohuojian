package com.jmbon.minitools.pregnancy.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.utils.isNotNullEmpty
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class PregnancyResultBean(
    @SerializedName("card_list")
    val cartList: List<Cart> = listOf(),
    @SerializedName("pregnant_success_rate")
    val pregnantSuccessRate: Int = 0,
    @SerializedName("pregnant_success_desc")
    val pregnantSuccessDesc: String = "",
    @SerializedName("recommend_circle")
    val recommendCircle: List<RecommendCircle>? = listOf()
) : Parcelable {

    val resultStr: String
        get() = if (pregnantSuccessRate != 0) {
            "$pregnantSuccessRate%"
        } else {
            "0"
        }

    val cartOne: Cart?
        get() = cartList.getOrNull(0)

    val cartTwo: Cart?
        get() = cartList.getOrNull(1)

    val recommendCircleNotNull: Boolean
        get() = recommendCircle.isNotNullEmpty()
}

@Parcelize
data class Cart(
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("img")
    val img: String = "",
    @SerializedName("sub_title")
    val subTitle: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("bg_color")
    val bgColor: BgColor? = null
) : Parcelable

@Keep
@Parcelize
data class BgColor(
    @SerializedName("left_color")
    val leftColor: String = "",
    @SerializedName("right_color")
    val rightColor: String = ""
) : Parcelable

@Parcelize
data class RecommendCircle(
    @SerializedName("description")
    val description: String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("member_avatar")
    val memberAvatar: List<String> = listOf(),
    @SerializedName("name")
    val name: String = "",
    @SerializedName("avatars")
    val avatars: List<String> = listOf(),
    @SerializedName("color")
    val color: String = "",
    @SerializedName("group_nickname")
    val groupNickname: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("txt")
    val txt: String = ""
) : Parcelable
