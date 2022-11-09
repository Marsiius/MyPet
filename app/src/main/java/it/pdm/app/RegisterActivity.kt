package it.pdm.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailTV: TextView
    private lateinit var passwordTV: TextView
    private lateinit var confirmPasswordTV: TextView
    private lateinit var regButton: Button
    private lateinit var loginTV: TextView

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        initializeUI()

        regButton.setOnClickListener{
            registerNewUser()
        }

        loginTV.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerNewUser() {

        val email = emailTV.text.toString()
        val password = passwordTV.text.toString()
        val confirmation = confirmPasswordTV.text.toString()

        if (TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Please, enter email", Toast.LENGTH_LONG).show()
            return
        }
        if (TextUtils.isEmpty(password) || password.length < 6) {
            Toast.makeText(applicationContext, "Invalid password", Toast.LENGTH_LONG).show()
            return
        }
        if(TextUtils.isEmpty(confirmPasswordTV.toString())){
            Toast.makeText(applicationContext, "Please confirm password!", Toast.LENGTH_LONG).show()
            return
        }
        if(password != confirmation){
            Toast.makeText(applicationContext, "Confirm password error: try again", Toast.LENGTH_LONG).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, LoginActivity::class.java )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext, "Registration failed! You have already account", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.et_email)
        passwordTV = findViewById(R.id.et_password)
        confirmPasswordTV = findViewById(R.id.et_confirm_password)
        regButton = findViewById(R.id.button_signin)
        loginTV = findViewById(R.id.textView3)
    }
}