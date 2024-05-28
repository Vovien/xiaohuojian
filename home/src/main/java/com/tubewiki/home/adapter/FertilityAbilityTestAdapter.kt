package com.tubewiki.home.adapter

import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.jmbon.middleware.arouter.service.IMiniToolProvider
import com.tubewiki.home.R
import com.jmbon.middleware.bean.TestFormBean
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.valid.action.Action

class FertilityAbilityTestAdapter :
    BaseQuickAdapter<TestFormBean, BaseViewHolder>(R.layout.item_fertility_ability_test_layout) {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            Action {
                ARouter.getInstance().navigation(IMiniToolProvider::class.java)
                    .toSelfTest(data[position].form, data[position].source)
            }.logInToIntercept()
        }
    }

    override fun convert(holder: BaseViewHolder, item: TestFormBean) {
        holder.setText(R.id.tv_title, item.title)
        holder.setText(R.id.tv_desc, item.desc)
        holder.setGone(R.id.tv_desc, item.desc.isNullOrBlank())
    }
}