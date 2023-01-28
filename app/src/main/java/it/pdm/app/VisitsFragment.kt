package it.pdm.app

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import it.pdm.app.R
import pets.Visit
import java.util.*

class VisitsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_visits, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun openPhoneCalendar(medVisit: Visit){
        val beginTime = Calendar.getInstance()
        //TODO: da finire
        beginTime.set(2022, 4, 19) // set the date and time of the event
        val endTime = Calendar.getInstance()
        endTime.set(2022, 4, 19) // set the date and time of the event
        //TODO
        val calendarIntent = Intent(Intent.ACTION_INSERT)
        calendarIntent.data = CalendarContract.Events.CONTENT_URI
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
        calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, medVisit.description)
        startActivity(calendarIntent)
    }
}