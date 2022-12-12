package it.pdm.app

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase

class FirebaseRealtimeDBHelper {
    companion object{
        var dbRef = FirebaseDatabase
            .getInstance("https://my-pet-application-default-rtdb.europe-west1.firebasedatabase.app")
            .getReference("users")

        fun readUserItem(userEventListener: ChildEventListener){
            dbRef.addChildEventListener(userEventListener)
        }

        fun setUserItem(key: String, userEventListener: ChildEventListener){
            dbRef.child(key).setValue(userEventListener)
        }

        fun removeUserItem(key: String){
            dbRef.child(key).removeValue()
        }
    }
}