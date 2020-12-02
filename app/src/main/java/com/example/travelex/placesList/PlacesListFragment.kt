package com.example.travelex.placesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.example.travelex.database.PlaceWithPhotos
import kotlinx.android.synthetic.main.places_list_fragment.view.*

class PlacesListFragment : Fragment(), PlacesListListener {

    private lateinit var placesListViewModel: PlacesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        placesListViewModel = ViewModelProvider(this).get(PlacesListViewModel::class.java)

        val root = inflater.inflate(R.layout.places_list_fragment, container, false)
        val adapter = PlaceWithPhotosRecyclerViewAdapter(this, requireActivity())

        placesListViewModel.allTasks.observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it)
            }
        }

        root.places_recycler_list.adapter = adapter
        root.places_recycler_list_fab.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                R.id.action_nav_places_list_to_nav_place_create,
                null
            )
        )

        return root
    }

    override fun onPlaceSelected(placeWithPhotos: PlaceWithPhotos) {
        val actionDetail =
            PlacesListFragmentDirections.actionNavPlacesListToNavPlaceDetail(placeWithPhotos)
        findNavController().navigate(actionDetail)
    }


}