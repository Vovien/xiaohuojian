package com.jmbon.minitools.tubetest.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ForecastResultBean(
    @SerializedName("articles")
    var articles: MutableList<Article> = mutableListOf(),
    @SerializedName("cost")
    var cost: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("points")
    var points: MutableList<Float> = mutableListOf(),
    @SerializedName("result")
    var result: Result = Result(),
    @SerializedName("types")
    var types: MutableList<Type> = mutableListOf(),
    val result_desc: String = "",
    val programme_title: String = "",
    /**
     * 方案花费金额
     */
    val programme_cost: Float = 0f,
    /**
     * 花费标志【-1：下降，0：持平，1：上升】
     */
    val programme_cost_flag: Int = 0,
    /**
     * 方案成功率
     */
    val programme_ratio: Float = 0f,
    /**
     * 花费标志【-1：下降，0：持平，1：上升】
     */
    val programme_ratio_flag: Int = 1,
    val programme_desc: String = "",
    val notice_list: List<NoticeItemBean>? = listOf(),
    val bottom_top_desc: String = "",
    val user_list: List<UserList> = listOf(),
    val bottom_button: BottomButton? = null,
    val bottom_top_word: BottomWordConfig? = null,

) : Parcelable {
    @Keep
    @Parcelize
    data class Article(
        @SerializedName("contents")
        var contents: MutableList<Content> = mutableListOf(),
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("title")
        var title: String = ""
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Content(
            @SerializedName("content")
            var content: String = "",
            @SerializedName("days")
            var days: String = "",
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("select_title")
            var selectTitle: String = "",
            @SerializedName("title_id")
            var titleId: Int = 0
        ) : Parcelable
    }

    @Keep
    @Parcelize
    data class Result(
        @SerializedName("age")
        var age: Int = 0,
        @SerializedName("cost_ids")
        var costIds: String = "",
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("one_success_rate")
        var oneSuccessRate: Float = 0.0f,
        @SerializedName("points")
        var points: String = "",
        @SerializedName("predict_cost")
        var predictCost: String = "",
        @SerializedName("schedule_days")
        var scheduleDays: String = "",
        @SerializedName("three_success_rate")
        var threeSuccessRate: Float = 0.0f,
        @SerializedName("two_success_rate")
        var twoSuccessRate: Float = 0.0f
    ) : Parcelable

    @Keep
    @Parcelize
    data class Type(
        @SerializedName("contents")
        var contents: MutableList<Content> = mutableListOf(),
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("select_title")
        var selectTitle: String = ""
    ) : Parcelable {
        @Keep
        @Parcelize
        data class Content(
            @SerializedName("checked")
            var checked: Boolean = false,
            @SerializedName("content")
            var content: String = "",
            @SerializedName("details")
            var details: MutableList<Detail> = mutableListOf(),
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("title")
            var title: String = "",
            @SerializedName("type_id")
            var typeId: Int = 0
        ) : Parcelable {
            @Keep
            @Parcelize
            data class Detail(
                @SerializedName("checked")
                var checked: Boolean = false,
                @SerializedName("cost")
                var cost: String = "",
                @SerializedName("id")
                var id: Int = 0,
                @SerializedName("inspect_id")
                var inspectId: Int = 0,
                @SerializedName("select_title")
                var selectTitle: String = ""
            ) : Parcelable
        }
    }
}

@Keep
@Parcelize
data class NoticeItemBean(
    val title: String = "",
    val content: String = ""
) : Parcelable

@Keep
@Parcelize
data class UserList(
    @SerializedName("avatar_file")
    val avatarFile: String = "",
    @SerializedName("txt")
    val txt: String = "",
    @SerializedName("uid")
    val uid: Int = 0,
    @SerializedName("user_name")
    val userName: String = ""
) : Parcelable {

    val txtStr: String
        get() = "$userName $txt"
}

@Keep
@Parcelize
data class BottomButton(
    @SerializedName("bg_color")
    val bgColor: String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable

@Keep
@Parcelize
data class BottomWordConfig(
    val identity: String = "",
    val item_type: String = "",
    val title: String = ""
) : Parcelable