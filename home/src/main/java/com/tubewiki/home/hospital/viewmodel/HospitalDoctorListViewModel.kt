package com.tubewiki.home.hospital.viewmodel

import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.minitools.tubetest.api.TubeApi
import com.tubewiki.home.api.WikiApi
import com.tubewiki.home.bean.hospital.bean.HospitalFilterPayload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 医院列表页面
 * @author MilkCoder
 * @date 2023/7/21
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
class HospitalDoctorListViewModel : BaseViewModel() {

    /**
     * 过滤数据
     */
    private val _payload = MutableStateFlow(HospitalFilterPayload())
    val payload = _payload

    fun getToolInfo(tool_id:String) = launchWithFlow({
        TubeApi.getToolInfo(tool_id)
    }, handleError = false)

    /**
     * 设置位置
     * @date 2023/7/20 9:56
     */
    fun setLocation(
        cityName: String,
        hasLocal: Int,
        lng: Double = _payload.value.lng,
        lat: Double = _payload.value.lat
    ) {
        val payload = _payload.value
        if (cityName.isEmpty()) {
            _payload.value = payload.copy(has_local = hasLocal)
            return
        }
        viewModelScope.launch {
            launchWithFlow(
                {
                    WikiApi.getCityPinyin(cityName)
                }, {
                    _payload.value = payload.copy(has_local = 0)
                }
            ).next {
                _payload.value =
                    payload.copy(
                        cityName = cityName, pinyin = this.pinyin, has_local = hasLocal, lng = lng,
                        lat = lat
                    )
            }
        }
    }

}