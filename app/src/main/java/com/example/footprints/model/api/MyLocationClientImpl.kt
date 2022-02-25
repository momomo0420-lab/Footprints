package com.example.footprints.model.api

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationResult
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * ロケーションクライアントの実装部分
 *
 * @property context コンテキスト
 * @property client ロケーションクライアント
 * @constructor 引数で指定されたコンテキストを基にロケーションのあれこれを行う
 */
class MyLocationClientImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: FusedLocationProviderClient
) : MyLocationClient {

    // 取得したロケーションをどう扱うかはリスナーにお任せ
    private lateinit var listener: (Location) -> Unit

    private val cancellationTokenSource = CancellationTokenSource()

    /**
     * ロケーションの定期取得を行う
     *
     * @param listener リスナー
     */
    @SuppressLint("MissingPermission")
    override fun startLocationUpdate(listener: (Location) -> Unit) {
        this.listener = listener

        client.requestLocationUpdates(
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
        client.removeLocationUpdates(locationCallback)
    }

    /**
     * 現在地を取得する
     *
     * @param listener リスナー
     */
    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(listener: (Location) -> Unit) {
        client.getCurrentLocation(
            PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        )
        .addOnSuccessListener {
            listener(it)
        }
    }
}