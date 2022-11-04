package com.example.speedtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //Fortwnw tin vasi
    //mMap.addMarker(Lat,Long)
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        try{
            db = openOrCreateDatabase("GPS_DB",MODE_PRIVATE,null);
        }catch(Exception e){
            Toast.makeText(this,String.valueOf(e.getMessage()),Toast.LENGTH_LONG);
        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    double longtitude,latitude;
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Cursor cursor = db.rawQuery("SELECT LAT,LONG FROM GPS_DATA",null);
        if(cursor != null && cursor.moveToFirst()){
            do{
                latitude = cursor.getDouble(0);
                longtitude = cursor.getDouble(1);
                LatLng myLatLong = new LatLng(latitude,longtitude);
                mMap.addMarker(new MarkerOptions().position(myLatLong).title("Speed limit passed!"));
            }while (cursor.moveToNext());
        }



        // Add a marker in Sydney and move the camera
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLong));
        //LatLng sydney = new LatLng();
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));

    }
}
