package com.chickenhouse.mathfacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

/**
 * Created by Jesse on 3/2/2018.
 */

public class LoginDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "login.db";
    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public FeedReaderContract() {}

        public abstract class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "LoginDB";
            public static final String COLUMN_NAME_TITLE = "Name";
            public static final String PIN = "PIN";
            public static final String EMAIL = "Email";
            public static final String DATEEMAILED = "DateEmailed";
            public static final String LOGINTIMEDATE = "LoginTimeDate";
            public static final String SPEED = "Speed";
            public static final String SELECTED = "SelectedFacts";
        }
    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String SQL_CREATE_ENTRIES =
            " CREATE TABLE " + LoginDBHelper.FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    LoginDBHelper.FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    LoginDBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    LoginDBHelper.FeedReaderContract.FeedEntry.PIN + TEXT_TYPE + COMMA_SEP +
                    LoginDBHelper.FeedReaderContract.FeedEntry.EMAIL + TEXT_TYPE + COMMA_SEP +
                    LoginDBHelper.FeedReaderContract.FeedEntry.DATEEMAILED + TEXT_TYPE + COMMA_SEP +
                    LoginDBHelper.FeedReaderContract.FeedEntry.LOGINTIMEDATE + TEXT_TYPE + COMMA_SEP +
                    LoginDBHelper.FeedReaderContract.FeedEntry.SPEED + TEXT_TYPE + COMMA_SEP +
                    LoginDBHelper.FeedReaderContract.FeedEntry.SELECTED + TEXT_TYPE +
                    " );";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private static final String SQL_DELETE_ROWS =
            "DELETE FROM" + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";


    public LoginDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {db.execSQL(SQL_CREATE_ENTRIES);    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean createNewUser  (String name, String pin, String email, String timedate, String defaultSpeed, String defaultSelection)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.PIN, pin);
        values.put(FeedReaderContract.FeedEntry.EMAIL, email);
        values.put(FeedReaderContract.FeedEntry.DATEEMAILED, timedate);
        values.put(FeedReaderContract.FeedEntry.LOGINTIMEDATE, timedate);
        values.put(FeedReaderContract.FeedEntry.SPEED, defaultSpeed);
        values.put(FeedReaderContract.FeedEntry.SELECTED, defaultSelection);

        db.insert("LoginDB", null, values);
        db.close();
        return true;
    }

    public boolean updateUserPin  (String name, String NewPin, String email, String timedate)
    {
        String[] NAME = new String[]{name};
        String defaultSpeed = this.getDefaultSpeedByName(NAME);
        String defSelection = this.getDefaultFactSelectionByName(NAME);

        SQLiteDatabase db = this.getWritableDatabase();

        // Remove the former User Pin
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?",NAME);
        //db.execSQL("DELETE FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + "= '"+ name + "'");

        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.PIN, NewPin);
        values.put(FeedReaderContract.FeedEntry.EMAIL, email);
        values.put(FeedReaderContract.FeedEntry.DATEEMAILED, timedate);
        values.put(FeedReaderContract.FeedEntry.LOGINTIMEDATE, timedate);
        values.put(FeedReaderContract.FeedEntry.SPEED, defaultSpeed);
        values.put(FeedReaderContract.FeedEntry.SELECTED, defSelection);

        db.insert("LoginDB", null, values);
        db.close();
        return true;
    }


    public boolean updateUserPinandSpeed  (String name, String NewPin, String email, String timedate, String defSPD)
    {
        String[] NAME = new String[]{name};

        SQLiteDatabase db = this.getWritableDatabase();

        String defSelection = this.getDefaultFactSelectionByName(NAME);

        // Remove the former User Pin
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?",NAME);
        //db.execSQL("DELETE FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + "= '"+ name + "'");

        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.PIN, NewPin);
        values.put(FeedReaderContract.FeedEntry.EMAIL, email);
        values.put(FeedReaderContract.FeedEntry.DATEEMAILED, timedate);
        values.put(FeedReaderContract.FeedEntry.LOGINTIMEDATE, timedate);
        values.put(FeedReaderContract.FeedEntry.SPEED, defSPD);
        values.put(FeedReaderContract.FeedEntry.SELECTED, defSelection);

        db.insert("LoginDB", null, values);
        db.close();
        return true;
    }

    public boolean updateFactSelected  (String name, String selection, String timedate)
    {
        String[] NAME = new String[]{name};

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result =  db.rawQuery( "select * from LoginDB WHERE Name = ? order by LoginTimeDate desc",NAME);

        ArrayList names = new ArrayList();

        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.SPEED)));
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.PIN)));
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.EMAIL)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            names.add(dummy);
        }
        result.close();

        String defSPD = names.get(names.size()-3).toString();
        String NewPin = names.get(names.size()-2).toString();
        String email = names.get(names.size()-1).toString();

        // Remove the former User Pin
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?",NAME);
        //db.execSQL("DELETE FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + "= '"+ name + "'");

        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.PIN, NewPin);
        values.put(FeedReaderContract.FeedEntry.EMAIL, email);
        values.put(FeedReaderContract.FeedEntry.DATEEMAILED, timedate);
        values.put(FeedReaderContract.FeedEntry.LOGINTIMEDATE, timedate);
        values.put(FeedReaderContract.FeedEntry.SPEED, defSPD);
        values.put(FeedReaderContract.FeedEntry.SELECTED, selection);

        db.insert("LoginDB", null, values);
        db.close();
        return true;
    }


    public boolean updateUserSpeed  (String name, String defSPD, String timedate)
    {
        String[] NAME = new String[]{name};

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor result =  db.rawQuery( "select * from LoginDB WHERE Name = ? order by LoginTimeDate desc",NAME);

        ArrayList names = new ArrayList();

        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.SELECTED)));
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.PIN)));
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.EMAIL)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            names.add(dummy);
        }
        result.close();

        String defSelection = names.get(names.size()-3).toString();
        String NewPin = names.get(names.size()-2).toString();
        String email = names.get(names.size()-1).toString();

        // Remove the former User Pin
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME,FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?",NAME);
        //db.execSQL("DELETE FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + "= '"+ name + "'");

        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.PIN, NewPin);
        values.put(FeedReaderContract.FeedEntry.EMAIL, email);
        values.put(FeedReaderContract.FeedEntry.DATEEMAILED, timedate);
        values.put(FeedReaderContract.FeedEntry.LOGINTIMEDATE, timedate);
        values.put(FeedReaderContract.FeedEntry.SPEED, defSPD);
        values.put(FeedReaderContract.FeedEntry.SELECTED, defSelection);

        db.insert("LoginDB", null, values);
        db.close();
        return true;
    }

    public boolean statusEmailed  (String name, String pin, String email, String timedate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.PIN, pin);
        values.put(FeedReaderContract.FeedEntry.EMAIL, email);
        values.put(FeedReaderContract.FeedEntry.DATEEMAILED, timedate);

        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        return true;
    }

    public boolean loggedIn  (String name, String pin, String timedate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.PIN, pin);
        values.put(FeedReaderContract.FeedEntry.LOGINTIMEDATE, timedate);

        db.insert(FactDBHelper.FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        return true;
    }

    public ArrayList availableUsers(ArrayList names) {
        // TODO Auto-generated method stub
        // ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select DISTINCT Name from LoginDB order by LoginTimeDate desc",null); //

//        Cursor result =  db.rawQuery( "select Name from LoginDB order by LoginTimeDate desc",null); //

        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            names.add(dummy);
        }
        result.close();
        db.close();


        return names;

    }

    public ArrayList logins(ArrayList names) {
        // TODO Auto-generated method stub
        // ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor result =  db.rawQuery( "select DISTINCT Name from LoginDB order by LoginTimeDate desc",null); //

        Cursor result =  db.rawQuery( "select * from LoginDB order by LoginTimeDate desc",null);

        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE)));
                names.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.PIN)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            names.add(dummy);
        }
        result.close();
        db.close();


        return names;

    }

    public ArrayList associatedPins(ArrayList pins) {
        // TODO Auto-generated method stub
        // ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select PIN from LoginDB order by LoginTimeDate desc",null);
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                pins.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.PIN)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            pins.add(dummy);
        }
        result.close();
        db.close();
        return pins;
    }

    public String getEmailAddress(String[] NamePin) {
        // TODO Auto-generated method stub
        // ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        //System.out.println(NamePin[0] + "  and  " + NamePin[1]);
        String emailAddress = "";
        Cursor result =  db.rawQuery( "select Email from LoginDB where Name = ? AND PIN = ?",NamePin);
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                emailAddress = (result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.EMAIL)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            emailAddress = "empty";
        }
        result.close();
        db.close();
        //System.out.println("emailAddress = " + emailAddress);
        return emailAddress;
    }

    public String getEmailAddressByName(String[] NamePin) {
        // TODO Auto-generated method stub
        // ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String emailAddress = "";
        Cursor result =  db.rawQuery( "select Email from LoginDB where Name = ?",NamePin);
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                emailAddress = (result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.EMAIL)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            emailAddress = "empty";
        }
        result.close();
        db.close();
        //System.out.println("emailAddress = " + emailAddress);
        return emailAddress;
    }


    public String getDefaultSpeedByName(String[] stu_Name) {
        // ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String defaultSpeed = "";
        Cursor result =  db.rawQuery( "select Speed from LoginDB where Name = ?",stu_Name);
        //System.out.println("The result is " + result.toString());
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                defaultSpeed = (result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.SPEED)));
                result.moveToNext();
            }
        }else{
            defaultSpeed = "10";
        }
        result.close();
        db.close();
        //System.out.println("defaultSpeed = " + defaultSpeed);
        return defaultSpeed;
    }

    public String getDefaultFactSelectionByName(String[] stu_Name) {

        // ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        String defSelection = "";
        try {
            Cursor result = db.rawQuery("select SelectedFacts from LoginDB where Name = ?", stu_Name);

        //System.out.println("The result is " + result.toString());
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                defSelection = (result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.SELECTED)));
                result.moveToNext();
            }
        }else{
            defSelection = "91111#81111#71111#61111#51111#41111#31111#21111#11111";
        }
        result.close();
        db.close();
       // System.out.println("defSelection = " + defSelection);
        return defSelection;
        }catch(Exception e){
            System.out.println("Unable to search DB used default values");
            defSelection = "91111#81111#71111#61111#51111#41111#31111#21111#11111";
            return defSelection;
        }
    }

}
