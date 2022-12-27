package it.pdm.app

import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
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
            findNavController().navigate(R.id.action_petFragment_to_signupPetFragment)
        }

        card_camera.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("How?")
            builder.setMessage("Open camera or update from local storage?")
            builder.setPositiveButton("Camera"
            ) { _, _ -> takePicture()}
            builder.setNeutralButton("Gallery"
            ) { _, _ -> selectPicture()}
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        card_information.setOnClickListener {
            findNavController().navigate(R.id.action_petFragment_to_petInformationFragment)
        }
    }

    private fun selectPicture(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    private fun takePicture(){
        if (context?.let { it1 ->
                ContextCompat.checkSelfPermission(
                    it1,
                    Manifest.permission.CAMERA
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }
        else{
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        }
    }

    /*override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode== CAMERA_PERMISSION_CODE)
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, CAMERA_REQUEST_CODE)
            }else{
                Toast.makeText(context, "DENIED ACCESS", Toast.LENGTH_LONG).show()
            }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                pet_picture.setImageBitmap(thumbnail)
            }else if(requestCode == GALLERY_REQUEST_CODE){
                val uri = data?.data
                pet_picture.setImageURI(uri)
            }
        }
    }

    private fun setNamePet(){
        FirebaseRealtimeDBHelper.dbRefRT.child("pets").child("name")
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
                TODO()
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
}






