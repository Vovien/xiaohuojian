package com.yxbabe.xiaohuojian.activity.knowledge

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.DataBindingFragment
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.BarUtils
import com.jmbon.middleware.adapter.UserBannerAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.yxbabe.xiaohuojian.databinding.FragmentKnowledgeBinding
import com.yxbabe.xiaohuojian.viewmodel.KnowledgeViewModel
import com.jmbon.middleware.adapter.ColumnDetailAdapter
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.utils.BannerHelper
import com.qmuiteam.qmui.kotlin.onClick

/**
 * 知识模块
 * @author MilkCoder
 * @date 2023/10/23
 * @copyright All copyrights reserved to ManTang.
 */
@Route(path = "/knowledge/fragment")
class KnowledgeFragment : DataBindingFragment<KnowledgeViewModel, FragmentKnowledgeBinding>() {

    private val bannerAdapter by lazy {
        UserBannerAdapter()
    }

    private val columnDetailAdapter by lazy {
        ColumnDetailAdapter()
    }


    override fun initView(view: View) {
        binding.apply {
            var statusHeight = BarUtils.getStatusBarHeight()
            if (statusHeight < 24.dp) {
                statusHeight = 32.dp
            }
            llContent.setPadding(0, statusHeight + 3.dp, 0, 0)
            recyclerView.init(columnDetailAdapter)
            bannerTip.setAdapter(bannerAdapter)
            bannerTip.setUserInputEnabled(false)
            bannerTip.addBannerLifecycleObserver(lifecycleOwner)
            initStateLayout(binding.llContent)

            clGroupBooking.onClick {
                binding.bean?.apply {
                    BannerHelper.onClick(
                        CommonBanner(
                            item_type = this.card.itemType,
                            identity = this.card.identity
                        )
                    )
                }
            }
            musicLayout.onClick {
                clGroupBooking.performClick()
            }
            tipView.onClick {
                clGroupBooking.performClick()
            }
        }

        columnDetailAdapter.setOnItemClickListener { adapter, view, pos ->
            val data = columnDetailAdapter.getItem(pos)
            BannerHelper.onClick(
                CommonBanner(
                    item_type = data.itemType,
                    item_id = data.identity.toIntOrNull() ?: 0,
                    identity = data.identity
                )
            )
        }
    }

    override fun getData() {
        super.getData()
        launch {
            viewModel.getKnowledgeInfo().netCatch {
                showErrorState()
                this.message.showToast()
            }.next {
                this.apply {
                    binding.bean = this
                    binding.executePendingBindings()
                    bannerAdapter.setDatas(this.card.randUser)
                    columnDetailAdapter.setNewInstance(this.articleList)
                }
                showContentState()
            }
        }
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getData()
    }
}