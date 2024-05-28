package com.tubewiki.home.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.jmbon.middleware.bean.ArticleDetailBean
import com.jmbon.middleware.decoration.GridSpacingItemDecoration
import com.jmbon.middleware.utils.dp
import com.tubewiki.home.adapter.ArticleHelpGroupAdapter
import com.tubewiki.home.bean.RecommendCircle
import com.tubewiki.home.databinding.ViewGroupChatAdLayoutBinding

/**
 * 群聊广告
 * @author MilkCoder
 * @date 2023/11/9 15:20
 * @copyright all rights reserved by ManTang
 */
class GroupChatAdLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {

    private val helpGroupAdapter by lazy {
        ArticleHelpGroupAdapter()
    }

    private val binding by lazy {
        ViewGroupChatAdLayoutBinding.inflate(LayoutInflater.from(context), this)
    }

    fun setData(articleCircles:  List<RecommendCircle>?) {
        if (articleCircles.isNullOrEmpty()) {
            isVisible = false
            return
        }

        isVisible = true
        binding.apply {
            binding.rvContent.apply {
                addItemDecoration(GridSpacingItemDecoration(spacing = 10.dp, edgeSpacing = 20.dp))
                adapter = helpGroupAdapter
            }
            helpGroupAdapter.setList(articleCircles)
        }
    }
}