package minicap.concordia.campusnav.components.placeholder

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import minicap.concordia.campusnav.R
import minicap.concordia.campusnav.databinding.ShuttleBusScheduleBinding
import minicap.concordia.campusnav.helpers.ScheduleFetcher
import minicap.concordia.campusnav.components.ShuttleSchedule
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


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
            sgwMonThursButton.isSelected = true
            sgwFridayButton.isSelected = false

            filterSchedule("SGW", "Monday-Thursday")
        }

        sgwFridayButton.setOnClickListener {
            sgwMonThursButton.isSelected = false
            sgwFridayButton.isSelected = true

            filterSchedule("SGW", "Friday")
        }

        loyMonThursButton.setOnClickListener {
            loyMonThursButton.isSelected = true
            loyFridayButton.isSelected = false

            filterSchedule("Loyola", "Monday-Thursday")
        }

        loyFridayButton.setOnClickListener {
            loyMonThursButton.isSelected = false
            loyFridayButton.isSelected = true

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

        // Get the current time
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val currentTime = try {
            sdf.parse(sdf.format(Date()))
        } catch (e: Exception) {
            return
        }

        // Next upcoming time
        val sortedTimes = schedules.flatMap { it.departureTimes }
            .mapNotNull { time ->
                try {
                    sdf.parse(time)
                } catch (e: Exception) {
                    null
                }
            }
            .filter { it.after(currentTime) }.sorted()

        // Populate the GridLayout with the schedule data
        for (schedule in schedules) {
            if (schedule.campus != campus) continue

            // Background for time slots
            for (time in schedule.departureTimes) {
                val textView = TextView(requireContext()).apply {
                    text = time
                    textSize = 16f
                    setTextColor(resources.getColor(android.R.color.black))
                    setPadding(16,16,16,16)
                    gravity = Gravity.CENTER

                    // Set background color based on time
                    val timeDate = try {
                        sdf.parse(time)
                    } catch (e: Exception) {
                        null
                    }
                    val shapeDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.shuttle_time_background) as GradientDrawable
                    val backgroundColor = when {
                        timeDate == null -> ContextCompat.getColor(requireContext(),R.color.white)// Past times
                        timeDate < currentTime -> ContextCompat.getColor(requireContext(),R.color.gray)// Past times
                        timeDate == sortedTimes.getOrNull(0)  -> ContextCompat.getColor(requireContext(),R.color.pink) // Current time
                        timeDate == sortedTimes.getOrNull(1) -> ContextCompat.getColor(requireContext(),R.color.yellow) // Upcoming time
                        else -> ContextCompat.getColor(requireContext(),R.color.white) // Default
                    }
                    shapeDrawable.setColor(backgroundColor)
                    background = shapeDrawable
                }

                // Set margins for the TextView
                val layoutParams = GridLayout.LayoutParams().apply {
                    width = 0
                    height = GridLayout.LayoutParams.WRAP_CONTENT
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                    setMargins(0, 0, 0, 0)
                }
                textView.layoutParams = layoutParams

                gridLayout.addView(textView)
            }
        }

        // Add empty slots to fill out Grid
        val emptySlots = if (schedules.any {it.day == "Monday-Thursday"}) 2 else 1
        repeat(emptySlots){
            val emptyTextView = TextView(requireContext()).apply {
                text = ""
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
                setBackgroundResource(R.drawable.shuttle_time_background)
                setPadding(16,16,16,16)
                gravity = Gravity.CENTER
            }

            // Set margins for the TextView
            val emptyLayoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(0, 0, 0, 0)
            }
            emptyTextView.layoutParams = emptyLayoutParams

            gridLayout.addView(emptyTextView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
