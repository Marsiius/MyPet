package it.pdm.app

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_pet_information.*


class PetInformationFragment : Fragment() {

    private lateinit var uId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setInformation()
    }

    private fun setInformation() {
        val ref = FirebaseRealtimeDBHelper.dbRef.child(uId).child("pets")
        ref.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val name = snapshot.child("name").value.toString()
                        tv_name.text = name

                        val birthday = snapshot.child("birthday").value.toString()
                        tv_birthday.text = birthday

                        val weight = snapshot.child("weight").value.toString()
                        tv_weight.text = weight + " kg"

                        val height = snapshot.child("height").value.toString()
                        tv_height.text = height + " cm"

                        val chipNumber = snapshot.child("chipNumber").value.toString()
                        tv_chipNumber.text = chipNumber

                        val gender = snapshot.child("gender").value.toString()
                        tv_gender.text = gender

                        val breed = snapshot.child("breed").value.toString()
                        tv_breed.text = breed
                    }
                }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun init() {
        uId = FirebaseAuth.getInstance().currentUser!!.uid
    }
}