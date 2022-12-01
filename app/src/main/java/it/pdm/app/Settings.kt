package it.pdm.app

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class Settings : PreferenceFragmentCompat() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var prefUser: Preference
    private lateinit var prefPassword: Preference
    private lateinit var prefSubscribe: Preference
    
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

        prefSubscribe.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setCancelable(true)
            builder.setTitle("DELETE ACCOUNT")
            builder.setMessage("Contact us to subscribe (mypet.mc@gmail.com)")
            builder.setPositiveButton("Ok"
            ) {_,_ ->}

            val dialog: AlertDialog = builder.create()
            dialog.show()
            true
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

    private fun initializeUI(){
        mAuth = FirebaseAuth.getInstance()
        user = FirebaseAuth.getInstance().currentUser!!
        prefUser = findPreference("user_email")!!
        prefPassword = findPreference("reset_password")!!
        prefSubscribe = findPreference("delete_account")!!
    }
}

