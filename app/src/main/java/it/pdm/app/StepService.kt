package it.pdm.app

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlin.properties.Delegates

class StepService : Service(), SensorEventListener {

    private var sensorManager : SensorManager? = null
    private var running : Boolean = false
    private var steps : Long = 0
    private var totalSteps by Delegates.notNull<Long>()

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        running = true
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
            Toast.makeText(this, "Sensore non presente", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onDestroy() {
        running = false
        // Toast.makeText(this, "$totalSteps", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            Log.d("Tracking","passo")
            // Toast.makeText(this, "Running", Toast.LENGTH_SHORT).show()
            //totalSteps = event!!.values[0]
            //val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            //binding.totalSteps.text = event!!.values[0].toString()
            steps = event!!.values[0].toLong() //steps nella sessione
            totalSteps = getSteps() //getto gli steps totali
            totalSteps += steps //aggiungo quelli fatti mo
            saveSteps(totalSteps) //li salvo
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private fun getSteps() : Long {
        val sharedPreferences = getSharedPreferences("trackingPrefs", Context.MODE_PRIVATE)
        val steps = sharedPreferences!!.getLong("steps",0)
        return steps
    }

    //salvo gli steps nelle shared prefs
    private fun saveSteps(steps : Long) {
        val sharedPreferences =
            getSharedPreferences("trackingPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.apply {
            putLong("steps",steps );
        }?.apply()
    }

}
