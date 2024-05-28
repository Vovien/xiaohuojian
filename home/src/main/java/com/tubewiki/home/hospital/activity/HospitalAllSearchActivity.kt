package com.tubewiki.home.hospital.activity

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
import com.jmbon.middleware.utils.LocationHelper
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.tubewiki.home.router.WikiRouter
import com.tubewiki.home.router.WikiRouter.WIKI_SEARCH_All_HOSPITAL
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.AllHospital
import com.tubewiki.home.bean.hospital.bean.SortItemBean
import com.tubewiki.home.databinding.ActivityHospitalAllSearchLayoutBinding
import com.tubewiki.home.dialog.SelectDoctorAllSortPopDialog
import com.tubewiki.home.dialog.SelectHospitalListLevelDialog
import com.tubewiki.home.hospital.adpater.AllHospitalAdapter
import com.tubewiki.home.hospital.viewmodel.HospitalAllSearchViewModel
import com.tubewiki.home.util.HospitalConstant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 所有医院搜索页面
 * @author MilkCoder
 * @date 2023/7/21
 * @version 6.2.1
 * @copyright All copyrights reserved to ManTang.
 */
@Route(path = WIKI_SEARCH_All_HOSPITAL)
class HospitalAllSearchActivity :
    DataBindingActivity<HospitalAllSearchViewModel, ActivityHospitalAllSearchLayoutBinding>() {
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

    override fun initView(savedInstanceState: Bundle?) {
        StatusBarCompat.StatusBarLightModeWithColor(this, Color.WHITE)
        binding.apply {
            clSearch.edInput.hint = "疾病、医院、科室"
            clSearch.icBack.onClick {
                finish()
            }
            clSearch.ivMore.setOnSingleClickListener({
                MiniMenuUtils.showMenuDialog(
                    this@HospitalAllSearchActivity,
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
                SavedSearchHistoryUtils.clean(SavedSearchHistoryUtils.HOSPITAL_SEARCH_HISTORY)
                updateHistoryList()
            })
            hospitalRecyclerview.apply {
                init(
                    hospitalAdapter,
                    layoutManager = LinearLayoutManager(
                        this@HospitalAllSearchActivity,
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

    override fun getData() {
        super.getData()
        getHospitalList()
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

        LocationHelper.getLocationCoordinate(this) { _, _ ->
            CommonViewModel.locationLD.value?.let {
                if (it.countryPinyin.isBlank()) {
                    viewModel.setLocation(
                        cityName = it.citySimpleName,
                        hasLocal = 1,
                        lat = it.latitude,
                        lng = it.longitude
                    )
                } else {
                    viewModel.setLocation(it.countryName, 1, 0.0, 0.0, it.countryPinyin)
                }
            } ?: kotlin.run {
                viewModel.setLocation("", 0)
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
        sortDialog = SelectDoctorAllSortPopDialog(this, viewModel.payload.value.type, true, list) {
            viewModel.setType(it)
            binding.tvAllSort.setTextColor(R.color.color_262626.toColorInt())
        }
        sortDialog?.showDialog(binding.ivAllSort)
    }

    private fun showHospitalLevelDialog() {
        val levelList = viewModel.hospitalLevelFlow.value
        if (levelList.isNullOrEmpty()) return "没有原因数据,请联系管理员".showToast()
        selectHospitalLevelDialog =
            SelectHospitalListLevelDialog(this, levelList) {
                it?.joinToString(",")?.let { it1 -> viewModel.setLevelId(it1) }
                //选择等级
                if (!it.isNullOrEmpty()) {
                    binding.tvHospitalLevel.setTextColor(R.color.color_262626.toColorInt())
                } else {
                    binding.tvHospitalLevel.setTextColor(R.color.color_7F7F7F.toColorInt())
                }
            }

        XPopup.Builder(this)
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

    private fun searchData(editStr: String) {
        if (editStr.isNotNullEmpty()) {
            saveHistory(editStr)
        }
    }

    private fun saveHistory(editStr: String) {
        // 搜索时候将内容添加到本地的搜索里面
        SavedSearchHistoryUtils.saveHistory(
            editStr,
            SavedSearchHistoryUtils.HOSPITAL_SEARCH_HISTORY
        )
        updateHistoryList()
    }

    private fun updateHistoryList() {
        val list =
            SavedSearchHistoryUtils.getHistory(SavedSearchHistoryUtils.HOSPITAL_SEARCH_HISTORY)
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