package com.example.travelex.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

private const val RC_LOCATION_FINE_AND_COARSE = 122

class MapsFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var mapsViewModel: MapsViewModel
    private lateinit var googleMap: GoogleMap


    private val callback = OnMapReadyCallback { map ->
        googleMap = map
        val currentPosition = mapsViewModel.currentPosition
        val zoom = 16f
        if (currentPosition != null) {
            googleMap.addMarker(MarkerOptions().position(currentPosition).title("Current mark"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, zoom))
        } else {
            getUserLocationPermissions()
        }

        setPoiClick(googleMap)
        setMapClick(googleMap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        mapsViewModel.currentPosition = arguments?.getParcelable("selectedPosition")

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(callback)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                savePosition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun savePosition() {
        mapsViewModel.currentPosition = mapsViewModel.selectedPosition
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            "location",
            mapsViewModel.selectedPosition
        )
        findNavController().popBackStack()
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            map.clear()
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
                    .snippet(poi.latLng.latitude.toString() + ", " + poi.latLng.longitude.toString())
            )
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(poi.latLng, 16f))
            poiMarker.showInfoWindow()
            mapsViewModel.selectedPosition = poi.latLng
        }
    }

    private fun setMapClick(map: GoogleMap) {
        map.setOnMapClickListener {
            map.clear()
            val marker = map.addMarker(
                MarkerOptions()
                    .position(it)
                    .title("Unknown Location")
                    .snippet(it.latitude.toString() + ", " + it.longitude.toString())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
            )
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 16f))
            marker.showInfoWindow()
            mapsViewModel.selectedPosition = it
        }
    }

    @AfterPermissionGranted(RC_LOCATION_FINE_AND_COARSE)
    private fun getUserLocationPermissions() {
        if (EasyPermissions.hasPermissions(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            initializeMapWithUserLocation()
        } else {
            EasyPermissions.requestPermissions(
                this,
                "",
                RC_LOCATION_FINE_AND_COARSE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun initializeMapWithUserLocation() {
        googleMap.isMyLocationEnabled = true
        val fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let { it: Location ->
                val position = LatLng(it.latitude, it.longitude)
                googleMap.addMarker(MarkerOptions().position(position).title("Current mark"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16f))
                mapsViewModel.selectedPosition = position
            } ?: Toast.makeText(
                requireContext(),
                "Problem setting localization!",
                Toast.LENGTH_SHORT
            ).show()

        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Problem getting user localization!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        initializeMapWithUserLocation()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
        Toast.makeText(requireContext(), "Permissions not granted!", Toast.LENGTH_SHORT).show()
    }
}
