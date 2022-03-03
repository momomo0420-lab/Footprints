package com.example.footprints.ui.main

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.*
import androidx.work.WorkManager
import com.example.footprints.model.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val geocoder: Geocoder?,
    private val workManager: WorkManager
) : ViewModel() {

    // 実行可能状態の確認フラグ
    private val _isRunnable = MutableLiveData(true)
    val isRunnable: LiveData<Boolean> get() = _isRunnable

    // 保持されているMyLocation全件
    val myLocationList = repository.loadAll().asLiveData()

    /**
     * 定期的にロケーションを取得する
     */
    fun startLocationUpdate() {
        repository.startLocationUpdate(getOnLocationUpdateListener())
//        repository.getCurrentLocation(getOnLocationUpdateListener())
        _isRunnable.value = false
    }

    /**
     * ロケーションを取得した際の動作
     */
    private fun getOnLocationUpdateListener(): (Location) -> Unit {
        return object : (Location) -> Unit {
            override fun invoke(p1: Location) {
                onLocationUpdate(p1)
//                _isRunnable.value = true
            }
        }
    }

    /**
     * 定期的にロケーションを取得した際の動作
     *
     * @param location ロケーション
     */
    private fun onLocationUpdate(location: Location) {
        val address = geocoder?.let {
            val addressList = it.getFromLocation(location.latitude, location.longitude, 1)
            addressList[0].getAddressLine(0).toString()
        } ?: return

        viewModelScope.launch {
            try {
                val lastLocation = repository.loadLastAddress()
                if(address == lastLocation) {
                    return@launch
                }
                repository.insert(location, address)

            } catch (e: Exception) {
            }
        }
    }

    /**
     * 定期的なロケーション取得を停止する
     */
    fun stopLocationUpdate() {
        repository.stopLocationUpdate()
        _isRunnable.value = true
    }
}