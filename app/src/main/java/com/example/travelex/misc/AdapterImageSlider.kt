package com.example.travelex.misc

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.example.travelex.R
import com.example.travelex.database.PhotoModel
import kotlinx.android.synthetic.main.item_image.view.*

class AdapterImageSlider(private val activity: Activity, items: List<PhotoModel>) : PagerAdapter() {

    private var photos: List<PhotoModel> = items
    private var onImageSliderImageClickedListener: OnImageSliderImageClickedListener? = null

    override fun getCount(): Int {
        return photos.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoModel: PhotoModel = photos[position]
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.item_image, container, false)
        view.item_image
        Glide.with(activity).load(photoModel.photoUrl).into(view.item_image)
        view.item_image_container.setOnClickListener { v ->
            if (onImageSliderImageClickedListener != null) {
                onImageSliderImageClickedListener!!.onItemClick(v, photoModel)
            }
        }
        (container as ViewPager).addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}