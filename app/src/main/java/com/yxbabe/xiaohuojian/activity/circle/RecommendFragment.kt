package com.yxbabe.xiaohuojian.activity.circle

import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.widget.refresh.JmbonRefreshFooter
import com.yxbabe.xiaohuojian.R
import com.yxbabe.xiaohuojian.adapter.CircleRecommendAdapter
import com.yxbabe.xiaohuojian.databinding.FragmentFindCircleVpItemBinding
import com.yxbabe.xiaohuojian.viewmodel.JoinGroupViewModel

@Route(path = "/app/circle/recommend")
class RecommendFragment :
    AppBaseFragment<FragmentFindCircleVpItemBinding>() {


    private val viewModel by lazy {
        activity?.viewModelStore?.let {
            ViewModelProvider(it, ViewModelFactory<Any, Any?>()).get(
                JoinGroupViewModel::class.java
            )
        }
    }

    private val circleAdapter by lazy { CircleRecommendAdapter() }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(view: View) {
        view.tag = 0
        binding.apply {
            recyclerView.init(
                circleAdapter, dividerHeight = 1f, dividerColor = Color.parseColor("#08000000"),
                left = 20f, right = 20f, vertical = false, showEnd = false
            )
            circleAdapter.setEmptyView(R.layout.default_viewstatus_no_box_empty2)

            val footer = JmbonRefreshFooter(requireContext())
            footer.setRefreshHeight(68.dp.toFloat())
            footer.setNoMoreDataText("到底啦，去搜索看看")
            srlRefresh.setRefreshFooter(footer)
            srlRefresh.setOnRefreshListener {
                getRecommendList(true)
            }
            srlRefresh.setOnLoadMoreListener {
                getRecommendList()
            }
        }

    }

    override fun getData() {
        getRecommendList(true)
    }

    private fun getRecommendList(refresh: Boolean = false) {
        started {
            viewModel?.getRecommendCircle(refresh)?.netCatch {
                this.message.showToast()
                binding.srlRefresh.finish()
            }?.next {
                if (refresh)
                    circleAdapter.setNewInstance(list)
                else
                    circleAdapter.addData(list ?: mutableListOf())
                if (totalCount <= circleAdapter.data.size) {
                    binding.srlRefresh.finishLoadMoreWithNoMoreData()
                } else {
                    binding.srlRefresh.finish()
                }
            }
        }
    }

}