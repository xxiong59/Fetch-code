package com.example.fetch_demo.ui.component

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fetch_demo.R
import com.example.fetch_demo.data.FetchDemoDataItem

class DataAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var dataItems: Map<Int, List<FetchDemoDataItem>> = emptyMap()
    private var listIds: List<Int> = emptyList()

    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_data, parent, false)
                DataViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (listId, relativePosition) = getPositionInfo(position)

        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(listId)
            }
            is DataViewHolder -> {
                val item = dataItems[listId]?.get(relativePosition)
                item?.let { holder.bind(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        var currentPosition = 0
        for (listId in listIds) {
            if (position == currentPosition) {
                return TYPE_HEADER
            }
            currentPosition++

            val itemsInGroup = dataItems[listId]?.size ?: 0
            if (position < currentPosition + itemsInGroup) {
                return TYPE_ITEM
            }
            currentPosition += itemsInGroup
        }
        return TYPE_ITEM
    }

    override fun getItemCount(): Int {
        var totalCount = 0
        for (listId in listIds) {
            totalCount += 1
            totalCount += (dataItems[listId]?.size ?: 0)
        }
        return totalCount
    }

    private fun getPositionInfo(adapterPosition: Int): Pair<Int, Int> {
        var currentPosition = 0
        for (listId in listIds) {
            if (adapterPosition == currentPosition) {
                return Pair(listId, -1)
            }
            currentPosition++

            val itemsInGroup = dataItems[listId]?.size ?: 0
            if (adapterPosition < currentPosition + itemsInGroup) {
                val relativePos = adapterPosition - currentPosition
                return Pair(listId, relativePos)
            }
            currentPosition += itemsInGroup
        }
        return Pair(0, 0)
    }

    fun setData(newData: Map<Int, List<FetchDemoDataItem>>) {
        Log.d("xxx", "data size: " + newData.size)
        dataItems = newData
        listIds = newData.keys.toList().sorted()
        notifyDataSetChanged()
    }

    class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvListId: TextView = itemView.findViewById(R.id.tvListHeader)

        fun bind(listId: Int) {
            tvListId.text = "ListID: $listId"
        }
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvId)
        private val tvListId: TextView = itemView.findViewById(R.id.tvListId)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)

        fun bind(item: FetchDemoDataItem) {
            tvId.text = "ID: ${item.id}"
            tvListId.text = "ListID: ${item.listId}"
            tvName.text = "Name: ${item.name ?: "N/A"}"
        }
    }
}