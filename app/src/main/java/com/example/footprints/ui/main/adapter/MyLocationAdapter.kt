package com.example.footprints.ui.main.adapter

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.footprints.databinding.MyLocationListItemBinding
import com.example.footprints.model.entity.MyLocation

class MyLocationAdapter(
    private val listener: (MyLocation) -> Unit
) : ListAdapter<MyLocation, MyLocationAdapter.ViewHolder>(callback) {
    private lateinit var viewHolder: ViewHolder

    class ViewHolder(
        private val binding: MyLocationListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bindTo(myLocation: MyLocation) {
            binding.apply {
                address.text = myLocation.address

                dateAndTime.text = DateFormat.format(
                    "yyyy-MM-dd hh:mm:ss",
                    myLocation.dateAndTime
                )
            }
        }
    }

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

//    fun onClick() {
//        listener("TODO1")
//    }

    private val onClickListener = View.OnClickListener {
            val position = viewHolder.bindingAdapterPosition
            val myLocation = getItem(position)
            listener(myLocation)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myLocation = getItem(position)
        holder.bindTo(myLocation)
    }

    companion object {
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