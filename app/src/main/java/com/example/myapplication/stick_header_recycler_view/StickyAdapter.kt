package com.example.myapplication.stick_header_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.databinding.RecyclerViewItemBinding

class StickyAdapter : RecyclerView.Adapter<StickyAdapter.StickyVH>() /*, HeaderItemDecoration_Java.StickyHeaderInterface*/{

    val items : List<String> = (1..20).map {
        "Title $it"
    }

    class StickyVH(val binding: RecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(title: String) {
            binding.textView.text = title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StickyVH {
        val binding =
            RecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StickyVH(binding)
    }

    override fun onBindViewHolder(holder: StickyVH, position: Int) {
        holder.onBind(items.get(position))
    }

    override fun getItemCount(): Int {
        return 5
    }

}