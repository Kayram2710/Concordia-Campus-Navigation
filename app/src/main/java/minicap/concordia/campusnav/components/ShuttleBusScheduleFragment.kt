package minicap.concordia.campusnav.components.placeholder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import minicap.concordia.campusnav.R
import minicap.concordia.campusnav.databinding.ShuttleBusScheduleBinding
import minicap.concordia.campusnav.helpers.ScheduleFetcher
import minicap.concordia.campusnav.components.ShuttleSchedule

class ShuttleBusScheduleFragment : BottomSheetDialogFragment() {

    private var _binding: ShuttleBusScheduleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ShuttleBusScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Fetch the shuttle schedule
        fetchShuttleSchedule()

        // Set up button click listeners
        val sgwMonThursButton: Button = binding.root.findViewById(R.id.sgwMonThursButton)
        val sgwFridayButton: Button = binding.root.findViewById(R.id.sgwFridayButton)
        val loyMonThursButton: Button = binding.root.findViewById(R.id.loyMonThursButton)
        val loyFridayButton: Button = binding.root.findViewById(R.id.loyFridayButton)

        sgwMonThursButton.setOnClickListener {
            filterSchedule("SGW", "Monday-Thursday")
        }

        sgwFridayButton.setOnClickListener {
            filterSchedule("SGW", "Friday")
        }

        loyMonThursButton.setOnClickListener {
            filterSchedule("Loyola", "Monday-Thursday")
        }

        loyFridayButton.setOnClickListener {
            filterSchedule("Loyola", "Friday")
        }

        // Apply default filters for both campuses when the fragment loads
        filterSchedule("SGW", "Monday-Thursday")
        filterSchedule("Loyola", "Monday-Thursday")
    }

    private fun filterSchedule(campus: String, day: String) {
        ScheduleFetcher.fetch(object : ScheduleFetcher.ScheduleFetchListener {
            override fun onScheduleFetched(schedules: List<ShuttleSchedule>) {
                // Filter schedules by campus and day
                val filteredSchedules = schedules.filter {
                    it.campus == campus && it.day == day
                }
                // Update the UI with the filtered schedules
                updateUI(filteredSchedules, campus)
            }
        })
    }

    private fun fetchShuttleSchedule() {
        ScheduleFetcher.fetch(object : ScheduleFetcher.ScheduleFetchListener {
            override fun onScheduleFetched(schedules: List<ShuttleSchedule>) {}
        })
    }

    private fun updateUI(schedules: List<ShuttleSchedule>, campus: String) {
        val gridLayout = when (campus) {
            "SGW" -> binding.root.findViewById<GridLayout>(R.id.sgwGridLayout)
            "Loyola" -> binding.root.findViewById<GridLayout>(R.id.loyGridLayout)
            else -> return
        }

        // Clear the GridLayout for the specified campus
        gridLayout.removeAllViews()

        // Populate the GridLayout with the schedule data
        for (schedule in schedules) {
            if (schedule.campus != campus) continue

            for (time in schedule.departureTimes) {
                val textView = TextView(requireContext()).apply {
                    text = time
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.black))
                }

                // Set margins for the TextView
                val layoutParams = GridLayout.LayoutParams().apply {
                    setMargins(8, 8, 8, 8) // Set margins for left, top, right, bottom
                }
                textView.layoutParams = layoutParams

                gridLayout.addView(textView)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
