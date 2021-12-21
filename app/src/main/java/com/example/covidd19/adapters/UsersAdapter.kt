package com.example.covidd19.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidd19.R
import com.example.covidd19.databinding.ItemcontainersserBinding
import com.example.covidd19.listeners.UsersListener
import com.example.covidd19.models.User

class UsersAdapter(private var users: ArrayList<User>, private var usersListener: UsersListener): RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(val itemBinding: ItemcontainersserBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(ItemcontainersserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        with(holder){
            itemBinding.textFirstChar.text = users[position].firstName.substring(0,1)
            itemBinding.textUserName.text = String.format("%s %s",users[position].firstName,users[position].lastName)
            itemBinding.textEmail.text = users[position].email
            itemBinding.imageAudioMeeting.setOnClickListener { usersListener.initiateAudioMeeting(users[position]) }
            itemBinding.imageVideoMeeting.setOnClickListener { usersListener.initiateVideoMeeting(users[position]) }

        }

    }


}