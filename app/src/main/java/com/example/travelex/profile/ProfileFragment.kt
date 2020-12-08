package com.example.travelex.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.travelex.MainActivity.Companion.currentLoggedInUser
import com.example.travelex.R
import com.example.travelex.database.User
import com.example.travelex.databinding.FragmentProfileBinding
import com.example.travelex.misc.ViewAnimation
import kotlinx.android.synthetic.main.fragment_place_create.*
import kotlinx.android.synthetic.main.fragment_place_create.back_drop
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File
import java.io.IOException
import java.text.DateFormat
import java.util.*
import kotlin.jvm.Throws

class ProfileFragment : Fragment() {

    private lateinit var profileViewModel: ProfileViewModel
    private val CAMERA_CODE = 0
    private val GALLERY_CODE = 1
    private lateinit var currentPhotoPath: String
    private lateinit var onProfileEditListener: OnProfileEditListener
    private var rotate = false


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val binding: FragmentProfileBinding =
            FragmentProfileBinding.inflate(inflater, container, false)
        binding.user = currentLoggedInUser

        binding.userEditButton.setOnClickListener {
            if (profileViewModel.photo != "") currentLoggedInUser.userPhoto = profileViewModel.photo
            currentLoggedInUser.userName = binding.userEditUsername.text.toString()
            profileViewModel.updateUser(currentLoggedInUser)
            onProfileEditListener.onProfileEdit(currentLoggedInUser)
            findNavController().popBackStack()
        }

        binding.executePendingBindings()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPhoto()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnProfileEditListener) {
            onProfileEditListener = context
        }
    }

    private fun initPhoto() {
        ViewAnimation.initShowOut(user_edit_gallery_container)
        ViewAnimation.initShowOut(user_edit_camera_container)
        user_edit_back_drop.visibility = View.GONE

        user_edit_add_photo.setOnClickListener { v -> toggleFabMode(v) }

        user_edit_back_drop.setOnClickListener { toggleFabMode(user_edit_add_photo) }

        user_edit_fab_gallery.setOnClickListener {
            val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickPhoto, GALLERY_CODE)
        }

        user_edit_fab_camera.setOnClickListener {
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

    private fun toggleFabMode(v: View) {
        rotate = ViewAnimation.rotateFab(v, !rotate)
        if (rotate) {
            ViewAnimation.showIn(user_edit_gallery_container)
            ViewAnimation.showIn(user_edit_camera_container)
            user_edit_back_drop.visibility = View.VISIBLE
        } else {
            ViewAnimation.showOut(user_edit_gallery_container)
            ViewAnimation.showOut(user_edit_camera_container)
            user_edit_back_drop.visibility = View.GONE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_CODE -> if (resultCode == Activity.RESULT_OK) {
                profileViewModel.photo = currentPhotoPath
                Glide.with(requireContext()).load(currentPhotoPath).into(user_edit_photo)
            }
            GALLERY_CODE -> if (resultCode == Activity.RESULT_OK) {
                val image: Uri? = data?.data
                if (image != null) {
                    profileViewModel.photo = image.toString()
                    Glide.with(requireContext()).load(image).into(user_edit_photo)
                }
            }
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

interface OnProfileEditListener {
    fun onProfileEdit(user: User)
}