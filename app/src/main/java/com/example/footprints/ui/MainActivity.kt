package com.example.footprints.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.footprints.Constants.AppConstants
import com.example.footprints.R
import com.example.footprints.model.util.MyPermissionsUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 必要な権限が存在するか確認
        checkRequiredPermissions()
    }

    /**
     * 必要な権限が存在するか確認する
     */
    private fun checkRequiredPermissions() {
        val checkResult = MyPermissionsUtil.checkRequiredPermissions(
            this, AppConstants.REQUIRED_PERMISSIONS
        )
        if(checkResult == MyPermissionsUtil.CheckResult.ALL_GRANTED) {
            viewModel.setHasPermissions(true)
        }
    }
}
