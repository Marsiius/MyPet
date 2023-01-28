package medpet

import Notes.FragmentPopUpNotes
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import it.pdm.app.R
import it.pdm.app.databinding.FragmentPopUpNotesBinding
import it.pdm.app.databinding.FragmentVaccinePopUpBinding
import kotlinx.android.synthetic.main.fragment_pop_up_notes.*
import kotlinx.android.synthetic.main.fragment_vaccine_pop_up.*
import java.text.SimpleDateFormat
import java.util.*

class VaccinePopUp : DialogFragment() {

    private lateinit var binding: FragmentVaccinePopUpBinding
    private var listener: VaccinePopUp.OnDialogNextBtnClickListener? = null
    val date1 = Calendar.getInstance()
    val date2 = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    fun setListener(listener: VaccinePopUp.OnDialogNextBtnClickListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVaccinePopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerEvents()
    }

    private fun registerEvents(){
        binding.popUpNext.setOnClickListener{
            val vaccineName = binding.nameVaccine.text.toString()

            date1.set(data_picker1.year, data_picker1.month, data_picker1.dayOfMonth)
            date2.set(data_picker2.year, data_picker2.month, data_picker2.dayOfMonth)

            val dataSelezionata = dateFormat.format(date1.time) //trasforma in stringa la data con il suo formato dd-MM-yyyy
            val recallSelezionata = dateFormat.format(date2.time) //trasforma in stringa la data con il suo formato dd-MM-yyyy
            if(vaccineName.isNotEmpty()){
                listener?.onSaveVaccine(vaccineName, binding.nameVaccine, dataSelezionata, recallSelezionata) //noteTask è la stringa che viene presa dal tvNote -------------------------------------------------------
            }else{
                Toast.makeText(context, "Non puoi lasciare il campo vuoto", Toast.LENGTH_SHORT).show()
            }

            binding.popUpClose.setOnClickListener{
                dismiss()
            }
        }
    }

    interface OnDialogNextBtnClickListener{
        fun onSaveVaccine(vaccine: String, tvVaccine: TextInputEditText, date1: String, date2: String)
    }
}
