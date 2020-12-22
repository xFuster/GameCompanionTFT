package com.example.gamecompanion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.example.gamecompanion.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Get Firebase Auth
        auth = Firebase.auth
        // Get Firebase Firestore
        db = Firebase.firestore
        // Progress Bar
        val progressBar = findViewById<ProgressBar>(R.id.pgbSignUp)
        // Init Listeners
        val registerUser:Button = findViewById(R.id.btnCreateAccount)
        registerUser.setOnClickListener {
            val email = findViewById<EditText>(R.id.edTxtEmail).text.toString()
            if(!isEmailValid(email)){
                Toast.makeText(this, getString(R.string.errorEmail), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val password = findViewById<EditText>(R.id.edTxtPassword).text.toString()
            if(!isPasswordValid(password)){
                Toast.makeText(this, getString(R.string.errorPassword), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val user = findViewById<EditText>(R.id.edTxtUser).text.toString()
            // Loading Bar
            progressBar.visibility = View.VISIBLE
            registerUser.isEnabled = false
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener{
                        if(it.isSuccessful){
                            auth.currentUser?.uid?.let {userId->
                                // Create User Model
                                val userModel = UserModel(userId, user)
                                // Create Firebase collection
                                db
                                        .collection("users")
                                        .document(userId)
                                        .set(userModel)
                                        .addOnCompleteListener{
                                            progressBar.visibility = View.GONE
                                            if(it.isSuccessful){
                                                finish()
                                            }
                                        }
                            }?: kotlin.run {
                                registerUser.isEnabled = true
                            }
                        }else{
                            registerUser.isEnabled = true
                        }
                    }
        }
    }
    private fun isEmailValid(email:String) :Boolean{
        val emailRegex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$"
       return email.isNotBlank() && email.contains("@") && email.contains(Regex(emailRegex))
    }

    private fun isPasswordValid(pass:String) :Boolean{
        return pass.isNotBlank()
                && pass.length > 5
    }
}