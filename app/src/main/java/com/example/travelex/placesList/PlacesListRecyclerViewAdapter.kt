package com.example.travelex.placesList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.ItemPlaceBinding


class PlaceWithPhotosRecyclerViewAdapter(private val placesListListener: PlacesListListener) :
    ListAdapter<PlaceWithPhotos, PlaceWithPhotosRecyclerViewAdapter.ViewHolder>(PlaceWithPhotosListDiffCallback()) {


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

        fun bind(item: PlaceWithPhotos, placesListListener: PlacesListListener) {
            binding.placeWithPhoto = item
            binding.clickListener = placesListListener
            binding.executePendingBindings()
        }
    }


}

class PlaceWithPhotosListDiffCallback : DiffUtil.ItemCallback<PlaceWithPhotos>() {
    override fun areItemsTheSame(oldItem: PlaceWithPhotos, newItem: PlaceWithPhotos): Boolean {
        return oldItem.place.id == newItem.place.id
    }

    override fun areContentsTheSame(oldItem: PlaceWithPhotos, newItem: PlaceWithPhotos): Boolean {
        return oldItem == newItem
    }
}

interface PlacesListListener {
    fun onPlaceSelected(place: PlaceWithPhotos)
}

