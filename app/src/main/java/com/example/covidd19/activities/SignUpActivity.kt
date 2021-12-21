package com.example.covidd19.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.example.covidd19.MainActivity
import com.example.covidd19.R
import com.example.covidd19.databinding.ActivitySignInBinding
import com.example.covidd19.databinding.ActivitySignUpBinding
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var preferenceManager: PreferenceManager
    private lateinit var selected: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding= ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textSignIn.setOnClickListener { onBackPressed() }
        binding.imageBack.setOnClickListener { onBackPressed() }

        // Spinner
        binding.inputSPGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selected = parent?.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) { selected = "None" }
        }


        binding.buttonSignUp.setOnClickListener {
            if (binding.inputFirstName.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter first name", Toast.LENGTH_SHORT).show()
            } else if (binding.inputLastName.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter last name", Toast.LENGTH_SHORT).show()
            } else if (binding.inputEmail.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter email", Toast.LENGTH_SHORT).show()
            } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.text.toString()).matches()) {
                Toast.makeText(applicationContext, "Enter valid email", Toast.LENGTH_SHORT).show()
            } else if (binding.inputPassword.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter your password", Toast.LENGTH_SHORT).show()
            } else if (binding.inputConfirmPassword.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Confirm your password", Toast.LENGTH_SHORT).show()
            } else if (binding.inputPassword.text.toString() != binding.inputConfirmPassword.text.toString()) {
                Toast.makeText(applicationContext, "Password & confirm password must be", Toast.LENGTH_SHORT).show()
            } else if (binding.inputIDNumber.text.toString().trim().isEmpty()) {
                Toast.makeText(applicationContext, "Enter your id", Toast.LENGTH_SHORT).show()
            } else if (binding.inputIDNumber.length() != 11) {
                Toast.makeText(applicationContext, "ID number must be equal to 11", Toast.LENGTH_SHORT).show()
            } else if (binding.inputIDNumber.toString().startsWith("0")) {
                Toast.makeText(applicationContext, "ID number can't start with 0", Toast.LENGTH_SHORT).show()
            } else if (selected == "None") {
                Toast.makeText(applicationContext, "Enter your other", Toast.LENGTH_SHORT).show()
            } else {
                signUp()
            }
        }

    }

    private fun signUp() {
        binding.buttonSignUp.visibility = View.INVISIBLE
        binding.signUpProgressBar.visibility = View.VISIBLE

        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user: HashMap<String, Any> = HashMap()
        user[Constants.KEY_FIRST_NAME] = binding.inputFirstName.text.toString()
        user[Constants.KEY_LAST_NAME] = binding.inputLastName.text.toString()
        user[Constants.KEY_EMAIL] = binding.inputEmail.text.toString()
        user[Constants.KEY_PASSWORD] = binding.inputPassword.text.toString()
        user[Constants.KEY_ID] = binding.inputIDNumber.text.toString()
        user[Constants.KEY_OTHER] = selected

        database.collection(Constants.KEY_COLLECTION_USERS)
            .add(user)
            .addOnSuccessListener {
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true)
                preferenceManager.putString(Constants.KEY_USER_ID,it.id)
                preferenceManager.putString(Constants.KEY_FIRST_NAME, binding.inputFirstName.text.toString())
                preferenceManager.putString(Constants.KEY_LAST_NAME, binding.inputLastName.text.toString())
                preferenceManager.putString(Constants.KEY_EMAIL, binding.inputEmail.text.toString())
                preferenceManager.putString(Constants.KEY_ID, binding.inputIDNumber.text.toString())
                preferenceManager.putString(Constants.KEY_OTHER, selected)

                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)

            }.addOnFailureListener {
                binding.signUpProgressBar.isVisible = false
                binding.buttonSignUp.isVisible = true
                Toast.makeText(applicationContext, "Error: " + it.message, Toast.LENGTH_SHORT).show()
            }


    }

}