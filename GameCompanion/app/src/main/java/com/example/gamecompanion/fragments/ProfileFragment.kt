package com.example.gamecompanion.fragments

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.gamecompanion.R
import com.example.gamecompanion.RegisterActivity
import com.example.gamecompanion.models.UserModel
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import kotlin.math.log

class ProfileFragment  : Fragment(){
    private lateinit var registerButton:Button
    private lateinit var loginButton:Button
    private lateinit var logoutButton:Button
    private lateinit var txtVwUser:TextView
    private lateinit var edTxtEmail:EditText
    private lateinit var edTxtPass:EditText
    private lateinit var avatarButton:Button
    private lateinit var avatarImg:ImageView

    private var logged:Boolean = true

    private val REQUEST_IMAGE_CAPTURE = 100
    //Get Firebase Auth
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Fire base needs
        auth = Firebase.auth
        firestore = Firebase.firestore

        initViews(view)

        checkUserAvailable()

        initListeners()

        loadProfile()
    }

    override fun onResume() {
        super.onResume()
        checkUserAvailable()
        loadProfile()
    }

    private fun initListeners(){
        registerButton.setOnClickListener {
            Firebase.analytics.logEvent("RegisterButtonClicked", null)
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
        }

        avatarButton.setOnClickListener{
            dispatchTakePictureIntent()
            loadProfile()
        }

        logoutButton.setOnClickListener {
            logOut()
            checkUserAvailable()
        }

        loginButton.setOnClickListener {
            //Validate email || Validate password
            if (!isEmailvalid() || !isPasswordvalid()) {
                return@setOnClickListener
            }

            //Get email and password
            val email = getUserMail()
            val password = getUserPassword()

            //Check if user exist
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

                if (it.isSuccessful) {
                    Log.i("ProfileLoginFragment", "User logged")
                    getSharedPreferences()

                }
            }
            loadProfile()
        }

    }

    private fun isEmailvalid(): Boolean {
        var email = getUserMail()
        val emailRegex = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
        return email.isNotBlank()
                && email.contains("@")
                && email.contains(Regex(emailRegex))
    }

    private fun isPasswordvalid(): Boolean {

        var password = getUserPassword()

        val passwordRegex = "[a-zA-Z0-9-.]";
        return password.isNotBlank()
                && password.length > 6
                && password.contains(Regex(passwordRegex))
    }

    private fun getUserMail(): String {
        return edTxtEmail.text.toString()
    }

    private fun getUserPassword(): String {
        return edTxtPass.text.toString()
    }
    private fun getSharedPreferences() {
        Firebase.auth.currentUser?.uid?.let { userId: String ->
            firestore
                .collection("users")
                .document(userId)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result?.toObject(UserModel::class.java)?.let { user: UserModel ->
                            val user = UserModel(
                                userId = Firebase.auth.currentUser?.uid!!,
                                userName = user.userName,
                                avatarUrl = user.avatarUrl
                            )
                            val editor =
                                context?.getSharedPreferences(
                                    "USER_PREFS",
                                    Context.MODE_PRIVATE
                                )
                                    ?.edit()
                            editor?.putString("PREF_USERID", user.userId)
                            editor?.putString("PREF_USERNAME", user.userName)
                            editor?.putString("PREF_URL", user.avatarUrl)
                            editor?.apply()
                        }
                        val fr = fragmentManager?.beginTransaction()
                        fr?.replace(R.id.fragmentContainer, ProfileFragment())
                        fr?.commit()
                    }
                    checkUserAvailable()
                }
        }
    }

    private fun logOut(){
        Firebase.auth.signOut()
        removeUserData()
        checkUserAvailable()
    }

    private fun removeUserData() {
        val editor = context?.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)?.edit()
        editor?.clear()
        editor?.apply()
    }

    private fun dispatchTakePictureIntent(){
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try{
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }catch (e: ActivityNotFoundException){
            Log.i("Camara", "Error al iniciar la camara")
        }
    }

    override fun onStart() {
        super.onStart()
        checkUserAvailable()
    }

    private fun initViews(view: View){
        registerButton= view.findViewById<Button>(R.id.btnGoSignUp)
        logoutButton = view.findViewById<Button>(R.id.btnLogOut)
        loginButton = view.findViewById<Button>(R.id.btnLogin)
        txtVwUser = view.findViewById<TextView>(R.id.txtVwUser)
        edTxtEmail = view.findViewById<EditText>(R.id.edTxtEmail)
        edTxtPass = view.findViewById<EditText>(R.id.edTxtPassword)
        avatarButton = view.findViewById<Button>(R.id.btnAvatar)
        avatarImg = view.findViewById<ImageView>(R.id.imgAvatar)
    }
    // Checks if the user is logged in to swap from one view to another
    private fun checkUserAvailable(){
        if(logged) {
            Firebase.auth.currentUser?.let { user ->
                // User available
                registerButton.visibility = View.GONE
                loginButton.visibility = View.GONE
                edTxtEmail.visibility = View.GONE
                edTxtPass.visibility = View.GONE
                txtVwUser.visibility = View.VISIBLE
                logoutButton.visibility = View.VISIBLE
                avatarButton.visibility = View.VISIBLE
                avatarImg.visibility = View.VISIBLE

                // Get user profile
                Firebase.firestore
                    .collection("users")
                    .document(user.uid)
                    .get()
                    .addOnSuccessListener {
                        it.toObject(UserModel::class.java)?.let { user ->
                            val avatarUrl = user.avatarUrl
                            Glide.with(avatarImg)
                                .load(avatarUrl)
                                .into(avatarImg)
                        }
                    }

            } ?: kotlin.run {
                // User unavailable
                registerButton.visibility = View.VISIBLE
                loginButton.visibility = View.VISIBLE
                edTxtEmail.visibility = View.VISIBLE
                edTxtPass.visibility = View.VISIBLE
                txtVwUser.visibility = View.GONE
                logoutButton.visibility = View.GONE
                avatarButton.visibility = View.GONE
                avatarImg.visibility = View.GONE
            }
        }else{
            registerButton.visibility = View.VISIBLE
            loginButton.visibility = View.VISIBLE
            edTxtEmail.visibility = View.VISIBLE
            edTxtPass.visibility = View.VISIBLE
            txtVwUser.visibility = View.GONE
            logoutButton.visibility = View.GONE
            avatarButton.visibility = View.GONE
            avatarImg.visibility = View.GONE
        }
    }

    private fun uploadImageToFirebaseStorage(bitmap: Bitmap){
        val storage = Firebase.storage

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100,baos)
        val data = baos.toByteArray()

        val userId = Firebase.auth.currentUser?.uid
        val storageReference = storage.reference.child("images/avatars/$userId.jpg")

        storageReference.putBytes(data)
                .addOnSuccessListener{ taskSnapshot ->
                    storageReference.downloadUrl
                            .addOnSuccessListener {uri ->
                                val url = uri.toString()
                                Firebase.firestore
                                        .collection("users")
                                        .document(userId!!)
                                        .update("avatarUrl", url)

                            }
                            .addOnFailureListener{

                            }
                }
                .addOnFailureListener{

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            (data?.extras?.get("data") as? Bitmap)?.let{bitmap ->
                avatarImg.setImageBitmap(bitmap)
                uploadImageToFirebaseStorage(bitmap)
            }
        }
    }

    private fun loadProfile() {
        val userPreferences = context?.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        val username = userPreferences?.getString("PREF_USERNAME", "")

        val user = Firebase.auth.currentUser
        user?.let {
            val email = user.email
            val usernameResult = email?.removeRange(email.indexOf("@"), email.length)

            if (username.isNullOrBlank()) {
                txtVwUser.text = usernameResult
            } else {
                txtVwUser.text = username
            }
        }
    }

}