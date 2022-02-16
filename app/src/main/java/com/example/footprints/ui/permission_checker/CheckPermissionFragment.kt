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
import androidx.navigation.fragment.findNavController
import com.example.footprints.R
import com.example.footprints.databinding.FragmentCheckPermissionBinding
import com.example.footprints.model.util.MyPermissionsUtil


class CheckPermissionFragment : Fragment() {
    companion object {
        private val PERMISSIONS_TO_REQUEST = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        private val REQUIRED_PERMISSIONS = PERMISSIONS_TO_REQUEST + Manifest.permission.ACCESS_BACKGROUND_LOCATION
    }

    private var _binding: FragmentCheckPermissionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCheckPermissionBinding.inflate(inflater, container, false)
        binding.apply {
            handler = this@CheckPermissionFragment
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

        gotoNextScreen()
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
            REQUIRED_PERMISSIONS
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

        gotoMainScreen()
    }

    /**
     * メイン画面へ遷移
     */
    private fun gotoMainScreen() {
        val action = CheckPermissionFragmentDirections.actionCheckPermissionFragmentToMainFragment()
        findNavController().navigate(action)
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
        locationPermissionsRequestLauncher.launch(PERMISSIONS_TO_REQUEST)
    }

    /**
     * ボタンクリック時の動作
     */
    fun onClick() {
        gotoNextScreen()
    }

    /**
     * ロケーション設定依頼用のダイアログを表示
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