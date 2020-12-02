package com.example.travelex.placeCreate

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.example.travelex.database.Place
import com.example.travelex.misc.AdapterImageSlider
import com.example.travelex.misc.ViewAnimation
import com.example.travelex.misc.getAddress
import com.example.travelex.misc.latLngToString
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.place_create_fragment.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.*
import kotlin.jvm.Throws


class PlaceCreateFragment : Fragment() {

    private val CAMERA_CODE = 0
    private val GALLERY_CODE = 1
    private lateinit var sliderAdapter: AdapterImageSlider
    private lateinit var placeCreateViewModel: PlaceCreateViewModel
    private var rotate = false
    private var selectedPosition: LatLng? = null
    private lateinit var currentPhotoPath: String

    private val callback = OnMapReadyCallback { googleMap ->
        val zoom = 16f
        if (selectedPosition != null) {
            googleMap.addMarker(MarkerOptions().position(selectedPosition!!).title("Current mark"))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPosition, zoom))
        }
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
        placeCreateViewModel = ViewModelProvider(this).get(PlaceCreateViewModel::class.java)
        return inflater.inflate(R.layout.place_create_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPhoto()
        initMap()
    }

    override fun onResume() {
        super.onResume()
        updateSlider()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                savePlace()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initPhoto() {
        ViewAnimation.initShowOut(place_create_gallery_container)
        ViewAnimation.initShowOut(place_create_camera_container)
        back_drop.visibility = View.GONE

        place_create_add_photo.setOnClickListener { v -> toggleFabMode(v) }

        back_drop.setOnClickListener { toggleFabMode(place_create_add_photo) }

        place_create_fab_gallery.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, GALLERY_CODE)
        }

        place_create_fab_camera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireContext().packageManager)?.also {
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        Toast.makeText(
                            requireContext(),
                            "Problem przy tworzeniu pliku",
                            Toast.LENGTH_SHORT
                        ).show()
                        null
                    }
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                            requireContext(),
                            "com.example.android.fileprovider",
                            it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, CAMERA_CODE)
                    }
                }
            }
        }
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.place_create_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(callback)
        mapFragment.requireView().isClickable = false

        place_create_map_button.setOnClickListener {
            val bundle = bundleOf("selectedPosition" to selectedPosition)
            findNavController().navigate(R.id.nav_maps, bundle)
        }

        mapFragment.requireView().visibility = View.GONE

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<LatLng>("location")
            ?.observe(viewLifecycleOwner) {
                if (it != null) {
                    place_create_location.setText(getAddress(it, requireContext()))
                    placeCreateViewModel.location = latLngToString(it)
                    mapFragment.requireView().visibility = View.VISIBLE
                    selectedPosition = it
                }
            }
    }

    private fun toggleFabMode(v: View) {
        rotate = ViewAnimation.rotateFab(v, !rotate)
        if (rotate) {
            ViewAnimation.showIn(place_create_gallery_container)
            ViewAnimation.showIn(place_create_camera_container)
            back_drop.visibility = View.VISIBLE
        } else {
            ViewAnimation.showOut(place_create_gallery_container)
            ViewAnimation.showOut(place_create_camera_container)
            back_drop.visibility = View.GONE
        }
    }

    private fun savePlace() {
        placeCreateViewModel.insert(
            Place(
                0,
                place_create_name.text.toString(),
                place_create_description.text.toString(),
                placeCreateViewModel.location.toString(),
                place_create_rating.rating,
                place_create_comment.text.toString()
            )
        )
        findNavController().popBackStack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_CODE -> if (resultCode == Activity.RESULT_OK) {
                placeCreateViewModel.savePhoto(currentPhotoPath)
                updateSlider()
            }
            GALLERY_CODE -> if (resultCode == Activity.RESULT_OK) {
                val image: Uri? = data?.data
                if (image != null) {
                    placeCreateViewModel.savePhoto(image.toString())
                    updateSlider()
                }
            }
        }
    }

    private fun updateSlider() {
        if (place_create_pager.isVisible) {
            sliderAdapter.stopAutoSlider()
            sliderAdapter.notifyDataSetChanged()
            sliderAdapter.startAutoSlider(placeCreateViewModel.photos.size, place_create_pager)
        } else {
            startSlider()
        }
    }

    private fun startSlider(){
        if (placeCreateViewModel.photos.size > 0) {
            place_create_pager.visibility = View.VISIBLE
            place_create_pager_placeholder.visibility = View.GONE
            sliderAdapter = AdapterImageSlider(requireActivity(), placeCreateViewModel.photos)
            place_create_pager.adapter = sliderAdapter
            sliderAdapter.startAutoSlider(placeCreateViewModel.photos.size, place_create_pager)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = DateFormat.getDateTimeInstance().format(Date())
        val storageDir: File =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES + File.separator + "Travelex")!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

}