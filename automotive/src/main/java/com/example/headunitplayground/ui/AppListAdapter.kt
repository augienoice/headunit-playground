package com.example.headunitplayground.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.headunitplayground.databinding.ItemAppBinding

class AppListAdapter(private val list: MutableList<Pair<String, String>>) : RecyclerView.Adapter<AppListAdapter.AppListViewHolder>() {

    inner class AppListViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Pair<String, String>) {
            binding.tvAppName.text = item.first
            binding.tvPackageName.text = item.second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppListViewHolder(binding)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
        holder.bind(list[position])
    }
}