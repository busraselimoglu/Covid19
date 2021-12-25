package com.example.covidd19.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.covidd19.MainActivity
import com.example.covidd19.databinding.ActivitySignInBinding
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var preferenceManager: PreferenceManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        preferenceManager = PreferenceManager(applicationContext)
        // The account will remain open until the sign-out button is clicked
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)){
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.buttonSignIn.setOnClickListener {
            if (binding.inputEmailIn.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter mail", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmailIn.text.toString()).matches()) {
                Toast.makeText(applicationContext, "Enter valid email", Toast.LENGTH_SHORT).show()
            } else if (binding.inputPasswordIn.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter password", Toast.LENGTH_SHORT).show()
            } else {
                signIn()
            }
        }
    }

    private fun signIn(){
        binding.buttonSignIn.isVisible = false
        binding.signInProgressBar.isVisible = true

        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .whereEqualTo(Constants.KEY_EMAIL,binding.inputEmailIn.text.toString())
            .whereEqualTo(Constants.KEY_PASSWORD,binding.inputPasswordIn.text.toString())
            .get()
            .addOnCompleteListener {
                it.let {
                    if (it.isSuccessful && it.result != null && it.result!!.documents.size > 0) {
                        val documentSnapshot = it.result!!.documents[0]
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                        preferenceManager.putString(Constants.KEY_USER_ID,documentSnapshot.id)
                        preferenceManager.putString(Constants.KEY_FIRST_NAME, documentSnapshot.getString(Constants.KEY_FIRST_NAME)!!)
                        preferenceManager.putString(Constants.KEY_LAST_NAME, documentSnapshot.getString(Constants.KEY_LAST_NAME)!!)
                        preferenceManager.putString(Constants.KEY_EMAIL, documentSnapshot.getString(Constants.KEY_EMAIL)!!)
                        preferenceManager.putString(Constants.KEY_ID,documentSnapshot.getString(Constants.KEY_ID)!!)
                        preferenceManager.putString(Constants.KEY_OTHER,documentSnapshot.getString(Constants.KEY_OTHER)!!)

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)

                    } else {
                        binding.signInProgressBar.isVisible = false
                        binding.buttonSignIn.isVisible = true
                        Toast.makeText(applicationContext, "Unable to sign in", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}