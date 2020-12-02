package com.example.travelex.misc

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.*

fun getAddress(latLng: LatLng, context: Context): String {
    val addressList = Geocoder(context, Locale.getDefault()).getFromLocation(latLng.latitude, latLng.longitude, 1)
    val locality = if (addressList[0].locality != null) addressList[0].locality else "Unknown"
    val countryName = if (addressList[0].countryName != null) addressList[0].countryName else "Unknown"
    return "$locality, $countryName"
}

fun latLngToString(latLng: LatLng) : String{
    return latLng.latitude.toString() + ", " + latLng.longitude.toString()
}