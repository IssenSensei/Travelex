package com.example.travelex.placeDetail

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelex.R
import com.example.travelex.database.PhotoModel
import com.example.travelex.databinding.FragmentPlaceDetailBinding
import com.example.travelex.misc.AdapterImageSliderAuto
import com.example.travelex.misc.PhotoGridAdapter
import com.example.travelex.misc.PhotoGridListener
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class PlaceDetailFragment : Fragment(), PhotoGridListener {
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
        val binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)
        val safeArgs: PlaceDetailFragmentArgs by navArgs()
        placeDetailViewModel.placeWithPhotos = safeArgs.placeWithPhotos
        binding.placeWithPhotos = placeDetailViewModel.placeWithPhotos

        initMap(binding)
        initSlider(binding)
        initGrid(binding)

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

    private fun initSlider(binding: FragmentPlaceDetailBinding) {
        //todo wyswietlać grid na dole z możliwością usuwania zdjeć
        val sliderAdapter = AdapterImageSliderAuto(requireActivity(), placeDetailViewModel.placeWithPhotos.photos)
        binding.placeDetailPager.adapter = sliderAdapter
        sliderAdapter.startAutoSlider(sliderAdapter.count, binding.placeDetailPager)
    }

    private fun initMap(binding: FragmentPlaceDetailBinding) {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.place_detail_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(callback)

        mapFragment.requireView().isClickable = false
    }

    private fun initGrid(binding: FragmentPlaceDetailBinding) {
        val adapter = PhotoGridAdapter(this, false)
        binding.placeDetailPhotoGrid.adapter = adapter
        adapter.submitList(placeDetailViewModel.placeWithPhotos.photos)
        val manager = GridLayoutManager(activity, 3)
        binding.placeDetailPhotoGrid.layoutManager = manager
    }

    override fun onDeleteClicked(photoModel: PhotoModel) {
        Toast.makeText(requireContext(), "delete" + photoModel.photoID.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onPhotoClicked(photoModel: PhotoModel) {
        Toast.makeText(requireContext(), "photo" + photoModel.photoID.toString(), Toast.LENGTH_SHORT).show()
    }
}