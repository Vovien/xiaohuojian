package com.apkdv.mvvmfast.view.state.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.apkdv.mvvmfast.view.state.MultiState
import com.apkdv.mvvmfast.view.state.MultiStateContainer

class SuccessState : MultiState() {
    override fun onCreateMultiStateView(
        context: Context,
        inflater: LayoutInflater,
        container: MultiStateContainer
    ): View {
        return View(context)
    }

    override fun onMultiStateViewCreate(
        view: View,
        retry: () -> Unit,
        offset: Int,
        orientation: Int
    ) {

    }


}