package it.pdm.app

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color.*
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_pet.*

var clicked = false

class Pet : Fragment() {

    private lateinit var user: FirebaseUser
    private lateinit var uId: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        setNamePet()

        /*fab.setOnClickListener{
            setFabAnimation()
        }*/

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_petFragment_to_signupPetFragment)
        }

        card_camera.setOnClickListener {
            TODO()
        }

        card_information.setOnClickListener {
            findNavController().navigate(R.id.action_petFragment_to_petInformationFragment)
        }
    }

    private fun setNamePet(){
        FirebaseRealtimeDBHelper.dbRef.child(uId).child("pets").child("name")
            .addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val data = snapshot.value.toString()
                    tv_pet_name.text = data
                    setPetVisibility()
                }else{
                    Toast.makeText(context, "Create your first pet!", Toast.LENGTH_LONG).show()
                    fab.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun init(){
        user = FirebaseAuth.getInstance().currentUser!!
        uId = user.uid
    }

    private fun setPetVisibility(){
        pet_picture.visibility = View.VISIBLE
        tv_pet_name.visibility = View.VISIBLE
        rl_pet_fragment.visibility = View.VISIBLE
    }

    /*private fun setFabAnimation(){
        if(!clicked){
            fab.startAnimation(AnimationUtils.loadAnimation(context,R.anim.rotate_forward))
            fab.backgroundTintList = ColorStateList.valueOf(rgb(255,0,0))
            fab_add.visibility = View.VISIBLE
            fab_edit.visibility = View.VISIBLE
            fab_camera.visibility = View.VISIBLE
        }else{
            fab.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rotate_backward))
            fab.backgroundTintList = ColorStateList.valueOf(rgb(76,175,80))
            fab_add.visibility = View.INVISIBLE
            fab_edit.visibility = View.INVISIBLE
            fab_camera.visibility = View.INVISIBLE
        }
        clicked = !clicked
    }*/
}






