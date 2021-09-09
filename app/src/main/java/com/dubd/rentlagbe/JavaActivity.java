package com.dubd.rentlagbe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dubd.bdlocationchooser.BdLocationChooser;
import com.dubd.bdlocationchooser.District;
import com.dubd.bdlocationchooser.Division;
import com.dubd.bdlocationchooser.LocationChooseListener;
import com.dubd.bdlocationchooser.Upazila;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new BdLocationChooser.Create(this)
                .setPickerTitle("Choose a location")
                .setLanguage("english")     // Default Bangla
                .setListener(new LocationChooseListener() {
                    @Override
                    public void onLocationChoose(@NonNull Division division, @NonNull District district, @NonNull Upazila upazila) {
                        Toast.makeText(JavaActivity.this, division.getName()+","+district.getName()+","+upazila.getName(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled() {
                        Log.e("LocationPicker","Cancelled");
                    }
                }).showDialog();
    }
}