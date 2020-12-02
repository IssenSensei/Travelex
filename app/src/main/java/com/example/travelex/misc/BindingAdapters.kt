package com.example.travelex.misc

import android.location.Geocoder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.travelex.R
import com.example.travelex.database.PhotoModel
import com.google.android.gms.maps.model.LatLng
import java.util.*

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

@BindingAdapter("photo")
fun setPhoto(imageView: ImageView, photo: PhotoModel) {
    Glide.with(imageView.context).load(photo.photoUrl).placeholder(R.drawable.material_bg_2)
        .into(imageView)
}

@BindingAdapter("location")
fun setAddress(view: TextView, string: String) {
    val location = string.split(",".toRegex()).toTypedArray()
    val latLng = LatLng(location[0].toDouble(), location[1].toDouble())

    val addressList = Geocoder(view.context, Locale.getDefault()).getFromLocation(
        latLng.latitude,
        latLng.longitude,
        1
    )
    val locality = if (addressList[0].locality != null) addressList[0].locality else "Unknown"
    val countryName =
        if (addressList[0].countryName != null) addressList[0].countryName else "Unknown"
    view.text = "$locality, $countryName"

}



