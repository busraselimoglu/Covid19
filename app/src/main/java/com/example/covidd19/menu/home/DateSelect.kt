package com.example.covidd19.menu.home


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covidd19.MainActivity
import com.example.covidd19.databinding.ActivityDateSelectBinding
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.*
import java.text.DateFormat
import java.util.*
import kotlin.collections.HashMap


class DateSelect : AppCompatActivity() {

    private lateinit var binding: ActivityDateSelectBinding

    private lateinit var preferenceManager: PreferenceManager
    private var userHash = HashMap<String, Any>()
    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDateSelectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferenceManager = PreferenceManager(applicationContext)

        binding.imageBack.setOnClickListener { onBackPressed() }
        binding.imageReject.setOnClickListener { onBackPressed() }

        //val selectedDoctor = intent.getStringExtra("userid")
        val bundle: Bundle? = intent.extras
        message = bundle!!.getString("userid")

        val selectClock = "Select Clock"
        val selectDate = "Select Date"
        binding.textViewClock.text = selectClock
        binding.textView.text = selectDate

        // get a calendar instance
        val calendar = Calendar.getInstance()
        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // set the calendar date as calendar view selected date
            calendar.set(year, month, dayOfMonth)

            // format the calendar selected date
            val dateFormatter = DateFormat.getDateInstance(DateFormat.MEDIUM)
            val formattedDate = dateFormatter.format(calendar.time)

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
            if (binding.textView.text == selectDate) {
                Toast.makeText(this, "Select Date", Toast.LENGTH_SHORT).show()
            } else if (binding.textViewClock.text == selectClock) {
                Toast.makeText(this, "Select Clock", Toast.LENGTH_SHORT).show()
            } else {
                getUsers()
            }
        }


    }


    private fun signAppointment(users: HashMap<String, Any>) {
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()

        database.collection(Constants.KEY_COLLECTION_APPOINTMENT)
            .document(message.toString())
            .collection(binding.textView.text.toString())
            .document(binding.textViewClock.text.toString())
            .set(users, SetOptions.merge())
            .addOnSuccessListener {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }

    }


    private fun getUsers() {
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get().addOnCompleteListener {
                val myUserId = preferenceManager.getString(Constants.KEY_USER_ID)
                if (it.isSuccessful && it.result != null) {
                    for (documentSnapshot: QueryDocumentSnapshot in it.result) {
                        if (myUserId.equals(documentSnapshot.id)) {
                            userHash[Constants.KEY_FIRST_NAME] =
                                documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()
                            userHash[Constants.KEY_LAST_NAME] =
                                documentSnapshot.getString(Constants.KEY_LAST_NAME).toString()
                            userHash[Constants.KEY_EMAIL] = documentSnapshot.getString(Constants.KEY_EMAIL).toString()
                            userHash[Constants.KEY_ID] = documentSnapshot.getString(Constants.KEY_ID).toString()
                        }
                    }
                    if (userHash.size > 0 ) {
                        signAppointment(userHash)
                    }
                }
            }
    }

}

