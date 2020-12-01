package com.example.travelex.placeDetail

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.travelex.R
import com.example.travelex.database.PlaceWithPhotos
import com.example.travelex.databinding.PlaceDetailFragmentBinding
import com.example.travelex.misc.AdapterImageSlider

class PlaceDetailFragment : Fragment() {
    private lateinit var placeWithPhotos: PlaceWithPhotos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = PlaceDetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        val safeArgs: PlaceDetailFragmentArgs by navArgs()
        placeWithPhotos = safeArgs.placeWithPhotos

        binding.placeWithPhotos = placeWithPhotos

        binding.executePendingBindings()

        //todo wyswietlać grid na dole z możliwością usuwania zdjeć
        val sliderAdapter = AdapterImageSlider(requireActivity(), placeWithPhotos.photos)
        binding.placeDetailPager.adapter = sliderAdapter
        sliderAdapter.startAutoSlider(sliderAdapter.count, binding.placeDetailPager)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_save -> {
                val actionEdit = PlaceDetailFragmentDirections.actionNavPlaceDetailToNavPlaceEdit(
                    placeWithPhotos
                )
                findNavController().navigate(actionEdit)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}