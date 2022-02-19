package com.example.footprints.ui.permission_checker

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.footprints.Constants.AppConstants
import com.example.footprints.R
import com.example.footprints.databinding.FragmentRequestPermissionsBinding
import com.example.footprints.model.util.MyPermissionsUtil
import com.example.footprints.ui.SharedViewModel


class RequestPermissionsFragment : Fragment() {
    private var _binding: FragmentRequestPermissionsBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestPermissionsBinding.inflate(inflater, container, false)
        binding.apply {
            handler = this@RequestPermissionsFragment
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()

        gotoNextScreen()
    }

    /**
     * アプリバーの設定
     */
    private fun setupToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.requestPermissionFragment, R.id.mainFragment)
        )

        val toolbar = binding.toolbarRequestPermissions
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    /**
     * 次の画面へ遷移
     * 権限許可されている場合 -> メイン画面へ
     * 許可されていない場合 -> 権限の設定画面へ
     * 一部許可されている場合 -> アプリの詳細設定画面
     */
    private fun gotoNextScreen() {
        val checkResult = MyPermissionsUtil.checkRequiredPermissions(
            requireContext(),
            AppConstants.REQUIRED_PERMISSIONS
        )

        if(checkResult == MyPermissionsUtil.CheckResult.UNAUTHORIZED) {
            requestRequiredPermissions()
            return
        }

        if(checkResult == MyPermissionsUtil.CheckResult.PARTIALLY_PERMITTED) {
            showNotificationDialog(
                getString(R.string.requested_always_allow)
            )
            return
        }

        sharedViewModel.setHasPermissions(true)
        gotoMainScreen()
    }

    /**
     * メイン画面へ遷移
     */
    private fun gotoMainScreen() {
        findNavController().navigate(R.id.mainFragment)
    }

    /**
     * アプリ詳細設定画面へ遷移
     */
    private fun gotoApplicationDetailScreen() {
        val packageName = requireContext().packageName
        val intent = Intent(
            android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:$packageName")
        )

        startActivity(intent)
    }

    /**
     * 必要な権限（ロケーション）要求結果。
     */
    private val locationPermissionsRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->

        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                showNotificationDialog(
                    getString(R.string.requested_always_allow)
                )
            }
            else -> {
                showNotificationDialog(
                    getString(R.string.requested_always_allow)
                            + getString(R.string.request_accurate_location)
                )
            }
        }
    }

    /**
     * 必要な権限を要求する
     */
    private fun requestRequiredPermissions() {
        locationPermissionsRequestLauncher.launch(AppConstants.REQUESTBLE_PERMISSIONS)
    }

    /**
     * ボタンクリック時の動作
     */
    fun onClick() {
        gotoNextScreen()
    }

    /**
     * ロケーション設定依頼用のダイアログを表示
     *
     * @param notice 通知文
     */
    private fun showNotificationDialog(notice: String) {
        RequestPermissionsDialogFragment(
            notice,
            notificationDialogListener
        ).show(parentFragmentManager, "Please set permissions")
    }

    /**
     * ダイアログ押下時の動作を実装
     */
    private val notificationDialogListener =
        object : RequestPermissionsDialogFragment.NotificationDialogListener {
            override fun onDialogPositiveClick(dialog: DialogFragment) {
                gotoApplicationDetailScreen()
            }

            override fun onDialogNegativeClick(dialog: DialogFragment) {
            }
    }
}