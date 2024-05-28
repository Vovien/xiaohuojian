package com.jmbon.minitools.report.fragment

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.graphics.drawable.toBitmapOrNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.showToast
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.BitmapUtils
import com.jmbon.middleware.utils.dp
import com.jmbon.minitools.databinding.FragmentFertilityAbilityTestLayoutBinding
import com.jmbon.minitools.report.bean.FertilityAbilityTestResultBean
import com.jmbon.minitools.report.viewmodel.FertilityAbilityTestViewModel
import com.sxu.shadowdrawable.ShadowDrawable

class FertilityAbilityTestFragment :
    ViewModelFragment<FertilityAbilityTestViewModel, FragmentFertilityAbilityTestLayoutBinding>() {

    override fun initView(view: View) {
        binding.apply {
            ShadowDrawable.setShadowDrawable(
                vShadow,
                Color.TRANSPARENT,
                36.dp,
                ColorUtils.setAlphaComponent(Color.BLACK, 0.15f),
                18.dp,
                0,
                18.dp
            )
            arguments?.getParcelable<FertilityAbilityTestResultBean>(PARAMS_KEY_DATA)?.apply {
                tvScore.text = "${score.toInt()}"
                tvDesc.text = result
                if (!Constant.isAuditMode) {
                    viewModel.getTestResultPicture(id)
                }

            } ?: kotlin.run {
                root.isVisible = false
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.testResultPictureLD.observe(this) {
            savePicture(it)
        }
    }

    /**
     * 下载并保存图片到相册
     */
    private fun savePicture(url: String?) {
        if (url.isNullOrBlank()) {
            return
        }

        GlideUtil.getInstance().loadUrlSimpleTarget(url, object :
            GlideUtil.GlideUtilSimpleTarget {
            override fun onResourceReady(resource: Drawable?) {
                val bitmap = resource?.toBitmapOrNull() ?: return
                context?.let { it1 ->
                    val result = BitmapUtils.saveBitmap(
                        it1,
                        bitmap,
                        "${it1.cacheDir.path}/${System.currentTimeMillis()}.jpeg"
                    )
                    if (result) {
                        "已自动保存测试表单信息\n可在本地相册查看".showToast()
                    }
                }
            }

            override fun onLoadFailed() {

            }

        })
    }

    companion object {

        private const val PARAMS_KEY_DATA = "data"

        fun newInstance(data: Parcelable?): Fragment {
            return FertilityAbilityTestFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PARAMS_KEY_DATA, data)
                }
            }
        }
    }
}