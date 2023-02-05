package stepCounter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import data.FirebaseDBHelper
import it.pdm.app.MySingleton
import it.pdm.app.R
import it.pdm.app.databinding.FragmentStepCounterBinding
import it.pdm.app.databinding.FragmentStepCounterBinding.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_pet_identity_card.*
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_step_counter.*
import java.text.SimpleDateFormat
import java.util.*

class StepCounterFragment : Fragment() {

    private lateinit var binding: FragmentStepCounterBinding
    private var passiFatti = 100.00f
    var peso = 0.00f
    private var weight = ""
    private var calPerse = 0.00f
    val currentDate = Date()
    val date1 = Date()
    val mySingleton = MySingleton.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStepCounterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_stop.setBackgroundColor(Color.GRAY)
        btn_stop.isClickable = false

        binding.btnStart.setOnClickListener {

            /*//una volta premuto start, se la data memorizza è null, gli memorizzo la data corrente
            if(mySingleton.date == null){
                mySingleton.date = currentDate
                Toast.makeText(context, "nuova data memorizzata ${mySingleton.date}", Toast.LENGTH_SHORT).show()
            }// se invece la data memorizzata non è null, la confronto per capire se il giorno è lo stesso oppure se è un nuovo giorno, se è un nuovo giorno setto a true il boolean per resettare la shared pref
            else if(currentDate > mySingleton.date){
                Log.d("tag", "la data è vecchia quindi resetShared = true")
                mySingleton.resetShared = true
            }else{
                Log.d("tag", "la data è la stessa")
            }*/

            binding.btnStart.setBackgroundColor(Color.GRAY)
            binding.btnStop.setBackgroundColor(Color.RED)
            btn_start.isClickable = false
            btn_stop.isClickable = true
            val intentFg = Intent(context, StepService::class.java)
            requireActivity().startService(intentFg)
        }
        binding.btnStop.setOnClickListener {
            binding.btnStart.setBackgroundColor(resources.getColor(R.color.Green))
            binding.btnStop.setBackgroundColor(Color.GRAY)
            btn_start.isClickable = true
            btn_stop.isClickable = false
            val intentFg = Intent(context, StepService::class.java)
            requireActivity().stopService(intentFg)
        }
        val mySingleton = MySingleton.getInstance()
        val myValue = mySingleton.myValue
        stepsValue_tv.text = myValue
        passiFatti = myValue.toFloat()
        calPerse = ((peso*0.02)*passiFatti).toFloat()
        Log.d("tag", calPerse.toString())
        Log.d("pesoLetto", peso.toString())
        Log.d("passiLetti", passiFatti.toString())
        
        val ref = FirebaseDBHelper.dbRefPets
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    weight = snapshot.child("weight").value.toString()
                    if(weight.isNotEmpty()){
                        peso = weight.toFloat()
                        calPerse = ((peso*0.02)*passiFatti).toFloat()
                        Log.d("peso", peso.toString())
                        Log.d("passiLetti", passiFatti.toString())
                        Log.d("tag", calPerse.toString())
                        val formattedNumber = String.format("%.2f", calPerse)
                        if(calValue_tv.text!=null)
                            calValue_tv.text = formattedNumber
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    @SuppressLint("SimpleDateFormat")
    fun saveDateToSharedPreferences(context: Context, date: Date) {
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("date_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        editor.putString("date", dateFormat.format(date))
        editor.apply()
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


