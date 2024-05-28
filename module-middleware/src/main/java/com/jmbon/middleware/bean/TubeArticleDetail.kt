package com.jmbon.middleware.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author : leimg
 * time   : 2022/8/2
 * desc   :
 * version: 1.0
 */
@Keep
@Parcelize
data class TubeArticleDetail(
    @SerializedName("articleType")
    var articleType: Int = 1,  //1 普通， 2百科
    @SerializedName("content")
    var content: String = "",
    @SerializedName("cover")
    var cover: String = "",
    @SerializedName("customDescription")
    var customDescription: String = "",
    @SerializedName("customTitle")
    var customTitle: String = "",
    @SerializedName("doctors")
    var doctors: MutableList<Doctor> = mutableListOf(),
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("indexCover")
    var indexCover: String = "",
    @SerializedName("info")
    var info: MutableList<ArticleInfo> = mutableListOf(), //分类 简介
    @SerializedName("introduction")
    var introduction: String = "",
    @SerializedName("readNum")
    var readNum: Int = 0,
    @SerializedName("reference")
    var reference: MutableList<Reference> = mutableListOf(),
    @SerializedName("helpful")
    var helpful: HelpfulBean = HelpfulBean(),
    @SerializedName("tag")
    var tag: MutableList<Tag> = mutableListOf(),
    @SerializedName("circles")
    var circles: MutableList<Circle> = mutableListOf(),
    @SerializedName("title")
    var title: String = "",
    @SerializedName("recommendType")
    var recommendType: String = "", //article 文章 topic 话题
    @SerializedName("topicName")
    var topicName: String = "",
    @SerializedName("articleNum")
    var articleNum: String = "",
    @SerializedName("icon")
    var icon: String = "",
    @SerializedName("description")
    var description: String = "",
    var type: String = "",
    var adv: ImageAdv? = ImageAdv(),
    @SerializedName("person_num")
    var personNum: Int = 0,
    @SerializedName("person_copywriting")
    var personCopywriting: String = "",
    @SerializedName("recommend_adv")
    val recommendAdv: RecommendAdv? = null,
    @SerializedName("ordinary_adv")
    val ordinaryAdv: OrdinaryAdv? = null,
    @SerializedName("wiki_adv")
    val wikiAdv: WikiAdvBean? = null,

) : Parcelable {
    @Keep
    @Parcelize
    data class Doctor(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("avatar")
        var avatar: String = "",
        @SerializedName("hospital")
        var hospital: String = "",
        @SerializedName("jobTitle")
        var jobTitle: String = "",
        @SerializedName("name")
        var name: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Reference(
        @SerializedName("authorname")
        var authorname: String = "",
        @SerializedName("pressname")
        var pressname: String = "",
        @SerializedName("reference_date")
        var referenceDate: String = "",
        @SerializedName("release_date")
        var releaseDate: String? = "",
        @SerializedName("articlename")
        var articlename: String = "",
        @SerializedName("webname")
        var webname: String = "",
        @SerializedName("show_value")
        var showValue: Int = 0,
        @SerializedName("type")
        var type: Int = 0,
        @SerializedName("workname")
        var workname: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Tag(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("tag")
        var tag: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class Circle(
        @SerializedName("id")
        var id: Int = 0,
        @SerializedName("customTitle")
        var customTitle: String = "",
        @SerializedName("cover")
        var cover: String = "",
        @SerializedName("spreadImg")
        var spreadImg: String = "", //群截图url
        @SerializedName("memberCount")
        var memberCount: String = "",
        var itemType: String = "",
        var identity: Int = 0,
    ) : Parcelable

    @kotlinx.parcelize.Parcelize
    @Keep
    data class ArticleInfo(
        @SerializedName("info_id")
        var infoId: Int? = 0,
        @SerializedName("key")
        var key: String? = "",
        @SerializedName("value")
        var value: String? = ""
    ) : Parcelable

    @kotlinx.parcelize.Parcelize
    @Keep
    data class HelpfulBean(
        @SerializedName("helpfulId")
        var helpfulId: Int = 0,
        @SerializedName("isHelpful")
        var isHelpful: Int = 0, //1有 2 无 0默认
        @SerializedName("unhelpfulType")
        var unhelpfulType: Int = 0,
        @SerializedName("count")
        var count: Int = 0,
    ) : Parcelable

    @Keep
    @Parcelize
    data class RecommendAdv(
        val id: Int = 0,
        val experience_id: Int = 0,
        val cover: String = "",
        val title: String = "",
        val identity: String = "",
        val item_type: String = "",
        val desc: String = "",
        val buttom_label: String = "",
        val buttom_word: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class OrdinaryAdv(
        @SerializedName("cover")
        val cover: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("identity")
        val identity: Int = 0,
        @SerializedName("item_type")
        val itemType: String = "",
        @SerializedName("qrcode")
        val qrcode: String = "",
        @SerializedName("qrcode_title")
        val qrcodeTitle: String = "",
        @SerializedName("statement")
        val statement: String = "",
        @SerializedName("sub_title1")
        val subTitle1: String = "",
        @SerializedName("sub_title2")
        val subTitle2: String = "",
        @SerializedName("title")
        val title: String = ""
    ) : Parcelable

    @Keep
    @Parcelize
    data class WikiAdvBean(
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("list")
        val list: List<WikiAdv> = listOf()
    ) : Parcelable

    @Keep
    @Parcelize
    data class WikiAdv(
        @SerializedName("cost")
        val cost: Int = 0,
        @SerializedName("cost_title")
        val costTitle: String = "",
        @SerializedName("cover")
        val cover: String = "",
        @SerializedName("cover_title")
        val coverTitle: String = "",
        @SerializedName("disease_name")
        val diseaseName: String = "",
        @SerializedName("disease_name_title")
        val diseaseNameTitle: String = "",
        @SerializedName("experience_id")
        val experienceId: Int = 0,
        @SerializedName("hospital_name")
        val hospitalName: String = "",
        @SerializedName("hospital_name_title")
        val hospitalNameTitle: String = "",
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("title")
        val title: String = "",
        @SerializedName("cost_str")
        var costStr:String = "",
        @SerializedName("identity")
        val identity: Int = 0,
        @SerializedName("item_type")
        val itemType: String = "",
    ) : Parcelable
}

fun TubeArticleDetail.getShareTitle(): String {
    return if (customTitle.isNullOrEmpty()) title else customTitle
}