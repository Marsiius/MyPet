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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_signup_pet.*

class SignupPetFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var firebaseUser: FirebaseUser

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


        initializeUI()


        button_sign_pet.setOnClickListener {
            writeUser()
            everythingOk()
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
    }

    private fun writeUser(){
        val ref = database.getReference("users")
        val user = User(firebaseUser.email.toString())
        ref.child(firebaseUser.uid).setValue(user)
        Toast.makeText(context, "REGISTRATO NEL DB", Toast.LENGTH_LONG).show()
    }

    private fun initializeUI(){
        database = Firebase.database
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
    }
}