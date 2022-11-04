package com.example.speedtracker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SetPrefsActivity extends AppCompatActivity {
    private RadioButton radioRed,radioOrange,radioMph,radioKmh;
    private RadioGroup radioGroupSpeed,radioGroupColor;
    private EditText integerText;
    private Button saveBtn;

    private static final String RADIO_RED = "red";
    private static final String RADIO_ORANGE = "orange";
    private static final String RADIO_MPH = "miles";
    private static final String RADIO_KMH = "kilometers";
    private static final String SPEED_LIMIT = "limit";
    private static final String SHARED_PREFS = "SharedPreferences";
    private int speedlimit;
    private SharedPreferences preferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        radioGroupSpeed = findViewById(R.id.RadioGroupSpeed);
        radioGroupColor = findViewById(R.id.RadioGroupColor);
        radioRed = findViewById(R.id.alertRed);
        radioOrange = findViewById(R.id.alertOrange);
        radioKmh = findViewById(R.id.KillometersUOS);
        radioMph = findViewById(R.id.millesUOS);
        integerText = findViewById(R.id.integerText);
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        saveBtn = findViewById(R.id.save_btn);
        loadData();

    }





    public void saveData(View view){

        SharedPreferences.Editor editor = preferences.edit();
        try{
            speedlimit = Integer.parseInt(integerText.getText().toString());
        }catch(ArithmeticException arithmetic){
            Toast.makeText(this,String.valueOf(arithmetic.getMessage()),Toast.LENGTH_SHORT).show();
        }
        editor.putInt(SPEED_LIMIT,Integer.parseInt(integerText.getText().toString()));
        editor.putBoolean(RADIO_RED,radioRed.isChecked());
        editor.putBoolean(RADIO_ORANGE,radioOrange.isChecked());
        editor.putBoolean(RADIO_KMH,radioKmh.isChecked());
        editor.putBoolean(RADIO_MPH,radioMph.isChecked());
        editor.commit();
        Toast.makeText(this,"DONE!",Toast.LENGTH_SHORT).show();

    }

    public void loadData(){

        radioRed.setChecked(preferences.getBoolean(RADIO_RED,true));
        radioOrange.setChecked(preferences.getBoolean(RADIO_ORANGE,false));
        radioKmh.setChecked(preferences.getBoolean(RADIO_KMH,true));
        radioMph.setChecked(preferences.getBoolean(RADIO_MPH,false));
        integerText.setText(String.valueOf(preferences.getInt(SPEED_LIMIT,0)));

    }


}
