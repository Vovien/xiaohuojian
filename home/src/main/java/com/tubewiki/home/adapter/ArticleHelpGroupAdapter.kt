package com.tubewiki.home.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.allen.library.CircleImageView
import com.apkdv.mvvmfast.ktx.BindingQuickAdapter
import com.jmbon.middleware.bean.ArticleDetailBean
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.extention.loadUrlWithSize
import com.jmbon.middleware.extention.setBackground
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.parseColor
import com.tubewiki.home.R
import com.tubewiki.home.bean.ExperienceDetailData
import com.tubewiki.home.bean.RecommendCircle
import com.tubewiki.home.databinding.ItemPregnancyHelpGroupLayoutBinding
import kotlin.math.min

class ArticleHelpGroupAdapter :
    BindingQuickAdapter<RecommendCircle, ItemPregnancyHelpGroupLayoutBinding>() {
    override fun convert(holder: BaseBindingHolder, item: RecommendCircle) {
        holder.getViewBinding<ItemPregnancyHelpGroupLayoutBinding>().apply {
            root.setBackground(
                backgroundColor = parseColor(
                    item.color.ifEmpty { "#FFFFFAE2" },
                    Color.TRANSPARENT
                ),
                radius = 12.dp
            )
            tvTitle.text = item.name
            tvDesc.text = item.desc
            holder.setGone(
                R.id.gp_member,
                item.memberAvatar.isNullOrEmpty()
            )
            initOnlineMemberLayout(holder.getView(R.id.fl_member_icons), item.memberAvatar)
            tvOnlineCount.text = item.txt
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        setOnItemClickListener { _, _, position ->
            BannerHelper.onClick(
                CommonBanner(
                    item_type = data[position].itemType,
                    identity = data[position].identity
                )
            )
        }
    }

    /**
     * 初始化用户头像
     */
    private fun initOnlineMemberLayout(containerLayout: FrameLayout, icons: List<String>?) {
        containerLayout.removeAllViews()
        if (icons.isNullOrEmpty()) {
            return
        }

        val iconSize = 20.dp
        val padding = 1.dp
        val count = min(icons.size, 3)
        icons.subList(0, count).forEachIndexed { index, item ->
            CircleImageView(containerLayout.context).apply {
                setPadding(padding, padding, padding, padding)
                loadUrlWithSize(item, iconSize, iconSize)
                setBackground(
                    shape = GradientDrawable.OVAL,
                    borderColor = Color.WHITE,
                    borderWidth = 1.dp
                )
                containerLayout.addView(this, FrameLayout.LayoutParams(iconSize, iconSize).apply {
                    leftMargin = index * 12.dp
                })
            }
        }
    }
}