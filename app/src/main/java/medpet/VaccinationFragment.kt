package medpet

import android.os.Bundle
import android.util.Log
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
import it.pdm.app.R
import kotlinx.android.synthetic.main.fragment_vaccination.*

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
    }

    private fun getVaccinesFromFirebase(){
        database.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                vaccineItemList.clear()
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

    private fun registerEvents(){
        fab.setOnClickListener{
            popUpFragment = VaccinePopUp()
            popUpFragment.setListener(this)
            popUpFragment.show(childFragmentManager, "VaccinePopUp")
        }
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

    override fun onDeleteVaccineBtnClicked(vaccineId: Vaccine) {
        database.child(vaccineId.idVaccine).removeValue().addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(context, "cancellato", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}