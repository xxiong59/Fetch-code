package com.example.fetch_demo.ui.component

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fetch_demo.R
import com.example.fetch_demo.data.FetchDemoDataItem

/**
 * RecyclerView adapter that displays grouped data items with expandable/collapsible headers.
 * This adapter handles two types of views:
 * 1. Header views that show the listId and can expand/collapse the group
 * 2. Item views that display individual data items
 */
class DataAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Map of listId to the list of items in that group
     */
    private var dataItems: Map<Int, List<FetchDemoDataItem>> = emptyMap()

    /**
     * Sorted list of all listIds
     */
    private var listIds: List<Int> = emptyList()

    /**
     * Tracks the expanded/collapsed state of each list group
     */
    private val expandedLists = mutableMapOf<Int, Boolean>()

    /**
     * View type constants
     */
    private val TYPE_HEADER = 0
    private val TYPE_ITEM = 1

    /**
     * Creates appropriate ViewHolder based on the view type
     *
     * @param parent The parent ViewGroup
     * @param viewType The type of view (header or item)
     * @return The appropriate ViewHolder instance
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view, this::toggleExpand)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_data, parent, false)
                DataViewHolder(view)
            }
        }
    }

    /**
     * Binds data to the ViewHolder based on its type and position
     *
     * @param holder The ViewHolder to bind data to
     * @param position The adapter position
     */
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val (listId, relativePosition) = getPositionInfo(position)

        when (holder) {
            is HeaderViewHolder -> {
                val isExpanded = expandedLists[listId] ?: true
                holder.bind(listId, isExpanded)
            }
            is DataViewHolder -> {
                val item = dataItems[listId]?.get(relativePosition)
                item?.let { holder.bind(it) }
            }
        }
    }

    /**
     * Determines the view type (header or item) for a given position
     *
     * @param position The adapter position
     * @return The view type (TYPE_HEADER or TYPE_ITEM)
     */
    override fun getItemViewType(position: Int): Int {
        var currentPosition = 0
        for (listId in listIds) {
            if (position == currentPosition) {
                return TYPE_HEADER
            }
            currentPosition++

            if (expandedLists[listId] == false) {
                continue
            }

            val itemsInGroup = dataItems[listId]?.size ?: 0
            if (position < currentPosition + itemsInGroup) {
                return TYPE_ITEM
            }
            currentPosition += itemsInGroup
        }
        return TYPE_ITEM
    }

    /**
     * Calculates the total number of items, including headers and data items
     * Takes into account the expanded/collapsed state of each group
     *
     * @return The total item count
     */
    override fun getItemCount(): Int {
        var totalCount = 0
        for (listId in listIds) {
            totalCount += 1  // Add 1 for the header
            if (expandedLists[listId] == true) {
                totalCount += (dataItems[listId]?.size ?: 0)  // Add items if expanded
            }
        }
        return totalCount
    }

    /**
     * Maps an adapter position to a (listId, relativePosition) pair
     *
     * @param adapterPosition The position in the adapter
     * @return A Pair where:
     *         - first: the listId of the group
     *         - second: the position within the group, or -1 for a header
     */
    private fun getPositionInfo(adapterPosition: Int): Pair<Int, Int> {
        var currentPosition = 0
        for (listId in listIds) {
            if (adapterPosition == currentPosition) {
                return Pair(listId, -1)  // -1 indicates this is a header
            }
            currentPosition++

            if (expandedLists[listId] == false) {
                continue  // Skip collapsed groups
            }

            val itemsInGroup = dataItems[listId]?.size ?: 0
            if (adapterPosition < currentPosition + itemsInGroup) {
                val relativePos = adapterPosition - currentPosition
                return Pair(listId, relativePos)
            }
            currentPosition += itemsInGroup
        }
        return Pair(0, 0)  // Default fallback, should not happen
    }

    /**
     * Toggles the expanded state of a group
     *
     * @param listId The listId of the group to toggle
     */
    private fun toggleExpand(listId: Int) {
        expandedLists[listId] = !(expandedLists[listId] ?: true)
        notifyDataSetChanged()
    }

    /**
     * Updates the adapter with new data
     *
     * @param newData Map of listId to list of data items
     */
    fun setData(newData: Map<Int, List<FetchDemoDataItem>>) {
        Log.d("xxx", "data size: " + newData.size)
        dataItems = newData
        listIds = newData.keys.toList().sorted()

        // Initialize expansion state for new lists
        listIds.forEach { listId ->
            if (!expandedLists.containsKey(listId)) {
                expandedLists[listId] = true  // Default to expanded
            }
        }
        notifyDataSetChanged()
    }

    /**
     * ViewHolder for group header items
     *
     * @property itemView The view for this header
     * @property onExpandClick Callback function when the expand/collapse button is clicked
     */
    class HeaderViewHolder(
        itemView: View,
        private val onExpandClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvListId: TextView = itemView.findViewById(R.id.tvListHeader)
        private val expandedBtn: ImageButton = itemView.findViewById(R.id.btnExpand)

        /**
         * Binds data to the header view
         *
         * @param listId The listId to display
         * @param isExpanded Whether this group is currently expanded
         */
        fun bind(listId: Int, isExpanded: Boolean) {
            tvListId.text = "ListID: $listId"
            expandedBtn.setImageResource(
                if (isExpanded) android.R.drawable.arrow_down_float
                else android.R.drawable.arrow_up_float
            )
            expandedBtn.setOnClickListener {
                onExpandClick(listId)
            }
        }
    }

    /**
     * ViewHolder for data item views
     *
     * @property itemView The view for this data item
     */
    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvId)
        private val tvListId: TextView = itemView.findViewById(R.id.tvListId)
        private val tvName: TextView = itemView.findViewById(R.id.tvName)

        /**
         * Binds data to the item view
         *
         * @param item The data item to display
         */
        fun bind(item: FetchDemoDataItem) {
            tvId.text = "ID: ${item.id}"
            tvListId.text = "ListID: ${item.listId}"
            tvName.text = "Name: ${item.name ?: "N/A"}"
        }
    }
}