package com.example.mariobeltran.meteokotlin

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {
        //URL of OpenWeather
    internal val WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather"
    // App ID to use OpenWeather data
    internal val APP_ID = "5ba4d484715af9ad7475c60b870194fd"
    var gString : String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textCity = findViewById<TextView>(R.id.textCity)
        val textTemperature = findViewById<TextView>(R.id.textTemperature)
        val textConditionMeteo  = findViewById<TextView>(R.id.textConditionMeteo)
        val imagenClima = findViewById<ImageView>(R.id.imagenClima)
        val nameCity = intent.getStringExtra("nameCity")

        //textCity.setText(nameCity)

        if(Network.checkNet(this)){
            //Request HTTP Meteo
            val finalURL = "$WEATHER_URL?q=$nameCity&appid=$APP_ID"
            //requireHTTPVolley("http://api.openweathermap.org/data/2.5/weather?q=Bucaramanga&appid=5ba4d484715af9ad7475c60b870194fd")
            requireHTTPVolley(finalURL)

        }else{
            //Message not network
        }

    }
    //fun HTTP request with volley
    private fun requireHTTPVolley(url:String){
        val queue = Volley.newRequestQueue(this)
        val require = StringRequest(Request.Method.GET, url, Response.Listener<String>{
                response ->
            try {
                  Log.d("Clima", response)
                //Toast.makeText(this,response,Toast.LENGTH_SHORT).show()
                val json = JSONObject(response)
                val mCity = json.getString("name")
                //val mCondition = json.getJSONArray("weather").getJSONObject(0).getInt("id")
                val tempResult =
                    json.getJSONObject("main").getDouble("temp") - 273.15
                val roundedValue = Math.rint(tempResult).toInt()//paso de double a int
                val mTemperature = Integer.toString(roundedValue);
                val mCondition = json.getJSONArray("weather").getJSONObject(0).getInt("id")
                //weatherIcon(mCondition)
                //Toast.makeText(this,mCity,Toast.LENGTH_SHORT).show()
                //Toast.makeText(this,mCondition,Toast.LENGTH_SHORT).show()
                textCity.setText(mCity)
                textTemperature.setText(mTemperature)
                val resourceID = resources.getIdentifier(weatherIcon(mCondition), "drawable", packageName)
                imagenClima.setImageResource(resourceID)

            }catch (e: Exception){
            }
        }, Response.ErrorListener {  })
        queue.add(require)
    }

    private fun  weatherIcon(condition: Int): String{

        if (condition >= 0 && condition < 300) {
            return "tstorm1"
        } else if (condition >= 300 && condition < 500) {
            return "light_rain"
        } else if (condition >= 500 && condition < 600) {
            return "shower3"
        } else if (condition >= 600 && condition <= 700) {
            return "snow4"
        } else if (condition >= 701 && condition <= 771) {
            return "fog"
        } else if (condition >= 772 && condition < 800) {
            return "tstorm3"
        } else if (condition == 800) {
            return "sunny"
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy2"
        } else if (condition >= 900 && condition <= 902) {
            return "tstorm3"
        } else if (condition == 903) {
            return "snow5"
        } else if (condition == 904) {
            return "sunny"
        } else if (condition >= 905 && condition <= 1000) {
            return "tstorm3"
        }

        return "dunno"
            }

}
