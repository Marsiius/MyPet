package it.pdm.app

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_pet.*

class Pet : Fragment() {

    private lateinit var user: FirebaseUser
    private lateinit var uId: String

    companion object{
        const val CAMERA_PERMISSION_CODE = 1
        const val CAMERA_REQUEST_CODE = 2
        const val GALLERY_REQUEST_CODE = 3
    }

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

        fab.setOnClickListener {
            //findNavController().navigate(R.id.action_petFragment_to_signupPetFragment)
        }
    }

    private fun setNamePet(){
        progressBarPetFragment.visibility = View.VISIBLE
        FirebaseRealtimeDBHelper.dbRefRT.child("pets").child("name")
            .addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val fragment = PetInformationFragment()
                    val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.rl_pet_fragment, fragment)
                    transaction?.commit()
                    val data = snapshot.getValue(String::class.java)
                    tv_pet_name.text = data
                    setPetVisibility()
                }else{
                    fab.visibility = View.VISIBLE
                }
                progressBarPetFragment.visibility = View.INVISIBLE

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "loadPost:onCancelled",error.toException())            }
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
}






