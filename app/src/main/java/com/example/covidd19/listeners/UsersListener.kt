package com.example.covidd19.listeners

import com.example.covidd19.models.User

interface UsersListener {

    fun initiateVideoMeeting(user : User)

    fun initiateAudioMeeting(user: User)

}