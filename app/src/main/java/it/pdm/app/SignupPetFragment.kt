package it.pdm.app

import pets.MyPet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_signup_pet.*

class SignupPetFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signup_pet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button_sign_pet.setOnClickListener {
            if (everythingOk()) {
                signupPet()
            }
        }
    }

    private fun everythingOk(): Boolean {
        var ok = true
        if (et_name.text.isEmpty())
            ok = false
        if (et_birthday.text.isEmpty())
            ok = false
        return ok
    }

    private fun signupPet() {
        val pet = MyPet(
            et_name.text.toString(),
            et_birthday.text.toString(),
            et_weight.text.toString(),
            et_height.text.toString(),
            et_chipnumber.text.toString(),
            et_gender.text.toString(),
            et_breed.text.toString()
        )
        val id: String? = FirebaseAuth.getInstance().currentUser?.uid
        val email: String? = FirebaseAuth.getInstance().currentUser?.email
        val user: User = User(email)
        val database = Firebase.database.getReference("https://my-pet-application-default-rtdb.europe-west1.firebasedatabase.app/")
        if (id != null) {
            database.child("users").child(id).setValue(user)
            Toast.makeText(context, "HO SCRITTO NEL DB", Toast.LENGTH_LONG).show()

        }
        Toast.makeText(context, "HO CREATO L'ANIMALE ZIO TUUTAPPOST", Toast.LENGTH_LONG).show()
    }

    private fun writeUser(){
        TODO()
    }

    private fun initializeUI(){
        TODO()
    }
}