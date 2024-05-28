package com.tubewiki.home.bean


import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ExperienceDetailData(
    @SerializedName("experience_detail")
    var experience: Experience? = Experience(),
    @SerializedName("left_button")
    val leftButton: Experience.DetailButtonBean? = null,
    @SerializedName("right_button")
    val rightButton: Experience.DetailButtonBean? = null,
    @SerializedName("recommend_group")
    val recommendCircle: List<RecommendCircle>? = listOf()
) : Parcelable {

    @Keep
    @Parcelize
    data class Experience(
        @SerializedName("content")
        val content: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("index")
        val index: Index = Index(),
        @SerializedName("index_experiences")
        val indexExperiences: List<IndexExperience> = listOf(),
        @SerializedName("infos")
        val infos: MutableList<Info> = mutableListOf(),
        @SerializedName("title")
        val title: String = "",
        @SerializedName("uid")
        val uid: Int = 0,
        @SerializedName("user_info")
        val user: User? = null
    ) : Parcelable {

        @Keep
        @Parcelize
        data class DetailButtonBean(
            val title: String = "",
            @SerializedName("subscript")
            val label: String = "",
            val item_type: String = "",
            val identity: String = ""
        ) : Parcelable

        @Parcelize
        data class User(
            @SerializedName("account_type")
            val accountType: Int = 0,
            @SerializedName("article_count")
            val articleCount: Int = 0,
            @SerializedName("avatar_file")
            val avatarFile: String = "",
            @SerializedName("group_id")
            val groupId: Int = 0,
            @SerializedName("is_anonymous")
            val isAnonymous: Int = 0,
            @SerializedName("is_focus")
            val isFocus: Boolean = false,
            @SerializedName("uid")
            val uid: Int = 0,
            @SerializedName("user_name")
            val userName: String = "",
            @SerializedName("sub_title")
            val subTitle: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Index(
            @SerializedName("experience_num")
            var experienceNum: Int = 0,
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("index_id")
            var indexId: Int = 0,
            @SerializedName("index_name")
            var indexName: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class IndexExperience(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("title")
            var title: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Tag(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("tag_name")
            var tagName: String = ""
        ) : Parcelable

        @Keep
        @Parcelize
        data class Info(
            @SerializedName("id")
            var id: Int = 0,
            @SerializedName("key")
            var key: String = "",
            @SerializedName("value")
            var value: String = ""
        ) : Parcelable
    }
}

@Parcelize
data class RecommendCircle(
    @SerializedName("description")
    val desc: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("txt")
    val txt:String = "",
    @SerializedName("identity")
    val identity: String = "",
    @SerializedName("color")
    val color: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("member_avatar")
    val memberAvatar: List<String>? = listOf()
) : Parcelable
