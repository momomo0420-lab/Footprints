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
    // 引数のロケーションを住所（文字列）に変換する
    fun convertLocationToAddress(location: Location) : String
}