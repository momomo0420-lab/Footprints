package com.example.footprints.model.api

import android.location.Location

/**
 * ロケーションクライアント
 */
interface MyLocationClient {
    // ロケーションの定期取得を開始する
    fun startLocationUpdate(listener: (Location) -> Unit)
    // ロケーションの定期取得を終了する
    fun stopLocationUpdate()
    // 現在地を取得する
    fun getCurrentLocation(listener: (Location) -> Unit)
}