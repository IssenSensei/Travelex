package com.example.travelex.allPlacesList

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travelex.R

class AllPlacesListFragment : Fragment() {

    companion object {
        fun newInstance() = AllPlacesListFragment()
    }

    private lateinit var viewModel: AllPlacesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_all_places_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AllPlacesListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}