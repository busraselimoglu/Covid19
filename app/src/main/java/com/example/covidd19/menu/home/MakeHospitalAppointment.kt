package com.example.covidd19.menu.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.covidd19.adapters.DoctorsAdapter
import com.example.covidd19.databinding.FragmentMakeHospitalAppointmentBinding
import com.example.covidd19.listeners.AppointmentInterface
import com.example.covidd19.models.User
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot


class MakeHospitalAppointment : Fragment(), AppointmentInterface {

    private var _binding: FragmentMakeHospitalAppointmentBinding ?= null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager
    private lateinit var doctorsAdapter: DoctorsAdapter
    private var users = ArrayList<User>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentMakeHospitalAppointmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = context?.let { PreferenceManager(it) }!!

        //Fragment is attached to a RecyclerView
        doctorsAdapter = DoctorsAdapter(users,this)
        binding.appointmentDoctor.adapter = doctorsAdapter

        binding.swipeRefreshLayout.setOnRefreshListener(this::getUsers)

        getUsers()


        example()
    }


    private fun getUsers() {
        binding.swipeRefreshLayout.isRefreshing = true
        val database = FirebaseFirestore.getInstance()
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get().addOnCompleteListener {
                binding.swipeRefreshLayout.isRefreshing = false
                val myUserId = preferenceManager.getString(Constants.KEY_USER_ID)
                if (it.isSuccessful && it.result != null) {
                    // Using swipe refresh layout, it can be called multiple times that's why I need to clear user list before adding new data
                    users.clear()

                    // Here, I will display the user list except for the currently signed-in user,
                    // because no one will have a meeting with himself. That's why I am excluding a signed-in user from the list
                    for (documentSnapshot: QueryDocumentSnapshot in it.result) {
                        if (myUserId.equals(documentSnapshot.id)) {
                            continue
                        }
                        val user = User()

                        user.other = documentSnapshot.getString(Constants.KEY_OTHER).toString()
                        if(user.other == "Doctor") {
                            Log.d("bk", "Other::::  " + user.other)
                            user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()
                            user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME).toString()
                            user.email = documentSnapshot.getString(Constants.KEY_EMAIL).toString()
                            user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN).toString()
                            user.id = documentSnapshot.getString(Constants.KEY_ID).toString()
                            users.add(user)
                        }

                    }
                    if (users.size > 0){
                        doctorsAdapter.notifyDataSetChanged()
                    } else{
                        binding.textErrorMessage.text = String.format("%s ", "No users available")
                        binding.textErrorMessage.visibility = View.VISIBLE
                    }

                } else {
                    binding.textErrorMessage.text = String.format("%s ", "No users available")
                    binding.textErrorMessage.visibility = View.VISIBLE
                }
            }
    }


    private fun example() {
        val database = FirebaseFirestore.getInstance()
        /*database.collection("example")
            .get()
            .addOnCompleteListener {
                val myUserId = preferenceManager.getString(Constants.KEY_USER_ID)
                Log.d("bk", "MakeHospitalAppointment/example/addOnComplete ::")
                if (it.isSuccessful && it.result != null) {
                    Log.d("bk", "MakeHospitalAppointment/example/addOnComplete/isSuccessful ::")
                    for (documentSnapshot: QueryDocumentSnapshot in it.result) {
                        Log.d("bk", "MakeHospitalAppointment/example/addOnComplete/isSuccessful/documentSnapshot ::")
                        if (myUserId.equals(documentSnapshot.id)) {
                            Log.d("bk", "MakeHospitalAppointment/example/addOnComplete/isSuccessful/documentSnapshot/equals ::")
                        }
                    }
                }

            }*/

        //.collection("Dec 24, 2021")
        /*val collections : Iterable<CollectionReference> =
            listOf(database.collection("example").document("54123674123").collection("Dec 24, 2021"))
        for (collRef: CollectionReference in collections){
            Log.d("bk","Found subcollection with id: "+collRef.id)
        }*/
    }

    override fun appointmentDate(user: User) {
        val intent = Intent(activity!!.applicationContext, DateSelect::class.java)
        intent.putExtra("userid", user.id)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}