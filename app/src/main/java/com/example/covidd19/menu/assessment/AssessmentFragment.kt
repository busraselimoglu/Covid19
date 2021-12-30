package com.example.covidd19.menu.assessment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.covidd19.R
import com.example.covidd19.databinding.FragmentAssessmentBinding


class AssessmentFragment : Fragment() {

    private var _binding: FragmentAssessmentBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentAssessmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Navigation
        binding.riskIcon.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_assessmentFragment_to_riskAssessment) }
        binding.riskTitle.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_assessmentFragment_to_riskAssessment) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}