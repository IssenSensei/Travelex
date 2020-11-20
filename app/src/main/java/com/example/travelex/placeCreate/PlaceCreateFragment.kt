package com.example.travelex.placeCreate

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.example.travelex.ViewAnimation
import com.example.travelex.database.Place
import kotlinx.android.synthetic.main.place_create_fragment.*

class PlaceCreateFragment : Fragment() {

    private lateinit var placeCreateViewModel: PlaceCreateViewModel
    private var rotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.place_create_fragment, container, false)
        placeCreateViewModel = ViewModelProvider(this).get(PlaceCreateViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ViewAnimation.initShowOut(place_create_gallery_container)
        ViewAnimation.initShowOut(place_create_camera_container)
        back_drop.visibility = View.GONE

        place_create_add_photo.setOnClickListener { v -> toggleFabMode(v) }

        back_drop.setOnClickListener { toggleFabMode(place_create_add_photo) }

        place_create_fab_gallery.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Gallery picked",
                Toast.LENGTH_SHORT
            ).show()
        }

        place_create_fab_camera.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Camera picked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun toggleFabMode(v: View) {
        rotate = ViewAnimation.rotateFab(v, !rotate)
        if (rotate) {
            ViewAnimation.showIn(place_create_gallery_container)
            ViewAnimation.showIn(place_create_camera_container)
            back_drop.visibility = View.VISIBLE
        } else {
            ViewAnimation.showOut(place_create_gallery_container)
            ViewAnimation.showOut(place_create_camera_container)
            back_drop.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                savePlace()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePlace() {
        placeCreateViewModel.insert(
            Place(
                0,
                place_create_name.text.toString(),
                place_create_description.text.toString(),
                place_create_location.text.toString(),
                R.drawable.material_bg_2.toString(),
                place_create_rating.rating,
                place_create_comment.text.toString(),
                "addInfoTest1"
            )
        )
        findNavController().popBackStack()
    }

}