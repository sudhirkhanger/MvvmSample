package com.sudhirkhanger.networksample.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudhirkhanger.networksample.NetworkSampleRepository
import com.sudhirkhanger.networksample.network.model.Resource
import com.sudhirkhanger.networksample.network.model.Status
import com.sudhirkhanger.networksample.utils.Event
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MainActivityViewModel internal constructor(
    private val repository: NetworkSampleRepository
) : ViewModel() {

    private val _countriesLiveData = MutableLiveData<Resource<CountriesResponse>>()
    val countriesLiveData: LiveData<Resource<CountriesResponse>>
        get() = _countriesLiveData

    private val _countriesViewEffects = MutableLiveData<Event<Resource<Status>>>()
    val countriesViewEffects: LiveData<Event<Resource<Status>>>
        get() = _countriesViewEffects

    private val countries: MutableList<Country?> = mutableListOf()

    init {
        getCountries(null)
    }

    private fun fetchCountries() {
        viewModelScope.launch {
            repository.getCountries()
                .onStart { _countriesViewEffects.postValue(Event(Resource.loading(null))) }
                .catch { _countriesViewEffects.postValue(Event(Resource.error(it.message ?: "", null))) }
                .collect {
                    it.data?.let { list -> countries.addAll(list) }
                    _countriesLiveData.postValue(Resource.success(it))
                }
        }
    }

    fun getCountries(query: String?) {
        if (query == null) {
            fetchCountries()
        } else {
            val filteredList =
                countries.filter { item -> item?.name?.contains(query, true) ?: false }
            _countriesLiveData.postValue(
                Resource.success(CountriesResponse(filteredList, "", "1"))
            )
        }
    }
}
