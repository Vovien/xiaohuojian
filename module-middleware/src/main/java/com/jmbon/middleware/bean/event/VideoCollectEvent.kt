package com.jmbon.middleware.bean.event

import androidx.annotation.Keep


@Keep
data class VideoCollectEvent(val isCollection: Boolean, val videoId: Int = 0)
