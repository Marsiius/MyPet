package it.pdm.app

import pets.Pet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        val pet = Pet(
            et_name.text.toString(),
            et_birthday.text.toString(),
            et_weight.text.toString(),
            et_height.text.toString(),
            et_chipnumber.text.toString(),
            et_gender.text.toString(),
            et_breed.text.toString()
        )
        Toast.makeText(context, "HO CREATO L'ANIMALE ZIO TUUTAPPOST", Toast.LENGTH_LONG).show()
    }
}