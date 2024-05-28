package com.apkdv.mvvmfast.ktx

import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder


abstract class BindingAdapter<T, VB : ViewBinding>(dataList: List<T>? = mutableListOf()) :
    BaseQuickAdapter<T, BaseBindingHolder>(layoutResId = -1, data = dataList?.toMutableList()) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int) =
        BaseBindingHolder(inflateBindingWithGeneric<VB>(parent))

    final override fun convert(holder: BaseBindingHolder, item: T) {
        holder.getViewBinding<VB>().apply {
            convert(this, item)
        }
    }

    abstract fun convert(binding: VB, item: T)

    private var last = 0L
    private var lastChildHashCode = 0
    var childTimeLag = 500
    override fun setOnItemChildClick(v: View, position: Int) {
        val current = System.currentTimeMillis()
        if (lastChildHashCode != v.hashCode()) {
            super.setOnItemChildClick(v, position)
            last = System.currentTimeMillis()
            lastChildHashCode = v.hashCode()
        } else if (current - last > childTimeLag) {
            super.setOnItemChildClick(v, position)
            last = System.currentTimeMillis()
            lastChildHashCode = v.hashCode()
        }
    }

    private var itemLast = 0L
    private var lastItemHashCode = 0
    override fun setOnItemClick(v: View, position: Int) {
        val current = System.currentTimeMillis()
        if (lastItemHashCode != v.hashCode()) {
            super.setOnItemClick(v, position)
        } else if (current - itemLast > childTimeLag) {
            super.setOnItemClick(v, position)
        }
        itemLast = System.currentTimeMillis()
        lastItemHashCode = v.hashCode()
    }
}

open class BaseBindingHolder(private val binding: ViewBinding) : BaseViewHolder(binding.root) {
    constructor(itemView: View) : this(ViewBinding { itemView })
    @Suppress("UNCHECKED_CAST")
    fun <VB : ViewBinding> getViewBinding() = binding as VB
}