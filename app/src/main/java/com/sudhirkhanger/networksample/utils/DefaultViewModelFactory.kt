package com.sudhirkhanger.networksample.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sudhirkhanger.networksample.NetworkSampleRepository
import com.sudhirkhanger.networksample.ui.MainActivityViewModel

class DefaultViewModelFactory(
    private val repository: NetworkSampleRepository
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
