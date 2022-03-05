package com.example.footprints.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import com.example.footprints.R
import com.example.footprints.databinding.FragmentMainBinding
import com.example.footprints.model.entity.MyLocation
import com.example.footprints.ui.SharedViewModel
import com.example.footprints.worker.LocationUpdateWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

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

        sharedViewModel.hasPermissions.observe(
            viewLifecycleOwner,
            getPermissionsStateObserver()
        )
    }

    private fun getPermissionsStateObserver(): Observer<Boolean> {
        return Observer {
            if(it) {
                // アプリバーの設定
                setupAppBar()
                // ロケーションリストの設定
                setupMyLocationList()
            } else {
                // 必要な権限がない場合、権限要求画面に移動する
                val action = MainFragmentDirections.actionMainFragmentToRequestPermissionFragment()
                findNavController().navigate(action)
            }
        }
    }

    /**
     * アプリバーの設定
     */
    private fun setupAppBar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_main_activity)
        toolbar.apply {
            menu.clear()
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener(OnMainMenuItemClickListener(viewModel))
        }

        viewModel.isRunnable.observe(viewLifecycleOwner) {
            val itemStart = toolbar.menu.findItem(R.id.action_start)
            itemStart.isVisible = it
            val itemStop = toolbar.menu.findItem(R.id.action_stop)
            itemStop.isVisible = !it
        }
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
     * ワークリクエスト作成
     */
    private fun createWorkerRequest(): PeriodicWorkRequest {
        return PeriodicWorkRequestBuilder<LocationUpdateWorker>(
            15, TimeUnit.MINUTES
        ).build()
    }
}