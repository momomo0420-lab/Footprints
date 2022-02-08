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
    // スタートボタンの状態。tureなら待機状態
    private val _startButtonIsEnabled = MutableLiveData(true)
    val startButtonIsEnabled: LiveData<Boolean> = _startButtonIsEnabled

    // ストップボタンの状態。tureなら実行状態
    private val _stopButtonIsEnabled = MutableLiveData(false)
    val stopButtonIsEnabled: LiveData<Boolean> = _stopButtonIsEnabled

    // 現在の住所
    private val _currentAddress = MutableLiveData("")
    val currentAddress: LiveData<String> = _currentAddress

    // 保持されているMyLocation全件
    val myLocationList = repository.loadAll().asLiveData()

    /**
     * ボタンの状態を切り替える
     */
    private fun switchRunnableFlag() {
        _startButtonIsEnabled.value = !startButtonIsEnabled.value!!
        _stopButtonIsEnabled.value = !stopButtonIsEnabled.value!!
    }

    /**
     * 定期的にロケーションを取得する
     */
    fun startLocationUpdate() {
        repository.startLocationUpdate(listener)
        switchRunnableFlag()
    }

    /**
     * ロケーションを取得した際の動作
     */
    private val listener = object : (Location) -> Unit {
        override fun invoke(location: Location) {
            onLocationUpdate(location)
        }
    }

    /**
     * 定期的にロケーションを取得した際の動作
     *
     * @param location ロケーション
     */
    private fun onLocationUpdate(location: Location) {
        val address = repository.convertLocationToAddress(location)
        _currentAddress.value = address

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
        switchRunnableFlag()
    }
}