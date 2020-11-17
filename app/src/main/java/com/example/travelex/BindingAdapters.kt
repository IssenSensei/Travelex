package com.example.travelex

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("photo")
fun setPhoto(imageView: ImageView, url: String?) {
    if (url != null) {
        imageView.visibility = View.VISIBLE
        Glide.with(imageView.context).load(url).placeholder(R.drawable.material_bg_2)
            .into(imageView)
    }
}
