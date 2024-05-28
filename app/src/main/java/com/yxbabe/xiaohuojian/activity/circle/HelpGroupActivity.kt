package com.yxbabe.xiaohuojian.activity.circle

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.tubewiki.home.adapter.HelpGroupAdapter
import com.yxbabe.xiaohuojian.router.AppRouter
import com.jmbon.middleware.decoration.GridSpacingItemDecoration
import com.jmbon.middleware.extention.dealPage
import com.jmbon.middleware.utils.dp
import com.yxbabe.xiaohuojian.databinding.ActivityHelpGroupLayoutBinding
import com.yxbabe.xiaohuojian.viewmodel.JoinGroupViewModel

/******************************************************************************
 * Description: 好孕互助群
 *
 * Author: jhg
 *
 * Date: 2023/9/15
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = AppRouter.APP_HELP_GROUP)
class HelpGroupActivity : ViewModelActivity<JoinGroupViewModel, ActivityHelpGroupLayoutBinding>() {

    @Autowired
    @JvmField
    var type: Int = 0

    @Autowired
    @JvmField
    var title: String = ""

    private val helpGroupAdapter by lazy {
        HelpGroupAdapter()
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(title)
        initPageState()
        binding.rvContent.apply {
            addItemDecoration(GridSpacingItemDecoration(spacing = 10.dp, edgeSpacing = 20.dp))
            adapter = helpGroupAdapter
        }

        binding.srlRefresh.apply {
            setOnLoadMoreListener {
                viewModel.currentPage++
                viewModel.getHelperGroupList(type)
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        viewModel.helpGroupInfoLD.observe(this) {
            showContentState()
            if (!it?.group_type_title.isNullOrBlank()) {
                setTitleName(it?.group_type_title!!)
            }
            binding.srlRefresh.dealPage(
                viewModel.currentPage,
                it?.circle_list,
                helpGroupAdapter,
                emptyViewAction = { _, emptyText ->
                    emptyText.text = "暂无相关圈子"
                })
        }
        viewModel.getHelperGroupList(type)
    }
}