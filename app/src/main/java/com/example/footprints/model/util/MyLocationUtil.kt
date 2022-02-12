package com.example.footprints.model.util

import android.content.Context
import android.location.Geocoder
import android.location.Location

object MyLocationUtil {
    //TODO この処理本当にここでいいの？
    /**
     * 引数のロケーションを住所（文字列）に変換する
     *
     * @param location ロケーション
     * @return ロケーションから導き出された住所（文字列）
     */
    fun convertLocationToAddress(context: Context, location: Location) : String {
        if(!Geocoder.isPresent()) {
            return ""
        }

        val geocoder = Geocoder(context)
        val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)

        return addressList[0].getAddressLine(0).toString()
    }
}