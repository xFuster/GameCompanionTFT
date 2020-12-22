package com.example.gamecompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Get Firebase Auth
        val auth = Firebase.auth

        val registerUser:Button = findViewById(R.id.btnCreateAccount)
        registerUser.setOnClickListener {
            // TODO: Registrar al usuario
            val email = findViewById<EditText>(R.id.edTxtEmail).text.toString()
            val password = findViewById<EditText>(R.id.edTxtPassword).text.toString()
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            Log.i("RegisterActivity", "User Registered!")
                        }else{
                            Log.i("RegisterActivity", "Error: ${it.exception}")
                        }
                    }
        }
    }
}