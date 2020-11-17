package com.example.travelex.placeDetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import com.example.travelex.R
import com.example.travelex.database.Place
import com.example.travelex.databinding.PlaceDetailFragmentBinding
import com.example.travelex.placesList.PlacesListFragmentDirections

class PlaceDetailFragment : Fragment() {
    private lateinit var place: Place

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = PlaceDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val safeArgs: PlaceDetailFragmentArgs by navArgs()
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
        when(item.itemId){
            R.id.action_save -> {
                val actionEdit = PlaceDetailFragmentDirections.actionNavPlaceDetailToNavPlaceEdit(place)
                findNavController().navigate(actionEdit)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}