package com.example.covidd19.fragments.assessment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidd19.R
import com.example.covidd19.databinding.FragmentAssessmentBinding


class AssessmentFragment : Fragment() {

    private var _binding: FragmentAssessmentBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAssessmentBinding.inflate(inflater,container,false)
        return binding.root
    }

}