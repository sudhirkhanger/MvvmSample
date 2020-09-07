package com.sudhirkhanger.networksample.ui.first

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sudhirkhanger.networksample.NetworkSampleRepository
import timber.log.Timber

class FirstViewModel internal constructor(
    private val repository: NetworkSampleRepository
) : ViewModel() {

    private val _counterLiveData = MutableLiveData(0)
    val counterLiveData: LiveData<Int>
        get() = _counterLiveData

    fun increment() {
        val current = _counterLiveData.value!!
        _counterLiveData.value = current.plus(1)
    }

    override fun onCleared() {
        Timber.e("cleared")
        super.onCleared()
    }
}