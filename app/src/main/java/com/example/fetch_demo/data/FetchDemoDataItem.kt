package com.example.fetch_demo.data

/**
 * Data model representing an item fetched from the Fetch API.
 * This matches the JSON structure returned by the API.
 *
 * @property id The unique identifier for the item.
 * @property listId The group identifier that this item belongs to.
 * @property name The display name of the item, typically in the format "Item X".
 *              This can be null or empty in the API response and will be filtered out.
 */
data class FetchDemoDataItem (
    val id: Int,
    val listId: Int,
    val name: String?
)