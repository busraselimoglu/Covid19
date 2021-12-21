package com.example.covidd19.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.covidd19.R
import com.example.covidd19.models.User

class UsersAdapter(private var users: ArrayList<User>): RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var textFirstChar: TextView = itemView.findViewById(R.id.textFirstChar)
        var textUserName: TextView = itemView.findViewById(R.id.textUserName)
        var textEmail: TextView = itemView.findViewById(R.id.textEmail)
        var imageAudioMeeting: ImageView = itemView.findViewById(R.id.imageAudioMeeting)
        var imageVideoMeeting: ImageView = itemView.findViewById(R.id.imageVideoMeeting)

        fun setUserData(user: User){
            textFirstChar.text = user.firstName.subSequence(0,1)
            textUserName.text = String.format("%s %s ",user.firstName,user.lastName)
            textEmail.text = user.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_container_user,parent,false))
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.setUserData(users[position])
    }


}