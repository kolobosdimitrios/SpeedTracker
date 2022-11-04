package com.example.speedtracker;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Queryhandle {


    private final static int SELECT_ALL = 1;
    private final static int WEEKLY = 2;
    private final static int MONTHLY = 4;
    private final static int YEARLY = 6;
    private SQLiteDatabase dBase;

    public Queryhandle(SQLiteDatabase db){
    //Get the reference to the SQLiteDatabase
        dBase = db;
    }

    public void initDB(){
    //Initialize table in the database
        dBase.execSQL("CREATE TABLE IF NOT EXISTS GPS_DATA( lat DOUBLE, long DOUBLE, speed INTEGER, daycount INTEGER, daymonthcount INTEGER, " +
                "monthcount INTEGER, yearcount INTEGER,exact_time TEXT);");

    }
    //Get the current time for the queries
    //Day of month,day o week,month,year
    //Write in database method (INSERT)
    //dayofweek : values 1-7 1=monday etc... -> daycount
    //dayofmonth :values 1-30(31) calendar -> daymonthcount
    //month : values 1-12 -> monthcount
    //year    -> yearcount
    //time -> HH-MM-SS
    public void insertDB(int dayofweek,int dayofmonth,int month,int year,String time,int speed,double lat,double longt){

        dBase.execSQL("INSERT INTO GPS_DATA VALUES"+"('"+lat+"','"+longt+"','"+speed+"','"+dayofweek+"','"+dayofmonth+"','"+month+"','"+year+"','"+time+"');");

    }

    //SELECT FROM DATABASE METHODS
    //CASE SELECT_ALL : SELECTS ALL DATA!
    //CASE WEEKLY : LAST WEEK'S DATA!
    //CASE MONTHLY : LAST MONTH'S DATA
    //CASE YEARLY : LAST YEAR'S DATA
    public String selectDB(int type,int dayOfWeek,int dayOfMonth,int month,int year){
        Cursor cursor;
        switch(type) {
            case SELECT_ALL:
                cursor = dBase.rawQuery("SELECT * FROM GPS_DATA",null);
                if(cursor.getCount() == 0){
                    return null;
                }
                else{
                    return String.valueOf(queryPrint(cursor));
                }
            case WEEKLY :
                cursor = dBase.rawQuery("SELECT * FROM GPS_DATA WHERE daymonthcount >="+
                        "'"+dayOfMonth+"' - ('"+dayOfWeek+"' - 1) AND daymonthcount <= '"+dayOfMonth+"' AND monthcount = '"+month+"' AND yearcount = '"+year+"';",null);
                if(cursor.getCount() == 0){
                    return null;
                }
                else{
                    return String.valueOf(queryPrint(cursor));
                }
            case MONTHLY:
                cursor = dBase.rawQuery("SELECT * FROM GPS_DATA WHERE monthcount = '"+month+"' AND yearcount = '"+year+"';",null);
                if(cursor.getCount() == 0){
                    return null;
                }
                else {
                    return String.valueOf(queryPrint(cursor));
                }
            case YEARLY:
                cursor = dBase.rawQuery("SELECT * FROM GPS_DATA WHERE yearcount = '"+year+"';",null);
                if(cursor.getCount() == 0){
                    return null;
                }
                else{
                   return String.valueOf(queryPrint(cursor));
                }
            default:
                return null;
        }
    }

    private StringBuffer queryPrint (Cursor cursor){
        StringBuffer buffer = new StringBuffer();
        //Show lat,long,speed,day,month,year
        while(cursor.moveToNext()){
            buffer.append("Latitude :");
            buffer.append(cursor.getDouble(0));
            buffer.append(",");
            buffer.append("Longitude : ");
            buffer.append(cursor.getDouble(1));
            buffer.append(",");
            buffer.append(" Speed : ");
            buffer.append(cursor.getString(2));
            buffer.append(",");
            buffer.append(" Date : ");
            buffer.append(cursor.getString(4));
            buffer.append("-");
            buffer.append(cursor.getString(5));
            buffer.append("-");
            buffer.append(cursor.getString(6));
            buffer.append(",");
            buffer.append(" Time : ");
            buffer.append(cursor.getString(7));
            buffer.append("\n");
        }


        return buffer;
    }









}
