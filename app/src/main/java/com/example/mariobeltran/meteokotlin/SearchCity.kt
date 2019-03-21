package com.example.mariobeltran.meteokotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult

class SearchCity : AppCompatActivity() {

    private val permisoFineLocation = android.Manifest.permission.ACCESS_FINE_LOCATION
    private val permisoCoarseLocation = android.Manifest.permission.ACCESS_COARSE_LOCATION

    private val REQUIRED_CODE = 103
    //let me know lat and long
    var fusedLocationClient: FusedLocationProviderClient? = null

    var locationRequest: LocationRequest? = null
    var callback: LocationCallback? = null
    var longitud = ""
    var latitud = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_city)

        fusedLocationClient = FusedLocationProviderClient(this)
        initLocationRequest()

        val entryCityName = findViewById<EditText>(R.id.entryCityName)
        val btnSearchCity = findViewById<Button>(R.id.btnSearchCity)
        val btnActualLocation = findViewById<Button>(R.id.btnActualLocation)

        btnSearchCity.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val nameCity = entryCityName.text.toString()
            intent.putExtra("nameCity", nameCity)
            startActivity(intent)
        }

        btnActualLocation.setOnClickListener {
            val intent = Intent(this, GpsWeatherActivity::class.java)
            intent.putExtra("latitud", latitud)
            intent.putExtra("longitud", longitud)
            startActivity(intent)
        }


          }

    private fun initLocationRequest(){//parameters to initializing
        locationRequest = LocationRequest()
        locationRequest?.interval = 10000
        locationRequest?.fastestInterval = 5000
        locationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY // acurracy  of location around 10mts is the highest
    }

    private fun checkLocationPermits():Boolean{//checking if the user give to the app the permission
        //to know if a permission is Granted o Denied
        val accurateLocation = ActivityCompat.checkSelfPermission(this,permisoFineLocation) == PackageManager.PERMISSION_GRANTED
        val ordinaryLocation = ActivityCompat.checkSelfPermission(this, permisoCoarseLocation) == PackageManager.PERMISSION_GRANTED

        return accurateLocation && ordinaryLocation
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
               callback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)

                for (ubicacion in locationResult?.locations!!){
                    latitud = ubicacion.latitude.toString()
                    longitud = ubicacion.longitude.toString()
                    //Toast.makeText(applicationContext, latitud +"#"+ longitud, Toast.LENGTH_LONG).show()
                }
            }
        }
        fusedLocationClient?.requestLocationUpdates(locationRequest, callback, null)

    }

    private fun infoPermit(){
        // to know if the client before was asked about permit to use the gps in the phone
        val rational = ActivityCompat.shouldShowRequestPermissionRationale(this, permisoFineLocation)

        if(rational){
            //send message with aditional explanation about to use gps in the phone
            requiredPermit()
        }else{
            requiredPermit()
        }

    }

    private fun requiredPermit(){
        requestPermissions(arrayOf(permisoFineLocation, permisoCoarseLocation),REQUIRED_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            REQUIRED_CODE ->{
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //get Location
                    getLocation()
                }else{
                    Toast.makeText(this, "No permission for gps location", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun stopUpdatingLocation(){
        fusedLocationClient?.removeLocationUpdates(callback)
    }

    override fun onStart() {
        super.onStart()
        if(checkLocationPermits()){
            getLocation()
        }else{
            infoPermit()
        }
    }

    override fun onPause() {
        super.onPause()
        stopUpdatingLocation()
    }

}//final brackets
