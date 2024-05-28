package com.tubewiki.home.dialog

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apkdv.mvvmfast.base.dialog.BasePartShadowPopupView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.decoration.GridSpaceItemDecoration
import com.jmbon.middleware.extention.setStateBackground
import com.jmbon.middleware.extention.setTextColor
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.qmuiteam.qmui.kotlin.onClick
import com.tubewiki.home.R
import com.tubewiki.home.databinding.DialogHospitalLevelSelectBinding
import com.tubewiki.home.doctor.bean.Territory

class SelectDoctorColumnDialog(
    context: Context,
    var dataSource: List<Territory>,
    var resultAction: (result: List<Territory>?) -> Unit
) :
    BasePartShadowPopupView<DialogHospitalLevelSelectBinding>(context) {

    private val hospitalLevelAdapter by lazy {
        object :
            BaseQuickAdapter<Territory, BaseViewHolder>(R.layout.item_hospital_level_layout) {

            override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
                super.onAttachedToRecyclerView(recyclerView)
                setOnItemClickListener { _, _, position ->
                    data[position].apply {
                        isSelected = !isSelected
                        notifyItemChanged(position)
                    }
                }
            }

            override fun convert(holder: BaseViewHolder, item: Territory) {
                holder.getView<TextView>(R.id.tv_value).apply {
                    text = item.columnName
                    isSelected = item.isSelected
                    setTextColor(
                        state = android.R.attr.state_selected,
                        falseTextColor = R.color.color_262626.toColorInt(),
                        trueTextColor = Color.WHITE
                    )
                    setStateBackground(
                        state = android.R.attr.state_selected,
                        falseBackgroundColor = R.color.color_F7F7F7.toColorInt(),
                        trueBackgroundColor = R.color.color_currency.toColorInt(),
                        radius = 8.dp
                    )
                }
            }
        }
    }

    override fun onCreate() {
        super.onCreate()

        binding.apply {
            rvLevel.addItemDecoration(GridSpaceItemDecoration(10.dp, false))
            rvLevel.adapter = hospitalLevelAdapter.apply {
                setList(dataSource)
            }

            sbReSet.onClick {
                hospitalLevelAdapter.data.forEach { it.isSelected = false }
                hospitalLevelAdapter.notifyDataSetChanged()
            }

            sbOk.onClick {
                val result = hospitalLevelAdapter.data.filter { it.isSelected }.map{it}
                resultAction(result)
                dismiss()
            }

        }
    }

}