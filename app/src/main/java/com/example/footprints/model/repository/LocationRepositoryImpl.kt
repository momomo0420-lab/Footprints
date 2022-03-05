package com.example.footprints.model.repository

import android.content.Context
import android.location.Location
import com.example.footprints.model.api.MyLocationClient
import com.example.footprints.model.dao.MyLocationDao
import com.example.footprints.model.entity.MyLocation
import dagger.hilt.android.qualifiers.ApplicationContext
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
    @ApplicationContext private val context: Context,
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

    override fun getCurrentLocation(listener: (Location) -> Unit) {
        client.getCurrentLocation(listener)
    }

    /**
     * ロケーションの定期取得を終了する
     */
    override fun stopLocationUpdate() {
        client.stopLocationUpdate()
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

    override suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            dao.deleteAll()
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
    override suspend fun loadLastAddress(): String  = withContext(Dispatchers.IO) {
        dao.loadLastAddress()
    }
}