package com.sudhirkhanger.networksample.ui.fourth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sudhirkhanger.networksample.NetworkSampleRepository
import com.sudhirkhanger.networksample.network.model.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FourthActivityViewModel internal constructor(
    private val repository: NetworkSampleRepository
) : ViewModel() {

    private val _countriesLiveData = MutableLiveData<Resource<CountriesResponse>>()
    val countriesLiveData: LiveData<Resource<CountriesResponse>>
        get() = _countriesLiveData

    private val countries: MutableList<Country?> = mutableListOf()

    init {
        getCountries()
    }

    fun getCountries() {
        viewModelScope.launch {
            repository.getCountries()
                .onStart { _countriesLiveData.postValue(Resource.loading(null)) }
                .catch { _countriesLiveData.postValue(Resource.error(it.message ?: "", null)) }
                .collect {
                    it.data?.let { list -> countries.addAll(list) }
                    _countriesLiveData.postValue(Resource.success(it))
                }
        }
    }

    fun queryCountries(query: String) {
        if (query.isEmpty()) {
            _countriesLiveData.postValue(
                Resource.success(CountriesResponse(countries, "", "1"))
            )
        } else {
            val filteredList =
                countries.filter { item -> item?.name?.contains(query, true) ?: false }
            _countriesLiveData.postValue(
                Resource.success(CountriesResponse(filteredList, "", "1"))
            )
        }
    }
}
