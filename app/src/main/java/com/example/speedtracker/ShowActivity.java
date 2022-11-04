package com.example.speedtracker;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;



public class ShowActivity extends AppCompatActivity {

    private final static int SELECT_ALL = 1;
    private final static int WEEKLY = 2;
    private final static int MONTHLY = 4;
    private final static int YEARLY = 6;
    private Datehandler datehandler;
    private RadioGroup radioGroup;
    private RadioButton radioWeek,radioYear,radioMonth,radioAll;
    private TextView txtView;
    private Queryhandle querry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        datehandler = new Datehandler();
        txtView = findViewById(R.id.textView);
        radioMonth = findViewById(R.id.radioButtonMonth);
        radioWeek = findViewById(R.id.radioBtnWeek);
        radioYear = findViewById(R.id.radioBtnYear);
        radioAll = findViewById(R.id.radioButtonAll);
        radioGroup = findViewById(R.id.radioGroup2);
        SQLiteDatabase db = openOrCreateDatabase("GPS_DB", MODE_PRIVATE, null);
        querry = new Queryhandle(db);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radioBtnWeek:
                            txtView.setText("");
                            txtView.setText(querry.selectDB(WEEKLY,datehandler.stampToDayWeek(),datehandler.stampToDayMonth(),datehandler.stampToMonth(),datehandler.stampToYear()));
                        break;
                    case R.id.radioButtonMonth:
                        txtView.setText("");
                        txtView.setText(querry.selectDB(MONTHLY,datehandler.stampToDayWeek(),datehandler.stampToDayMonth(),datehandler.stampToMonth(),datehandler.stampToYear()));
                        break;
                    case R.id.radioBtnYear:
                        txtView.setText("");
                        txtView.setText(querry.selectDB(YEARLY,datehandler.stampToDayWeek(),datehandler.stampToDayMonth(),datehandler.stampToMonth(),datehandler.stampToYear()));
                        break;
                    case R.id.radioButtonAll:
                        txtView.setText("");
                        txtView.setText(querry.selectDB(SELECT_ALL,datehandler.stampToDayWeek(),datehandler.stampToDayMonth(),datehandler.stampToMonth(),datehandler.stampToYear()));
                        break;
                    default:
                        txtView.setText("");
                }
            }


        });


    }



    }

