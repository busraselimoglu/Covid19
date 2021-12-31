package com.example.covidd19.menu.assessment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidd19.databinding.FragmentResultAssessmentBinding



class ResultAssessment : Fragment() {

    private var _binding : FragmentResultAssessmentBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentResultAssessmentBinding.inflate(inflater,container,false)
        return binding.root
    }


}