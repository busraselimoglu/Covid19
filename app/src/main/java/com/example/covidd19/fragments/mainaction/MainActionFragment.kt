package com.example.covidd19.fragments.mainaction

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidd19.R
import com.example.covidd19.databinding.FragmentMainActionBinding


class MainActionFragment : Fragment() {

    private var _binding: FragmentMainActionBinding ?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainActionBinding.inflate(inflater,container,false)
        return binding.root
    }


}