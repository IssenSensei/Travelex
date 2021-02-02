package com.example.travelex.placeEdit

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.travelex.MainActivity.Companion.currentLoggedInUser
import com.example.travelex.R
import com.example.travelex.database.PhotoModel
import com.example.travelex.database.Place
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.FragmentPlaceEditBinding
import com.example.travelex.misc.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_place_edit.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.*
import kotlin.jvm.Throws

class PlaceEditFragment : Fragment(), PhotoGridListener {

    private lateinit var placeEditViewModel: PlaceEditViewModel
    private lateinit var placeWithPhotos: PlaceWithPhotos
    private lateinit var selectedPosition: LatLng
    private lateinit var sliderAdapterAuto: AdapterImageSliderAuto
    private val CAMERA_CODE = 0
    private val GALLERY_CODE = 1
    private var rotate = false
    private lateinit var currentPhotoPath: String
    private lateinit var photoGridAdapter: PhotoGridAdapter
    private lateinit var auth: FirebaseAuth

    private val callback = OnMapReadyCallback { googleMap ->
        val zoom = 16f

        googleMap.addMarker(
            MarkerOptions().position(selectedPosition).title(placeWithPhotos.place.name)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedPosition, zoom))

        googleMap.uiSettings.isMapToolbarEnabled = false
        googleMap.uiSettings.isZoomControlsEnabled = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        auth = FirebaseAuth.getInstance();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        placeEditViewModel = ViewModelProvider(this).get(PlaceEditViewModel::class.java)
        val binding = FragmentPlaceEditBinding.inflate(inflater, container, false)
        val safeArgs: PlaceEditFragmentArgs by navArgs()
        placeWithPhotos = safeArgs.placeWithPhotos
        placeEditViewModel.photos.addAll(placeWithPhotos.photos)
        placeEditViewModel.location = placeWithPhotos.place.latLng
        binding.placeWithPhotos = placeWithPhotos
        binding.executePendingBindings()

        initPhoto(binding)
        initMap(binding)
        initGrid(binding)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        updateSlider()
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

    private fun initMap(binding: FragmentPlaceEditBinding) {
        val latlong = placeWithPhotos.place.latLng.split(",".toRegex()).toTypedArray()
        selectedPosition = LatLng(latlong[0].toDouble(), latlong[1].toDouble())

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.place_edit_map) as SupportMapFragment?
        mapFragment!!.getMapAsync(callback)


        binding.placeEditMapButton.setOnClickListener {
            val latlong = placeWithPhotos.place.latLng.split(",".toRegex()).toTypedArray()
            val bundle =
                bundleOf("selectedPosition" to LatLng(latlong[0].toDouble(), latlong[1].toDouble()))
            findNavController().navigate(R.id.nav_maps, bundle)
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<LatLng>("location")
            ?.observe(viewLifecycleOwner) {
                if (it != null) {
                    place_edit_location.setText(getAddress(it, requireContext()))
                    placeEditViewModel.location = latLngToString(it)
                    mapFragment.requireView().visibility = View.VISIBLE
                    selectedPosition = it
                }
            }
    }

