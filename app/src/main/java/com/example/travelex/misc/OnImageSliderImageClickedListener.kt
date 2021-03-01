package com.example.travelex.misc

import android.view.View
import com.example.travelex.database.PhotoModel

interface OnImageSliderImageClickedListener {
    fun onItemClick(view: View?, obj: PhotoModel?)
}