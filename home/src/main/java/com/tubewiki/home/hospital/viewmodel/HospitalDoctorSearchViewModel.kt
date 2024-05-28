package com.tubewiki.home.hospital.viewmodel

import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.api.API
import com.tubewiki.home.api.WikiApi
import com.tubewiki.home.bean.hospital.bean.HospitalFilterPayload
import com.tubewiki.home.bean.hospital.bean.HospitalLevelItemBean
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HospitalDoctorSearchViewModel : BaseViewModel() {

    /**
     * 过滤数据
     */
    private val _payload = MutableStateFlow(HospitalFilterPayload())
    val payload = _payload

    private val _searchKeyFlow = MutableStateFlow(false)
    val searchKeyFlow = _searchKeyFlow

    /** 页数 */
    private val pageFlow = MutableStateFlow(1)

    /**
     * 医院等级
     */
    private val _hospitalLevelFlow = MutableStateFlow<List<HospitalLevelItemBean>?>(null)

    fun getHotSearch() = launchWithFlow({
        WikiApi.searchHot()
    })

    /**
     * 获取医院等级
     */
    fun getHospitalLevel() {
        viewModelScope.launch {
            launchWithFlow({
                WikiApi.getLocalHospitalLevel()
            }, {
                _hospitalLevelFlow.value = null
            }).next {
                _hospitalLevelFlow.value = level_list
            }
        }
    }

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
            setPageInit()
            _payload.value = payload.copy(has_local = hasLocal)
            return
        }
        viewModelScope.launch {
            launchWithFlow(
                {
                    WikiApi.getCityPinyin(cityName)
                }, {
                    setPageInit()
                    _payload.value = payload.copy(has_local = 0)
                }
            ).next {
                setPageInit()
                _payload.value =
                    payload.copy(
                        cityName = cityName, pinyin = this.pinyin, has_local = hasLocal, lng = lng,
                        lat = lat
                    )
            }
        }
    }

    /**
     * 设置排序类型
     * @date 2023/7/21 9:25
     */
    fun setType(type: Int) {
        setPageInit()
        val payload = _payload.value
        _payload.value =
            payload.copy(type = type)
    }

    /**
     * 初始化页数
     * @date 2023/7/20 10:19
     */
    private fun setPageInit() {
        pageFlow.value = 1
    }

    /**
     * 设置擅长领域
     * @date 2023/7/21 9:24
     */
    fun setLevelId(ids: String) {
        setPageInit()
        val payload = _payload.value
        _payload.value =
            payload.copy(level_ids = ids)
    }

    fun setKeyword(keyword: String) {
        setPageInit()
        val payload = _payload.value
        _payload.value =
            payload.copy(keyword = keyword)
        setSearchShow(true)
    }

    fun setSearchShow(isShow: Boolean) {
        _searchKeyFlow.value = isShow
    }
}