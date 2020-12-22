package com.example.gamecompanion.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.gamecompanion.R
import com.example.gamecompanion.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlin.math.log

class ProfileFragment  : Fragment(){
    private lateinit var registerButton:Button
    private lateinit var loginButton:Button
    private lateinit var logoutButton:Button
    private lateinit var txtVwEmail:TextView
    private lateinit var txtVwUser:TextView
    private lateinit var edTxtEmail:EditText
    private lateinit var edTxtPass:EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        checkUserAvailable()
        registerButton.setOnClickListener {
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        checkUserAvailable()
    }

    private fun initViews(){
        registerButton= view!!.findViewById<Button>(R.id.btnGoSignUp)
        logoutButton = view!!.findViewById<Button>(R.id.btnLogOut)
        loginButton = view!!.findViewById<Button>(R.id.btnLogin)
        txtVwEmail = view!!.findViewById<TextView>(R.id.txtVwEmail)
        txtVwUser = view!!.findViewById<TextView>(R.id.txtVwUser)
        edTxtEmail = view!!.findViewById<EditText>(R.id.edTxtEmail)
        edTxtPass = view!!.findViewById<EditText>(R.id.edTxtPassword)
    }
    // Checks if the user is logged in to swap from one view to another
    private fun checkUserAvailable(){
        Firebase.auth.currentUser?.let {
            // User available
            registerButton.visibility = View.GONE
            loginButton.visibility = View.GONE
            edTxtEmail.visibility = View.GONE
            edTxtPass.visibility = View.GONE
            txtVwEmail.visibility = View.VISIBLE
            txtVwUser.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE

        }?: kotlin.run {
            // User unavailable
            registerButton.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            edTxtEmail.visibility = View.VISIBLE
            edTxtPass.visibility = View.VISIBLE
            txtVwEmail.visibility = View.GONE
            txtVwUser.visibility = View.GONE
            logoutButton.visibility = View.GONE
        }
    }

}