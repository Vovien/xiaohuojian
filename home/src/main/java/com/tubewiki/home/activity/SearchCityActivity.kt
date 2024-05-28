package com.tubewiki.home.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ResourceUtils
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.adapter.OverseasCountryAdapter
import com.tubewiki.home.databinding.ActivitySearchCityLayoutBinding
import com.tubewiki.home.router.HomeRouter
import com.tubewiki.home.viewmodel.LocalViewModel

@Route(path = HomeRouter.HOME_SEARCH_CITY)
class SearchCityActivity :
    ViewModelActivity<LocalViewModel, ActivitySearchCityLayoutBinding>() {

    private val cityAdapter by lazy {
        OverseasCountryAdapter(true)
    }

    override fun initView(savedInstanceState: Bundle?) {
        titleBarView.addView(createSearchView())
        titleBarView.centerTextView
        binding.rvContent.adapter = cityAdapter
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.apply {
            searchCityResultLD.observe(this@SearchCityActivity) {
                cityAdapter.setList(it)
                dismissLoading()
            }
        }
    }

    /**
     * 创建顶部的搜索布局
     */
    private fun createSearchView(): View {
        return LinearLayout(this).apply {
            setPadding(10.dp, 8.dp, 10.dp, 8.dp)
            setBackground(backgroundColor = R.color.colorF5F5F5.toColorInt(), radius = 12.dp)
            val etSearch = EditText(this@SearchCityActivity).apply {
                setPadding(0, 0, 12.dp, 0)
                setBackgroundColor(Color.TRANSPARENT)
                gravity = Gravity.CENTER_VERTICAL
                textSize = 16f
                setTextColor(Color.parseColor("#262626"))
                hint = "请输入城市名称"
                setHintTextColor(Color.parseColor("#7F7F7F"))
                imeOptions = EditorInfo.IME_ACTION_SEARCH
                compoundDrawablePadding = 10.dp
                ResourceUtils.getDrawable(R.drawable.icon_search_black).apply {
                    setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                    setCompoundDrawables(this, null, null, null)
                }

                setOnEditorActionListener { v, actionId, event ->
                    if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                        false
                    }

                    searchDisease(text.toString())
                    true
                }
                addView(this, LinearLayout.LayoutParams(
                   0,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    weight = 1f
                })
            }
            ImageView(this@SearchCityActivity).apply {
                setImageResource(R.drawable.search_close_icon)
                onClick {
                    etSearch.setText("")
                }
                addView(this)
            }
            layoutParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                leftMargin = 68.dp
                rightMargin = 20.dp
                addRule(RelativeLayout.CENTER_VERTICAL)
            }
        }
    }

    /**
     * 执行搜索操作
     * @param keyword: 要搜索的城市名
     */
    private fun searchDisease(keyword: String?) {
        if (keyword.isNullOrBlank()) {
            "请先输入要搜索的城市名".showToast()
            return
        }

        showLoading()
        viewModel.searchCity(keyword)
    }
}