package it.pdm.app

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_pet.*
import java.io.ByteArrayOutputStream

class Pet : Fragment() {

    private lateinit var user: FirebaseUser
    private lateinit var uId: String

    //companion object per tenere in riferimento i codici per accedere alla fotocamera/memoria
    //tramite il metodo StartActivityForResult
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

        setPet()

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_petFragment_to_signupPetFragment)
        }

        fab_camera.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("How?")
            builder.setMessage("Open camera or select picture from local storage?")
            builder.setPositiveButton("Camera"
            ) { _, _ -> takePicture()}
            builder.setNeutralButton("Gallery"
            ) { _, _ -> selectPicture()}
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    //metodo per aprire la galleria
    private fun selectPicture(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    //metodo per aprire la fotocamera
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

    //override del metodo che verifica l'autorizzazione per l'accesso alla fotocamera
    override fun onRequestPermissionsResult(
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
                Toast.makeText(context, "DENIED ACCESS, check device settings", Toast.LENGTH_LONG).show()
            }
    }

    //override che gestisce entrambe le opzioni (galleria/fotocamera) mediante dei controlli if
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == CAMERA_REQUEST_CODE){
                val thumbnail: Bitmap = data!!.extras!!.get("data") as Bitmap
                pet_picture.setImageBitmap(thumbnail)
                val bytes = ByteArrayOutputStream()
                thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path: String = MediaStore.Images.Media.insertImage(
                    requireContext().contentResolver,
                    thumbnail,
                    "Title",
                    null
                )
                val uri = Uri.parse(path)
                val imageRef = FirebaseRealtimeDBHelper.dbRefST.child("images/pet_picture.jpg")
                imageRef.putFile(uri)
                pet_picture.setImageURI(uri)
            }else if(requestCode == GALLERY_REQUEST_CODE){
                val uri = data!!.data
                pet_picture.setImageURI(uri)
                val imageRef = FirebaseRealtimeDBHelper.dbRefST.child("images/pet_picture.jpg")
                if (uri != null)
                    imageRef.putFile(uri)
            }
        }
    }

    //questo metodo viene eseguito appena il fragment viene lanciato. Esso scarica dal DB l'immagine
    //e il nome dell'animale (ancora da correggere la lettura dal DB della foto)
    private fun setPet(){
        progressBarPetFragment.visibility = View.VISIBLE
        val refRT = FirebaseRealtimeDBHelper.dbRefRT.child("pets").child("name")
        refRT.keepSynced(true)
        refRT
            .addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    val fragment = PetInformationFragment()
                    val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.rl_pet_fragment, fragment)
                    transaction?.commit()
                    val data = snapshot.getValue(String::class.java)
                    tv_pet_name.text = data
                    //setPicturePet()
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

    //metodo che scarica la foto dal DB Storage (da sistemare)
    private fun setPicturePet(){
        val refPicture = FirebaseRealtimeDBHelper.dbRefST.child("images/pet_picture.jpg")
        Glide.with(this /* context */)
            .load(refPicture)
            .into(pet_picture)
    }

    //metodo che imposta la visibilità degli elementi del fragment che fanno riferimento all'animale
    private fun setPetVisibility(){
        pet_picture.visibility = View.VISIBLE
        tv_pet_name.visibility = View.VISIBLE
        rl_pet_fragment.visibility = View.VISIBLE
        fab_camera.visibility = View.VISIBLE
    }

    private fun init(){
        user = FirebaseAuth.getInstance().currentUser!!
        uId = user.uid
    }
}






