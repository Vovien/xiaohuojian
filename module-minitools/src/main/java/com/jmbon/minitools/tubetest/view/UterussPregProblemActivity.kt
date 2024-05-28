package com.jmbon.minitools.tubetest.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SpanUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.minitools.R
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.databinding.ActivityPrePregProblemBinding
import com.jmbon.minitools.tubetest.adapter.ProblemItemAdapter
import com.jmbon.minitools.tubetest.util.ArouterUtil
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.minitools.tubetest.viewmodel.PregnantTipsViewModel


@Route(path = TubeArouterConstant.TUBE_ZG_PREG_PROBLEM, name = "子宫问题页面")
class UterussPregProblemActivity :
    ViewModelActivity<PregnantTipsViewModel, ActivityPrePregProblemBinding>() {
    val problemItemAdapter by lazy { ProblemItemAdapter() }
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        TubeConstant.problemList.add(168)
    }

    override fun initView(savedInstanceState: Bundle?) {
        TubeConstant.problemList.removeAt(0)
        binding.tvTitle.text = getString(R.string.uteruss_problem)
        titleBarView.setRightView(R.layout.layout_minitool_right)
        if (TubeConstant.resultType.isNotEmpty()) {
            titleBarView.rightCustomView.isVisible = false
        }
        binding.recycleView.init(
            problemItemAdapter,
            layoutManager = GridLayoutManager(this, 2),
            24f,
            vertical = false,
            showEnd = true
        )

        SpanUtils.with(binding.tvGuide).append("可以通过").append("B超、宫腔镜、腹腔镜、子宫腔造影")
            .setForegroundColor(resources.getColor(R.color.color_313EF0)).setBold()
            .append("等检查来了解女方的子宫情况哦。").create()

        problemItemAdapter.setNewInstance(TubeConstant.baseInfoBean.uteruss)

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


            if (problemItemAdapter.selectedIds.contains(TubeConstant.baseInfoBean.uteruss[TubeConstant.baseInfoBean.uteruss.size - 1].id)) {
                //其他原因
                binding.llEdit.visible()
            } else {
                binding.llEdit.gone()
            }

            if (problemItemAdapter.selectedIds.contains(TubeConstant.baseInfoBean.uteruss[TubeConstant.baseInfoBean.uteruss.size - 2].id)) {
                //我不清楚
                binding.clTips.visible()
            } else {
                binding.clTips.gone()
            }

            if (TubeConstant.resultType == "pregnancy") {
                val isNex = TubeConstant.problemList.contains(167) ||
                        TubeConstant.problemList.contains(168) ||
                        TubeConstant.problemList.contains(169) ||
                        TubeConstant.problemList.contains(171)
                binding.btnSure.text = if (!isNex) "马上预测" else "下一步"
                if (problemItemAdapter.selectedIds.isEmpty()) {
                    binding.btnSure.text = "下一步"
                }
            }

        }



        initListener()
    }

    private fun initListener() {

        // 键盘监听
        KeyboardUtils.registerSoftInputChangedListener(window) { height ->
            Log.e(
                "initListener",
                "${binding.scrollLayout.height},${binding.llEdit.bottom},${binding.clTips.bottom},${binding.editLine.bottom},${binding.editLine.y}"
            )
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

            TubeConstant.userInfoBean.uteruss = selectedId
            TubeConstant.userInfoBean.other_uteruss = binding.edInput.text.toString()
            ArouterUtil.toProblemActivity(0)

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