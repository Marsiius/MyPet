package it.pdm.app

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.io.File


class Settings : PreferenceFragmentCompat() {
    private val SHARED_PREFS = "sharedPrefs"
    private lateinit var mAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var prefUser: Preference
    private lateinit var prefPassword: Preference
    private lateinit var prefLogout: Preference
    private lateinit var prefDeletePet : Preference
    private lateinit var prefInfo: Preference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        initializeUI()

        setEmail()

        prefPassword.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("Reset password?")
            builder.setMessage("You will receive an email with a link")
            builder.setPositiveButton("Confirm"
                ) { _, _ -> resetPassword()}
            builder.setNegativeButton(android.R.string.cancel
                ) { _, _ -> }

            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
        }

        prefLogout.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            logout()
            true
        }

        prefDeletePet.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("DELETE PET")
            builder.setMessage("Are you sure? Any progress will be lost")
            builder.setPositiveButton("Ok"
            ) {_,_ -> deletePet() }
            builder.setNegativeButton("Cancel"
            ){_,_ ->}

            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
        }

        prefInfo.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("Info")
            builder.setMessage(
                "MyPet is a project developed by Luca Canali and Eros Marsichina, " +
                        "University Insubria's students "
            )
            builder.setPositiveButton("Ok"
            ) {_,_ ->}

            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
        }
    }

    private fun deletePet(){
        FirebaseDBHelper.dbRefRT.addValueEventListener(object: ValueEventListener
            {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        snapshot.ref.removeValue()
                        deleteImageFromInternalStorage()
                        deleteImageFromFirebaseST()
                        val intent = Intent(context, MainActivity::class.java )
                        //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        Toast.makeText(context, "Pet deleted", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "onCancelled", error.toException())
                }
            })
    }

    private fun deleteImageFromInternalStorage(){
        val directory = context?.getDir("imageDir", Context.MODE_PRIVATE)
        val file = File(directory, "your-image.jpg")
        if(file.exists()){
            file.delete()
        }
    }

    private fun deleteImageFromFirebaseST(){
        val refPictureST = FirebaseDBHelper.dbRefST
        refPictureST.delete().addOnSuccessListener {
            Toast.makeText(context, "FILE ELIMINATO", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener {
            Toast.makeText(context, "FAAAAIIIILLLLL", Toast.LENGTH_LONG).show()
        }
    }

    private fun setEmail(){
            prefUser.summary = user.email.toString()
    }

    private fun resetPassword() {
        mAuth.sendPasswordResetEmail(user.email.toString()).addOnSuccessListener {
            Toast.makeText(context, "Please, check your email", Toast.LENGTH_LONG).show()
        }
            .addOnFailureListener{
                Toast.makeText(context, "ERROR: try again", Toast.LENGTH_LONG).show()
            }
    }


    private fun logout() {
        Toast.makeText(context, "LOG OUT", Toast.LENGTH_LONG).show()

        val sharedPreferences: SharedPreferences? = context?.getSharedPreferences(SHARED_PREFS,
            AppCompatActivity.MODE_PRIVATE
        )
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString("name", "")
        editor?.apply()

        val intent = Intent(context, RegisterActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
    }

    private fun initializeUI(){
        mAuth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser!!
        prefUser = findPreference("user_email")!!
        prefPassword = findPreference("reset_password")!!
        prefLogout = findPreference("logout")!!
        prefDeletePet = findPreference("delete_pet")!!
        prefInfo = findPreference("info")!!
    }
}

