package com.dubd.rentlagbe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dubd.bdlocationchooser.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BdLocationChooser.Create(this)
            .setPickerTitle("Choose a location")
            .setLanguage("english")     // Default Bangla
            .setListener(object : LocationChooseListener{
                override fun onLocationChoose(
                    division: Division,
                    district: District,
                    upazila: Upazila
                ) {
                    Toast.makeText(this@MainActivity, division.name+","+district.name+","+upazila.name, Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled() {
                    Log.e("LocationPicker","Cancelled")
                }

            })
            .showDialog()
    }
}