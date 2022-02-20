package com.example.footprints.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
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

        // アプリバーの設定
        setupAppBar()

        // 必要な権限がない場合、権限要求画面に移動する
        if(!sharedViewModel.hasPermissions.value!!) {
            val action = MainFragmentDirections.actionMainFragmentToRequestPermissionFragment()
            findNavController().navigate(action)
            return
        }

        // ロケーションリストの設定
        setupMyLocationList()
    }

    /**
     * アプリバーの設定
     */
    private fun setupAppBar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_main_activity)
        toolbar.apply {
            menu.clear()
            inflateMenu(R.menu.menu_main)
            setOnMenuItemClickListener(getOnMenuItemClickListener())
        }

        viewModel.isRunnable.observe(viewLifecycleOwner) {
            val itemStart = toolbar.menu.findItem(R.id.action_start)
            itemStart.isVisible = it
            val itemStop = toolbar.menu.findItem(R.id.action_stop)
            itemStop.isVisible = !it
        }
    }

    private fun getOnMenuItemClickListener(): Toolbar.OnMenuItemClickListener {
        return Toolbar.OnMenuItemClickListener {
            when(it.itemId) {
                R.id.action_start -> {
                    onClickStartButton()
                    true
                }
                R.id.action_stop -> {
                    onClickStopButton()
                    true
                }
                else -> {
                    false
                }
            }
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
     * スタートボタンが押された際の動作
     */
    private fun onClickStartButton() {
        viewModel.startLocationUpdate()
    }

    /**
     * ストップボタンが押された際の動作
     */
    private fun onClickStopButton() {
        viewModel.stopLocationUpdate()
    }
}