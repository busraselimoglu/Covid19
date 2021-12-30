package com.example.covidd19.menu.assessment


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.covidd19.MainActivity
import com.example.covidd19.R
import com.example.covidd19.databinding.FragmentRiskAssessmentBinding
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class RiskAssessment : Fragment() {

    private var _binding : FragmentRiskAssessmentBinding?= null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager

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

        preferenceManager = context?.let { PreferenceManager(it) }!!

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


        binding.nextButton.setOnClickListener {
            radioCheckControl(it)
        }


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
            if((binding.pre4A.isChecked || binding.pre4B.isChecked || binding.pre4C.isChecked ||
                        binding.pre4D.isChecked || binding.pre4E.isChecked || binding.pre4F.isChecked ||
                        binding.pre4G.isChecked || binding.pre4H.isChecked || binding.pre4I.isChecked ||
                        binding.pre4J.isChecked ) && binding.pre5Answer.checkedRadioButtonId != -1 ){
                mScreen = healthScreen
                renderScreen()
            }else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        } else if (mScreen == healthScreen) {
            if (binding.health7Answer.checkedRadioButtonId != -1 && binding.health8Answer.checkedRadioButtonId != -1){
                if(binding.health7A.isChecked && (binding.health8A.isChecked || binding.health8C.isChecked)){
                    if(binding.health7Acancer.text.toString().trim().isNotEmpty()){
                        mScreen = medicationScreen
                        renderScreen()
                    }else{
                        binding.health7Acancer.visibility = View.VISIBLE
                        binding.health8Bquit.visibility = View.GONE
                        Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show()
                    }

                }else if (binding.health7B.isChecked && binding.health8B.isChecked){
                    if (binding.health8Bquit.text.toString().trim().isNotEmpty()){
                        mScreen = medicationScreen
                        renderScreen()
                    }else{
                        binding.health7Acancer.visibility = View.GONE
                        binding.health8Bquit.visibility = View.VISIBLE
                        Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show()
                    }

                }else if(binding.health7A.isChecked && binding.health8B.isChecked){
                    if (binding.health7Acancer.text.toString().trim().isNotEmpty() && binding.health8Bquit.text.toString().trim().isNotEmpty()){
                        mScreen = medicationScreen
                        renderScreen()
                    }else{
                        Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show()
                        binding.health7Acancer.visibility = View.VISIBLE
                        binding.health8Bquit.visibility = View.VISIBLE
                    }

                }else if( binding.health7B.isChecked && (binding.health8A.isChecked || binding.health8C.isChecked)){
                    binding.health7Acancer.visibility = View.GONE
                    binding.health8Bquit.visibility = View.GONE
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
                recordFirebase()
                Navigation.findNavController(view).navigate(R.id.action_riskAssessment_to_resultAssessment)
            } else{ Toast.makeText(context, "Answer all questions", Toast.LENGTH_SHORT).show() }
        }
    }

    private fun recordFirebase(){
        val database: FirebaseFirestore = FirebaseFirestore.getInstance()
        val question: HashMap<String, Any> = HashMap()

        val demo1Answer : RadioButton?  = activity?.findViewById(binding.demo1Answer.checkedRadioButtonId) as? RadioButton
        if (demo1Answer != null) { question["1.demo1"] = demo1Answer.text }


        question["1.demo8feet"] = binding.demo8feet.text.toString()
        question["1.demo8inches"] = binding.demo8inches.text.toString()
        question["1.demo9Ibs"] = binding.demo9Ibs.text.toString()
        question["1.demo10size"] = binding.demo10size.text.toString()

        if (binding.pre4A.isChecked){ question["2.pre4A"] = binding.pre4A.text.toString() } else { question["2.pre4A"]  = "null"}
        if (binding.pre4B.isChecked){ question["2.pre4B"] = binding.pre4B.text.toString() } else { question["2.pre4B"] = "null"}
        if (binding.pre4C.isChecked){ question["2.pre4C"] = binding.pre4C.text.toString() } else { question["2.pre4C"] = "null"}
        if (binding.pre4D.isChecked){ question["2.pre4D"] = binding.pre4D.text.toString() } else { question["2.pre4D"] = "null"}
        if (binding.pre4E.isChecked){ question["2.pre4E"] = binding.pre4E.text.toString() } else { question["2.pre4E"] = "null"}
        if (binding.pre4F.isChecked){ question["2.pre4F"] = binding.pre4F.text.toString() } else { question["2.pre4F"] = "null"}
        if (binding.pre4G.isChecked){ question["2.pre4G"] = binding.pre4G.text.toString() } else { question["2.pre4G"] = "null"}
        if (binding.pre4H.isChecked){ question["2.pre4H"] = binding.pre4H.text.toString() } else { question["2.pre4H"] = "null"}
        if (binding.pre4I.isChecked){ question["2.pre4I"] = binding.pre4I.text.toString() } else { question["2.pre4I"] = "null"}
        if (binding.pre4J.isChecked){ question["2.pre4J"] = binding.pre4J.text.toString() } else { question["2.pre4J"] = "null"}
        val pre5Answer : RadioButton?  = activity?.findViewById(binding.pre5Answer.checkedRadioButtonId) as? RadioButton
        if (pre5Answer != null) { question["2.pre5"] = pre5Answer.text }


        if (binding.health7A.isChecked){ question["3.health7A"] = binding.health7Acancer.text.toString() }
        else { question["3.health7B"]  = binding.health7B.text.toString()}

        if (binding.health8A.isChecked){ question["3.health8A"] = binding.health8A.text.toString() }
        else if (binding.health7A.isChecked){question["3.health8A"] = binding.health8Bquit.text.toString()}
        else { question["3.health7B"]  = binding.health8C.text.toString()}

        val medication1Answer : RadioButton?  = activity?.findViewById(binding.medication1Answer.checkedRadioButtonId) as? RadioButton
        if (medication1Answer != null) { question["4.medication1"] = medication1Answer.text }

        val medication2Answer : RadioButton?  = activity?.findViewById(binding.medication2Answer.checkedRadioButtonId) as? RadioButton
        if (medication2Answer != null) { question["4.medication2"] = medication2Answer.text }

        val medication3Answer : RadioButton?  = activity?.findViewById(binding.medication3Answer.checkedRadioButtonId) as? RadioButton
        if (medication3Answer != null) { question["4.medication1"] = medication3Answer.text }

        val medication4Answer : RadioButton?  = activity?.findViewById(binding.medication4Answer.checkedRadioButtonId) as? RadioButton
        if (medication4Answer != null) { question["4.medication1"] = medication4Answer.text }


        val risk1Answer : RadioButton?  = activity?.findViewById(binding.risk1Answer.checkedRadioButtonId) as? RadioButton
        if (risk1Answer != null) { question["5.risk1"] = risk1Answer.text }

        val risk2Answer : RadioButton?  = activity?.findViewById(binding.risk2Answer.checkedRadioButtonId) as? RadioButton
        if (risk2Answer != null) { question["5.risk2"] = risk2Answer.text }


        val diet1Answer : RadioButton?  = activity?.findViewById(binding.diet1Answer.checkedRadioButtonId) as? RadioButton
        if (diet1Answer != null) { question["6.diet1"] = diet1Answer.text }

        val diet2Answer : RadioButton?  = activity?.findViewById(binding.diet2Answer.checkedRadioButtonId) as? RadioButton
        if (diet2Answer != null) { question["6.diet2"] = diet2Answer.text }


        preferenceManager.getString(Constants.KEY_USER_ID)?.let {
            database.collection("Risk Assessment")
                .document(it)
                .set(question, SetOptions.merge())
                .addOnSuccessListener {
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
        }
    }

}