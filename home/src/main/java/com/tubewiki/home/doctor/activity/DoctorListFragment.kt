package com.tubewiki.home.doctor.activity

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
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.home.router.WikiRouter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.SortItemBean
import com.tubewiki.home.databinding.FragmentDoctorListBinding
import com.tubewiki.home.dialog.SelectDoctorAllSortPopDialog
import com.tubewiki.home.dialog.SelectDoctorColumnDialog
import com.tubewiki.home.doctor.adapter.LocalDoctorAdapter
import com.tubewiki.home.doctor.bean.LocalDoctor
import com.tubewiki.home.doctor.bean.Territory
import com.tubewiki.home.doctor.viewmodel.DoctorListViewModel
import com.tubewiki.home.hospital.viewmodel.HospitalDoctorSearchViewModel
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
class DoctorListFragment : DataBindingFragment<DoctorListViewModel, FragmentDoctorListBinding>() {

    private val isSearch: Boolean?
        get() = arguments?.getBoolean("isSearch")

    private val model: HospitalDoctorSearchViewModel by activityViewModels()

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
    }


    override fun initView(view: View) {
        binding.recycleLocal.apply {
            init(
                localDoctorAdapter,
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
        localDoctorAdapter.setEmptyView(R.layout.default_viewstatus_box_empty)
        localDoctorAdapter.setOnItemClickListener { _, _, pos ->
            WikiRouter.toDoctorDetailsActivity(localDoctorAdapter.getItem(pos).doctorId)
        }

        binding.apply {
            if (isSearch == true) {
                llCity.isVisible = true
                centerView.isVisible = true
            }
            smartRefresh.setOnLoadMoreListener {
                getDoctorList()
            }
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
            llCity.setOnSingleClickListener({
                ARouter.getInstance().navigation(IHomeProvider::class.java).toChooseCity(false)
            })
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
            if (isSearch == true) {
                model.payload.next {
                    viewModel.setKeyword(this.keyword)
                }
            }
        }

        launch {
            viewModel.payload.next {
                binding.payload = this
                binding.executePendingBindings()
            }
        }
        viewModel.getColumnList()
        getDoctorList()
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
        if (list == null) return
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

    private fun showAllSortDialog() {
        val list = mutableListOf(
            SortItemBean("综合排序", 1),
            SortItemBean("评分最高", 2),
            SortItemBean("问诊量", 3),
            SortItemBean("回复速度最快", 4),
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

    private fun showDoctorColumnDialog() {
        val columList = viewModel.columListFlow.value
        if (columList.isEmpty()) return "没有原因数据,请联系管理员".showToast()
        selectDoctorColumnDialog = SelectDoctorColumnDialog(requireContext(), columList) {
            it?.map(Territory::columnId)?.joinToString(",")
                ?.let { it1 -> viewModel.setColumnId(it1) }
            if (!it.isNullOrEmpty()) {
                binding.tvDoctorColumn.setTextColor(R.color.color_262626.toColorInt())
            } else {
                binding.tvDoctorColumn.setTextColor(R.color.color_7F7F7F.toColorInt())
            }
        }

        XPopup.Builder(requireContext())
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

    companion object {
        operator fun invoke(isSearch: Boolean?): DoctorListFragment {
            val fragment = DoctorListFragment()
            fragment.arguments = bundleOf("isSearch" to isSearch)
            return fragment
        }
    }

}