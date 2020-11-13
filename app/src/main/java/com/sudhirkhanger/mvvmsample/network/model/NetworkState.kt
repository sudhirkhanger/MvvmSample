package com.sudhirkhanger.mvvmsample.network.model

enum class NetworkStatus {
    LOADING,
    SUCCESS,
    FAILED
}

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: NetworkStatus,
    val msg: String? = null
) {
    companion object {
        val SUCCESS = NetworkState(NetworkStatus.SUCCESS)
        val LOADING = NetworkState(NetworkStatus.LOADING)
        fun error(msg: String?) = NetworkState(NetworkStatus.FAILED, msg)
    }
}