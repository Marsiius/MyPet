package it.pdm.app

import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.widget.TextView
//import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import it.pdm.app.databinding.FragmentBinding
import kotlinx.android.synthetic.main.fragment_settings.*

//import kotlinx.android.synthetic.main.activity_main.*
//import kotlinx.android.synthetic.main.fragment_settings.*

class Settings : Fragment(){

    private var totalSteps : Long=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSteps()
        startStepService()
        stepsValue.text = "" + totalSteps
    }

    private fun setSteps() {
        val sharedPref = requireContext().getSharedPreferences("trackingPrefs", Context.MODE_PRIVATE)
        totalSteps = sharedPref.getLong("steps", 0L)
    }

    private fun startStepService() {
        val intent = Intent(context, StepService::class.java)
        requireActivity().startService(intent)
    }

}

