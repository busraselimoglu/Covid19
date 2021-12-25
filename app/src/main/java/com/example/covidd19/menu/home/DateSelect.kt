package com.example.covidd19.menu.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.covidd19.databinding.ActivityDateSelectBinding
import com.example.covidd19.models.User
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.SetOptions
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DateSelect : AppCompatActivity() {

    private lateinit var binding: ActivityDateSelectBinding

    private lateinit var preferenceManager: PreferenceManager

    //private var users = ArrayList<String>()
    private var userHash = HashMap<String, Any>()
    private var doctors = ArrayList<String>()
    private lateinit var formattedDate: String
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(applicationContext)

        binding.imageBack.setOnClickListener { onBackPressed() }
        binding.imageReject.setOnClickListener { onBackPressed() }

        //val selectedDoctor = intent.getStringExtra("userid")
        var bundle: Bundle? = intent.extras
        message = bundle!!.getString("userid")


        // get a calendar instance
        val calendar = Calendar.getInstance()
        binding.calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // set the calendar date as calendar view selected date
            calendar.set(year, month, dayOfMonth)

            // format the calendar selected date
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
            formattedDate = dateFormatter.format(calendar.time)

            // show calendar view selected date on text view
            // medium format date
            binding.textView.text = formattedDate
        }

        binding.clockText9.setOnClickListener { binding.textViewClock.text = binding.clockText9.text }
        binding.clockText10.setOnClickListener { binding.textViewClock.text = binding.clockText10.text.toString() }
        binding.clockText11.setOnClickListener { binding.textViewClock.text = binding.clockText11.text.toString() }
        binding.clockText13.setOnClickListener { binding.textViewClock.text = binding.clockText13.text.toString() }
        binding.clockText14.setOnClickListener { binding.textViewClock.text = binding.clockText14.text.toString() }
        binding.clockText15.setOnClickListener { binding.textViewClock.text = binding.clockText15.text.toString() }
        binding.imageAccept.setOnClickListener {
            if (binding.textView.text == "Select Date") {
                Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show()


            } else {
                getUsers()
            }
        }


        //getDoctors()


    }

    //
    private fun signAppointment(doctor: ArrayList<String>, users: HashMap<String, Any>) {
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()

        Log.d("bk", "message: " + message)

        database.collection(Constants.KEY_COLLECTION_APPOINTMENT).document(message.toString())
            .collection(binding.textView.text.toString())
            .document(binding.textViewClock.text.toString()).set(users, SetOptions.merge())

    }

    private fun getUsers() {
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get().addOnCompleteListener {
                val myUserId = preferenceManager.getString(Constants.KEY_USER_ID)
                if (it.isSuccessful && it.result != null) {
                    // Using swipe refresh layout, it can be called multiple times that's why I need to clear user list before adding new data
                    //users.clear()
                    userHash.clear()
                    // Here, I will display the user list except for the currently signed-in user,
                    // because no one will have a meeting with himself. That's why I am excluding a signed-in user from the list
                    for (documentSnapshot: QueryDocumentSnapshot in it.result) {
                        if (myUserId.equals(documentSnapshot.id)) {
                            continue
                        }

                        val user = User()

                        user.other = documentSnapshot.getString(Constants.KEY_OTHER).toString()
                        if (user.other == "Doctor") {
                            Log.d("bk", "Other:::: Date:::  " + user.other)
                            user.id = documentSnapshot.getString(Constants.KEY_ID).toString()
                            doctors.add(user.id)
                        }


                        /*user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()
                        user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME).toString()
                        user.email = documentSnapshot.getString(Constants.KEY_EMAIL).toString()
                        user.id = documentSnapshot.getString(Constants.KEY_ID).toString()
                        users.add(user.firstName)
                        users.add(user.lastName)
                        users.add(user.email)
                        users.add(user.id)*/
                        userHash[Constants.KEY_FIRST_NAME] =
                            documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()
                        userHash[Constants.KEY_LAST_NAME] =
                            documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()
                        userHash[Constants.KEY_EMAIL] = documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()
                        userHash[Constants.KEY_ID] = documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()

                    }
                    if (doctors.size > 0 && userHash.size > 0) {
                        Log.d("bk", "Boş değil")
                        signAppointment(doctors, userHash)

                    }
                }
            }
    }


}