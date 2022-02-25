package com.example.footprints.model.api

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * ロケーションクライアントの実装部分
 *
 * @property context コンテキスト
 * @constructor 引数で指定されたコンテキストを基にロケーションのあれこれを行う
 */
class MyLocationClientImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MyLocationClient {
    // ロケーションの提供元
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // 取得したロケーションをどう扱うかはリスナーにお任せ
    private lateinit var listener: (Location) -> Unit

    /**
     * ロケーションの定期取得を行う
     *
     * @param listener リスナー
     */
    @SuppressLint("MissingPermission")
    override fun startLocationUpdate(listener: (Location) -> Unit) {
        this.listener = listener

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.requestLocationUpdates(
            createLocationRequest(),
            locationCallback,
            Looper.myLooper()!!
        )
    }

    /**
     * ロケーションの取得条件を作成
     *
     * @return ロケーションの取得条件
     */
    private fun createLocationRequest() : LocationRequest {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        return locationRequest
    }

    /**
     * 取得したロケーションの処理はリスナーにお任せ
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            listener(p0.locations[0])
        }
    }

    /**
     * ロケーションの定期取得を終了する
     */
    override fun stopLocationUpdate() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}