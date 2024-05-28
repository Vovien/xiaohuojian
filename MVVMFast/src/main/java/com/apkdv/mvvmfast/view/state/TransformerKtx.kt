package com.apkdv.mvvmfast.view.state

import com.apkdv.mvvmfast.R
import com.apkdv.mvvmfast.view.state.state.*

fun MultiStateContainer?.showEmptyState() {
    this?.show<EmptyState>()
}

fun MultiStateContainer?.showEmptyState2() {
    this?.show<EmptyState2>()
}

fun MultiStateContainer?.showEmptyState2(tipsMsg: String) {
    this?.show<EmptyState2> { emptyState ->
        emptyState.setEmptyMsg(tipsMsg)
    }
}

fun MultiStateContainer?.showNoSearchDataState() {
    this?.show<EmptyState> { state ->
        state.setEmptyMsg("没有搜索到相关内容")
        state.setEmptyIcon(R.drawable.icon_no_data)
    }
}


fun MultiStateContainer?.showMessageEmptyState() {
    this?.show<EmptyState> { state ->
        state.setEmptyMsg("暂无消息")
        state.setEmptyIcon(R.drawable.icon_empty_message)
    }
}


fun MultiStateContainer?.showErrorState() {
    this?.show<ErrorState>()
}

fun MultiStateContainer?.showErrorState(errorMsg: String?) {
    this?.show<ErrorState> { errorState ->
        errorState.setErrorMsg(errorMsg)
    }
}

fun MultiStateContainer?.showErrorState(bgColor: Int) {
    this?.show<ErrorState> { errorState ->
        errorState.setErrorBgColor(bgColor)
    }
}

fun MultiStateContainer?.showErrorState(errorMsg: String?, bgColor: Int) {
    this?.show<ErrorState> { errorState ->
        errorState.setErrorMsg(errorMsg)
        errorState.setErrorBgColor(bgColor)
    }
}


fun MultiStateContainer?.showLoadingState() {
    this?.show<LoadingState>()
}

fun MultiStateContainer?.showNoNetState() {
    this?.show<NoNetWorkState>()
}

fun MultiStateContainer?.showContentState() {
    this?.show<SuccessState>()
}
