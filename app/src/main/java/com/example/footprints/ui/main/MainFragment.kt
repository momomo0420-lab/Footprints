package com.example.footprints.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.footprints.databinding.FragmentMainBinding
import com.example.footprints.model.entity.MyLocation
import com.example.footprints.ui.main.adapter.MyLocationAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.apply {
            handler = this@MainFragment
            viewModel = this@MainFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter =  MyLocationAdapter(onItemSelectedLister)

        binding.recycler.adapter = adapter

        val checkResult = checkRequiredPermissions()
        if(!checkResult) {
            requestRequiredPermissions()
        }

        viewModel.myLocationList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private val onItemSelectedLister = object : (MyLocation) -> Unit {
        override fun invoke(myLocation: MyLocation) {
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment(myLocation)
            findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    fun onClickStartButton() {
        if(!checkRequiredPermissions()) {
            requestRequiredPermissions()
            return
        }
        viewModel.startLocationUpdate()
    }

    fun onClickStopButton() {
        viewModel.stopLocationUpdate()
    }

    private val locationPermissionRequest = registerForActivityResult(
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

    private fun requestRequiredPermissions() {
        locationPermissionRequest.launch(arrayOf(
//            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }
}