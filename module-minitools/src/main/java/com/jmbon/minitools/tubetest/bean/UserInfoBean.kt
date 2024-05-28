package com.jmbon.minitools.tubetest.bean

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

/**
 * @author : leimg
 * time   : 2022/12/7
 * desc   :
 * version: 1.0
 */

/**
 * info:基本信息，年龄 身高，体重对应id用英文逗号隔开
 * problems :对应备孕难题id，多个用英文逗号隔开
 * sperms :对应精子问题id，多个用英文逗号隔开
 * ovarys  :对应卵巢问题id，多个用英文逗号隔开
 * uteruss  :对应子宫问题，多个用英文逗号隔开
 * fallopians :对应输卵管问题id，多个用英文逗号隔开
 * pregnancy_number :对应怀孕次数id
 * birth_number  :对应生育次数id
 * tubetest_number  :对应做试管次数id
 * tubetest_type  :对应选择的试管类型id
 * programme   :对应选择的促排方案id
 * demands  :对应选择的试管需求id,多个用英文逗号隔开
 * city_id  :对应选择的城市id
 * consider_tube  :对应是否有意愿做试管,1：有意愿，2：没有意愿，3：考虑中
 * tube_city  :对应选择愿意时的城市id, 0:未选择，1：省内，2：国内，3：海外
 * tube_cost  :0：没有费用，1:5万以下，2:10万以下，3:20万以下，4:30万以下，5:30万以上
 */
@Keep
@Parcelize
data class UserInfoBean(
    var info: UserInfo,
    var problems: String = "",
    var sperms: String = "",
    var ovarys: String = "",
    var uteruss: String = "",
    var fallopians: String = "",
    var pregnancy_number: String = "",
    var birth_number: String = "",
    var tubetest_number: String = "",
    var tubetest_type: String = "",
    var programme: String = "",
    var demands: String = "227", //没有意愿试管需求默认选择没有，id=227
    var city_id: String = "",
    var consider_tube: String = "",
    var tube_city: String = "",
    var tube_cost: String = "",
    var other_problem: String = "",
    var other_ovarys: String = "",
    var other_uteruss: String = "",
    var other_fallopians: String = "",
    var other_demands: String = "",
) : Parcelable {
    @Keep
    @Parcelize
    data class UserInfo(
        var age: Int = 30,
        var height: Int = 160,
        var weight: Float = 50.0f,
        var ageId: Int = 0,
        var heightId: Int = 0,
        var weightId: Int = 0
    ) : Parcelable



}
