package com.example.travelex.placeEdit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelex.R
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.PlaceEditFragmentBinding
import com.example.travelex.misc.AdapterImageSlider
import kotlinx.android.synthetic.main.place_edit_fragment.*

class PlaceEditFragment : Fragment() {

    private lateinit var placeEditViewModel: PlaceEditViewModel
    private lateinit var placeWithPhotos: PlaceWithPhotos

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

        binding.placeWithPhotos = placeWithPhotos

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