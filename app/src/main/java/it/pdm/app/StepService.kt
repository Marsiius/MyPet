package it.pdm.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import it.pdm.app.databinding.FragmentBinding
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*
import kotlin.properties.Delegates

class StepService : Service(), SensorEventListener {

    private lateinit var binding: FragmentBinding
    private var sensorManager : SensorManager? = null
    private var running : Boolean = false
    private var steps : Long = 0
    private var passi = 0
    private var passiTotali = 0
    private var totalSteps by Delegates.notNull<Long>()
    private val CHANNEL_ID = "ForegroundService Kotlin"
    val mySingleton = MySingleton.getInstance()
    val currentDate = Date()

    override fun onCreate() {
        super.onCreate()
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        running = true
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        Toast.makeText(applicationContext, "passi", Toast.LENGTH_SHORT).show()

        if (stepsSensor == null) {
            Toast.makeText(this, "Sensore non presente", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, FLAG_IMMUTABLE)
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Contapassi MyPet")
            .setContentText("Sto contando i tuoi passi")
            .setSmallIcon(R.drawable.app_icon)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        running = false
        Toast.makeText(applicationContext, "Destroy", Toast.LENGTH_SHORT).show()
        // Toast.makeText(this, "$totalSteps", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(running){
            Log.d("Tracking","passo")
            //steps = event!!.values[0].toLong()
            passi = event!!.values[0].toInt()
            Log.d("passoInt", passi.toString())
            passiTotali = getPassi()
            passiTotali += getPassi()
            savePassi(passiTotali)
            mySingleton.myValue = passi.toString()
            Log.d("Singleton", mySingleton.myValue)
            /*totalSteps = getSteps()
            totalSteps += steps
            saveSteps(totalSteps)*/
        }
        //TODO("cambiare il nome della variabile passi, e salvarlo nelle shared preferences")
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    private fun getSteps() : Long {
        val sharedPreferences = getSharedPreferences("trackingPrefs", Context.MODE_PRIVATE)
        val steps = sharedPreferences!!.getLong("steps",0)
        return steps
    }

    private fun getPassi() : Int{
        val sharedPreferences = getSharedPreferences("trackingSteps", Context.MODE_PRIVATE)
        val passi = sharedPreferences!!.getInt("passi",0)
        return passi
    }

    private fun savePassi(passi : Int){
        val sharedPreferences = getSharedPreferences("trakingSteps", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor?.apply {
            putInt("passi", passi)
        }?.apply()
        if(mySingleton.resetShared == true){
            editor.clear()
            Log.d("tag", "shared ripristinate")
            mySingleton.resetShared = false
        }
    }
    //salvo gli step
    private fun saveSteps(steps : Long) {
        val sharedPreferences =
            getSharedPreferences("trackingPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.apply {
            putInt("steps", passi);
            //putLong("steps",steps );
            //TODO("Verificare se il putInt salva correttamente, prima usavamo il putLong con vecchia variabile")
        }?.apply()
        //Log.d("saveSteps", steps.toString())
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

}
