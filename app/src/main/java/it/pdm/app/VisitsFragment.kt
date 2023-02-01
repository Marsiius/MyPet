package it.pdm.app

import Visit.Visit
import Visit.VisitAdapter
import Visit.VisitPopUp
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import it.pdm.app.databinding.FragmentVisitsBinding
import pets.Visits
import java.util.*

class VisitsFragment : Fragment(), VisitPopUp.OnDialogNextBtnClickListener,
    VisitAdapter.adapterClickInterface {

    private lateinit var binding: FragmentVisitsBinding

    /*variabili primo codice*/
    private lateinit var auth: FirebaseAuth
    private lateinit var authId: String
    private lateinit var database: DatabaseReference
    private lateinit var visitAdapter: VisitAdapter
    private lateinit var visitItemList: MutableList<Visit>
    /*--------------VARIABILI-----------------*/
    private lateinit var popUpFragment: VisitPopUp

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentVisitsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getNoteFromFirebase()
        registerEvents()
    }

    private fun openPhoneCalendar(medVisit: Visits){
        val beginTime = Calendar.getInstance()
        //TODO: da finire
        beginTime.set(2022, 4, 19) // set the date and time of the event
        val endTime = Calendar.getInstance()
        endTime.set(2022, 4, 19) // set the date and time of the event
        //TODO
        val calendarIntent = Intent(Intent.ACTION_INSERT)
        calendarIntent.data = CalendarContract.Events.CONTENT_URI
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
        calendarIntent.putExtra(CalendarContract.Events.DESCRIPTION, medVisit.description)
        startActivity(calendarIntent)
    }
    private fun getNoteFromFirebase(){
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                visitItemList.clear()
                for (noteSnapshot in snapshot.children){
                    val ns = noteSnapshot.value.toString()
                    val stringArray = ns.split(",")
                    val title = stringArray[0]
                    val visitDate = stringArray[1]
                    val toDoNote = noteSnapshot.key?.let { Visit(it, title, visitDate) } //con it, passo nel primo parametro(idNote)
                    //val toDoDate = noteSnapshot.key?.let { Note(noteSnapshot.value.toString()) }
                    if(toDoNote != null){
                        visitItemList.add(toDoNote)
                    }
                }
                visitAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun init() {

        auth = FirebaseAuth.getInstance()
        authId = auth.currentUser!!.uid
        database = Firebase.database.reference.child("users").child(authId).child("Visit")

        binding.visitList.setHasFixedSize(true)
        binding.visitList.layoutManager = LinearLayoutManager(context)

        visitItemList = mutableListOf()
        visitAdapter = VisitAdapter(visitItemList)
        visitAdapter.setListener(this)
        binding.visitList.adapter = visitAdapter
    }

    private fun registerEvents(){
        binding.fab.setOnClickListener{
            popUpFragment = VisitPopUp()
            popUpFragment.setListener(this)
            popUpFragment.show(childFragmentManager, "FragmentPopUpNotes")
        }
    }

    override fun onDeleteNoteBtnClicked(visit: Visit) {
        database.child(visit.idVisit).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "cancella", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveVisit(visit: String, tvVisit: TextInputEditText, date1: String)
    {
        database.push().setValue(visit+","+date1).addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context, "Visita aggiunta", Toast.LENGTH_SHORT).show()
                tvVisit.text = null
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
            popUpFragment.dismiss()
        }
    }
}