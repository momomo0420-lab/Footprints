package com.example.footprints.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.footprints.R
import com.example.footprints.databinding.FragmentMainBinding
import com.example.footprints.model.entity.MyLocation
import com.example.footprints.ui.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    // バインディングデータ
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    // ビューモデル
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!sharedViewModel.locationClientIsEnabled.value!!) {
            val controller = findNavController()
            controller.navigate(R.id.checkPermissionFragment)
            return
        }

        setupMyLocationList()
    }

    private fun setupMyLocationList() {
        val adapter =  MyLocationAdapter(getOnItemSelectedListener())

        binding.recycler.adapter = adapter
        viewModel.myLocationList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    /**
     * リスト選択選択時の動作用リスナーを取得する
     *
     * @return リスト選択選択時の動作用リスナー
     */
    private fun getOnItemSelectedListener(): (MyLocation) -> Unit {
        return object : (MyLocation) -> Unit {
            override fun invoke(p1: MyLocation) {
                val action = MainFragmentDirections.actionMainFragmentToDetailFragment(p1)
                findNavController().navigate(action)
            }
        }
    }

    /**
     * スタートボタンが押された際の動作
     */
    fun onClickStartButton() {
        viewModel.startLocationUpdate()
    }

    /**
     * ストップボタンが押された際の動作
     */
    fun onClickStopButton() {
        viewModel.stopLocationUpdate()
    }

}