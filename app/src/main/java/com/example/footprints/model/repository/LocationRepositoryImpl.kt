package com.example.footprints.model.repository

import android.location.Location
import com.example.footprints.model.api.MyLocationClient
import com.example.footprints.model.dao.MyLocationDao
import com.example.footprints.model.entity.MyLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject


/**
 * ロケーションリポジトリ
 *　データの一括管理を行う
 *
 * @property client ロケーションクライアント
 * @property dao ロケーションDAO
 */
class LocationRepositoryImpl @Inject constructor(
    private val client: MyLocationClient,
    private val dao: MyLocationDao
) : LocationRepository {

    // リスナー
    private lateinit var listener: (Location) -> Unit

    /**
     * ロケーションの定期取得を行い、リスナーに処理を移譲する
     *
     * @param listener リスナー
     */
    override fun startLocationUpdate(listener: (Location) -> Unit) {
        this.listener = listener

        client.startLocationUpdate { location ->
            listener(location)
        }
    }

    /**
     * ロケーションの定期取得を終了する
     */
    override fun stopLocationUpdate() {
        client.stopLocationUpdate()
    }

    /**
     * ロケーションを住所（文字列）に変換する
     *
     * @param location ロケーション
     * @return ロケーションから導き出された住所（文字列）
     */
    override fun convertLocationToAddress(location: Location): String {
        return client.convertLocationToAddress(location)
    }

    /**
     * ロケーションをDBに登録する
     *
     * @param location ロケーション
     * @param address 住所文字列
     */
    override suspend fun insert(location: Location, address: String) {
        val now = System.currentTimeMillis()

        val myLocation = MyLocation(
            latitude = location.latitude,
            longitude = location.longitude,
            address = address,
            dateAndTime = now
        )

        withContext(Dispatchers.IO) {
            dao.insert(myLocation)
        }
    }

    /**
     * DBに登録されているMyLocationを全件取得
     *
     * @return MyLocationリスト
     */
    override fun loadAll(): Flow<List<MyLocation>> {
        return dao.loadAll()
    }

    /**
     * DBに登録されている最後に取得したMyLocationを取得
     *
     * @return MyLocation
     */
    override suspend fun loadLastLocation(): String {
        var address = ""
        withContext(Dispatchers.IO) {
            address = dao.loadLastLocation()
        }
        return address
    }
}