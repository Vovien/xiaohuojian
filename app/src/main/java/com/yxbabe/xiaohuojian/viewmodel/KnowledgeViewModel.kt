package com.yxbabe.xiaohuojian.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.yxbabe.xiaohuojian.api.MainApi

/**
 * @author MilkCoder
 * @date 2023/10/23
 * @copyright All copyrights reserved to ManTang.
 */
class KnowledgeViewModel : BaseViewModel() {

    /**
     * 知识信息
     * @date 2023/10/24 9:37
     * @param
     */
    fun getKnowledgeInfo() = launchWithFlow({ MainApi.getKnowledgeInfo() })

}