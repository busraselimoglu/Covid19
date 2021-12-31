package com.example.covidd19.menu.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidd19.databinding.FragmentDoctorAppointmentBinding
import com.example.covidd19.utilities.PreferenceManager


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

    }

}