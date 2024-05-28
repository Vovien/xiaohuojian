package com.tubewiki.home.hospital.activity

import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.DataBindingFragment
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.ktx.activityViewModels
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.arouter.service.IHomeProvider
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.common.CommonViewModel.Companion.locationLD
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.LocationHelper
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.home.router.WikiRouter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.AllHospital
import com.tubewiki.home.bean.hospital.bean.SortItemBean
import com.tubewiki.home.databinding.FragmentHospitalListBinding
import com.tubewiki.home.dialog.SelectDoctorAllSortPopDialog
import com.tubewiki.home.dialog.SelectHospitalListLevelDialog
import com.tubewiki.home.hospital.adpater.AllHospitalAdapter
import com.tubewiki.home.hospital.viewmodel.HospitalDoctorSearchViewModel
import com.tubewiki.home.hospital.viewmodel.HospitalListViewModel
import kotlinx.coroutines.flow.collectLatest
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 医生列表页面
 * @author MilkCoder
 * @date 2023/7/21
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
class HospitalListFragment :
    DataBindingFragment<HospitalListViewModel, FragmentHospitalListBinding>() {

    private val isSearch: Boolean?
        get() = arguments?.getBoolean("isSearch")

    private val model: HospitalDoctorSearchViewModel by activityViewModels()

    /**
     * 排序弹框
     */
    private var sortDialog: SelectDoctorAllSortPopDialog? = null
    private var selectHospitalLevelDialog: SelectHospitalListLevelDialog? = null


    private val hospitalAdapter by lazy { AllHospitalAdapter() }

    override fun beforeViewInit() {
        super.beforeViewInit()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun initView(view: View) {
        binding.recycleLocal.apply {
            init(
                hospitalAdapter,
                layoutManager = LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL,
                    false
                ),
                dividerHeight = 16f,
                dividerColor = ColorUtils.getColor(R.color.color_fa),
                vertical = false
            )
        }
        hospitalAdapter.setEmptyView(R.layout.default_viewstatus_box_empty)
        hospitalAdapter.setOnItemClickListener { _, _, pos ->
            WikiRouter.toHospitalDetailsActivity(hospitalAdapter.getItem(pos).id)
        }
        binding.apply {
            if (isSearch == true) {
                llCity.isVisible = true
                centerView.isVisible = true
            }
            smartRefresh.setOnLoadMoreListener {
                getHospitalList()
            }
            llAllSort.onClick {
                if (sortDialog?.isShown == true) {
                    return@onClick
                }
                binding.ivAllSort.animate()?.rotation(180f)
                showAllSortDialog()
            }
            llHospitalLevel.onClick {
                if (selectHospitalLevelDialog?.isShown == true) {
                    return@onClick
                }
                binding.ivHospitalLevel.animate()?.rotation(180f)
                showHospitalLevelDialog()
            }
            llCity.setOnSingleClickListener({
                ARouter.getInstance().navigation(IHomeProvider::class.java).toChooseCity(false)
            })
        }
    }


    override fun getData() {
        super.getData()

        launch {
            if (isSearch == true) {
                model.payload.next {
                    viewModel.setKeyWord(this.keyword)
                }
            }
        }

        launch {
            LocationHelper.getLocationCoordinate(requireContext()) { longitude, latitude ->
                locationLD.value.let {
                    if (it != null) {
                        if (it.countryPinyin.isBlank()) {
                            viewModel.setLocation(
                                cityName = it.citySimpleName,
                                hasLocal = 1,
                                lat = latitude,
                                lng = longitude
                            )
                        } else {
                            viewModel.setLocation(it.countryName, 1, 0.0, 0.0, it.countryPinyin)
                        }
                    } else {
                        viewModel.setLocation("", 0)
                    }
                }
                getHospitalList()
            }
        }
        launch {
            viewModel.payload.next {
                binding.payload = this
                binding.executePendingBindings()
            }
        }
        viewModel.getHospitalLevel()

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
            viewModel.setLocation(areaName, 1, 0.0, 0.0, event.countryPinyin)
        }
    }

    private fun getHospitalList() {
        launch {
            viewModel.hospitalListFlow.netCatch {
                message.showToast()
            }.collectLatest(::setHospitalAdapter)
        }
    }

    private fun setHospitalAdapter(list: ResultThreeData<MutableList<AllHospital>?, Boolean, Boolean>?) {
        if (list == null) return
        if (list.data3) {
            binding.smartRefresh.finishLoadMoreWithNoMoreData()
        }
        binding.smartRefresh.finish()
        if (list.data2) {
            hospitalAdapter.setNewInstance(list.data1)
        } else {
            list.data1?.let { hospitalAdapter.addData(it) }
        }
    }

    private fun showAllSortDialog() {
        val list = mutableListOf(
            SortItemBean("综合排序", 1),
            SortItemBean("医院等级", 2),
            SortItemBean("专家最多", 3),
        )
        sortDialog = SelectDoctorAllSortPopDialog(
            requireContext(),
            viewModel.payload.value.type,
            true,
            list
        ) {
            viewModel.setType(it)
            binding.tvAllSort.setTextColor(R.color.color_262626.toColorInt())
        }
        sortDialog?.showDialog(binding.ivAllSort)
    }

    private fun showHospitalLevelDialog() {
        val levelList = viewModel.hospitalLevelFlow.value
        if (levelList.isNullOrEmpty()) return "没有原因数据,请联系管理员".showToast()
        selectHospitalLevelDialog =
            SelectHospitalListLevelDialog(requireContext(), levelList) {
                it?.joinToString(",")?.let { it1 -> viewModel.setLevelId(it1) }
                //选择等级
                if (!it.isNullOrEmpty()) {
                    binding.tvHospitalLevel.setTextColor(R.color.color_262626.toColorInt())
                } else {
                    binding.tvHospitalLevel.setTextColor(R.color.color_7F7F7F.toColorInt())
                }
            }

        XPopup.Builder(requireContext())
            .atView(binding.llHospitalLevel)
            .isClickThrough(true)
            .autoOpenSoftInput(false)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .enableDrag(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onDismiss(popupView: BasePopupView?) {
                    binding.ivHospitalLevel.animate()?.rotation(0f)
                }

            })
            .asCustom(selectHospitalLevelDialog).show()
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }

    companion object {
        operator fun invoke(isSearch: Boolean?): HospitalListFragment {
            val fragment = HospitalListFragment()
            fragment.arguments = bundleOf("isSearch" to isSearch)
            return fragment
        }
    }
}