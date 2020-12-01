package com.example.travelex.placeEdit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelex.R
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.PlaceEditFragmentBinding
import com.example.travelex.misc.AdapterImageSlider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.place_create_fragment.*
import kotlinx.android.synthetic.main.place_edit_fragment.*

class PlaceEditFragment : Fragment() {

    private lateinit var placeEditViewModel: PlaceEditViewModel
    private lateinit var placeWithPhotos: PlaceWithPhotos
    private lateinit var selectedPosition: LatLng

    private val callback = OnMapReadyCallback { googleMap ->
        val zoom = 16f

        googleMap.addMarker(MarkerOptions().position(selectedPosition).title(placeWithPhotos.place.name))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPosition, zoom))

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
        placeEditViewModel = ViewModelProvider(this).get(PlaceEditViewModel::class.java)
        val binding = PlaceEditFragmentBinding.inflate(inflater, container, false)

        val view = binding.root

        val safeArgs: PlaceEditFragmentArgs by navArgs()
        placeWithPhotos = safeArgs.placeWithPhotos

        val latlong = placeWithPhotos.place.location.split(",".toRegex()).toTypedArray()
        selectedPosition = LatLng(latlong[0].toDouble(), latlong[1].toDouble())

        binding.placeWithPhotos = placeWithPhotos

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.place_edit_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(callback)


        binding.placeEditMapButton.setOnClickListener {
            val latlong = placeWithPhotos.place.location.split(",".toRegex()).toTypedArray()
            val bundle = bundleOf("selectedPosition" to LatLng(latlong[0].toDouble(), latlong[1].toDouble()))
            findNavController().navigate(R.id.nav_maps, bundle)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<LatLng>("location")
            ?.observe(viewLifecycleOwner) {
                if (it != null) {
                    place_edit_location.setText(it?.latitude.toString() + ", " + it?.longitude.toString())
                    mapFragment.requireView().visibility = View.VISIBLE
                    selectedPosition = it
                }
            }

        //todo pobierać zdjęcia z viewmodelu bo te mogą się zmienić
        //todo wyswietlać grid na dole z możliwością usuwania zdjeć
        val sliderAdapter = AdapterImageSlider(requireActivity(), placeWithPhotos.photos)
        binding.placeEditPager.adapter = sliderAdapter
        sliderAdapter.startAutoSlider(placeWithPhotos.photos.size, binding.placeEditPager)
        binding.executePendingBindings()

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_edit, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                updatePlace()
            }
            R.id.action_delete -> {
                deletePlace()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deletePlace() {
        placeEditViewModel.delete(placeWithPhotos)
        findNavController().popBackStack(R.id.nav_places_list, true)
    }

    private fun updatePlace() {
        placeWithPhotos.place.name = place_edit_name.text.toString()
        placeWithPhotos.place.description = place_edit_description.text.toString()
        placeWithPhotos.place.location = place_edit_location.text.toString()
        placeWithPhotos.place.rating = place_edit_rating.rating
        placeWithPhotos.place.comment = place_edit_comment.text.toString()
        placeEditViewModel.insert(placeWithPhotos)
        findNavController().popBackStack()
    }

}