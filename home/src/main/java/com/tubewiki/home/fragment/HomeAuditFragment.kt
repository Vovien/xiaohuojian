package com.tubewiki.home.fragment

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.DataBindingFragment
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jmbon.middleware.adapter.ColumnDetailAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.databinding.FragmentMainLayoutBinding
import com.tubewiki.home.viewmodel.MainFragmentViewModel


/**
 * 我的首页(审核模式)
 */
@Route(path = "/home/audit/fragment")
class HomeAuditFragment : DataBindingFragment<MainFragmentViewModel, FragmentMainLayoutBinding>() {

    private val columnDetailAdapter by lazy {
        ColumnDetailAdapter()
    }

    private val statusHeight by lazy { StatusBarCompat.getStatusBarHeight(view?.context) }

    override fun initView(view: View) {
        binding.apply {
            nestScroll.setPadding(0, statusHeight, 0, 0)
            initStateLayout(stateLayout)
            recyclerView.init(columnDetailAdapter)
            columnDetailAdapter.setEmptyView(R.layout.default_viewstatus_box_empty3)
            columnDetailAdapter.setOnItemClickListener { _, _, pos ->
                val data = columnDetailAdapter.getItem(pos)
                BannerHelper.onClick(
                    CommonBanner(
                        item_type = data.itemType,
                        item_id = data.identity.toIntOrNull() ?: 0,
                        identity = data.identity
                    )
                )
            }
            tvGirlToolTitle.typeface = Typeface.createFromAsset(
                activity?.assets, "fonts/Alibaba-PuHuiTi-Bold.ttf"
            )
            tvManToolTitle.typeface = Typeface.createFromAsset(
                activity?.assets, "fonts/Alibaba-PuHuiTi-Bold.ttf"
            )
            clGirlTool.onClick {
                Action {
                    val form = viewModel.homeHeaderBean.value?.womanCategory?.form
                    val source = viewModel.homeHeaderBean.value?.womanCategory?.source
                    ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                        .toSelfTest(form, source)
                }.logInToIntercept()
            }
            clManTool.onClick {
                Action {
                    val form = viewModel.homeHeaderBean.value?.manCategory?.form
                    val source = viewModel.homeHeaderBean.value?.manCategory?.source
                    ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                        .toSelfTest(form, source)
                }.logInToIntercept()
            }
            flFindDoctor.onClick {
                BannerHelper.toDoctorList()
            }
            flFindHospital.onClick {
                BannerHelper.toHospitalList()
            }
        }
    }

    override fun getData() {
        viewModel.homeHeaderBean.observe(this) {
            if (it == null) {
                showErrorState()
                return@observe
            }
            binding.bean = it
            binding.executePendingBindings()
            columnDetailAdapter.setList(it.everydayKnowledge)
            if (it.everydayKnowledge.isNotNullEmpty()) {
                columnDetailAdapter.addFooterLayout()
            }
            showContentState()
        }
        viewModel.getAuditHeaderInfo()
        viewModel.startLocation()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getAuditHeaderInfo()
    }

    private fun BaseQuickAdapter<*, *>.addFooterLayout() {
        if (hasFooterLayout()) {
            return
        }

        addFooterView(TextView(requireContext()).apply {
            setPadding(0, 32.dp, 0, 32.dp)
            text = "今天的知识浏览完毕"
            textSize = 14f
            setTextColor(R.color.color_BFBFBF.toColorInt())
            gravity = Gravity.CENTER
        })
    }


}