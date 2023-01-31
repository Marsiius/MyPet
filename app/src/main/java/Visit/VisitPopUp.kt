package Visit

import Notes.FragmentPopUpNotes
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import it.pdm.app.R
import it.pdm.app.databinding.FragmentVaccinePopUpBinding
import it.pdm.app.databinding.FragmentVisitPopUpBinding
import kotlinx.android.synthetic.main.fragment_vaccine_pop_up.*
import kotlinx.android.synthetic.main.fragment_visit_pop_up.*
import medpet.VaccinePopUp
import java.text.SimpleDateFormat
import java.util.*

class VisitPopUp : DialogFragment() {

    private lateinit var binding: FragmentVisitPopUpBinding
    private var listener: OnDialogNextBtnClickListener? = null
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
        binding = FragmentVisitPopUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerEvents()
    }

    private fun registerEvents(){
        binding.popUpNext.setOnClickListener{
            val visitName = binding.tvVisit.text.toString()

            date1.set(data_picker.year, data_picker.month, data_picker.dayOfMonth)

            val dataSelezionata = dateFormat.format(date1.time) //trasforma in stringa la data con il suo formato dd-MM-yyyy
            if(visitName.isNotEmpty()){
                listener?.onSaveVisit(visitName, binding.tvVisit, dataSelezionata) //noteTask è la stringa che viene presa dal tvNote -------------------------------------------------------
            }else{
                Toast.makeText(context, "Non puoi lasciare il campo vuoto", Toast.LENGTH_SHORT).show()
            }

            binding.popUpClose.setOnClickListener{
                dismiss()
            }
        }
    }
    interface OnDialogNextBtnClickListener{
        fun onSaveVisit(vaccine: String, tvVaccine: TextInputEditText, date1: String)
    }
}