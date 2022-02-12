package com.example.footprints.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footprints.databinding.MyLocationListItemBinding
import com.example.footprints.model.entity.MyLocation
import com.example.footprints.model.entity.convertDateAndTimeToMyFormat

/**
 * MyLocationアダプタ
 *
 * @property listener リスナー
 * @constructor 選択されたアイテムに対する動作
 */
class MyLocationAdapter(
    private val listener: (MyLocation) -> Unit
) : ListAdapter<MyLocation, MyLocationAdapter.ViewHolder>(callback) {
    // ビューホルダー
    private lateinit var viewHolder: ViewHolder

    /**
     * ビューホルダー
     *
     * @property binding バインディングデータ
     */
    class ViewHolder(
        private val binding: MyLocationListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        /**
         * リストアイテムの登録
         *
         * @param myLocation 画面に表示するMyLocationのアイテム
         */
        fun bindTo(myLocation: MyLocation) {
            binding.apply {
                address.text = myLocation.address
                dateAndTime.text = myLocation.convertDateAndTimeToMyFormat()
            }
        }
    }

    /**
     * ビューホルダーの作成
     *
     * @param parent ビューグループ
     * @param viewType //TODO わからん
     * @return ビューホルダー
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MyLocationListItemBinding.inflate(
            inflater,
            parent,
            false
        )

        binding.root.setOnClickListener(onClickListener)
        viewHolder = ViewHolder(binding)

        return viewHolder
    }

    //TODO 最終的にはレイアウト側にクリック時の動作を配置したい
//    fun onClick() {
//        listener("TODO1")
//    }

    /**
     * クリック時の動作。クリックされたアイテムを取得しリスナーに渡す
     */
    private val onClickListener = View.OnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val myLocation = getItem(position)
            listener(myLocation)
    }

    /**
     * ビューホルダーへのMuLocation登録
     *
     * @param holder ビューホルダー
     * @param position position番目のアイテム
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myLocation = getItem(position)
        holder.bindTo(myLocation)
    }

    companion object {
        /**
         * 差分検知のためのコールバック。
         * //TODO 正直よくわからん
         */
        private val callback = object : DiffUtil.ItemCallback<MyLocation>() {
            override fun areItemsTheSame(oldItem: MyLocation, newItem: MyLocation): Boolean {
                return oldItem._id == newItem._id
            }

            override fun areContentsTheSame(oldItem: MyLocation, newItem: MyLocation): Boolean {
                return oldItem.address == newItem.address
            }
        }
    }

}