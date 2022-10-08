package com.example.myapplication.stick_header_recycler_view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.utils.Section
import com.shuhart.stickyheader.StickyAdapter


class StickyItemsAdapter : StickyAdapter<RecyclerView.ViewHolder, RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context)
        return if (viewType == Section.HEADER || viewType == Section.CUSTOM_HEADER) {
            HeaderViewholder(
                inflater.inflate(
                    R.layout.recycler_view_header_item,
                    parent,
                    false
                )
            )
        } else ItemViewHolder(
            inflater.inflate(
                R.layout.recycler_view_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {



        if (position == Section.HEADER) {
            (holder as HeaderViewholder).textView.text = "Header"
        } else if (position == Section.ITEM) {
            (holder as ItemViewHolder).textView.text = "Item"
        }
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun getHeaderPositionForItem(itemPosition: Int): Int {
        return 0
    }

    override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?, headerPosition: Int) {
        (holder as HeaderViewholder).textView.text = "Header $headerPosition"
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return createViewHolder(parent, Section.HEADER)
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==0) Section.HEADER else Section.ITEM
    }


    //////////////////////
    class HeaderViewholder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.text_view)
        }
    }

    class ItemViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.text_view)
        }
    }
}


