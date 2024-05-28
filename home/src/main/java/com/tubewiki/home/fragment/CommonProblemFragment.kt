package com.tubewiki.home.fragment

import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.base.AppBaseFragment
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.activityViewModels
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.next
import com.jmbon.middleware.adapter.ImageCommonBannerAdapter
import com.jmbon.middleware.decoration.GridSpaceItemDecoration
import com.jmbon.middleware.search.SearchMessageViewModel
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.setLayoutHeight
import com.tubewiki.home.adapter.CommonProblemAdapter
import com.tubewiki.home.databinding.FragmentCommonPorblemBinding
import com.tubewiki.home.viewmodel.HomeViewModel
import com.tubewiki.home.viewmodel.MainFragmentViewModel

/**
 * 生育问题
 * @author MilkCoder
 * @date 2024/3/21
 * @copyright All copyrights reserved to ManTang.
 */
class CommonProblemFragment : AppBaseFragment<FragmentCommonPorblemBinding>() {

    private val isFirstScreen: Boolean?
        get() = arguments?.getBoolean("isFirstScreen")

    val viewModel: HomeViewModel by activityViewModels()

    private val commonProblemAdapter by lazy {
        CommonProblemAdapter()
    }

    override fun initView(view: View) {
        binding.rvCommonProblem.adapter = commonProblemAdapter
        binding.rvCommonProblem.addItemDecoration(GridSpaceItemDecoration(16.dp, false))
    }

    override fun getData() {
        super.getData()
        launch {
            viewModel.listScreen.next {
                if (this.isEmpty()) return@next
                if (isFirstScreen == true) {
                    if (this.size >= 8) commonProblemAdapter.setList(
                        this.subList(0, 8)
                    ) else commonProblemAdapter.setList(this.subList(0, this.size))
                } else {
                    commonProblemAdapter.setList(this.subList(8, this.size))
                }
            }
        }
    }

    companion object {
        operator fun invoke(isFirstScreen: Boolean?): CommonProblemFragment {
            val fragment = CommonProblemFragment()
            fragment.arguments = bundleOf("isFirstScreen" to isFirstScreen)
            return fragment
        }
    }
}