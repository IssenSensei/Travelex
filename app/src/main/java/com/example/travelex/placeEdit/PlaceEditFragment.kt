package com.example.travelex.placeEdit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelex.R
import com.example.travelex.database.Place
import com.example.travelex.databinding.PlaceDetailFragmentBinding
import com.example.travelex.databinding.PlaceEditFragmentBinding
import com.example.travelex.placeDetail.PlaceDetailFragmentArgs
import com.example.travelex.placeDetail.PlaceDetailFragmentDirections
import kotlinx.android.synthetic.main.place_create_fragment.*
import kotlinx.android.synthetic.main.place_create_fragment.place_create_comment
import kotlinx.android.synthetic.main.place_create_fragment.place_create_description
import kotlinx.android.synthetic.main.place_create_fragment.place_create_location
import kotlinx.android.synthetic.main.place_create_fragment.place_create_name
import kotlinx.android.synthetic.main.place_edit_fragment.*

class PlaceEditFragment : Fragment() {

    private lateinit var placeEditViewModel: PlaceEditViewModel
    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        placeEditViewModel = ViewModelProvider(this).get(PlaceEditViewModel::class.java)
        val binding = PlaceEditFragmentBinding.inflate(inflater, container, false)

        val view = binding.root

        val safeArgs: PlaceEditFragmentArgs by navArgs()
        place = safeArgs.place

        binding.place = place

        binding.executePendingBindings()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                updatePlace()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updatePlace() {
        place.name = place_edit_name.text.toString()
        place.description = place_edit_description.text.toString()
        place.location = place_edit_location.text.toString()
        place.rating = place_edit_rating.rating
        place.comment = place_edit_comment.text.toString()
        placeEditViewModel.insert(place)
        findNavController().popBackStack()
    }

}