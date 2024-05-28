package com.jmbon.minitools.report.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.minitools.report.api.ReportApi
import com.jmbon.minitools.report.bean.FertilityAbilityTestResultBean
import kotlinx.coroutines.launch

/******************************************************************************
 * Description: 生育力自测
 *
 * Author: jhg
 *
 * Date: 2023/4/21
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
class FertilityAbilityTestViewModel: BaseViewModel() {

    /**
     * 生育力自测结果
     */
    private val _testResultLD = MutableLiveData<FertilityAbilityTestResultBean?>()
    val testResultLD: LiveData<FertilityAbilityTestResultBean?> = _testResultLD

    /**
     * 测试结果图片
     */
    private val _testResultPictureLD = MutableLiveData<String?>()
    val testResultPictureLD: LiveData<String?> = _testResultPictureLD


    /**
     * 获取生育力自测表单
     * @param type: 自测类型
     * @param values: 选择的值
     */
    fun getTestResult(type: Int, values: List<Int>) {
        viewModelScope.launch {
            launchWithFlow({
                ReportApi.getFertilityAbilityTestResult(type, values.joinToString(","))
            }, {
                _testResultLD.postValue(null)
            }).next {
                _testResultLD.postValue(this)
            }
        }
    }

    /**
     * 获取测试结果图片
     * @param id: 测试结果id
     */
    fun getTestResultPicture(id: Int) {
        viewModelScope.launch {
            launchWithFlow({
                ReportApi.getTestResultPicture(id)
            }, {
                _testResultPictureLD.postValue(null)
            }).next {
                _testResultPictureLD.postValue(url)
            }
        }
    }

}