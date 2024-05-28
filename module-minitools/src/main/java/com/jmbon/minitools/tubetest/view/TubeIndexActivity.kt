package com.jmbon.minitools.tubetest.view

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.started
import com.blankj.utilcode.util.ActivityUtils
import com.jmbon.minitools.R
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.databinding.ActivityTubeIndexBinding
import com.jmbon.minitools.pregnancy.util.PregnancyRouterConstant
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.minitools.tubetest.viewmodel.TubeViewModel


@Route(path = TubeArouterConstant.TUBE_INDEX, name = "试管自测首页")
class TubeIndexActivity : ViewModelActivity<TubeViewModel, ActivityTubeIndexBinding>() {

    val toolId: String = "2021102900000003"

    @Autowired(name = PregnancyRouterConstant.RESULT_TYPE)
    @JvmField
    var resultType: String = ""

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        titleBarView.centerTextView.setSingleLine()
        titleBarView.centerTextView.ellipsize = TextUtils.TruncateAt.END
        titleBarView.setRightView(R.layout.layout_minitool_right)
        when (resultType) {
            "pregnancy" -> {
                setTitleName("怀孕自测")
                titleBarView.rightCustomView.isVisible = false
            }

            else -> {
                setTitleName(getString(R.string.tube_test))
            }
        }
        TubeConstant.lastActivity = ActivityUtils.getTopActivity().javaClass

        initListener()


    }

    private fun initListener() {

        titleBarView.rightCustomView.findViewById<View>(R.id.iv_more).setOnClickListener {
            MiniMenuUtils.showMenuDialog(
                this,
                TubeConstant.miniApp

            )
        }

        titleBarView.rightCustomView.findViewById<View>(R.id.iv_close).setOnClickListener {
            TubeConstant.lastActivity?.let {
                ActivityUtils.finishToActivity(it, true)
            }
        }

        binding.btnSure.setOnClickListener {
            try {
                // 清空上次的选择内容
                TubeConstant.clearUserInfoBean()
                TubeConstant.userInfoBean.info.age = binding.tvAge.text.toString().toInt()
                TubeConstant.userInfoBean.info.height = binding.tvHeight.text.toString().toInt()
                TubeConstant.userInfoBean.info.weight = binding.tvWeight.text.toString().toFloat()

                var userInfo = TubeConstant.baseInfoBean.ages.find {
                    it.selectValue == TubeConstant.userInfoBean.info.age
                }
                userInfo?.let {
                    TubeConstant.userInfoBean.info.ageId = it.id
                }


                userInfo = TubeConstant.baseInfoBean.heights.find {
                    it.selectValue == TubeConstant.userInfoBean.info.height
                }
                userInfo?.let {
                    TubeConstant.userInfoBean.info.heightId = it.id
                }

                userInfo = TubeConstant.baseInfoBean.weights.find {
                    it.selectValue == TubeConstant.userInfoBean.info.weight.toInt()
                }
                userInfo?.let {
                    TubeConstant.userInfoBean.info.weightId = it.id
                }
                TubeConstant.resultType = resultType

            } catch (e: Exception) {

            }
            ARouter.getInstance().build(TubeArouterConstant.TUBE_PRE_PREG_PROBLEM).navigation()
        }

        binding.rulerAge.getmRulerView().setScrollSelected {
            binding.tvAge.text = it
        }
        binding.rulerHeight.getmRulerView().setScrollSelected {
            binding.tvHeight.text = it
        }
        binding.rulerWeight.getmRulerView().setScrollSelected {
            binding.tvWeight.text = it
        }

    }

    override fun initData() {
    }

    override fun getData() {
        started {
            viewModel.getBaseInfo().netCatch {
                binding.apply {
                    rulerAge.setScope(18, 80, 1)
                    rulerAge.setCurrentItem("30")
                    rulerHeight.setScope(140, 200, 1)
                    rulerHeight.setCurrentItem("160")

                    rulerWeight.setScope(30, 120, 1)
                    rulerWeight.setCurrentItem("50.0")
                }

                //  "获取数据异常".showToast()
            }.next {
                TubeConstant.baseInfoBean = this

                //设置默认数据
                binding.apply {
                    rulerAge.setScope(
                        TubeConstant.baseInfoBean.ages[0].selectValue,
                        TubeConstant.baseInfoBean.ages.last().selectValue,
                        1
                    )
                    rulerAge.setCurrentItem("30")
                    rulerHeight.setScope(
                        TubeConstant.baseInfoBean.heights[0].selectValue,
                        TubeConstant.baseInfoBean.heights.last().selectValue,
                        1
                    )
                    rulerHeight.setCurrentItem("160")
                    rulerWeight.setScope(
                        TubeConstant.baseInfoBean.weights[0].selectValue,
                        TubeConstant.baseInfoBean.weights.last().selectValue,
                        1
                    )
                    rulerWeight.setCurrentItem("50.0")

                    binding.btnSure.isEnabled = true

                }


            }

            viewModel.getToolInfo(toolId).netCatch {
                // "获取小工具信息失败".showToast()
            }.next {
                TubeConstant.miniApp = this.tool
            }

        }
    }

}