package com.example.houseproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.houseproject.databinding.ItemLayoutBinding

class Adapter (private val strings: List<String>) : RecyclerView.Adapter<Adapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.itemTextview.text = strings[position]
    }

    override fun getItemCount(): Int {
        return strings.size
    }

    class MyViewHolder( val binding: ItemLayoutBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}