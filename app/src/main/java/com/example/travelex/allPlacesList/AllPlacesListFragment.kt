package com.example.travelex.allPlacesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.example.travelex.TravelexApplication
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.misc.PlaceWithPhotosRecyclerViewAdapter
import com.example.travelex.misc.PlacesListListener
import kotlinx.android.synthetic.main.fragment_all_places_list.view.*

class AllPlacesListFragment : Fragment(), PlacesListListener {

    private val viewModel: AllPlacesListViewModel by viewModels {
        AllPlacesListViewModelFactory(
            (requireActivity().application as TravelexApplication).placeDao
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_places_list, container, false)

        val adapter = PlaceWithPhotosRecyclerViewAdapter(this, requireActivity())

        viewModel.placesList.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        view.all_places_recycler_list.adapter = adapter

        return view
    }

    override fun onPlaceSelected(placeWithPhotos: PlaceWithPhotos) {
        findNavController().navigate(AllPlacesListFragmentDirections.actionNavAllPlacesListToNavPlaceDetail(placeWithPhotos))
    }
}