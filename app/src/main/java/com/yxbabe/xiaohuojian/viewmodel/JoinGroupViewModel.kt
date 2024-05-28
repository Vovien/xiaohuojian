package com.yxbabe.xiaohuojian.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.bean.CircleConfigBean
import com.jmbon.middleware.bean.HelpGroupBean
import com.jmbon.middleware.common.CommonViewModel
import com.yxbabe.xiaohuojian.api.MainApi
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class JoinGroupViewModel : CommonViewModel() {

    var recommendIndex = 1

    /**
     * 圈子配置信息
     */
    private val _circleConfigLD = MutableLiveData<CircleConfigBean?>()
    val circleConfigLD: LiveData<CircleConfigBean?> = _circleConfigLD

    /**
     * 好孕互助群
     */
    private val _helpGroupInfoLD = MutableLiveData<HelpGroupBean?>()
    val helpGroupInfoLD: LiveData<HelpGroupBean?> = _helpGroupInfoLD

    fun getRecommendCircle(refresh: Boolean) = launchWithFlow({
        if (refresh) {
            recommendIndex = 1
        }
        MainApi.getRecommendCircle(page = recommendIndex)
    }, handleError = true).map {
        recommendIndex++
        it
    }


    /**
     * 获取指定类型的互助群列表
     * @param type: 互助群的类型
     */
    fun getHelperGroupList(type: Int) {
        viewModelScope.launch {
            launchWithFlow({ MainApi.getHelperGroupList(type, currentPage) }, {
                _helpGroupInfoLD.postValue(null)
            }).next {
                _helpGroupInfoLD.postValue(this)
            }
        }
    }

    /**
     * 获取圈子配置信息
     */
    fun getCircleConfig() {
        viewModelScope.launch {
            launchWithFlow({ MainApi.getCircleConfig() }, {
                _circleConfigLD.postValue(null)
            }).next {
                _circleConfigLD.postValue(this)
            }
        }
    }
}