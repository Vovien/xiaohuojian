package com.yxbabe.xiaohuojian.activity

import android.view.View
import androidx.core.view.isVisible
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.DataBindingFragment
import com.gyf.immersionbar.ImmersionBar
import com.yxbabe.xiaohuojian.R
import com.yxbabe.xiaohuojian.viewmodel.LaunchAdvertisingViewModel
import com.yxbabe.xiaohuojian.databinding.FragmentLaunchAdvertisingBinding
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.extention.getViewModel
import com.jmbon.middleware.utils.BannerHelper
import com.tubewiki.home.bean.LaunchAdv
import kotlinx.coroutines.Job

/**
 * 启动页广告
 * @author MilkCoder
 * @date 2023/5/29
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
@Route(path = "/app/advertising/fragment")
class LaunchAdvertisingFragment :
    DataBindingFragment<LaunchAdvertisingViewModel, FragmentLaunchAdvertisingBinding>() {

    private var mCountdownJob: Job? = null

    private val ownerVM by lazy {
        context.getViewModel(LaunchAdvertisingViewModel::class.java)
    }

    @Autowired
    @JvmField
    var launchAdv: LaunchAdv? = null

    override fun beforeViewInit() {
        super.beforeViewInit()
        ImmersionBar.with(this)
            .statusBarDarkFont(true)
            .transparentNavigationBar()
            .init()
    }

    override fun initView(view: View) {
        binding.launchAdv = launchAdv
        binding.executePendingBindings()
        binding.onClickListener = View.OnClickListener {
            when (it.id) {
                R.id.jump_linear_layout -> {
                    mCountdownJob?.cancel()
                }

                R.id.adv_image, R.id.go_to_detail -> {
                    launchAdv?.adv?.apply {
                        mCountdownJob?.cancel()
                        BannerHelper.onClick(
                            CommonBanner(
                                item_type = this.itemType,
                                identity = this.identity
                            )
                        )
                    }
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
        ownerVM?.advBitmapLD?.observe(this) {
            if (it == null || it.isRecycled) {
                ownerVM?.postEnterAppEvent(true)
                return@observe
            }

            binding.advImage.setImageBitmap(it)
            binding.jumpLinearLayout.isVisible = true
            binding.goToDetail.isVisible = true

            mCountdownJob = viewModel.countDownCoroutines(5, onTick = { it1 ->
                binding.jumpNumTextView.text = it1.toString()
            }, onFinish = {
                ownerVM?.postEnterAppEvent(true)
            })
        } ?: kotlin.run {
            ownerVM?.postEnterAppEvent(true)
        }
        ownerVM?.loadUrlGetBitmap(launchAdv?.adv?.cover)
    }
}