package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpV6 {
    @Domain(name = "V6", className = "V6")
    const val BASE_URL_V5 = "${BuildConfig.BASE_URL}app/v6/"
    fun get(url: String): RxHttpNoBodyParam {
        return RxV6Http.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxV6Http.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxV6Http.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxV6Http.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxV6Http.postJsonArray(url)
    }
}