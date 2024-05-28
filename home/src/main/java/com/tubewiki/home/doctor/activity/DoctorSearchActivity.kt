package com.tubewiki.home.doctor.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
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
import com.blankj.utilcode.util.KeyboardUtils
import com.jmbon.middleware.arouter.service.IHomeProvider
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.search.SavedSearchHistoryUtils
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.tubewiki.home.router.WikiRouter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.SortItemBean
import com.tubewiki.home.databinding.ActivityDoctorSearchLayoutBinding
import com.tubewiki.home.dialog.SelectDoctorAllSortPopDialog
import com.tubewiki.home.dialog.SelectDoctorColumnDialog
import com.tubewiki.home.doctor.adapter.LocalDoctorAdapter
import com.tubewiki.home.doctor.bean.LocalDoctor
import com.tubewiki.home.doctor.bean.Territory
import com.tubewiki.home.doctor.viewmodel.DoctorListViewModel
import com.tubewiki.home.util.HospitalConstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 医生搜索页面
 * @author MilkCoder
 * @date 2023/7/20
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Route(path = WikiRouter.DOCTOR_SEARCH)
class DoctorSearchActivity :
    DataBindingActivity<DoctorListViewModel, ActivityDoctorSearchLayoutBinding>() {
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

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarCompat.StatusBarLightModeWithColor(this, Color.WHITE)
        binding.clSearch.clSearchContent.setBackgroundColor(Color.WHITE)
        binding.apply {
            clSearch.edInput.hint = "疾病、医院、科室、医生名字"
            clSearch.icBack.onClick {
                finish()
            }
            clSearch.ivMore.setOnSingleClickListener({
                MiniMenuUtils.showMenuDialog(
                    this@DoctorSearchActivity,
                    HospitalConstant.miniApp,
                    3
                )
            })
            clSearch.ivClose.setOnSingleClickListener({
                HospitalConstant.lastActivity?.let {
                    ActivityUtils.finishToActivity(it, true)
                }
            })
            clSearch.tvSearch.setOnSingleClickListener({
                if (clSearch.edInput.text.isNullOrBlank()) {
                    R.string.search_key_empty.showToast()
                    return@setOnSingleClickListener
                }
                searchData(clSearch.edInput.text.toString())
                viewModel.setKeyword(clSearch.edInput.text.toString())
                KeyboardUtils.hideSoftInput(clSearch.edInput)
            })
            clSearch.textCleanHistory.setOnSingleClickListener({
                SavedSearchHistoryUtils.clean(SavedSearchHistoryUtils.DOCTOR_SEARCH_HISTORY)
                updateHistoryList()
            })
            doctorRecyclerview.apply {
                init(
                    localDoctorAdapter,
                    layoutManager = LinearLayoutManager(
                        this@DoctorSearchActivity,
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

            clSearch.edInput.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (clSearch.edInput.text.isNullOrBlank()) {
                        R.string.search_key_empty.showToast()
                        return@setOnEditorActionListener false
                    }
                    searchData(clSearch.edInput.text.toString())
                    viewModel.setKeyword(clSearch.edInput.text.toString())
                    KeyboardUtils.hideSoftInput(clSearch.edInput)
                }
                return@setOnEditorActionListener true
            }

            clSearch.edInput.setIconClickListener {
                viewModel.setSearchShow(false)
            }

            clSearch.edInput.addTextChangedListener(afterTextChanged = {
                if (it.isNullOrEmpty()) {
                    viewModel.setSearchShow(false)
                }
            })

            lifecycleScope.launch {
                delay(400)
                KeyboardUtils.showSoftInput(binding.clSearch.edInput)
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

    private fun getDoctorList() {
        launch {
            viewModel.doctorListFlow.netCatch {
                message.showToast()
            }.collectLatest(::setDoctorAdapter)
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
        launch {
            viewModel.searchKeyFlow.next {
                binding.llChoose.isVisible = this
                binding.clSearch.clHistory.isVisible = !this
                binding.vEmpty.isVisible = !this
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

    private fun setDoctorAdapter(list: ResultThreeData<MutableList<LocalDoctor>, Boolean, Boolean>?) {
        if (list == null) return
        if (viewModel.payload.value.keyword.isEmpty()) {
            return
        }
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
            it?.map(Territory::columnId)?.joinToString(",")
                ?.let { it1 -> viewModel.setColumnId(it1) }
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

    private fun searchData(editStr: String) {
        if (editStr.isNotNullEmpty()) {
            saveHistory(editStr)
        }
    }

    private fun saveHistory(editStr: String) {
        // 搜索时候将内容添加到本地的搜索里面
        SavedSearchHistoryUtils.saveHistory(editStr, SavedSearchHistoryUtils.DOCTOR_SEARCH_HISTORY)
        updateHistoryList()
    }

    private fun updateHistoryList() {
        val list = SavedSearchHistoryUtils.getHistory(SavedSearchHistoryUtils.DOCTOR_SEARCH_HISTORY)
        if (list.isNotNullEmpty()) {
            binding.clSearch.searchHistory.removeAllViews()
            val newList: List<String> = if (list.size > 5) {
                list.takeLast(5)
            } else {
                list
            }
            newList.forEach {
                if (!textKeyHas(it))
                    binding.clSearch.searchHistory.addView(buildHotBtn(it))
            }
            binding.clSearch.searchHistory.maxNumber
        } else {
            binding.clSearch.clHistory.visibility = View.GONE
        }
    }

    private fun buildHotBtn(str: String): TextView {
        val btn = TextView(this)
        btn.text = str
        btn.setTextColor(ColorUtils.getColor(R.color.color_262626))
        btn.textSize = 14f
        btn.setBackgroundResource(R.drawable.shape_hot_search_bg)
        btn.setOnClickListener {
            // 点击热榜
            searchData(str)
            binding.clSearch.edInput.setText(str)
            viewModel.setKeyword(str)
        }
        return btn
    }

    private fun textKeyHas(key: String): Boolean {
        var hasKey = false
        find@ for (i in 0 until binding.clSearch.searchHistory.childCount) {
            val text = binding.clSearch.searchHistory.getChildAt(i) as TextView
            if (text.text.toString() == key) {
                hasKey = true
                break@find
            }
        }
        return hasKey
    }

    override fun onResume() {
        super.onResume()
        updateHistoryList()
    }

    override fun onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        super.onDestroy()
    }
}