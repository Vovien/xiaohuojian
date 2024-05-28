package com.jmbon.minitools.tubetest.view

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.Scroller
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.minitools.R
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.databinding.ActivityTubeSpecialProblemBinding
import com.jmbon.minitools.tubetest.adapter.ProblemItemAdapter
import com.jmbon.minitools.tubetest.util.ArouterUtil
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.minitools.tubetest.viewmodel.PregnantTipsViewModel


@Route(path = TubeArouterConstant.TUBE_SPECIAL_PROBLEM, name = "特殊试管问题页面")
class TubeSpecialProblemActivity :
    ViewModelActivity<PregnantTipsViewModel, ActivityTubeSpecialProblemBinding>() {
    val problemItemAdapter by lazy { ProblemItemAdapter() }
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        titleBarView.setRightView(R.layout.layout_minitool_right)
        binding.recycleView.init(
            problemItemAdapter,
            layoutManager = GridLayoutManager(this, 2),
            24f,
            vertical = false,
            showEnd = true
        )

        binding.edInput.setScroller(Scroller(this))
        binding.edInput.isVerticalScrollBarEnabled = true
        binding.edInput.movementMethod = ScrollingMovementMethod()


        problemItemAdapter.setNewInstance(TubeConstant.baseInfoBean.demands)

        problemItemAdapter.setOnItemClickListener { adapter, view, pos ->

            if (pos == 0 && problemItemAdapter.selectedIds.isNotNullEmpty() && problemItemAdapter.selectedIds[0] != problemItemAdapter.getItem(
                    pos
                ).id
            ) {
                //没有问题
                problemItemAdapter.selectedIds.clear()
            } else if (pos != 0 && problemItemAdapter.selectedIds.isNotNullEmpty() && problemItemAdapter.selectedIds[0] == problemItemAdapter.getItem(
                    0
                ).id
            ) {
                //当先选择没有问题时和其他的互斥
                problemItemAdapter.selectedIds.clear()
            }

            if (problemItemAdapter.selectedIds.contains(problemItemAdapter.getItem(pos).id)) {
                problemItemAdapter.selectedIds.remove(problemItemAdapter.getItem(pos).id)
            } else {
                problemItemAdapter.selectedIds.add(problemItemAdapter.getItem(pos).id)
            }
            problemItemAdapter.notifyDataSetChanged()

            binding.btnSure.isEnabled = !problemItemAdapter.selectedIds.isNullOrEmpty()
            binding.btnSure.setUseShape()


            if (problemItemAdapter.selectedIds.contains(TubeConstant.baseInfoBean.demands[TubeConstant.baseInfoBean.demands.size - 1].id)) {
                //其他原因
                binding.llEdit.visible()
            } else {
                binding.llEdit.gone()
            }


        }



        initListener()
    }

    private fun initListener() {

        // 键盘监听
        KeyboardUtils.registerSoftInputChangedListener(window) { height ->

            if (height > 200) {
                binding.btnSure.gone()
                binding.scrollLayout.setPadding(0, 0, 0, 40f.dp())
                binding.scrollLayout.smoothScrollTo(0, binding.llEdit.bottom)

            } else {
                binding.scrollLayout.setPadding(0, 0, 0, 100f.dp())
                binding.btnSure.visible()
            }
        }
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

        binding.edInput.setOnFocusChangeListener { view, b ->
            binding.editLine.setBackgroundResource(if (b) R.drawable.ed_underline_selected else R.drawable.ed_underline_select)
        }

        binding.btnSure.setOnClickListener {
            if (problemItemAdapter.selectedIds.isNullOrEmpty()) {
                R.string.please_choice_problem.showToast()
                return@setOnClickListener
            }
            var selectedId = ""
            //排序
            problemItemAdapter.selectedIds.sort()

            problemItemAdapter.selectedIds.forEach {
                selectedId += "${it},"
            }
            selectedId = selectedId.substring(0, selectedId.length - 1)

            TubeConstant.userInfoBean.demands = selectedId
            TubeConstant.userInfoBean.other_demands = binding.edInput.text.toString()

            ArouterUtil.toActivity(TubeArouterConstant.TUBE_CALCULATE_RESULT)

        }
    }

    override fun initData() {
    }

    override fun getData() {


    }

    override fun onDestroy() {
        super.onDestroy()
        KeyboardUtils.unregisterSoftInputChangedListener(this.window)
    }

}