package com.example.travelex

import android.view.View

object ViewAnimation {
    fun showIn(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(200)
            .translationY(0f)
            .alpha(1f)
            .start()
    }

    fun initShowOut(v: View) {
        v.visibility = View.GONE
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }

    fun showOut(v: View) {
        v.visibility = View.VISIBLE
        v.alpha = 1f
        v.translationY = 0f
        v.animate()
            .setDuration(200)
            .translationY(v.height.toFloat())
            .alpha(0f)
            .start()
    }

    fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(200)
            .rotation(if (rotate) 135f else 0f)
        return rotate
    }
}