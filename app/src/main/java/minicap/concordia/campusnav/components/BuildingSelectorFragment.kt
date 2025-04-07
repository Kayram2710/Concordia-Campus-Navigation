package minicap.concordia.campusnav.components

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import minicap.concordia.campusnav.buildingmanager.ConcordiaBuildingManager
import minicap.concordia.campusnav.buildingmanager.entities.Building
import minicap.concordia.campusnav.buildingmanager.enumerations.CampusName
import minicap.concordia.campusnav.databinding.FragmentBuildingSelectorBinding
import java.util.Locale

class BuildingSelectorFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentBuildingSelectorBinding? = null
    private val binding get() = _binding!!
    private lateinit var sgwAdapter: BuildingAdapter
    private lateinit var loyAdapter: BuildingAdapter
    private var allSgwBuildings: List<Building> = emptyList()
    private var allLoyBuildings: List<Building> = emptyList()

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

        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
            behavior.isHideable = true

            val displayMetrics = resources.displayMetrics
            val screenHeight = displayMetrics.heightPixels
            behavior.peekHeight = (screenHeight * 0.9).toInt()
            behavior.maxHeight = screenHeight

            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                private var lastSlideOffset = 0f
                private var dragStartOffset = 0f
                private var isDraggingDown = false
                private val dismissThreshold = 0.4f

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    val scrollView = binding.root as ScrollView
                    val canScrollUp = scrollView.canScrollVertically(-1)

                    when {
                        slideOffset < lastSlideOffset && !isDraggingDown -> {
                            isDraggingDown = true
                            dragStartOffset = slideOffset
                        }

                        slideOffset > lastSlideOffset -> {
                            isDraggingDown = false
                        }

                        isDraggingDown -> {
                            if (!canScrollUp) {
                                val dragDistance = dragStartOffset - slideOffset
                                if (dragDistance >= dismissThreshold) {
                                    behavior.state = BottomSheetBehavior.STATE_HIDDEN
                                }
                            } else {
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        }
                    }

                    lastSlideOffset = slideOffset
                }
            })
        }

        binding.sgwRecyclerView.isNestedScrollingEnabled = true
        binding.loyRecyclerView.isNestedScrollingEnabled = true

        // Set up RecyclerViews
        binding.sgwRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.loyRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        fetchBuildings()

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterBuildings(s.toString())
            }
        })
    }

    private fun fetchBuildings() {
        val buildingManager = ConcordiaBuildingManager.getInstance()

        allSgwBuildings = buildingManager.getBuildingsForCampus(CampusName.SGW)
        allLoyBuildings = buildingManager.getBuildingsForCampus(CampusName.LOYOLA)

        sgwAdapter = BuildingAdapter(allSgwBuildings.toMutableList())
        loyAdapter = BuildingAdapter(allLoyBuildings.toMutableList())

        sgwAdapter.setOnBuildingClickListener { building ->
            showBuildingInformation(building)
        }
        binding.sgwRecyclerView.adapter = sgwAdapter

        loyAdapter.setOnBuildingClickListener { building ->
            showBuildingInformation(building)
        }
        binding.loyRecyclerView.adapter = loyAdapter
    }

    private fun filterBuildings(query: String) {
        val lowerCaseQuery = query.lowercase(Locale.getDefault())

        val filteredSgw = allSgwBuildings.filter { building ->
            building.getBuildingName().lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    building.getBuildingAddress().lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    building.getDescription().lowercase(Locale.getDefault()).contains(lowerCaseQuery)
        }

        val filteredLoy = allLoyBuildings.filter { building ->
            building.getBuildingName().lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    building.getBuildingAddress().lowercase(Locale.getDefault()).contains(lowerCaseQuery) ||
                    building.getDescription().lowercase(Locale.getDefault()).contains(lowerCaseQuery)
        }

        sgwAdapter.updateData(filteredSgw.toMutableList())
        loyAdapter.updateData(filteredLoy.toMutableList())
    }

    /**
     * Shows an alert dialog with the building details.
     */
    private fun showBuildingInformation(building: Building) {

        val bottomSheet = BuildingInfoBottomSheetFragment.newInstance(building.buildingIdentifier)
        bottomSheet.show(parentFragmentManager, "BuildingInfoBottomSheet")
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
