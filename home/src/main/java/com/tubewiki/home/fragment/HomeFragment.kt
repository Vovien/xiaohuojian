package com.tubewiki.home.fragment

import android.graphics.Typeface
import android.view.View
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.DataBindingFragment
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.apkdv.mvvmfast.view.state.initState
import com.blankj.utilcode.util.ActivityUtils
import com.jmbon.middleware.arouter.MiniProgramService
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.common.CommonViewModel.Companion.popupImageFlow
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.adapter.FertilityAbilityTestAdapter
import com.tubewiki.home.adapter.HomeCategoryAdapter
import com.tubewiki.home.databinding.FragmentHomeBinding
import com.tubewiki.home.dialog.WeChatDialog
import com.tubewiki.home.viewmodel.MainFragmentViewModel

@Route(path = "/home/fragment")
class HomeFragment : DataBindingFragment<MainFragmentViewModel, FragmentHomeBinding>() {

    private val testFormAdapter by lazy {
        FertilityAbilityTestAdapter()
    }

    private val categoryAdapter by lazy {
        HomeCategoryAdapter()
    }

    private val statusHeight by lazy { StatusBarCompat.getStatusBarHeight(view?.context) }

    override fun initView(view: View) {
        binding.apply {
            nestScroll.setPadding(0, statusHeight, 0, 0)
            initStateLayout(stateLayout)

            rvCategory.adapter = categoryAdapter
            recyclerView.init(
                testFormAdapter,
                dividerHeight = 10f,
                vertical = false,
                showEnd = true
            )

            tvManToolTitle.typeface = Typeface.createFromAsset(
                activity?.assets, "fonts/Alibaba-PuHuiTi-Bold.ttf"
            )
            tvGirlToolTitle.typeface = Typeface.createFromAsset(
                activity?.assets, "fonts/Alibaba-PuHuiTi-Bold.ttf"
            )

            clGirlTool.onClick {
                if (Constant.isAuditMode) {
                    val form = viewModel.homeHeaderBean.value?.womanCategory?.form
                    val source = viewModel.homeHeaderBean.value?.womanCategory?.source
                    ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                        .toSelfTest(form, source)
                } else {
                    Action {
                        val form = viewModel.homeHeaderBean.value?.womanCategory?.form
                        val source = viewModel.homeHeaderBean.value?.womanCategory?.source
                        ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                            .toSelfTest(form, source)
                    }.logInToIntercept()
                }

            }
            clManTool.onClick {
                if (Constant.isAuditMode) {
                    val form = viewModel.homeHeaderBean.value?.manCategory?.form
                    val source = viewModel.homeHeaderBean.value?.manCategory?.source
                    ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                        .toSelfTest(form, source)
                } else {
                    Action {
                        val form = viewModel.homeHeaderBean.value?.manCategory?.form
                        val source = viewModel.homeHeaderBean.value?.manCategory?.source
                        ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                            .toSelfTest(form, source)
                    }.logInToIntercept()
                }
            }

            clTool.onClick {
                Action {
                    binding.bean?.tubeTest?.apply {
                        ARouter.getInstance().navigation(MiniProgramService::class.java)
                            .startMiniProgram(this.toolId)
                    }
                }.logInToIntercept()
            }
        }
    }

    override fun getData() {
        launch {
            popupImageFlow.next {
                if (this == null) return@next
                if (viewModel.firstShowFlow.value) {
                    val popupImage = popupImageFlow.value?.popup_adv
                    if (popupImage?.popupImg?.isNotEmpty() == true) {
                        val dialog = WeChatDialog(ActivityUtils.getTopActivity(), popupImage)
                        dialog.showDialog()
                        viewModel.firstShowFlow.value = false
                    } else {
                        viewModel.firstShowFlow.value = false
                    }
                }
            }
        }

        viewModel.homeHeaderBean.observe(this) {
            if (it == null) {
                showErrorState()
                return@observe
            }
            binding.bean = it
            binding.executePendingBindings()
            testFormAdapter.setList(it.categoryList)
            categoryAdapter.setList(it.topic_list)
            binding.rvCategory.isVisible = !it.topic_list.isNullOrEmpty()
            showContentState()
        }
        viewModel.getPopupImage()
        viewModel.getHeaderInfo()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getHeaderInfo()
    }
}
