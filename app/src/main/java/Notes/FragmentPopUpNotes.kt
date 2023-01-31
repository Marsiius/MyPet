package Notes

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import it.pdm.app.R
import it.pdm.app.databinding.FragmentPopUpNotesBinding
import kotlinx.android.synthetic.main.fragment_pop_up_notes.*
import java.lang.reflect.Modifier
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Year
import java.util.*


class FragmentPopUpNotes : DialogFragment() {

    private lateinit var binding: FragmentPopUpNotesBinding
    private var listener: OnDialogNextBtnClickListener? = null
    @RequiresApi(Build.VERSION_CODES.O)
    val currentDate = LocalDate.now()
    var selectedDate = ""
    val date1 = Calendar.getInstance()
    val date2 = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    fun setListener(listener: OnDialogNextBtnClickListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPopUpNotesBinding.inflate(inflater, container, false)

        /*val datePick = binding.dataPicker
        val calendar = Calendar.getInstance()
        datePick.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ) { view, year, monthOfYear, dayOfMonth ->
            // Do something with the selected date
            selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
            Log.d("Selected Date", selectedDate)

            //date1.set(year, monthOfYear, dayOfMonth)
            Log.d("date1", date1.toString())
        }*/

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun registerEvents(){
        binding.popUpNext.setOnClickListener{
            val noteTask = binding.tvNote.text.toString()
            val bodyTask = binding.tvNoteBody.text.toString()

            //
            date1.set(data_picker.year, data_picker.month, data_picker.dayOfMonth)
            if (date1.before(date2)) {
                Log.d("La Data", "Selezionata è prima di quella corrente")
                //aggiungere al firebase
            }else if(date1.after(date2)){
                Log.d("La Data", "Selezionata è dopo quella corrente")
            }else{
                Log.d("La Data", "Selezionata è la stessa di quella corrente")
                //aggiungere al firebase
            }
            //

            val dataSelezionata = dateFormat.format(date1.time) //trasforma in stringa la data con il suo formato dd-MM-yyyy
            if(noteTask.isNotEmpty()){
                listener?.onSaveTask(noteTask, binding.tvNote, dataSelezionata, bodyTask, binding.tvNoteBody) //noteTask è la stringa che viene presa dal tvNote -------------------------------------------------------
            }else{
                Toast.makeText(context, "Non puoi lasciare il campo vuoto", Toast.LENGTH_SHORT).show()
            }



            /*if (noteTask.isNotEmpty()){
                if(selectedDate.after(currentDate)){
                    listener?.onSaveTask(noteTask, binding.tvNote)
                }else{
                    Log.d("datePick", selectedDate)
                    Log.d("datePick", currentDate.toString())
                    Toast.makeText(context, "Non puoi ricordarti cose passate", Toast.LENGTH_SHORT).show()
                }
            }else{
                Log.d("data", currentDate.toString())
                Toast.makeText(context, "Non puoi lasciare il campo vuoto", Toast.LENGTH_SHORT).show()

            }*/

            binding.popUpClose.setOnClickListener{
                dismiss()

            }
        }
    }

    interface OnDialogNextBtnClickListener{
        fun onSaveTask(note : String, tvNote: TextInputEditText, date: String, body : String, tvNoteBody : TextInputEditText)
    }

}
