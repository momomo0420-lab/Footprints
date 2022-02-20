package com.example.footprints.ui.main

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
        _isRunnable.value = false
    }

    /**
     * ロケーションを取得した際の動作
     */
    private fun getOnLocationUpdateListener(): (Location) -> Unit {
        return object : (Location) -> Unit {
            override fun invoke(p1: Location) {
                onLocationUpdate(p1)
            }
        }
    }

    /**
     * 定期的にロケーションを取得した際の動作
     *
     * @param location ロケーション
     */
    private fun onLocationUpdate(location: Location) {
        val address = repository.convertLocationToAddress(location)

        viewModelScope.launch {
            try {
                val lastLocation = repository.loadLastLocation()
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