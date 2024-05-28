package com.tubewiki.home.doctor.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.DataBindingActivity
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.arouter.service.IHomeProvider
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.tubewiki.home.router.WikiRouter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.SortItemBean
import com.tubewiki.home.databinding.ActivityDoctorListBinding
import com.tubewiki.home.dialog.SelectDoctorAllSortPopDialog
import com.tubewiki.home.dialog.SelectDoctorColumnDialog
import com.tubewiki.home.doctor.adapter.LocalDoctorAdapter
import com.tubewiki.home.doctor.bean.LocalDoctor
import com.tubewiki.home.doctor.bean.Territory
import com.tubewiki.home.doctor.viewmodel.DoctorListViewModel
import com.tubewiki.home.util.DoctorConstant
import kotlinx.coroutines.flow.collectLatest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 医生列表界面
 * @author MilkCoder
 * @date 2023/7/19
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Route(path = WikiRouter.DOCTOR_LIST)
class DoctorListActivity : DataBindingActivity<DoctorListViewModel, ActivityDoctorListBinding>() {

    /**
     * 排序弹框
     */
    private var sortDialog: SelectDoctorAllSortPopDialog? = null

    /**
     * 擅长弹框
     */
    private var selectDoctorColumnDialog: SelectDoctorColumnDialog? = null

    private val localDoctorAdapter by lazy {
        LocalDoctorAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
        StatusBarCompat.setTransparentStatusBar(this.window)
    }

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarCompat.setStatusBarDarkMode(this, true)
        initStateLayout(binding.stateLayout)
        DoctorConstant.lastActivity = ActivityUtils.getTopActivity().javaClass

        binding.recycleLocal.apply {
            init(
                localDoctorAdapter,
                layoutManager = LinearLayoutManager(
                    this@DoctorListActivity,
                    RecyclerView.VERTICAL,
                    false
                ),
                dividerHeight = 16f,
                dividerColor = ColorUtils.getColor(R.color.color_fa),
                vertical = false
            )
        }
        localDoctorAdapter.setEmptyView(R.layout.default_viewstatus_box_empty)
        localDoctorAdapter.setOnItemClickListener { _, _, pos ->
            WikiRouter.toDoctorDetailsActivity(localDoctorAdapter.getItem(pos).doctorId)
        }

        binding.apply {
            smartRefresh.setOnLoadMoreListener {
                getDoctorList()
            }
            llSearch.setOnSingleClickListener({
                WikiRouter.toSearchDoctor()
            })
            ivMore.setOnSingleClickListener({
                MiniMenuUtils.showMenuDialog(
                    this@DoctorListActivity,
                    DoctorConstant.miniApp,
                    4
                )
            })
            ivClose.setOnSingleClickListener({
                DoctorConstant.lastActivity?.let {
                    ActivityUtils.finishToActivity(it, true)
                }
            })
            cityLayout.setOnSingleClickListener({
                ARouter.getInstance().navigation(IHomeProvider::class.java).toChooseCity(false)
            })
            llAllSort.onClick {
                if (sortDialog?.isShown == true) {
                    return@onClick
                }
                binding.ivAllSort.animate()?.rotation(180f)
                showAllSortDialog()
            }
            llDoctorColumn.onClick {
                if (selectDoctorColumnDialog?.isShown == true) {
                    return@onClick
                }
                binding.ivDoctorColumn.animate()?.rotation(180f)
                showDoctorColumnDialog()
            }
        }

    }

    override fun initViewModel() {
        super.initViewModel()
        CommonViewModel.locationLD.value.let {
            if (it != null) {
                if (it.countryPinyin.isBlank()) {
                    viewModel.setLocation(it.citySimpleName, 1)
                } else {
                    viewModel.setLocation(it.countryName, 1, it.countryPinyin)
                }
            } else {
                viewModel.setLocation("", 0)
            }
        }
    }

    override fun getData() {
        super.getData()
        launch {
            viewModel.payload.next {
                binding.payload = this
                binding.executePendingBindings()
            }
        }
        viewModel.getColumnList()
        getDoctorList()
        launch {
            viewModel.getToolInfo("2021102900000004").netCatch {
            }.next {
                DoctorConstant.miniApp = this.tool
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(event: CommonEventHub.ChangeCityEvent) {
        if (!event.byClick) {
            return
        }
        val areaName = if (!event.cityName.isNullOrBlank()) {
            event.cityName
        } else {
            event.countryName
        }
        if (!event.cityName.isNullOrBlank()) {
            viewModel.setLocation(areaName, 1)
        } else {
            viewModel.setLocation(areaName, 1, event.countryPinyin)
        }
    }

    private fun getDoctorList() {
        launch {
            viewModel.doctorListFlow.netCatch {
                message.showToast()
            }.collectLatest(::setDoctorAdapter)
        }
    }

    private fun setDoctorAdapter(list: ResultThreeData<MutableList<LocalDoctor>, Boolean, Boolean>?) {
        if (list==null) return
        if (list.data3) {
            binding.smartRefresh.finishLoadMoreWithNoMoreData()
        }
        binding.smartRefresh.finish()
        if (list.data2) {
            localDoctorAdapter.setNewInstance(list.data1)
        } else {
            localDoctorAdapter.addData(list.data1)
        }
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.setPageInit()
        getData()
    }

    private fun showAllSortDialog() {
        val list = mutableListOf(
            SortItemBean("综合排序", 1),
            SortItemBean("评分最高", 2),
            SortItemBean("问诊量", 3),
            SortItemBean("回复速度最快", 4),
        )
        sortDialog = SelectDoctorAllSortPopDialog(this, viewModel.payload.value.type, true, list) {
            viewModel.setType(it)
            binding.tvAllSort.setTextColor(R.color.color_262626.toColorInt())
        }
        sortDialog?.showDialog(binding.ivAllSort)
    }

    private fun showDoctorColumnDialog() {
        val columList = viewModel.columListFlow.value
        if (columList.isEmpty()) return "没有原因数据,请联系管理员".showToast()
        selectDoctorColumnDialog = SelectDoctorColumnDialog(this, columList) {
            it?.map(Territory::columnId)?.joinToString(",")?.let { it1 -> viewModel.setColumnId(it1) }
            if (!it.isNullOrEmpty()) {
                binding.tvDoctorColumn.setTextColor(R.color.color_262626.toColorInt())
            } else {
                binding.tvDoctorColumn.setTextColor(R.color.color_7F7F7F.toColorInt())
            }
        }

        XPopup.Builder(this)
            .atView(binding.llDoctorColumn)
            .isClickThrough(true)
            .autoOpenSoftInput(false)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .enableDrag(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onDismiss(popupView: BasePopupView?) {
                    binding.ivDoctorColumn.animate()?.rotation(0f)
                }

            })
            .asCustom(selectDoctorColumnDialog).show()
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

}