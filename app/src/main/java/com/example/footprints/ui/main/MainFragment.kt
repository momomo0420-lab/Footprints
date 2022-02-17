package com.example.footprints.ui.main

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.footprints.R
import com.example.footprints.databinding.FragmentMainBinding
import com.example.footprints.model.entity.MyLocation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    // バインディングデータ
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    // ビューモデル
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

        setupAppBar(view, R.id.tb_main)

        val adapter =  MyLocationAdapter(getOnItemSelectedListener())

        binding.recycler.adapter = adapter
        viewModel.myLocationList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setupAppBar(view: View, toolbarId: Int) {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        val toolbar = view.findViewById<Toolbar>(toolbarId)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_star_and_stop, menu)
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