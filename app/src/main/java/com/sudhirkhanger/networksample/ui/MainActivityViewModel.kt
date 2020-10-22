package com.sudhirkhanger.networksample.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sudhirkhanger.networksample.NetworkSampleRepository
import kotlinx.coroutines.Dispatchers

class MainActivityViewModel internal constructor(
    repository: NetworkSampleRepository
) : ViewModel() {

    private val countriesListing = MutableLiveData(repository.getCountries(Dispatchers.IO))
    val countries = countriesListing.switchMap { it.data }
    val networkState = countriesListing.switchMap { it.networkState }

    fun refresh() = countriesListing.value?.refresh?.invoke()
    fun search(query: String?) = countriesListing.value?.search?.invoke(query)
}
