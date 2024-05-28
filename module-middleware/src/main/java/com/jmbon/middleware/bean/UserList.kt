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

    val txtStr:String
        get() = "$userName 刚刚参与了拼购"
}