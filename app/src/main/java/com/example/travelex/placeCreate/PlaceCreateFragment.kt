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

        ViewAnimation.initShowOut(lyt_mic)
        ViewAnimation.initShowOut(lyt_call)
        back_drop.visibility = View.GONE

        fab_add.setOnClickListener { v -> toggleFabMode(v) }

        back_drop.setOnClickListener { toggleFabMode(fab_add) }

        fab_mic.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Voice clicked",
                Toast.LENGTH_SHORT
            ).show()
        }

        fab_call.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Call clicked",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun toggleFabMode(v: View) {
        rotate = ViewAnimation.rotateFab(v, !rotate)
        if (rotate) {
            ViewAnimation.showIn(lyt_mic)
            ViewAnimation.showIn(lyt_call)
            back_drop.visibility = View.VISIBLE
        } else {
            ViewAnimation.showOut(lyt_mic)
            ViewAnimation.showOut(lyt_call)
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