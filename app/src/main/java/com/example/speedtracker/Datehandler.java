package com.example.speedtracker;
import java.util.Calendar;
public class Datehandler {

    private long stamp;
    private Calendar cal = Calendar.getInstance();
    //Get the stamp from the system
    //and convert it to date
    public Datehandler(){


    }
    public int stampToDayWeek(){

        return cal.get(Calendar.DAY_OF_WEEK);

    }
    public int stampToDayMonth(){

        return cal.get(Calendar.DAY_OF_MONTH);

    }
    public int stampToMonth(){

        return cal.get(Calendar.MONTH);

    }
    public int stampToYear(){

        return cal.get(Calendar.YEAR);

    }
    public String stampToHour(){
        StringBuffer buffer = new StringBuffer();
        buffer.append(cal.get(Calendar.HOUR_OF_DAY));
        buffer.append("-");
        buffer.append(cal.get(Calendar.MINUTE));
        buffer.append("-");
        buffer.append(cal.get(Calendar.SECOND));

        return  String.valueOf(buffer);
    }
    //Set the timestamp
    public void setStamp(long newstamp){
        stamp = newstamp;

    }

}
