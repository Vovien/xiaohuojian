package com.tubewiki.mine.bean


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Keep
@Parcelize
data class ContactBean(
    @SerializedName("contact_us")
    var contact: ArrayList<Contact> = arrayListOf<Contact>(),
    @SerializedName("customer_service_mobile")
    val mobile:String = ""
) : Parcelable {
    @Keep
    @Parcelize
    data class Contact(
        @SerializedName("content")
        var content: ArrayList<String> = arrayListOf<String>(),
        @SerializedName("title")
        var title: String = ""
    ) : Parcelable
}