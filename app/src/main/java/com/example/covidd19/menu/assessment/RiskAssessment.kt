package com.example.covidd19.menu.assessment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.covidd19.R
import com.example.covidd19.databinding.FragmentRiskAssessmentBinding


class RiskAssessment : Fragment() {

    private var _binding : FragmentRiskAssessmentBinding?= null
    private val binding get() = _binding!!

    private var mScreen = 0

    private var demoScreen = 0
    private var previousScreen = 1
    private var healthScreen = 2
    private var medicationScreen = 3
    private var riskScreen = 4
    private var dietScreen = 5


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentRiskAssessmentBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Navigation
        binding.imageBackDemo.setOnClickListener { Navigation.findNavController(it).navigate(R.id.action_riskAssessment_to_assessmentFragment) }
        binding.imageBackPrevious.setOnClickListener {
            mScreen = demoScreen
            renderScreen()
        }
        binding.imageBackHealth.setOnClickListener {
            mScreen = previousScreen
            renderScreen()
        }
        binding.imageBackMedication.setOnClickListener {
            mScreen = healthScreen
            renderScreen()
        }
        binding.imageBackRisk.setOnClickListener {
            mScreen = medicationScreen
            renderScreen()
        }
        binding.imageBackDiet.setOnClickListener {
            mScreen = riskScreen
            renderScreen()
        }

        /*if(mScreen == previousScreen){
            if (binding.pre6A.isChecked){
                binding.pre6Along.visibility = View.VISIBLE
                binding.pre6Bsymptom.visibility = View.GONE
            }
            if (binding.pre6B.isChecked) {
                binding.pre6Bsymptom.visibility = View.VISIBLE
                binding.pre6Along.visibility = View.GONE
            }
        }*/

        binding.nextButton.setOnClickListener {
            radioCheckControl(it)
            //renderScreen()


            val e : RadioButton  = activity?.findViewById(binding.demo1Answer.checkedRadioButtonId) as RadioButton
            println("e:::: "+ e.text)
        }

        /*binding.demo1Answer.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.demo1A -> Toast.makeText(context, "Checked: " + binding.demo1A.text, Toast.LENGTH_SHORT).show()
                R.id.demo1B -> Toast.makeText(context, "Checked: " + checkedId, Toast.LENGTH_SHORT).show()
                R.id.demo1C -> Toast.makeText(context, "Checked: " + checkedId, Toast.LENGTH_SHORT).show()
                R.id.demo1D -> Toast.makeText(context, "Checked: " + checkedId, Toast.LENGTH_SHORT).show()
                R.id.demo1E -> Toast.makeText(context, "Checked: " + checkedId, Toast.LENGTH_SHORT).show()
                R.id.demo1F -> Toast.makeText(context, "Checked: " + checkedId, Toast.LENGTH_SHORT).show()
            }
        }*/

        /*binding.demo1Answer.setOnCheckedChangeListener { group, checkedId ->
            val radioButton : RadioButton  = activity?.findViewById(checkedId) as RadioButton
            Toast.makeText(context, radioButton.text, Toast.LENGTH_SHORT).show()
        }*/



        // Default screen
        mScreen = demoScreen
        renderScreen()
    }

    private fun renderScreen(){
        binding.demographics.visibility = if (mScreen == demoScreen) View.VISIBLE else View.GONE
        binding.previousExposure.visibility = if (mScreen == previousScreen) View.VISIBLE else View.GONE
        binding.healthPrecondition.visibility = if (mScreen == healthScreen) View.VISIBLE else View.GONE
        binding.medicationSupplements.visibility = if (mScreen == medicationScreen) View.VISIBLE else View.GONE
        binding.riskReduction.visibility = if (mScreen == riskScreen) View.VISIBLE else View.GONE
        binding.dietLifestyle.visibility = if (mScreen == dietScreen) View.VISIBLE else View.GONE
    }

    private fun radioCheckControl(view: View){
        if (mScreen == demoScreen){
            if (binding.demo1Answer.checkedRadioButtonId != -1 && binding.demo8feet.text.toString().trim().isNotEmpty() &&
                binding.demo8inches.text.toString().trim().isNotEmpty() && binding.demo9Ibs.text.toString().trim().isNotEmpty() &&
                binding.demo10size.text.toString().trim().isNotEmpty()){
                mScreen = previousScreen
                renderScreen()
            }else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        } else if(mScreen == previousScreen){
            if (binding.pre6A.isChecked){
                binding.pre6Along.visibility = View.VISIBLE
                binding.pre6Bsymptom.visibility = View.GONE
            }
            if (binding.pre6B.isChecked) {
                binding.pre6Bsymptom.visibility = View.VISIBLE
                binding.pre6Along.visibility = View.GONE
            }
            if(binding.pre4Answer.checkedRadioButtonId != -1 && binding.pre5Answer.checkedRadioButtonId != -1 &&
                (binding.pre6Along.text.toString().trim().isNotEmpty() || binding.pre6Bsymptom.text.toString().trim().isNotEmpty()) ){
                mScreen = healthScreen
                renderScreen()
            }else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        } else if (mScreen == healthScreen) {
            /*if (binding.health7A.isChecked) binding.health7Acancer.visibility = View.VISIBLE
            if (binding.health7B.isChecked) binding.health7Acancer.visibility = View.GONE
            if (binding.health8B.isChecked) binding.health8Bquit.visibility = View.VISIBLE
            if (binding.health8A.isChecked || binding.health8C.isChecked) binding.health8Bquit.visibility = View.GONE*/
            if (binding.health7Answer.checkedRadioButtonId != -1 && binding.health8Answer.checkedRadioButtonId != -1){
                if (binding.health7A.isChecked){
                    binding.health7Acancer.visibility = View.VISIBLE
                    if (binding.health7Acancer.text.toString().trim().isNotEmpty()){
                        mScreen = medicationScreen
                        renderScreen()
                    } else Toast.makeText(context, "Answer cancer questions", Toast.LENGTH_SHORT).show()
                } else if (binding.health7B.isChecked){
                    binding.health7Acancer.visibility = View.GONE
                    mScreen = medicationScreen
                    renderScreen()
                }

                if (binding.health8B.isChecked){
                    binding.health8Bquit.visibility = View.VISIBLE
                    if (binding.health8Bquit.text.toString().trim().isNotEmpty()){
                        mScreen = medicationScreen
                        renderScreen()
                    } else{
                        Toast.makeText(context, "Answer quit questions", Toast.LENGTH_SHORT).show()
                    }
                } else if (binding.health8A.isChecked || binding.health8C.isChecked){
                    binding.health7Acancer.visibility = View.GONE
                    mScreen = medicationScreen
                    renderScreen()
                }

            } else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        } else if (mScreen == medicationScreen){
            if(binding.medication1Answer.checkedRadioButtonId != -1 && binding.medication2Answer.checkedRadioButtonId != -1 &&
                binding.medication3Answer.checkedRadioButtonId != -1 && binding.medication4Answer.checkedRadioButtonId != -1 ){
                mScreen = riskScreen
                renderScreen()
            } else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        }else if (mScreen == riskScreen){
            if(binding.risk1Answer.checkedRadioButtonId != -1 && binding.risk2Answer.checkedRadioButtonId != -1){
                mScreen = dietScreen
                renderScreen()
            } else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        } else if (mScreen == dietScreen){
            if(binding.diet1Answer.checkedRadioButtonId != -1 && binding.diet2Answer.checkedRadioButtonId != -1){
                Navigation.findNavController(view).navigate(R.id.action_riskAssessment_to_resultAssessment)
            } else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        }
    }

}