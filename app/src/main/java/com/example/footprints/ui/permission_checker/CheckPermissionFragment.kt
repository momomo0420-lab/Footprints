package com.example.footprints.ui.permission_checker

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.footprints.databinding.FragmentCheckPermissionBinding

class CheckPermissionFragment : Fragment() {
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

        val checkResult = checkRequiredPermissions()
        if(!checkResult) {
            requestRequiredPermissions()
        }
    }

    /**
     * 必要な権限（ロケーション）要求結果。
     */
    private val locationPermissionsRequestLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                }
                else -> {
                }
            }
    }

    /**
     * 必要な権限を保持しているか確認する
     * //TODO フラグメントの見栄えがよくないのでどこかに移したい
     *
     * @return 確認結果（ture: 保持してる、 false: 保持していない）
     */
    private fun checkRequiredPermissions() : Boolean {
        var result = true

        val checkResult = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION)

        if(checkResult != PackageManager.PERMISSION_GRANTED) {
            result = false
        }

        return result
    }

    /**
     * 必要な権限を要求する
     */
    private fun requestRequiredPermissions() {
        locationPermissionsRequestLauncher
            .launch(arrayOf(
//                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            )
    }

    /**
     * ボタンクリック時の動作
     */
    fun onClick() {
        if(!checkRequiredPermissions()) {
            requestRequiredPermissions()
            return
        }

        val action = CheckPermissionFragmentDirections.actionCheckPermissionFragmentToMainFragment()
        findNavController().navigate(action)
    }
}