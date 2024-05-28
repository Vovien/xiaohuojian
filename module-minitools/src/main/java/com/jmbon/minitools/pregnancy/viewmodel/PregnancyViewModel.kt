package com.jmbon.minitools.pregnancy.viewmodel

import com.apkdv.mvvmfast.base.BaseViewModel
import com.jmbon.minitools.pregnancy.api.PregnancyApi
import com.jmbon.minitools.tubetest.bean.UserInfoBean
import com.jmbon.minitools.tubetest.util.TubeConstant

/**
 * 生育viewModel
 * @author MilkCoder
 * @date 2023/11/6 11:30
 * @copyright all rights reserved by ManTang
 */
class PregnancyViewModel : BaseViewModel() {

    /**
     * 怀孕自测
     * @date 2023/11/6 11:49
     */
    fun pregnancySubmit() = launchWithFlow({
        TubeConstant.userInfoBean.let {
            PregnancyApi.pregnancySubmit(
                base_info = it.info.getUserInfoStr(),
                problems = it.problems,
                sperms = it.sperms,
                ovarys = it.ovarys,
                uteruss = it.uteruss,
                fallopians = it.fallopians,
                other_problem = it.other_problem,
                other_ovarys = it.other_ovarys,
                other_uteruss = it.other_uteruss,
                other_fallopians = it.other_fallopians,
            )
        }
    }, handleError = false)

    private fun UserInfoBean.UserInfo.getUserInfoStr(): String {
        return "${this.ageId},${this.heightId},${this.weightId}"
    }

}