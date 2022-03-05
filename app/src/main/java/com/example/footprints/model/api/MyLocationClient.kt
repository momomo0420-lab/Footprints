package com.example.footprints.model.api

import android.location.Location

/**
 * ロケーションクライアント
 */
interface MyLocationClient {
    // 現在地を取得する
    fun getCurrentLocation(listener: (Location) -> Unit)
}