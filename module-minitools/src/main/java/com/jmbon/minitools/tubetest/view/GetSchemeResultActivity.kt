package com.jmbon.minitools.tubetest.view

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.text.bold
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.BindingAdapter
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.extention.setRightDrawable
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.helper.ShadowHelper
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.setTextWhenNotEmpty
import com.jmbon.minitools.R
import com.jmbon.minitools.databinding.ActivityGetSchemeResultLayoutBinding
import com.jmbon.minitools.databinding.FooterGetSchemeDetailLayoutBinding
import com.jmbon.minitools.databinding.ItemSchemeResultNoticeLayoutBinding
import com.jmbon.minitools.router.MiniToolRouter
import com.jmbon.minitools.tubetest.adapter.ImageTextBannerAdapter
import com.jmbon.minitools.tubetest.bean.ForecastResultBean
import com.jmbon.minitools.tubetest.bean.NoticeItemBean
import com.jmbon.minitools.tubetest.viewmodel.TubeViewModel
import com.qmuiteam.qmui.kotlin.onClick


/******************************************************************************
 * Description: 获取方案结果页
 *
 * Author: jhg
 *
 * Date: 2023/6/1
 *
 * Copyright: all rights reserved by Mantang.
 *******************************************************************************/
@Route(path = MiniToolRouter.MINI_TOOL_GET_SCHEME_RESULT)
class GetSchemeResultActivity :
    ViewModelActivity<TubeViewModel, ActivityGetSchemeResultLayoutBinding>() {

    /**
     * ScrollView的高度
     */
    private var scrollViewHeight = 0

    /**
     * 内容部分的高度
     */
    private var contentHeight = 0

    @JvmField
    @Autowired
    var resultInfo: ForecastResultBean? = null

    private val bannerAdapter by lazy {
        ImageTextBannerAdapter()
    }

    private val footerBinding by lazy {
        FooterGetSchemeDetailLayoutBinding.inflate(LayoutInflater.from(this)).apply {
            resultInfo?.bottom_top_word?.let {
                tvAddWechat.setTextWhenNotEmpty(it.title)
            }
            tvAddWechat.onClick {
                resultInfo?.bottom_top_word?.let {
                    BannerHelper.onClick(CommonBanner(item_type = it.item_type, identity = it.identity))
                }
            }
        }
    }

    private val noticeAdapter by lazy {

        object :
            BindingAdapter<NoticeItemBean, ItemSchemeResultNoticeLayoutBinding>() {
            override fun convert(
                binding: ItemSchemeResultNoticeLayoutBinding,
                item: NoticeItemBean
            ) {
                binding.apply {
                    tvTitle.text = item.title
                    tvContent.text = item.content
                }
            }
        }.apply {
            val itemCount = resultInfo?.notice_list?.size ?: 0
            if (itemCount > 2) {
                // Item的数量超过2个时显示底部的加微信遮罩
                setList(resultInfo?.notice_list?.subList(0, 2))
                resultInfo?.notice_list?.getOrNull(2)?.apply {
                    footerBinding.llItem.tvTitle.text = title
                    footerBinding.llItem.tvContent.text = content
                }
                addFooterView(footerBinding.root)
            } else {
                setList(resultInfo?.notice_list)
            }
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        titleBarView.setBackgroundColor(R.color.colorF5F5F5.toColorInt())
        ImmersionBar.with(this)
            .statusBarColor(R.color.colorF5F5F5)
            .transparentNavigationBar()
            .init()
        StatusBarCompat.StatusBarLightModeWithColor(this, R.color.colorF5F5F5.toColorInt())
        binding.apply {
            tvResult.text = String.format("%.2f", (resultInfo?.result?.oneSuccessRate ?: 0f) * 100) + "%"
            tvDayCount.text = resultInfo?.result?.scheduleDays
            tvCost.text = resultInfo?.result?.predictCost
            tvResultDesc.text = resultInfo?.result_desc
            tvResultDesc.isVisible = !resultInfo?.result_desc.isNullOrBlank()
            tvBetterScheme.text = resultInfo?.programme_title
            setTipInfo(tvTip, resultInfo?.programme_desc)
            tvMedicalCost.text = String.format("%.2f", resultInfo?.programme_cost ?: 0f)
            //tvMedicalCost.setRightDrawable(getFlagIcon(resultInfo?.programme_cost_flag ?: 0))
            tvSuccessRate.text = String.format("%.2f", (resultInfo?.result?.oneSuccessRate ?: 0f) * 100 + (resultInfo?.programme_ratio  ?: 0f))
            //tvSuccessRate.setRightDrawable(getFlagIcon(resultInfo?.programme_ratio_flag ?: 0))
            rvNotice.init(noticeAdapter, dividerHeight = 24f, vertical = false)
            bannerTip.setAdapter(bannerAdapter)
            bannerTip.setUserInputEnabled(false)
            bannerTip.addBannerLifecycleObserver(this@GetSchemeResultActivity)
            bannerTip.isVisible = !resultInfo?.user_list.isNullOrEmpty()
            setViewShadowBg(llMedicalCost)
            setViewShadowBg(llSuccessRate)
            bannerAdapter.setDatas(resultInfo?.user_list)
            tvAddCounselor.setTextWhenNotEmpty(resultInfo?.bottom_button?.title)

            ShadowHelper.Builder()
                .setBgColor(Color.WHITE)
                .setShapeRadius(12.dp)
                .setShadowColor(R.color.black10.toColorInt())
                .setShadowRadius(8.dp)
                .applyTo(clAddCounselor)

            tvResultDesc.post {
                if (tvResultDesc.lineCount > 3) {
                    tvResultDesc.maxLines = 3
                    tvExpand.isVisible = true
                    ivExpand.isVisible = true
                }
                initContentHeight()
            }

            nsvContent.post {
                scrollViewHeight =
                    nsvContent.measuredHeight - nsvContent.paddingTop - nsvContent.paddingBottom
            }

            tvExpand.onClick {
                tvExpand.isSelected = !tvExpand.isSelected
                if (tvExpand.isSelected) {
                    tvResultDesc.maxLines = Int.MAX_VALUE
                    tvExpand.text = "收起内容"
                    ivExpand.animate().rotation(180f)
                } else {
                    tvResultDesc.maxLines = 3
                    tvExpand.text = "展开全部"
                    ivExpand.animate().rotation(0f)
                }
                initContentHeight()
            }

            tvAddCounselor.onClick {
                resultInfo?.bottom_button?.apply {
                    BannerHelper.onClick(CommonBanner(item_type = this.itemType, identity = this.identity ))
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        if (resultInfo == null) {
            finish()
            return
        }
    }

    private fun setViewShadowBg(targetView: View) {
        ShadowHelper.Builder()
            .setBgColor(Color.WHITE)
            .setShapeRadius(12.dp)
            .setShadowColor(R.color.black03.toColorInt())
            .setShadowRadius(16.dp)
            .applyTo(targetView)
    }

    private fun setTipInfo(tipText: TextView, content: String?) {
        if (content.isNullOrBlank()) {
            tipText.isVisible = false
            return
        }

        tipText.isVisible = true
        // 由于文案是后端返回的, 所有需要进行匹配处理
        val pattern = "减少(\\d+(\\.\\d+)?)元费用".toRegex()
        val pattern2 = "提高(\\d+(\\.\\d+)?)%成功率".toRegex()
        val matchResult1 = pattern.find(content)
        val matchResult2 = pattern2.find(content)
        try {
            buildSpannedString {
                if (matchResult1 != null) {
                    append(content.subSequence(0, content.indexOf(matchResult1.value)))
                    bold {
                        color(Color.parseColor("#47BF5B")) {
                            append(matchResult1.value)
                        }
                    }
                }
                if (matchResult2 != null) {
                    append(content.subSequence(this.length, content.indexOf(matchResult2.value)))
                    bold {
                        color(Color.parseColor("#ED5A5A")) {
                            append(matchResult2.value)
                        }
                    }
                }
                append(content.subSequence(this.length, content.length))
                tipText.text = this
            }
        } catch (e: Exception) {
            e.printStackTrace(System.out)
            tipText.text = content
        }
    }

    /**
     * 初始化页面高度
     */
    private fun initContentHeight() {
        binding.nsvContent.getChildAt(0).apply {
            post {
                contentHeight = measuredHeight
            }
        }
    }

    private fun getFlagIcon(flag: Int): Int {
        return when {
            flag > 0 -> R.drawable.common_red_arrow_icon
            flag < 0 -> R.drawable.common_green_arrow_icon
            else -> 0
        }
    }
}