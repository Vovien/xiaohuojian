package com.tubewiki.home.doctor.viewmodel

import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.api.API
import com.jmbon.minitools.tubetest.api.TubeApi
import com.tubewiki.home.api.WikiApi
import com.tubewiki.home.doctor.api.DoctorApi
import com.tubewiki.home.doctor.bean.DoctorFilterPayload
import com.tubewiki.home.doctor.bean.Territory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * 医生列表VM
 * @author MilkCoder
 * @date 2023/7/19
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DoctorListViewModel : BaseViewModel() {

    /**
     * 过滤数据
     */
    private val _payload = MutableStateFlow(DoctorFilterPayload())
    val payload = _payload

    /**
     * 医生擅长数据列表
     */
    private val _columnListFlow = MutableStateFlow<List<Territory>>(emptyList())
    val columListFlow = _columnListFlow

    private val _searchKeyFlow = MutableStateFlow(false)
    val searchKeyFlow = _searchKeyFlow

    private val _hospitalId = MutableStateFlow(0)

    /** 页数 */
    private val pageFlow = MutableStateFlow(1)

    fun getToolInfo(tool_id: String) = launchWithFlow({
        TubeApi.getToolInfo(tool_id)
    }, handleError = false)

    /** 医生列表 */
    val doctorListFlow = _payload.flatMapLatest {
        launchWithFlow({
            if (it.has_local == -1) return@launchWithFlow null
            //是否需要刷新
            val isNeedRefresh = pageFlow.value == 1
            val data = DoctorApi.getDoctorList(
                keyword = it.keyword,
                type = it.type,
                columnIds = it.column_ids,
                cityPinyin = it.pinyin,
                hasLocal = it.has_local,
                page = pageFlow.value
            )
            defLayout.showContent.call()
            val result = ResultThreeData(
                data.doctors,
                isNeedRefresh,
                data.page_count < pageFlow.value
            )
            pageFlow.value++
            result
        }, handleError = true)
    }

    /** 同院医生 */
    val hospitalDoctorListFlow = _hospitalId.flatMapLatest {
        launchWithFlow({
            //是否需要刷新
            val isNeedRefresh = pageFlow.value == 1
            val data = DoctorApi.hospitalDoctorList(
                hospitalId = it,
                page = pageFlow.value
            )
            defLayout.showContent.call()
            val result = ResultThreeData(
                data.doctors,
                isNeedRefresh,
                data.page_count < pageFlow.value
            )
            pageFlow.value++
            result
        }, handleError = true)
    }

    fun getColumnList() = launchOnlyResult(
        block = {
            DoctorApi.getColumnList()
        },
        success = {
            _columnListFlow.value = it.territory
        }
    )

    /**
     * 设置位置
     * @date 2023/7/20 9:56
     */
    fun setLocation(cityName: String, hasLocal: Int, countryPinyin: String = "") {
        val payload = _payload.value
        if (cityName.isEmpty()) {
            _payload.value = payload.copy(has_local = hasLocal)
            return
        }
        //如果是国外，直接请求数据
        if (countryPinyin.isNotBlank()) {
            setPageInit()
            _payload.value =
                payload.copy(
                    cityName = cityName,
                    pinyin = countryPinyin,
                    has_local = hasLocal
                )
        } else {
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
                            cityName = cityName,
                            pinyin = this.pinyin,
                            has_local = hasLocal
                        )
                }
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
    fun setPageInit() {
        pageFlow.value = 1
    }

    /**
     * 设置擅长领域
     * @date 2023/7/21 9:24
     */
    fun setColumnId(ids: String) {
        setPageInit()
        val payload = _payload.value
        _payload.value =
            payload.copy(column_ids = ids)
    }

    /**
     * 关键字设置
     * @date 2023/7/21 9:27
     */
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

    fun setHospitalId(hospitalId: Int) {
        _hospitalId.value = hospitalId
    }

}