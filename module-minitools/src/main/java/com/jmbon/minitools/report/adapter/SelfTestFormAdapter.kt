package com.jmbon.minitools.report.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.SpanUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.bean.Form
import com.jmbon.middleware.bean.ItemAnswer
import com.jmbon.middleware.extention.setStateBackground
import com.jmbon.middleware.extention.setTextColor
import com.jmbon.middleware.extention.toColorInt
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.minitools.R


class SelfTestFormAdapter(
    private val source: String,
    private val action: (url: String) -> Unit
) :
    BaseQuickAdapter<Form, BaseViewHolder>(R.layout.item_test_form_layout) {

    private var itemAction: ((action: ItemAnswer) -> Unit)? = null

    private fun createAnswerAdapter(): BaseQuickAdapter<ItemAnswer, BaseViewHolder> {
        return object :
            BaseQuickAdapter<ItemAnswer, BaseViewHolder>(R.layout.item_child_answer_layout) {

            init {
                setOnItemClickListener { _, _, position ->
                    itemAction?.invoke(data[position])
                }
            }

            override fun convert(
                holder: BaseViewHolder,
                item: ItemAnswer
            ) {
                holder.setText(R.id.tv_answer, item.title)
                holder.getView<TextView>(R.id.tv_answer).apply {
                    text = item.title
                    setTextColor(
                        state = android.R.attr.state_pressed,
                        falseTextColor = R.color.color_262626.toColorInt(),
                        trueTextColor = Color.WHITE
                    )
                    setStateBackground(
                        state = android.R.attr.state_pressed,
                        falseBackgroundColor = R.color.color_F1F1F1.toColorInt(),
                        trueBackgroundColor = R.color.color_currency.toColorInt(),
                        radius = 8.dp
                    )
                }
            }
        }
    }

    override fun convert(holder: BaseViewHolder, item: Form) {
        holder.setText(R.id.tv_question, item.title)
        if (source.isNotEmpty()) {
            val textTip = holder.getView<TextView>(R.id.tv_tip)
            textTip.isVisible = true
            val index = source.indexOf("http")
            if (index != -1) {
                val link = source.substring(index)
                SpanUtils.with(textTip)
                    .append("数据来源：")
                    .append(source.substring(0, index))
                    .append(link)
                    .setClickSpan(ColorUtils.getColor(R.color.color_currency), false) {
                        if (link.contains(".pdf")) {
                            action(link)
                            return@setClickSpan
                        }
                        ARouter.getInstance().build("/webview/activity")
                            .withString("url", link)
                            .withString("title", "备孕小火箭")
                            .withBoolean("enableTBS", false)
                            .navigation()
                    }
                    .create()
            } else {
                SpanUtils.with(textTip)
                    .append("数据来源：")
                    .append(source)
                    .create()
            }
        }
        holder.getView<RecyclerView>(R.id.rv_answer).apply {
            createAnswerAdapter().apply {
                setList(item.content)
                init(this, dividerHeight = 8f, vertical = false)
            }
        }
    }

    /**
     * 设置答案的选择事件
     */
    fun setItemClickListener(action: (answerInfo: ItemAnswer) -> Unit) {
        this.itemAction = action
    }
}