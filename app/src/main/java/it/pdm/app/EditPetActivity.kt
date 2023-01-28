package it.pdm.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit_pet.*
import pets.MyPet

class EditPetActivity : AppCompatActivity() {

    lateinit var name: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_pet)

        initView()

        button2.setOnClickListener{
            editPet()
        }
    }

    private fun initView(){
        val petReference = FirebaseDBHelper.dbRefPets
        petReference.keepSynced(true)
        petReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = dataSnapshot.child("name").getValue(String::class.java)
                val birthday = dataSnapshot.child("birthday").getValue(String::class.java)
                et_name.text = name
                et_birthday.hint = birthday
                // utilizza la stringa name
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // gestisci l'errore
            }
        })
    }

    private fun editPet(){
        val petReference = FirebaseDBHelper.dbRefPets
        petReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val name = et_name.text.toString()
                val birthday = et_birthday.text.toString()
                val weight = et_weight.text.toString()
                val height = et_height.text.toString()
                val chip = et_chipNumber.text.toString()
                val gender = et_gender.text.toString()
                val breed = et_breed.text.toString()
                val pet = MyPet(name, birthday, weight, height, chip, gender, breed)
                petReference.setValue(pet)
                val intent = Intent(this@EditPetActivity, MainActivity::class.java )
                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }
}