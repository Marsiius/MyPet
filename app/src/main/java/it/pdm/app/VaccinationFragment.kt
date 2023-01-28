package it.pdm.app

import Notes.FragmentPopUpNotes
import Notes.Note
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_vaccination.*
import medpet.Vaccine
import medpet.VaccinePopUp
import java.text.SimpleDateFormat
import java.util.*

class VaccinationFragment : Fragment(), VaccineAdapter.adapterClickInterface, VaccinePopUp.OnDialogNextBtnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private lateinit var database: DatabaseReference
    private lateinit var vacAdapter: VaccineAdapter
    private lateinit var vaccineItemList: MutableList<Vaccine>
    private lateinit var popUpFragment: VaccinePopUp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vaccination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getVaccinesFromFirebase()
        registerEvents()

        /*fab.setOnClickListener{
            openPopup()
        }*/
    }

    private fun registerEvents(){
        fab.setOnClickListener{
            popUpFragment = VaccinePopUp()
            popUpFragment.setListener(this)
            popUpFragment.show(childFragmentManager, "FragmentPopUpNotes")
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
        ) { _, _ -> registerVaccine2(et.text.toString(),
            etDate.text.toString(),
            etRecall.text.toString() )
        }
        builder.setNegativeButton("Cancel"
        ){ _, _ ->

        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun registerVaccine(it: String, name: String, date: String, recall: String){
        if(name.isNotEmpty() and date.isNotEmpty()){
            val vaccine = Vaccine(it, name, date, recall)
            FirebaseDBHelper.dbRefRT.child("vaccines").child(vaccine.nameVaccine)
                .setValue(vaccine)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(context, "Nota aggiunta", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }private fun registerVaccine2(name: String, date: String, recall: String){
        if(name.isNotEmpty() and date.isNotEmpty()){
            FirebaseDBHelper.dbRefRT.child("vaccines").push().setValue(name+","+date+","+recall)
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

    private fun getVaccinesFromFirebase(){
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (vacSnapshot in snapshot.children){
                    val ns = vacSnapshot.value.toString()
                    val stringArray = ns.split(",")
                    val name = stringArray[0]
                    val date = stringArray[1]
                    val recall = stringArray[2]
                    val toDoNote = snapshot.key?.let { Vaccine(it, name, date, recall) } //con it, passo nel primo parametro(idNote)
                    //val toDoDate = noteSnapshot.key?.let { Note(noteSnapshot.value.toString()) }
                    if(toDoNote != null){
                        vaccineItemList.add(toDoNote)
                        Log.d("vaccino", name+date+recall)
                    }
                    //scaricare vaccini da firebase e memorizarli nella listview
                }
                vacAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    private fun init() {

        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("users").child(authId).child("vaccines")

        vaccinesList.setHasFixedSize(true)
        vaccinesList.layoutManager = LinearLayoutManager(context)

        vaccineItemList = mutableListOf()
        vacAdapter = VaccineAdapter(vaccineItemList)
        vacAdapter.setListener(this)
        vaccinesList.adapter = vacAdapter

    }

    override fun onSaveVaccine(vaccine: String, tvVaccine: TextInputEditText, date1: String, date2: String) { //tvNote è l'inputEditText nel popUpNotes

        database.push().setValue(vaccine+","+date1+","+date2).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Nota aggiunta", Toast.LENGTH_SHORT).show()
                tvVaccine.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpFragment.dismiss()
        }
    }

    override fun onDeleteNoteBtnClicked(vaccine: Vaccine) {
        database.child(vaccine.idVaccine).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "cancellato", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}