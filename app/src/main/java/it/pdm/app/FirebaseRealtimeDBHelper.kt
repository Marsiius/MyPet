package it.pdm.app

import com.google.firebase.database.FirebaseDatabase

class FirebaseRealtimeDBHelper {
    companion object{
        var dbRef = FirebaseDatabase
            .getInstance("https://my-pet-application-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users")
    }
}