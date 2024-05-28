package com.jmbon.middleware.helper

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.apkdv.mvvmfast.glide.GlideApp
import com.apkdv.mvvmfast.ktx.launch
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jmbon.middleware.utils.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * 设置等比例缩放图片
 * @param url: 图片地址
 * @param maxHeight: 图片的最大高度
 */
fun ImageView.setScaleImage(url: String?, maxHeight: Int = 600.dp) {
    (context as? FragmentActivity)?.launch {
        withContext(Dispatchers.IO) {
            runCatching {
                GlideApp.with(Utils.getApp())
                    .asBitmap()
                    .load(url ?: "")
                    .apply(RequestOptions().timeout(3000))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .submit()
                    .get()
            }.getOrNull()
        }.apply {
            if (this == null || this.isRecycled) {
                return@launch
            }

            var imageWidth = measuredWidth
            if (imageWidth <= 0) {
                imageWidth = ScreenUtils.getScreenWidth()
            }
            val hScale = this.width * 1.0f / imageWidth
            val newBitmap = if (hScale > 0) {
                if (this.height / hScale > maxHeight) {
                    Bitmap.createBitmap(this, 0, 0, this.width, maxHeight)
                } else {
                    Bitmap.createScaledBitmap(this, imageWidth, (this.height / hScale).toInt(), true)
                }
            } else {
                this
            }
            setImageBitmap(newBitmap)
        }
    }
}