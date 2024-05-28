package com.tubewiki.mine.view.setting

import android.os.Bundle
import android.view.LayoutInflater
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.databinding.DefaultViewstatusBoxEmpty4Binding
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.jmbon.middleware.adapter.ColumnDetailAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.bean.event.ArticleCollectEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.CommonListLayoutBinding
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.tubewiki.mine.R
import com.tubewiki.mine.view.model.SettingViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@Route(path = "/mine/setting/collect")
class CollectingActivity : ViewModelActivity<SettingViewModel, CommonListLayoutBinding>(),
    OnRefreshLoadMoreListener {

    var page = 1

    private val collectDetailAdapter by lazy {
        ColumnDetailAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun collectEvent(event: ArticleCollectEvent) {
        if (event.canecl) {
            page = 1
            getData()
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName("我的收藏")
        titleBarView.setBackgroundColor(R.color.color_fa.toColorInt())
        StatusBarCompat.StatusBarLightModeWithColor(this, ColorUtils.getColor(R.color.ColorFAFA))
        initPageState()
        binding.srlRefresh.setOnRefreshLoadMoreListener(this)

        binding.rvContent.init(collectDetailAdapter)
        collectDetailAdapter.setEmptyView(R.layout.default_viewstatus_box_empty)

        collectDetailAdapter.setOnItemClickListener { _, _, pos ->
            val data = collectDetailAdapter.getItem(pos)
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
            viewModel.collectList(page).netCatch {
                showErrorState()
                message.showToast()
            }.next {
                showContentState()
                if (page == 1) {
                    collectDetailAdapter.setNewInstance(collectList)
                    if (collectList.isNullOrEmpty()) {
                        var viewBinding =
                            DefaultViewstatusBoxEmpty4Binding.inflate(LayoutInflater.from(this@CollectingActivity))
                        collectDetailAdapter.setFooterView(viewBinding.root)
                        var params = viewBinding.root.layoutParams
                        params.height = ScreenUtils.getScreenHeight() - 200f.dp()
                        viewBinding.root.layoutParams = params
                        binding.srlRefresh.finishRefresh()
                        binding.srlRefresh.setEnableLoadMore(false)
                    }
                } else {
                    collectList?.let { collectDetailAdapter.addData(it) }
                }

                page++
                binding.srlRefresh.finishLoadMore()

                if (collectList != null) {
                    if (collectList!!.size < Constant.PAGE_SIZE) {
                        binding.srlRefresh.finishLoadMoreWithNoMoreData()
                    }
                }
            }
        }
    }

    override fun onRefresh(refreshLayout: RefreshLayout) {
        page = 1
        getData()
    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        getData()
    }
}