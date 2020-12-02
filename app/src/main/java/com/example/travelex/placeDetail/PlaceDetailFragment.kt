package com.example.travelex.placeDetail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelex.R
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.PlaceDetailFragmentBinding
import com.example.travelex.misc.AdapterImageSlider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PlaceDetailFragment : Fragment() {
    private lateinit var placeDetailViewModel: PlaceDetailViewModel

    private val callback = OnMapReadyCallback { googleMap ->

        val latLng = placeDetailViewModel.placeWithPhotos.place.latLng.split(",".toRegex()).toTypedArray()
        val location = LatLng(latLng[0].toDouble(), latLng[1].toDouble())

        val zoom = 16f
        googleMap.addMarker(MarkerOptions().position(location).title(placeDetailViewModel.placeWithPhotos.place.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoom))

        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isZoomControlsEnabled = false
    }

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
        val safeArgs: PlaceDetailFragmentArgs by navArgs()
        placeDetailViewModel.placeWithPhotos = safeArgs.placeWithPhotos
        binding.placeWithPhotos = placeDetailViewModel.placeWithPhotos

        initMap(binding)
        initSlider(binding)

        binding.executePendingBindings()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val actionEdit = PlaceDetailFragmentDirections.actionNavPlaceDetailToNavPlaceEdit(
                    placeDetailViewModel.placeWithPhotos
                )
                findNavController().navigate(actionEdit)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initSlider(binding: PlaceDetailFragmentBinding) {
        //todo wyswietlać grid na dole z możliwością usuwania zdjeć
        val sliderAdapter = AdapterImageSlider(requireActivity(), placeDetailViewModel.placeWithPhotos.photos)
        binding.placeDetailPager.adapter = sliderAdapter
        sliderAdapter.startAutoSlider(sliderAdapter.count, binding.placeDetailPager)
    }

    private fun initMap(binding: PlaceDetailFragmentBinding) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.place_detail_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(callback)

        mapFragment.requireView().isClickable = false
    }
}