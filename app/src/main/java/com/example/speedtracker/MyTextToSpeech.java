package com.example.speedtracker;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class MyTextToSpeech {
    //Text To Speech Object
    private TextToSpeech tts;
    //Override on Init super method
    //Argument an integer = 0 Succesfull init
    //and set the language yp Engilsh
    private TextToSpeech.OnInitListener initListener =
            new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status==TextToSpeech.SUCCESS)
                        tts.setLanguage(Locale.ENGLISH);
                }
            };
//Constructor
    //Create the objcext type textToSpeech
    //Give a context
    public MyTextToSpeech(Context context) {
        //Take the context and the on initialize listener
        tts = new TextToSpeech(context,initListener);
    }
    //Message to say!
    //Using speak method!
    public void speak(String message){
        tts.speak(message,TextToSpeech.QUEUE_ADD,null,null);
    }

}
