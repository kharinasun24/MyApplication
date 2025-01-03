package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemGroceryBinding
import com.example.myapplication.model.GroceryItem

class GroceryAdapter(private val onDelete: (GroceryItem) -> Unit) :
    ListAdapter<GroceryItem, GroceryAdapter.GroceryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryViewHolder {
        val binding = ItemGroceryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroceryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroceryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class GroceryViewHolder(private val binding: ItemGroceryBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: GroceryItem) {
            binding.textViewItemName.text = item.name
            binding.textViewQuantity.text = item.quantity.toString()

            binding.buttonDelete.setOnClickListener {
                onDelete(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GroceryItem>() {
        override fun areItemsTheSame(oldItem: GroceryItem, newItem: GroceryItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GroceryItem, newItem: GroceryItem): Boolean {
            return oldItem == newItem
        }
    }
}