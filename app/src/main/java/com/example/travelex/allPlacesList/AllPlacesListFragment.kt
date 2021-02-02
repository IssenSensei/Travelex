package com.example.travelex.allPlacesList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.misc.PlaceWithPhotosRecyclerViewAdapter
import com.example.travelex.misc.PlacesListListener
import com.example.travelex.userPlacesList.UserPlacesListFragmentDirections
import com.example.travelex.userPlacesList.UserPlacesListViewModel
import kotlinx.android.synthetic.main.fragment_all_places_list.view.*
import kotlinx.android.synthetic.main.fragment_user_places_list.view.*

class AllPlacesListFragment : Fragment(), PlacesListListener {

    private lateinit var viewModel: AllPlacesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(AllPlacesListViewModel::class.java)
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