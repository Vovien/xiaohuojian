package com.yxbabe.xiaohuojian.activity.circle

import android.graphics.Color
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.gone
import com.blankj.utilcode.util.BarUtils
import com.yxbabe.xiaohuojian.router.AppRouter
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.load
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.utils.setTextWhenNotEmpty
import com.yxbabe.xiaohuojian.R
import com.yxbabe.xiaohuojian.adapter.CircleGroupAdapter
import com.yxbabe.xiaohuojian.databinding.FragmentJoinGroupBinding
import com.yxbabe.xiaohuojian.viewmodel.JoinGroupViewModel

@Route(path = "/join/group/fragment")
class JoinGroupFragment : ViewModelFragment<JoinGroupViewModel, FragmentJoinGroupBinding>() {

    private val circleAdapter by lazy { CircleGroupAdapter() }

    override fun initView(view: View) {
        binding.apply {
            var statusHeight = BarUtils.getStatusBarHeight()
            if (statusHeight < 24.dp) {
                statusHeight = 32.dp
            }
            llContent.setPadding(0, statusHeight + 3.dp, 0, 0)
            initStateLayout(binding.llContent)
            recyclerView.init(
                circleAdapter, dividerHeight = 1f, dividerColor = Color.parseColor("#08000000"),
                left = 20f, right = 20f, vertical = false, showEnd = false
            )
            circleAdapter.setEmptyView(R.layout.default_viewstatus_no_box_empty2)
            clSingle.setOnSingleClickListener({
                AppRouter.toHelpGroup(
                    binding.tvSingleTitle.text.toString(),
                    viewModel.circleConfigLD.value?.group_type?.getOrNull(0)?.group_type ?: 0
                )
            })
            clFemale.setOnSingleClickListener({
                AppRouter.toHelpGroup(
                    binding.tvSingleTitle.text.toString(),
                    viewModel.circleConfigLD.value?.group_type?.getOrNull(1)?.group_type ?: 0
                )
            })
        }
    }

    override fun getData() {
        viewModel.circleConfigLD.observe(this) {
            if (it == null) {
                showErrorState()
                return@observe
            }
            it.circleList?.apply {
                circleAdapter.setNewInstance(this)
            }
            it.group_type?.getOrNull(0)?.let { it1 ->
                binding.tvSingleTitle.setTextWhenNotEmpty(it1.title)
                binding.tvSingleDesc.setTextWhenNotEmpty(it1.sub_title)
                if (!it1.icon.isNullOrBlank()) {
                    binding.ivSingleIcon.load(it1.icon)
                }
            } ?: run {
                binding.clSingle.gone()
            }
            it.group_type?.getOrNull(1)?.let { it1 ->
                binding.tvFemaleTitle.setTextWhenNotEmpty(it1.title)
                binding.tvFemaleDesc.setTextWhenNotEmpty(it1.sub_title)
                if (!it1.icon.isNullOrBlank()) {
                    binding.ivFemaleIcon.load(it1.icon)
                }
            } ?: kotlin.run {
                binding.clFemale.gone()
            }
            showContentState()
        }
        viewModel.getCircleConfig()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getCircleConfig()
    }
}