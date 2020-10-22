package com.sudhirkhanger.networksample.network.model

import androidx.lifecycle.LiveData
import com.sudhirkhanger.networksample.utils.Event

data class Listing<T>(
        val data: LiveData<List<T?>>,
        val networkState: LiveData<Event<NetworkState>>,
        val search: ((String?) -> Unit)? = null,
        val refresh: (() -> Unit)? = null
)