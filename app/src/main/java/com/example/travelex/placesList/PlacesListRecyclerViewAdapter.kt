package com.example.travelex.placesList

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.ItemPlaceBinding
import com.example.travelex.helpers.AdapterImageSlider


class PlaceWithPhotosRecyclerViewAdapter(
    private val placesListListener: PlacesListListener,
    activity: FragmentActivity
) :
    ListAdapter<PlaceWithPhotos, PlaceWithPhotosRecyclerViewAdapter.ViewHolder>(PlaceWithPhotosListDiffCallback()) {

    private var activity: Activity = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPlaceBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, placesListListener, activity)
    }

    class ViewHolder(private val binding: ItemPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PlaceWithPhotos, placesListListener: PlacesListListener, activity: Activity) {
            binding.placeWithPhoto = item

            val sliderAdapter = AdapterImageSlider(activity, item.photos)
            binding.placeItemPager.adapter = sliderAdapter
            sliderAdapter.startAutoSlider(item.photos.size, binding.placeItemPager)

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

