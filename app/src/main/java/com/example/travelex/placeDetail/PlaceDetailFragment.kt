package com.example.travelex.placeDetail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.example.travelex.R
import com.example.travelex.databinding.PlaceDetailFragmentBinding

class PlaceDetailFragment : Fragment() {

    private lateinit var placeDetailViewModel: PlaceDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        placeDetailViewModel = ViewModelProvider(this).get(PlaceDetailViewModel::class.java)

        val binding = PlaceDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val safeArgs: PlaceDetailFragmentArgs by navArgs()
        val place = safeArgs.place

        binding.place = place

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.fragment_place_detail, menu)
    }
}