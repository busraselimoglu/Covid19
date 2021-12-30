package com.example.covidd19.menu.mainaction

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.covidd19.R
import com.example.covidd19.databinding.FragmentMainActionBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*


class MainActionFragment : Fragment() , OnMapReadyCallback {

    private var _binding: FragmentMainActionBinding?= null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager : LocationManager
    private lateinit var locationListener: LocationListener

    private val haseki = LatLng(41.010067633003054,28.944380336423986)
    private val cerrah = LatLng(41.006319116164256,28.93970256395163)
    private val capa = LatLng(41.01382359850377,28.93424721473574)
    private val capaGogus = LatLng(41.017465298692805,28.93368588576598)
    private val yedikule = LatLng(41.00239876355557, 28.91509934631578)
    private val bezmi = LatLng(41.01893319238704, 28.93581073081761)

    private var markerHaseki : Marker ?= null
    private var markerCerrah : Marker ?= null
    private var markerCapa : Marker ?= null
    private var markerCapaGogus : Marker ?= null
    private var markerYedikule : Marker ?= null
    private var markerBezmi : Marker ?= null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentMainActionBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


    }

    // method definition
    private fun getMarkerIcon(color: String?): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(Color.parseColor(color), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMapLongClickListener(listener)


        locationManager = activity!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = LocationListener { location ->
            // Setting Current Location
            mMap.clear() // marker is clear
            val currentLocation = LatLng(location.latitude,location.longitude)
            mMap.addMarker(MarkerOptions().position(currentLocation).title("Current Location").icon(getMarkerIcon("#ff2299")))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,13f))

            markerHaseki = googleMap.addMarker(MarkerOptions().position(haseki).title("Haseki Eğitim ve Araştırma Hastanesi"))
            markerCerrah = googleMap.addMarker(MarkerOptions().position(cerrah).title("Cerrahpaşa Hastanesi"))
            markerCapa = googleMap.addMarker(MarkerOptions().position(capa).title("Özel Çapa Hastanesi"))
            markerCapaGogus = googleMap.addMarker(MarkerOptions().position(capaGogus).title("Özel Çapa Göğüs Hastalıkları Hastanesi"))
            markerYedikule = googleMap.addMarker(MarkerOptions().position(yedikule).title("Yedikule Göğüs Hastalıkları Hastanesi"))
            markerBezmi = googleMap.addMarker(MarkerOptions().position(bezmi).title("Bezmialem Vakıf Üniversitesi Tıp Fakültesi Hastanesi"))

        }

        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // if not allowed
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)

            // last known location
            val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (lastLocation != null){
                val lastLatLng = LatLng(lastLocation.latitude,lastLocation.longitude)
                mMap.addMarker(MarkerOptions().position(lastLatLng).title("Last Known Location").icon(getMarkerIcon("#ff2299")))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng,15f))
            }
            markerHaseki = googleMap.addMarker(MarkerOptions().position(haseki).title("Haseki Eğitim ve Araştırma Hastanesi"))
            markerCerrah = googleMap.addMarker(MarkerOptions().position(cerrah).title("Cerrahpaşa Hastanesi"))
            markerCapa = googleMap.addMarker(MarkerOptions().position(capa).title("Özel Çapa Hastanesi"))
            markerCapaGogus = googleMap.addMarker(MarkerOptions().position(capaGogus).title("Özel Çapa Göğüs Hastalıkları Hastanesi"))
            markerYedikule = googleMap.addMarker(MarkerOptions().position(yedikule).title("Yedikule Göğüs Hastalıkları Hastanesi"))
            markerBezmi = googleMap.addMarker(MarkerOptions().position(bezmi).title("Bezmialem Vakıf Üniversitesi Tıp Fakültesi Hastanesi"))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && ContextCompat.checkSelfPermission(context!!,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            // allowed
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1f,locationListener)

        }else{
            Toast.makeText(context, "You need to allow", Toast.LENGTH_SHORT).show()
            requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
    }

    private val listener = GoogleMap.OnMapLongClickListener { latLng ->
        mMap.clear()

        val geocoder = Geocoder(context, Locale.getDefault())

        if (latLng != null){
            var addressLoc = ""
            try {
                val addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1)
                if (addressList.size > 0){
                    if (addressList[0].thoroughfare != null){
                        addressLoc += addressList[0].thoroughfare
                        if (addressList[0].subThoroughfare != null){
                            addressLoc += addressList[0].subThoroughfare
                        }
                    }
                }
            }catch (e: Exception){
                println(e.message)
            }
            mMap.addMarker(MarkerOptions().position(latLng).title(addressLoc))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}