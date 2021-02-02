package com.example.travelex.misc

import android.app.Activity
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.travelex.R
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.ItemPlaceBinding


class PlaceWithPhotosRecyclerViewAdapter(
    private val placesListListener: PlacesListListener,
    activity: FragmentActivity
) :
    ListAdapter<PlaceWithPhotos, PlaceWithPhotosRecyclerViewAdapter.ViewHolder>(
        PlaceWithPhotosListDiffCallback()
    ) {

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

        fun bind(
            item: PlaceWithPhotos,
            placesListListener: PlacesListListener,
            activity: Activity
        ) {
            binding.placeWithPhoto = item

            val sliderAdapter = AdapterImageSlider(activity, item.photos)
            binding.placeItemPager.adapter = sliderAdapter
            addSliderDots(binding.placeItemDots, sliderAdapter.count, 0)
            binding.placeItemPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                }

                override fun onPageSelected(position: Int) =
                    addSliderDots(binding.placeItemDots, sliderAdapter.count, position)

                override fun onPageScrollStateChanged(state: Int) {}
            })

            binding.clickListener = placesListListener
            binding.executePendingBindings()
        }

        private fun addSliderDots(placeItemDots: LinearLayout, count: Int, position: Int) {
            placeItemDots.removeAllViews()
            val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams(15, 15))
            params.setMargins(10, 10, 10, 10)
            for (i in 0 until count) {
                val image: ImageView = ImageView(placeItemDots.context).apply {
                    layoutParams = params
                    setImageResource(R.drawable.shape_dot)
                    if (i != position) {
                        setColorFilter(
                            ContextCompat.getColor(
                                placeItemDots.context,
                                R.color.overlay_dark_10
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    } else {
                        setColorFilter(
                            ContextCompat.getColor(
                                placeItemDots.context,
                                R.color.colorPrimary
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    }
                }
                placeItemDots.addView(image)
            }

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
    fun onPlaceSelected(placeWithPhotos: PlaceWithPhotos)
}

