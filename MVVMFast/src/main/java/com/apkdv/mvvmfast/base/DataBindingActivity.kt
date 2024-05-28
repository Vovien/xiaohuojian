package com.apkdv.mvvmfast.base

import androidx.databinding.ViewDataBinding

abstract class DataBindingActivity<VM : BaseViewModel, DB : ViewDataBinding> : ViewModelActivity<VM, DB>()