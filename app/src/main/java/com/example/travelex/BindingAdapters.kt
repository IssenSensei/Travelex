package com.example.travelex

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.travelex.database.PhotoModel

@BindingAdapter("photo")
fun setPhoto(imageView: ImageView, photoList: MutableList<PhotoModel>) {
    if (photoList.size != 0) {
        imageView.visibility = View.VISIBLE
        photoList.forEach {
            Glide.with(imageView.context).load(it.photoUrl).placeholder(R.drawable.material_bg_2)
                .into(imageView)
        }
    }
}
