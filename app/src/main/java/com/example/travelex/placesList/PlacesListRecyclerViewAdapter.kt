package com.example.travelex.placesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.travelex.database.Place
import com.example.travelex.databinding.ItemPlaceBinding

class PlacesRecyclerViewAdapter(private val placesListListener: PlacesListListener) :
    ListAdapter<Place, PlacesRecyclerViewAdapter.ViewHolder>(PlaceListDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaceBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, placesListListener)
    }

    class ViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Place, placesListListener: PlacesListListener) {
            binding.place = item
            binding.clickListener = placesListListener
            binding.executePendingBindings()
        }
    }


}

class PlaceListDiffCallback : DiffUtil.ItemCallback<Place>() {
    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean {
        return oldItem == newItem
    }
}

interface PlacesListListener {
    fun onPlaceSelected(task: Place)
}
