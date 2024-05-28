package com.jmbon.middleware.activity

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.utils.SolveEditTextScrollClash
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ClickUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.R
import com.jmbon.middleware.adapter.ReportAdapter
import com.jmbon.middleware.bean.ReportTypeData
import com.jmbon.middleware.databinding.ActivityReportBinding
import com.jmbon.middleware.model.ReportModel
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp


/**
 * @author leimg
 * @time  2021/4/14
 * 举报
 */
@Route(path = "/question/activity/report")
class ReportActivity : ViewModelActivity<ReportModel, ActivityReportBinding>() {


    @Autowired(name = TagConstant.REPORT_TYPE)
    @JvmField
    var reportType = ""

    @Autowired(name = TagConstant.TARGET_ID)
    @JvmField
    var targetId = ""

    @Autowired(name = TagConstant.TARGET_CONTENT)
    @JvmField
    var targetContent = ""


    @Autowired(name = TagConstant.GROUP_NUMBER)
    @JvmField
    var groupNumber = ""

    @Autowired(name = TagConstant.USER_UID)
    @JvmField
    var reportTedUid = "0"

    private var isFirst = true
    var softKeyboardHeight = 0


    private val typeAdapter by lazy { ReportAdapter() }

    lateinit var typeSelected: ReportTypeData.Report

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName("举报")

        titleBarView.setBackgroundColor(resources.getColor(R.color.ColorFAFA))
        StatusBarCompat.setStatusBarColor(this, resources.getColor(R.color.ColorFAFA))
        StatusBarCompat.setStatusBarDarkMode(this, true)

        binding.btnSure.isEnabled = false

        initStateLayout(binding.loadingStatus)

        binding.edText.setOnTouchListener(SolveEditTextScrollClash(binding.edText))

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.isMotionEventSplittingEnabled = false
        binding.recyclerView.layoutManager = LinearLayoutManager(baseContext)
        binding.recyclerView.adapter = typeAdapter

        typeAdapter.setOnItemClickListener { adapter, view, position ->
            typeSelected = typeAdapter.data[position]

            if (typeAdapter.getSelectedPos() == position) {
                binding.btnSure.isEnabled = false
                typeAdapter.setSelectedPos(-1)
            } else {
                binding.btnSure.isEnabled = true
                typeAdapter.setSelectedPos(position)
            }
        }


        ClickUtils.applySingleDebouncing(binding.btnSure) {
            // if (groupNumber.isNotNullEmpty()) {
            started {
                viewModel.groupMessageReport(
                    typeSelected.id,
                    groupNumber,
                    targetContent,
                    targetId,
                    reportType,
                    binding.edText.text.toString().trim(),
                    reportTedUid
                )
                    .next {
                        R.string.report_question_success.showToast()
                        finish()
                    }
            }
//            } else {
//                viewModel.commonReport(
//                    reportType,
//                    targetId,
//                    typeSelected.id,
//                    binding.edText.text.toString().trim()
//                )
//            }
        }

        //监听软键盘
        KeyboardUtils.registerSoftInputChangedListener(window)
        {
            //软键盘弹出，顶起pop布局
            if (it > 200) {
                binding.scrollLayout.setPadding(0, 0, 0, it - 114f.dp()) //114=50+64
                softKeyboardHeight = it
                //偷个懒，不动态计算，保证滑动到底部
                binding.scrollLayout.smoothScrollTo(0, 100000)
            } else {
                doScrollAnimator(softKeyboardHeight - 114f.dp())
            }
        }

    }

    private fun doScrollAnimator(offset: Int) {
        val valueAnimator = ValueAnimator.ofInt(offset, 0).setDuration(300)
        valueAnimator.addUpdateListener { animation ->
            val width = animation.animatedValue as Int
            binding.scrollLayout.setPadding(0, 0, 0, width)

        }
        //valueAnimator.interpolator = EaseCubicInterpolator()
        valueAnimator.start()
    }

    override fun onResume() {
        super.onResume()

        if (isFirst) {
            binding.scrollLayout.postDelayed({
                binding.scrollLayout.scrollTo(0, 0)
            }, 500)
            isFirst = false
        }
    }

    override fun initData() {
        viewModel.getReportResult.observe(this) {
            typeAdapter.setNewInstance(it)
        }
    }

    override fun getData() {
        viewModel.getReportType()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.getReportType()
    }
}