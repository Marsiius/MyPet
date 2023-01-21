package it.pdm.app

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FirebaseDBHelper {
    companion object{
        val dbRefRT = FirebaseDatabase
            .getInstance("https://my-pet-application-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val dbRefPets = dbRefRT
            .child("pets")

        val dbRefNotes = dbRefRT
            .child("notes")

        val dbRefST = FirebaseStorage
            .getInstance("gs://my-pet-application.appspot.com")
            .getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

    }
}