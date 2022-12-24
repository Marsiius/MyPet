package it.pdm.app

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class FirebaseRealtimeDBHelper {
    companion object{
        var dbRef = FirebaseDatabase
            .getInstance("https://my-pet-application-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users")
        val dbRefNote = FirebaseDatabase
            .getInstance("https://my-pet-application-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("notes")
    }
}