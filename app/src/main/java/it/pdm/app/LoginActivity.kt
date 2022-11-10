package it.pdm.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var emailTV: TextView
    private lateinit var passwordTV: TextView
    private lateinit var logbutton: Button
    private lateinit var signinTV: TextView
    private lateinit var mAuth: FirebaseAuth
    private lateinit var fpasswordTV: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        logbutton.setOnClickListener{
            loginUserAccount()
        }
        signinTV.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        fpasswordTV.setOnClickListener{
            resetPassword()
        }
    }

    private fun resetPassword() {
        val email = emailTV.text.toString()
        if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        } else{
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener {
                Toast.makeText(applicationContext, "Please, check your email", Toast.LENGTH_LONG).show()
            }
                .addOnFailureListener{
                    Toast.makeText(applicationContext, "ERROR: try again", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun loginUserAccount() {
        val email = emailTV.text.toString()
        val password = passwordTV.text.toString()

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(applicationContext, "Please enter email...", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(applicationContext, "Please enter password!", Toast.LENGTH_LONG).show()
            return
        }

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if (it.isSuccessful) {
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Login failed! Please try again later", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.et_email)
        passwordTV = findViewById(R.id.et_password)
        signinTV = findViewById(R.id.tv_log_in)
        logbutton = findViewById(R.id.button_login)
        fpasswordTV = findViewById(R.id.tv_forgot_password)
    }
}