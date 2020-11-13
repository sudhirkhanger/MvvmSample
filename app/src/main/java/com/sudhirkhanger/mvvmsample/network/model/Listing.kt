package com.sudhirkhanger.mvvmsample.network.model

import androidx.lifecycle.LiveData
import com.sudhirkhanger.mvvmsample.utils.Event

data class Listing<T>(
        val data: LiveData<List<T?>>,
        val networkState: LiveData<Event<NetworkState>>,
        val refresh: (() -> Unit)? = null
)