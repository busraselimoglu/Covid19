package com.example.covidd19.menu.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.covidd19.activities.OutgoingInvitationActivity
import com.example.covidd19.adapters.UsersAdapter
import com.example.covidd19.databinding.FragmentAppointmentBinding
import com.example.covidd19.listeners.UsersListener
import com.example.covidd19.models.User
import com.example.covidd19.utilities.Constants
import com.example.covidd19.utilities.PreferenceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.messaging.FirebaseMessaging

class Appointment : Fragment(), UsersListener {

    private var _binding: FragmentAppointmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var preferenceManager: PreferenceManager
    private var users = ArrayList<User>()
    private lateinit var usersAdapter: UsersAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentAppointmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preferenceManager = context?.let { PreferenceManager(it) }!!

        // Send FCM token database
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful && task.result != null) {
                sendFCMTokenDatabase(task.result)
            }
        }

        //Fragment is attached to a RecyclerView
        usersAdapter = UsersAdapter(users,this)
        //binding.usersRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.usersRecyclerView.adapter = usersAdapter

        binding.swipeRefreshLayout.setOnRefreshListener(this::getUsers)

        getUsers()

    }

    private fun sendFCMTokenDatabase(token: String) {
        val database = FirebaseFirestore.getInstance()
        val documentReference = database.collection(Constants.KEY_COLLECTION_USERS)
            .document(preferenceManager.getString(Constants.KEY_USER_ID)!!)

        documentReference.update(Constants.KEY_FCM_TOKEN, token)
            .addOnFailureListener {
                Toast.makeText(context, "Unable to send token: " + it.message, Toast.LENGTH_SHORT).show()
            }
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
                        user.firstName = documentSnapshot.getString(Constants.KEY_FIRST_NAME).toString()
                        user.lastName = documentSnapshot.getString(Constants.KEY_LAST_NAME).toString()
                        user.email = documentSnapshot.getString(Constants.KEY_EMAIL).toString()
                        user.token = documentSnapshot.getString(Constants.KEY_FCM_TOKEN).toString()
                        user.other = documentSnapshot.getString(Constants.KEY_OTHER).toString()
                        users.add(user)
                    }
                    if (users.size > 0){
                        usersAdapter.notifyDataSetChanged()
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

    override fun initiateVideoMeeting(user: User) {
        if (user.token == "null" || user.token.trim().isEmpty() ){
            Toast.makeText(context, user.firstName + " " + user.lastName + " is not available for meeting", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(activity!!.applicationContext, OutgoingInvitationActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("type","video")
            startActivity(intent)
        }
    }

    override fun initiateAudioMeeting(user: User) {
        if (user.token == "null" || user.token.trim().isEmpty() ){
            Toast.makeText(context, user.firstName + " " + user.lastName + " is not available for meeting", Toast.LENGTH_SHORT).show()
        }else{
            val intent = Intent(activity!!.applicationContext, OutgoingInvitationActivity::class.java)
            intent.putExtra("user", user)
            intent.putExtra("type","video")
            startActivity(intent)
        }
    }

}