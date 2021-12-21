package com.example.covidd19.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.covidd19.R
import com.example.covidd19.databinding.ActivityIncomingInvitationBinding
import com.example.covidd19.network.ApiClient
import com.example.covidd19.network.ApiService
import com.example.covidd19.utilities.Constants
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.URL


class IncomingInvitationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIncomingInvitationBinding

    private var meetingType: String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIncomingInvitationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        meetingType = intent.getStringExtra(Constants.REMOTE_MSG_MEETING_TYPE)

        // Show icon
        if (meetingType != null){
            if (meetingType == "video"){
                binding.imageMeetingType.setImageResource(R.drawable.ic_video)
            }else{
                binding.imageMeetingType.setImageResource(R.drawable.ic_audio)
            }
        }

        val firstName = intent.getStringExtra(Constants.KEY_FIRST_NAME)
        if (firstName != null) { binding.textFirstCharIncoming.text = firstName.substring(0, 1) }

        binding.textUsernameIncoming.text = String.format("%s %s ", firstName, intent.getStringExtra(Constants.KEY_LAST_NAME))
        binding.textEmailIncoming.text = intent.getStringExtra(Constants.KEY_EMAIL)

        binding.imageAcceptInvitation.setOnClickListener {
            sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_ACCEPTED,intent.getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN).toString())
        }
        binding.imageRejectInvitation.setOnClickListener {
            sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_REJECTED,intent.getStringExtra(Constants.REMOTE_MSG_INVITER_TOKEN).toString())
        }

    }

    private fun sendInvitationResponse(typeIncoming: String, receiverTokens: String){
        try {
            val tokens = JSONArray()
            tokens.put(receiverTokens)

            val body = JSONObject()
            val data = JSONObject()

            data.put(Constants.REMOTE_MSG_TYPE,Constants.REMOTE_MSG_INVITATION_RESPONSE)
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE,typeIncoming)

            body.put(Constants.REMOTE_MSG_DATA,data)
            body.put(Constants.REMOTE_MSG_REGISTRATION_IDS,tokens)

            sendRemoteMessage(body.toString(),typeIncoming)

        }catch (e:Exception){
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun sendRemoteMessage(remoteMessageBody: String, type: String) {
        val apiClient = ApiClient().getClient()
        apiClient!!.create(ApiService::class.java).sendRemoteMessage(
            Constants.getRemoteMessageHeaders(), remoteMessageBody
        ).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful){
                    if (type == Constants.REMOTE_MSG_INVITATION_ACCEPTED){
                        try {
                            //Jitsi Meet connect
                            val serverURL = URL("https://meet.jit.si")
                            val builder = JitsiMeetConferenceOptions.Builder()
                                .setServerURL(serverURL)
                                .setWelcomePageEnabled(false)
                                .setRoom(intent.getStringExtra(Constants.REMOTE_MSG_MEETING_ROOM))
                            if (meetingType == "audio"){
                                builder.setVideoMuted(true)
                            }
                            JitsiMeetActivity.launch(this@IncomingInvitationActivity,builder.build())
                            finish()
                        }catch (e : Exception){
                            Toast.makeText(this@IncomingInvitationActivity, e.message, Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }else{
                        Toast.makeText(this@IncomingInvitationActivity, "Invitation Rejected", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }else{
                    Toast.makeText(this@IncomingInvitationActivity, response.message(), Toast.LENGTH_SHORT).show()
                    finish()
                }

            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Toast.makeText(this@IncomingInvitationActivity, t.message, Toast.LENGTH_SHORT).show()
                finish()
            }
        })
    }

    private val invitationResponseReceiver : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val type : String? = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE)
            type?.let {
                if (type == Constants.REMOTE_MSG_INVITATION_CANCELLED) {
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(
            invitationResponseReceiver,
            IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE)
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(applicationContext).unregisterReceiver(invitationResponseReceiver)
    }
}