    private fun initPhoto(binding: FragmentPlaceEditBinding) {
        ViewAnimation.initShowOut(binding.placeEditGalleryContainer)
        ViewAnimation.initShowOut(binding.placeEditCameraContainer)
        binding.backDrop.visibility = View.GONE

        binding.placeEditAddPhoto.setOnClickListener { v -> toggleFabMode(v) }
        binding.backDrop.setOnClickListener { toggleFabMode(binding.placeEditAddPhoto) }

        binding.placeEditFabGallery.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, GALLERY_CODE)
        }

        binding.placeEditFabCamera.setOnClickListener {
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

    private fun initGrid(binding: FragmentPlaceEditBinding) {
        photoGridAdapter = PhotoGridAdapter(this, true)
        binding.placeEditPhotoGrid.adapter = photoGridAdapter

        //todo fix double submit
        photoGridAdapter.submitList(placeEditViewModel.photos)

        placeEditViewModel.photosLive.observe(viewLifecycleOwner, {
            it.let {
                photoGridAdapter.submitList(it)
            }
        })
        val manager = GridLayoutManager(activity, 3)
        binding.placeEditPhotoGrid.layoutManager = manager
    }


    private fun deletePlace() {
        placeEditViewModel.delete(placeWithPhotos)
        findNavController().popBackStack(R.id.nav_user_places_list, true)
    }

    private fun updatePlace() {
        placeWithPhotos.place.name = place_edit_name.text.toString()
        placeWithPhotos.place.description = place_edit_description.text.toString()
        placeWithPhotos.place.latLng = placeEditViewModel.location
        placeWithPhotos.place.rating = place_edit_rating.rating
        placeWithPhotos.place.comment = place_edit_comment.text.toString()
        placeEditViewModel.update(
            PlaceWithPhotos(
                Place(
                    placeWithPhotos.place.id,
                    currentLoggedInUser.uid,
                    placeWithPhotos.place.name,
                    currentLoggedInUser.userName,
                    currentLoggedInUser.userEmail,
                    currentLoggedInUser.userPhoto,
                    placeWithPhotos.place.description,
                    placeWithPhotos.place.latLng,
                    placeWithPhotos.place.rating,
                    placeWithPhotos.place.comment
                ),
                mutableListOf()
            )
        )
        placeWithPhotos.photos.clear()
        placeWithPhotos.photos.addAll(placeEditViewModel.photos)

        val actionDetail = PlaceEditFragmentDirections.actionNavPlaceEditToNavPlaceDetail(
            placeWithPhotos
        )
        findNavController().navigate(actionDetail)
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

    private fun toggleFabMode(v: View) {
        rotate = ViewAnimation.rotateFab(v, !rotate)
        if (rotate) {
            ViewAnimation.showIn(place_edit_gallery_container)
            ViewAnimation.showIn(place_edit_camera_container)
            back_drop.visibility = View.VISIBLE
        } else {
            ViewAnimation.showOut(place_edit_gallery_container)
            ViewAnimation.showOut(place_edit_camera_container)
            back_drop.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_CODE -> if (resultCode == Activity.RESULT_OK) {
                placeEditViewModel.savePhoto(currentPhotoPath)
                updateSlider()
            }
            GALLERY_CODE -> if (resultCode == Activity.RESULT_OK) {
                val image: Uri? = data?.data
                if (image != null) {
                    placeEditViewModel.savePhoto(image.toString())
                    updateSlider()
                }
            }
        }
    }

    private fun updateSlider() {
        if (place_edit_pager.isVisible) {
            sliderAdapterAuto.stopAutoSlider()
            sliderAdapterAuto.notifyDataSetChanged()
            sliderAdapterAuto.startAutoSlider(placeEditViewModel.photos.size, place_edit_pager)
        } else {
            startSlider()
        }
    }

    private fun startSlider() {
        if (placeEditViewModel.photos.size > 0) {
            place_edit_pager.visibility = View.VISIBLE
            place_edit_pager_placeholder.visibility = View.INVISIBLE
            sliderAdapterAuto = AdapterImageSliderAuto(requireActivity(), placeEditViewModel.photos)
            place_edit_pager.adapter = sliderAdapterAuto
            sliderAdapterAuto.startAutoSlider(placeEditViewModel.photos.size, place_edit_pager)
        }
    }

    override fun onDeleteClicked(photoModel: PhotoModel) {
        placeEditViewModel.removePhoto(photoModel)
        //todo remove this call after detecting error with views not vanishing
        photoGridAdapter.notifyDataSetChanged()
        if (placeEditViewModel.photos.size == 0) {
            place_edit_pager.visibility = View.GONE
            place_edit_pager_placeholder.visibility = View.VISIBLE
        }
        updateSlider()
    }

    override fun onPhotoClicked(photoModel: PhotoModel) {
        Toast.makeText(
            requireContext(),
            "photo" + photoModel.photoID.toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

}