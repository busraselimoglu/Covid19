package com.example.covidd19.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.covidd19.fragments.home.Appointment
import com.example.covidd19.fragments.home.MakeHospitalAppointment

internal class HomeAdapter(var context: Context, fm: FragmentManager, var totalTabs: Int) : FragmentPagerAdapter(fm) {

    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                MakeHospitalAppointment()
            }
            1 -> {
                Appointment()
            }

            else -> getItem(position)
        }
    }

}