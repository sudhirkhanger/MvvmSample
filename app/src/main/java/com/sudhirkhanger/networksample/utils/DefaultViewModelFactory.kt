package com.sudhirkhanger.networksample.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sudhirkhanger.networksample.NetworkSampleRepository
import com.sudhirkhanger.networksample.ui.first.FirstViewModel
import com.sudhirkhanger.networksample.ui.fourth.FourthActivityViewModel
import com.sudhirkhanger.networksample.ui.second.SecondViewModel
import com.sudhirkhanger.networksample.ui.third.ThirdViewModel

class DefaultViewModelFactory(
    private val repository: NetworkSampleRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FirstViewModel::class.java) ->
                FirstViewModel(repository) as T
            modelClass.isAssignableFrom(SecondViewModel::class.java) ->
                SecondViewModel(repository) as T
            modelClass.isAssignableFrom(ThirdViewModel::class.java) ->
                ThirdViewModel(repository) as T
            modelClass.isAssignableFrom(FourthActivityViewModel::class.java) ->
                FourthActivityViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}
