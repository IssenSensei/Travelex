package com.example.travelex.userPlacesList

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
import kotlinx.android.synthetic.main.fragment_user_places_list.view.*

class UserPlacesListFragment : Fragment(), PlacesListListener {

    private val userPlacesListViewModel: UserPlacesListViewModel by viewModels {
        UserPlacesListViewModelFactory(
            (requireActivity().application as TravelexApplication).placeDao
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val root = inflater.inflate(R.layout.fragment_user_places_list, container, false)
        val adapter = PlaceWithPhotosRecyclerViewAdapter(this, requireActivity())

        userPlacesListViewModel.userPlacesList.observe(viewLifecycleOwner) {
            it.let {
                adapter.submitList(it)
            }
        }

        root.places_recycler_list.adapter = adapter
        return root
    }

    override fun onPlaceSelected(placeWithPhotos: PlaceWithPhotos) {
        val actionDetail =
            UserPlacesListFragmentDirections.actionNavUserPlacesListToNavPlaceDetail(placeWithPhotos)
        findNavController().navigate(actionDetail)
    }
}