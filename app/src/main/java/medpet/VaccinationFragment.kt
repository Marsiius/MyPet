package medpet

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.android.material.datepicker.MaterialDatePicker
import it.pdm.app.FirebaseDBHelper
import it.pdm.app.R
import kotlinx.android.synthetic.main.fragment_signup_pet.*
import kotlinx.android.synthetic.main.fragment_vaccination.*
import java.text.SimpleDateFormat
import java.util.*

class VaccinationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vaccination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fab.setOnClickListener{
            openPopup()
        }
    }

    private fun openPopup(){
        val builder = AlertDialog.Builder(context)
        builder.setTitle("ENTER VACCINATION")

        val et = EditText(context)
        et.hint = "Text..."
        val etDate = EditText(context)
        etDate.isFocusableInTouchMode = false
        etDate.hint = "Vaccine date: __/__/____"
        val etRecall = EditText(context)
        etRecall.isFocusableInTouchMode = false
        etRecall.hint = "Possible recall: __/__/____"
        etDate.setOnClickListener {
            openCalendar(etDate)
        }
        etRecall.setOnClickListener {
            openCalendar(etRecall)
        }

        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(et)
        layout.addView(etDate)
        layout.addView(etRecall)

        builder.setView(layout)

        builder.setPositiveButton("Confirm"
        ) { _, _ -> registerVaccine(et.text.toString(),
            etDate.text.toString(),
            etRecall.text.toString() )
        }
        builder.setNegativeButton("Cancel"
        ){ _, _ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun registerVaccine(name: String, date: String, recall: String){
        if(name.isNotEmpty() and date.isNotEmpty()){
            val vaccine = Vaccine(name, date, recall)
            FirebaseDBHelper.dbRefPets.child("vaccines").child(vaccine.name)
                .setValue(vaccine)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun openCalendar(editText: EditText){
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        picker.show(childFragmentManager, picker.toString())
        picker.addOnPositiveButtonClickListener {
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = formatter.format(Date(it))
            editText.setText(date)
        }
    }
}