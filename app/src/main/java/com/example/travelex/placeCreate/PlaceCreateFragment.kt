package com.example.travelex.placeCreate

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.example.travelex.ViewAnimation
import com.example.travelex.database.Place
import kotlinx.android.synthetic.main.place_create_fragment.*
import java.io.File
import java.io.IOException

class PlaceCreateFragment : Fragment() {

    private val CAMERA_CODE = 0
    private val GALLERY_CODE = 1

    private lateinit var placeCreateViewModel: PlaceCreateViewModel
    private var rotate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.place_create_fragment, container, false)
        placeCreateViewModel = ViewModelProvider(this).get(PlaceCreateViewModel::class.java)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, CAMERA_CODE)
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

    private fun savePlace() {
        placeCreateViewModel.insert(
            Place(
                0,
                place_create_name.text.toString(),
                place_create_description.text.toString(),
                place_create_location.text.toString(),
                place_create_rating.rating,
                place_create_comment.text.toString())
        )
        findNavController().popBackStack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_CODE -> if (resultCode == Activity.RESULT_OK) {
                val extras = data?.extras
                val image = extras!!["data"] as Bitmap?
                if (image != null) {
                    val relativeLocation = Environment.DIRECTORY_PICTURES + File.separator + "Travelex"

                    val contentValues = ContentValues().apply {
                        put(MediaStore.Images.ImageColumns.DISPLAY_NAME, System.currentTimeMillis().toString())
                        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) { //this one
                            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
                        }
                    }

                    val resolver = requireActivity().contentResolver
                    val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                    try {

                        uri?.let { uri ->
                            val stream = resolver.openOutputStream(uri)

                            stream?.let { stream ->
                                if (!image.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                                    throw IOException("Failed to save bitmap.")
                                }
                            } ?: throw IOException("Failed to get output stream.")

                        } ?: throw IOException("Failed to create new MediaStore record")

                    } catch (e: IOException) {
                        if (uri != null) {
                            resolver.delete(uri, null, null)
                        }
                        throw IOException(e)
                    } finally {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                        placeCreateViewModel.savePhoto(uri.toString())
                        addImageToGrid(uri)
                    }

                }
            }
            GALLERY_CODE -> if (resultCode == Activity.RESULT_OK) {
                val image: Uri? = data?.data
                if (image != null) {
                    placeCreateViewModel.savePhoto(image.toString())
                    addImageToGrid(image)
                }
            }
        }
    }

    //todo
    private fun addImageToGrid(uri: Uri?) {
//        val imageView = ImageView(requireContext()).apply {
//            id = (taskCreateViewModel.photos.size - 1)
//            setImageURI(uri)
//        }
//        task_photos_grid.addView(imageView)
    }


}