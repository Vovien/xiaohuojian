package com.tubewiki.home.hospital.viewmodel

import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.utils.isNotNullBlank
import com.jmbon.minitools.tubetest.api.TubeApi
import com.tubewiki.home.api.WikiApi
import com.tubewiki.home.bean.hospital.bean.HospitalFilterPayload
import com.tubewiki.home.bean.hospital.bean.HospitalLevelItemBean
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * 医院列表页面
 * @author MilkCoder
 * @date 2023/7/21
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HospitalListViewModel : BaseViewModel() {

    /**
     * 过滤数据
     */
    private val _payload = MutableStateFlow(HospitalFilterPayload())
    val payload = _payload

    /** 页数 */
    private val pageFlow = MutableStateFlow(1)

    /**
     * 医院等级
     */
    private val _hospitalLevelFlow = MutableStateFlow<List<HospitalLevelItemBean>?>(null)
    val hospitalLevelFlow = _hospitalLevelFlow

    fun getToolInfo(tool_id: String) = launchWithFlow({
        TubeApi.getToolInfo(tool_id)
    }, handleError = false)

    /** 医院列表 */
    val hospitalListFlow = _payload.flatMapLatest {
        launchWithFlow({
            if (it.has_local == -1) return@launchWithFlow null
            //是否需要刷新
            val isNeedRefresh = pageFlow.value == 1
            val data = WikiApi.getHospitalList(
                keyword = it.keyword,
                type = it.type,
                cityPinyin = it.pinyin,
                hasLocal = it.has_local,
                levelIds = it.level_ids,
                lng = it.lng,
                lat = it.lat,
                page = pageFlow.value
            )
            defLayout.showContent.call()
            val result = ResultThreeData(
                data.hospitals,
                isNeedRefresh,
                data.pageCount < pageFlow.value
            )
            pageFlow.value++
            result
        }, handleError = true)
    }

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
        lat: Double = _payload.value.lat,
        countryPinyin: String = ""
    ) {
        val payload = _payload.value
        if (cityName.isEmpty()) {
            setPageInit()
            _payload.value = payload.copy(has_local = hasLocal)
            return
        }
        //如果是国外，直接请求数据
        if (countryPinyin.isNotBlank()) {
            setPageInit()
            _payload.value =
                payload.copy(
                    cityName = cityName, pinyin = countryPinyin, has_local = hasLocal, lng = lng,
                    lat = lat
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
                            has_local = hasLocal,
                            lng = lng,
                            lat = lat
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

    fun setKeyWord(keyWord: String) {
        setPageInit()
        val payload = _payload.value
        _payload.value =
            payload.copy(keyword = keyWord)
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
}