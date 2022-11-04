package com.example.speedtracker;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private TextView txtView;
    private Button btnOn;
    private Button btnOff;
    private Button talkBtn;
    private LocationManager manager;
    private SQLiteDatabase database;
    private Queryhandle qhandler;
    private SharedPreferences preferences;
    private MyTextToSpeech myTextToSpech;
    private int speed;
    private int counter = 0;
    private static final String TITLE = "Say show map or show data or set preferences!";
    private static final int REQ_CODE = 684;
    private static final String RADIO_RED = "red";
    private static final String RADIO_ORANGE = "orange";
    private static final String RADIO_MPH = "miles";
    private static final String RADIO_KMH = "kilometers";
    private static final String SPEED_LIMIT = "limit";
    private static final String SHARED_PREFS = "SharedPreferences";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        txtView = findViewById(R.id.textView2);
        txtView.setTextColor(Color.BLACK);
        btnOff = findViewById(R.id.gpsOff);
        btnOn = findViewById(R.id.gpsOn);
        talkBtn = findViewById(R.id.talkButton);
        myTextToSpech = new MyTextToSpeech(this);
        database = openOrCreateDatabase("GPS_DB",MODE_PRIVATE,null);
        qhandler = new Queryhandle(database);
        qhandler.initDB();
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
    }

    public void gpsStart(View view) {

        //Start the GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQ_CODE);
        }
        else {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
            Toast.makeText(this, "GPS ACTIVATED!", Toast.LENGTH_SHORT).show();
        }



    }

    public void gpsStop(View view){
        //Stop the gps
        manager.removeUpdates(this);
        Toast.makeText(this,"GPS DEACTIVATED",Toast.LENGTH_SHORT).show();
        txtView.setText("-||- km/h");
        txtView.setTextColor(Color.BLACK);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==742 && resultCode==RESULT_OK){
            ArrayList<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);

            for (String s : results) {
                Intent intent;
                switch (s){

                    case "show map" :
                        intent = new Intent(this,MapActivity.class);
                        startActivity(intent);
                        break;
                    case "show data":
                        intent = new Intent(this,ShowActivity.class);
                        startActivity(intent);
                        break;
                    case "set preferences":
                        intent = new Intent(this,SetPrefsActivity.class);
                        startActivity(intent);
                        break;

                }

            }

        }
    }
    //SpeechToText
    //if premissions not granted
    //Ask for permissions
    //if user accepts create the intent
    //Next time user will not be asked!
    public void onClickTalk(View view){
    if(ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, REQ_CODE);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,TITLE);
            startActivityForResult(intent,742);
        }
    }else{
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,TITLE);
        startActivityForResult(intent,742);
    }

    }

    @Override
    public void onLocationChanged(Location location) {
        //Speed show..
        //Write data to DB (SQLite)
        if(location != null) {
            //Write on the db
            //The date will be written date and hour
            // m/s to km/h
            speed = (int) ((location.getSpeed() * 3600) / 1000);
            Datehandler datehandler = new Datehandler();
            datehandler.setStamp(System.currentTimeMillis());
            if(speed > preferences.getInt(SPEED_LIMIT,80)){
                //Write on the db
                //Only when the limit is passed!
                //Speed is in metric system only!
                qhandler.insertDB(datehandler.stampToDayWeek(), datehandler.stampToDayMonth(), datehandler.stampToMonth(),
                        datehandler.stampToYear(), datehandler.stampToHour(), speed, location.getLatitude(), location.getLongitude());
                //Don't speak all the time
                counter = counter + 1;
                if(counter == 1){
                    myTextToSpech.speak("SLOW DOWN");
                }
                //Set color for the textView...
                if(preferences.getBoolean(RADIO_RED,true)){
                    txtView.setTextColor(Color.RED);
                }
                else if(preferences.getBoolean(RADIO_ORANGE,false)){
                    txtView.setTextColor(Color.rgb(219,94,9));
                }
            }else{
               //Colored only when the limit is passed!
                txtView.setTextColor(Color.BLACK);
                //Don't speak all the time
                counter = 0;
            }
            //Miles or meters?
            //Only for the view!
            if(preferences.getBoolean(RADIO_KMH,true)){
                txtView.setText(speed + " km/h");
            }else if(preferences.getBoolean(RADIO_MPH,false)){

                txtView.setText((int) (speed * 0.62) + "mp/h");
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch ((item.getItemId())){
            case R.id.map:
                intent = new Intent(this,MapActivity.class);
                startActivity(intent);
                Toast.makeText(this,"Show Map!",Toast.LENGTH_SHORT).show();
                break;
            case R.id.prefs:
                intent = new Intent(this,SetPrefsActivity.class);
                startActivity(intent);
                break;
            case R.id.data:
                intent = new Intent(this,ShowActivity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }









    @Override
    public void onClick(View v) {

    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


}