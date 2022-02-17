package com.example.footprints.model.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object MyPermissionsUtil {
    enum class CheckResult {
        ALL_GRANTED,
        PARTIALLY_PERMITTED,
        UNAUTHORIZED
    }

    /**
     * 必要な権限を保持しているか確認する
     *
     * @return 確認結果
     *          ALL_GRANTED - 権限をすべて保持している
     *          PARTIALLY_PERMITTED - 権限を一部保持している
     *          UNAUTHORIZED - 権限を保持していない
     */
    fun checkRequiredPermissions(
        context: Context,
        requiredPermissions: Array<String>
    ) : CheckResult {
        var result = CheckResult.ALL_GRANTED

        var errorCount = 0
        var errorPermission = ""

        for(requiredPermission in requiredPermissions) {
            val checkResult = ContextCompat.checkSelfPermission(
                context,
                requiredPermission)

            if(checkResult != PackageManager.PERMISSION_GRANTED) {
                result = CheckResult.UNAUTHORIZED
                errorPermission = requiredPermission
                errorCount++
                break
            }
        }

        if((errorCount == 1)
            && (errorPermission == Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
            result = CheckResult.PARTIALLY_PERMITTED
        }

        return result
    }
}