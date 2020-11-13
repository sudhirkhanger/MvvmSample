package com.sudhirkhanger.mvvmsample.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sudhirkhanger.mvvmsample.MvvmSampleRepository
import com.sudhirkhanger.mvvmsample.ui.MainActivityViewModel

class DefaultViewModelFactory(
    private val repository: MvvmSampleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainActivityViewModel::class.java) ->
                MainActivityViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}
