package com.jmbon.minitools.report.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.minitools.report.api.ReportApi
import com.jmbon.minitools.report.bean.FertilityGroupChatListBean
import kotlinx.coroutines.launch

class GroupChatViewModel: BaseViewModel() {

    /**
     * 群聊列表
     */
    private val _groupChatListLD = MutableLiveData<FertilityGroupChatListBean?>()
    val groupChatListLD: LiveData<FertilityGroupChatListBean?> = _groupChatListLD


    /**
     * 请求生育群聊列表
     */
    fun getGroupChatList() {
        viewModelScope.launch {
            launchWithFlow({
                ReportApi.getGroupChatList(4)
            }, {
                _groupChatListLD.postValue(null)
            }).next {
                _groupChatListLD.postValue(this)
            }
        }
    }

}