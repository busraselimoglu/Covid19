package com.example.covidd19.menu.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidd19.R
import com.example.covidd19.databinding.FragmentDoctorAppointmentBinding
import com.example.covidd19.databinding.FragmentMakeHospitalAppointmentBinding
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot


class DoctorAppointment : Fragment() {

    private var _binding: FragmentDoctorAppointmentBinding?= null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentDoctorAppointmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = context?.let { PreferenceManager(it) }!!

        example()
    }

    private fun example() {
        val database = FirebaseFirestore.getInstance()
        database.collection("example")
            .get()
            .addOnCompleteListener {
                val myUserId = preferenceManager.getString(Constants.KEY_USER_ID)
                Log.d("bk", "DoctorAppointment/example/addOnComplete ::")
                if (it.isSuccessful && it.result != null) {
                    Log.d("bk", "DoctorAppointment/example/addOnComplete/isSuccessful ::")
                    for (documentSnapshot: QueryDocumentSnapshot in it.result) {
                        Log.d("bk", "DoctorAppointment/example/addOnComplete/isSuccessful/documentSnapshot ::")
                        if (myUserId.equals(documentSnapshot.id)) {
                            Log.d("bk", "DoctorAppointment/example/addOnComplete/isSuccessful/documentSnapshot/equals ::")
                        }
                    }
                }

            }
    }


}