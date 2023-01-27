package it.pdm.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import pets.MyPet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.fragment_pop_up_notes.*
import kotlinx.android.synthetic.main.fragment_signup_pet.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class SignupPetFragment : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var firebaseUser: FirebaseUser
    private lateinit var email: String
    private lateinit var uId: String
    //private lateinit var SHARED_PREFERENCES: SharedPreferences TODO()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_pet, container, false)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button_sign_pet.setBackgroundColor(Color.BLUE)
        button_sign_pet.background = resources.getDrawable(R.drawable.countor_signuppet)

        initializeUI()

        button_sign_pet.setOnClickListener {
            if(everythingOk()){
                writeUser()
                writePet()
                Toast.makeText(context,"Successful!", Toast.LENGTH_LONG)
                    .show()
            }else {
                Toast.makeText(context,"Please check name and birthday", Toast.LENGTH_LONG)
                    .show()
            }
        }

        et_birthday.setOnClickListener {
            val builder = MaterialDatePicker.Builder.datePicker()
            val picker = builder.build()
            picker.show(childFragmentManager, picker.toString())
            picker.addOnPositiveButtonClickListener {
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = formatter.format(Date(it))
                et_birthday.setText(date)
            }
        }
    }

    //metodo che verifica che sia stato inserito sia il nome sia il compleanno dell'animale
    private fun everythingOk(): Boolean {
        var ok = true
        if (et_name.text.isEmpty())
            ok = false
        if (et_birthday.text.isEmpty())
            ok = false
        return ok
    }

    //metodo che crea il nodo "user_uId" nel real time DB
    private fun writeUser(){
        database.child(uId)
    }

    //metodo che scrive l'animale nel real time DB
    private fun writePet() {
        val pet = MyPet(
            et_name.text.toString(),
            et_birthday.text.toString(),
            et_weight.text.toString(),
            et_height.text.toString(),
            et_chipnumber.text.toString(),
            et_gender.text.toString(),
            et_breed.text.toString()
        )
        FirebaseDBHelper.dbRefPets.setValue(pet)
        .addOnSuccessListener {
            Toast.makeText(context, "Successfully", Toast.LENGTH_LONG).show()
        }
        val intent = Intent(context, MainActivity::class.java )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun initializeUI(){
        database = FirebaseDBHelper.dbRefRT
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        email = firebaseUser.email.toString()
        uId = firebaseUser.uid
    }
}