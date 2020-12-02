package com.example.travelex.misc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelex.database.PhotoModel
import com.example.travelex.databinding.ItemGridImageBinding

class PhotoGridAdapter(private val photoGridListener: PhotoGridListener, private val isRemovable: Boolean) :
    ListAdapter<PhotoModel, PhotoGridAdapter.ViewHolder>(PhotoGridDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, photoGridListener, isRemovable)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ItemGridImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PhotoModel, photoGridListener: PhotoGridListener, isRemovable: Boolean) {
            binding.photo = item
            binding.clickListener = photoGridListener
            binding.itemImageRemove.visibility = if(isRemovable) View.VISIBLE else View.GONE
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGridImageBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class PhotoGridDiffCallback : DiffUtil.ItemCallback<PhotoModel>() {

    override fun areItemsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem.photoUrl == newItem.photoUrl
    }

    override fun areContentsTheSame(oldItem: PhotoModel, newItem: PhotoModel): Boolean {
        return oldItem == newItem
    }
}

interface PhotoGridListener {
    fun onDeleteClicked(photoModel: PhotoModel)
    fun onPhotoClicked(photoModel: PhotoModel)
}