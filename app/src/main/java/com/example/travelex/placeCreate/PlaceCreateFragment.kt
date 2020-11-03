package com.example.travelex.placeCreate

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.travelex.R
import com.example.travelex.database.Place

class PlaceCreateFragment : Fragment() {

    private lateinit var placeCreateViewModel: PlaceCreateViewModel

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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_place_create, menu)
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
                "nameTest1",
                "descriptionTest1",
                "locationTest1",
                "photoTest1",
                5,
                "commentTest1",
                "addInfoTest1"
            )
        )
        findNavController().popBackStack()
    }

}