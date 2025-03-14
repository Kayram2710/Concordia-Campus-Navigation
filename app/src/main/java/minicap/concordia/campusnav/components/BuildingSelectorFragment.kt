package minicap.concordia.ca

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager
import minicap.concordia.campusnav.buildingmanager.entities.Building
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName
import minicap.concordia.campusnav.components.BuildingAdapter
import minicap.concordia.campusnav.databinding.FragmentBuildingSelectorBinding

class BuildingSelectorFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBuildingSelectorBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuildingSelectorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the bottom sheet behavior and set it to half-expanded
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
            behavior.isHideable = true
        }

        // Set up RecyclerViews with GridLayoutManager (2 columns)
        binding.sgwRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.loyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // Fetch building data and update RecyclerViews
        fetchBuildings()
    }

    /**
     * Fetches building data from ConcordiaBuildingManager by campus and attaches click listeners.
     */
    private fun fetchBuildings() {
        val buildingManager = ConcordiaBuildingManager.getInstance()

        // Retrieve the list of buildings for each campus
        val sgwBuildings: MutableList<Building> = buildingManager.getBuildingsForCampus(CampusName.SGW)
        val loyBuildings: MutableList<Building> = buildingManager.getBuildingsForCampus(CampusName.LOYOLA)

        // Create adapter for SGW buildings and set its click listener
        val sgwAdapter = BuildingAdapter(sgwBuildings)
        sgwAdapter.setOnBuildingClickListener { building ->
            showBuildingPopup(building)
        }
        binding.sgwRecyclerView.adapter = sgwAdapter

        // Create adapter for Loyola buildings and set its click listener
        val loyAdapter = BuildingAdapter(loyBuildings)
        loyAdapter.setOnBuildingClickListener { building ->
            showBuildingPopup(building)
        }
        binding.loyRecyclerView.adapter = loyAdapter
    }

    /**
     * Shows an alert dialog with the building details.
     */
    private fun showBuildingPopup(building: Building) {
        val message = "Building: ${building.getBuildingName()}\n" +
                "Description: ${building.getDescription()}\n" +
                "Campus: ${building.getAssociatedCampus()}"
        AlertDialog.Builder(requireContext())
            .setTitle("Building Details")
            .setMessage(message)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
