package com.apkdv.mvvmfast.base.dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewbinding.ViewBinding
import com.apkdv.mvvmfast.ktx.inflateBindingWithGeneric
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.CenterPopupView

/**
 * time   : 2021/4/29
 * desc   : 改造CenterPopupView  使用 viewbinding
 * version: 1.0
 */
abstract class BaseCenterDialog<T : ViewBinding>(context: Context) : CenterPopupView(context) {

    protected lateinit var binding: T

    init {
        XPopup.setAnimationDuration(300)
    }

    override fun addInnerContent() {
        binding = inflateBindingWithGeneric(LayoutInflater.from(context))
        binding.root.layoutParams = LinearLayout.LayoutParams(
            centerPopupContainer.layoutParams.width,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        centerPopupContainer.addView(binding.root)

    }

    fun getMyRootView(): View {
        return binding.root
    }

}