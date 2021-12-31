package com.example.covidd19.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.covidd19.databinding.ItemdoctoruserBinding
import com.example.covidd19.listeners.AppointmentInterface
import com.example.covidd19.models.User

class DoctorsAdapter(private var userDoctor: ArrayList<User>, private var appointmentInterface: AppointmentInterface) : RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder>() {

    class DoctorViewHolder(val itemBinding: ItemdoctoruserBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        return DoctorViewHolder(ItemdoctoruserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int = userDoctor.size

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        with(holder){
            itemBinding.textUserName.text = String.format("%s %s ",userDoctor[position].firstName,userDoctor[position].lastName)
            itemBinding.textEmail.text = userDoctor[position].email
            itemBinding.nextImage.setOnClickListener {appointmentInterface.appointmentDate(userDoctor[position])}

        }
    }




}