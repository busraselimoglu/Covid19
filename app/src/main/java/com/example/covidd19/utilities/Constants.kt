package com.example.covidd19.utilities

object Constants {
    const val KEY_COLLECTION_USERS = "users"
    const val KEY_FIRST_NAME = "first_name"
    const val KEY_LAST_NAME = "last_name"
    const val KEY_EMAIL = "email"
    const val KEY_PASSWORD = "password"
    const val KEY_ID = "id"
    const val KEY_OTHER = "other"
    const val KEY_USER_ID = "user_id"
    const val KEY_FCM_TOKEN = "fcm_token"

    const val KEY_PREFERENCE_NAME = "videoMeetingPreference"
    const val KEY_IS_SIGNED_IN = "isSignedIn"

    private const val REMOTE_MSG_AUTHORIZATION = "Authorization"
    private const val REMOTE_MSG_CONTENT_TYPE = "Content-Type"

    const val REMOTE_MSG_TYPE = "type"
    const val REMOTE_MSG_INVITATION = "invitation"
    const val REMOTE_MSG_MEETING_TYPE = "meetingType"
    const val REMOTE_MSG_INVITER_TOKEN = "inviterToken"
    const val REMOTE_MSG_DATA = "data"
    const val REMOTE_MSG_REGISTRATION_IDS = "registration_ids"

    const val REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse"

    const val REMOTE_MSG_INVITATION_ACCEPTED = "accepted"
    const val REMOTE_MSG_INVITATION_REJECTED = "rejected"
    const val REMOTE_MSG_INVITATION_CANCELLED = "cancelled"

    const val REMOTE_MSG_MEETING_ROOM = "meeting_room"

    const val KEY_COLLECTION_APPOINTMENT = "appointment"
    const val KEY_DATE = "date"
    const val KEY_CLOCK= "clock"


    fun getRemoteMessageHeaders(): HashMap<String, String> {
        val headers: HashMap<String, String> = HashMap()
        headers[REMOTE_MSG_AUTHORIZATION] = "key=AAAAhzP84-Y:APA91bE_DSH0CkqOz2hSP5OLeD2f6MGFr6XM2PFUDQ_ymGZMgaD0fF4tl6cssPcrYjkJZZ8LWFAHWXczd3ZhRx4NoMAc9uDXyK7k_xxxTdM8SmHCXOc3-tR1MEIwYNbJ5t3dHu5GsSfX"
        headers[REMOTE_MSG_CONTENT_TYPE] = "application/json"
        return headers
    }

}