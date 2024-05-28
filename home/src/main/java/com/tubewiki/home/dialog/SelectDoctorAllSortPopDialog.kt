package com.tubewiki.home.dialog

import android.content.Context
import android.graphics.Typeface
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.base.dialog.BasePartShadowPopupView
import com.apkdv.mvvmfast.ktx.setTextStyle
import com.blankj.utilcode.util.ColorUtils
import com.jmbon.middleware.extention.toColorInt
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.SimpleCallback
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.SortItemBean
import com.tubewiki.home.databinding.DialogAllSortSearchSelectBinding
import com.tubewiki.home.databinding.ItemHospitalSortLayoutBinding

class SelectDoctorAllSortPopDialog(
    context: Context,
    var type: Int,
    var isWhiteBg: Boolean = false,
    val list: MutableList<SortItemBean>,
    var result: (Int) -> Unit
) :
    BasePartShadowPopupView<DialogAllSortSearchSelectBinding>(context) {

    private val sortAdapter by lazy {
        object : com.apkdv.mvvmfast.ktx.BindingAdapter<SortItemBean, ItemHospitalSortLayoutBinding>(
            list.apply {
                find { it.value == type }?.isSelected = true
            }
        ) {

            override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                super.onAttachedToRecyclerView(recyclerView)
                setOnItemClickListener { _, _, position ->
                    data[position].apply {
                        if (isSelected) {
                            return@setOnItemClickListener
                        }

                        data.find { it.isSelected }?.isSelected = false
                        isSelected = true
                        notifyDataSetChanged()
                        result(value)
                        dismiss()
                    }
                }
            }

            override fun convert(binding: ItemHospitalSortLayoutBinding, item: SortItemBean) {
                binding.apply {
                    tvSort.text = item.name
                    if (item.isSelected) {
                        tvSort.setTextStyle(Typeface.BOLD)
                        tvSort.setTextColor(R.color.color_currency.toColorInt())
                    } else {
                        tvSort.setTextStyle(Typeface.NORMAL)
                        tvSort.setTextColor(R.color.color_262626.toColorInt())
                    }
                    ivChecked.isVisible = item.isSelected
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (!isWhiteBg) {
            binding.root.setBackgroundColor(ColorUtils.getColor(R.color.color_fa))
        }
        binding.rvSort.adapter = sortAdapter
    }

    fun showDialog(anchorView: View) {
        XPopup.Builder(context)
            .atView(anchorView)
            .isClickThrough(true)
            .autoOpenSoftInput(false)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .enableDrag(false)
            .setPopupCallback(object : SimpleCallback() {
                override fun onDismiss(popupView: BasePopupView?) {
                    anchorView.animate()?.rotation(0f)
                }
            })
            .asCustom(this).show()
    }
}
