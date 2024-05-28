package com.tubewiki.home.bean

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import com.jmbon.middleware.bean.ColumnArticles
import com.jmbon.middleware.bean.Form
import com.jmbon.middleware.bean.TestFormBean
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class HomeHeaderBean(
    @SerializedName("everyday_knowledge")
    val everydayKnowledge: MutableList<ColumnArticles> = mutableListOf(),
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("man_category")
    val manCategory: ManCategory = ManCategory(),
    @SerializedName("woman_category")
    val womanCategory: WomanCategory = WomanCategory(),
    @SerializedName("other_category", alternate = ["category_list"])
    val categoryList: List<TestFormBean> = listOf(),
    @SerializedName("topic_list")
    val topic_list: List<TopicCategoryBean>? = listOf(),
    @SerializedName("tube_test_card")
    val tubeTest: TubeTest? = TubeTest()
) : Parcelable

@Parcelize
data class ManCategory(
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("form")
    val form: ArrayList<Form> = arrayListOf(),
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("source")
    val source: String = "",
    @SerializedName("sub_title")
    val subTitle: String = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable

@Parcelize
data class WomanCategory(
    @SerializedName("desc")
    val desc: String = "",
    @SerializedName("form")
    val form: ArrayList<Form> = arrayListOf(),
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("logo")
    val logo: String = "",
    @SerializedName("source")
    val source: String = "",
    @SerializedName("sub_title")
    val subTitle: String = "",
    @SerializedName("title")
    val title: String = ""
) : Parcelable

@Keep
@Parcelize
data class TopicCategoryBean(
    val icon: String = "",
    val name: String = "",
    @SerializedName("item_type")
    val itemType: String = "",
    @SerializedName("identity")
    val identity:String = "",
    val topic_id: Int = 0
) : Parcelable

@Keep
@Parcelize
data class PregnantCard(
    @SerializedName("right_img")
    val rightImg: String = "",
    @SerializedName("sub_title")
    val subTitle: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("tool_id")
    val toolId: String = ""
) : Parcelable

@Keep
@Parcelize
data class TubeTest(
    @SerializedName("icon")
    val icon: String = "",
    @SerializedName("sub_title")
    val subTitle: String = "",
    @SerializedName("title")
    val title: String = "",
    @SerializedName("tool_id")
    val toolId: String = ""
) : Parcelable
