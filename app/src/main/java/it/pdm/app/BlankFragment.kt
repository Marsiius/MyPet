package it.pdm.app

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.util.Log
//import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import it.pdm.app.databinding.ActivityLoginBinding.inflate
import it.pdm.app.databinding.ActivityMainBinding
import it.pdm.app.databinding.FragmentBinding
import it.pdm.app.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment.*
import kotlinx.android.synthetic.main.fragment_settings.*

class BlankFragment : Fragment() {

    private lateinit var binding: FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnStart.setOnClickListener {
            val intentFg = Intent(context, StepService::class.java)
            requireActivity().startService(intentFg)
        }
        binding.btnStop.setOnClickListener {
            val intentFg = Intent(context, StepService::class.java)
            requireActivity().stopService(intentFg)
        }
        val mySingleton = MySingleton.getInstance()
        val myValue = mySingleton.myValue
        stepsValue_tv.text = myValue
    }

    /*var running = false
    var starting = false
    var lastStep = 0
    var step = 0
    var sensorManager: SensorManager? = null
    private lateinit var binding: FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun startingBtn(){
        starting = true
    }

    private fun stopBtn(){
        starting = false
    }

    override fun onResume() {
        super.onResume()
        running = true
        var stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(activity, "No Step Counter Sensor !", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(event: SensorEvent) {
        if (running) {
            stepsValue_tv.setText("" + event.values[0])
            lastStep = event.values[0].toInt()
            Log.d("step", lastStep.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        running = false
        Toast.makeText(context, "stoppato", Toast.LENGTH_SHORT).show()
        for(i in 1..10000){
        }
        Log.d("stop", lastStep.toString())
    }*/
}
