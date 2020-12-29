package com.chickenhouse.mathfacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Jesse on 11/12/2019.
 */

public class summaryDBHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "summary.db";


    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public FeedReaderContract() {
        }

        public abstract class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "summaryDB";
            public static final String COLUMN_NAME_TITLE = "Student";
            public static final String TIMEDATE = "TimeDate";
            public static final String CORRECT = "Correct";
            public static final String INCORRECT = "Incorrect";
            public static final String TOOSLOW = "TooSlow";
            public static final String TOTAL = "Total";
            public static final String FACTSPEED = "FactSpeed";
            public static final String MESSAGETXT = "MessageTxt";
            public static final String NUMOFEMAIL = "NumberOfEmailsSent";
        }
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + summaryDBHelper.FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    summaryDBHelper.FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    summaryDBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.TIMEDATE + INT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.MESSAGETXT + TEXT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.NUMOFEMAIL + INT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.CORRECT + INT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.INCORRECT + INT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.TOOSLOW + INT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.TOTAL + INT_TYPE + COMMA_SEP +
                    summaryDBHelper.FeedReaderContract.FeedEntry.FACTSPEED + INT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FactDBHelper.FeedReaderContract.FeedEntry.TABLE_NAME;

    private static final String SQL_DELETE_USER =
            "DELETE FROM" + FactDBHelper.FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " +
                    FactDBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";


    public summaryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertSummary(String name, String messagetxt, Integer correct, Integer incorrect, Integer tooSlow, Integer timedate, Integer total, Integer numOfEmail, String factSpeed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.TIMEDATE, timedate);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.MESSAGETXT, messagetxt);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.NUMOFEMAIL, numOfEmail);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.CORRECT, correct);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.INCORRECT, incorrect);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.TOOSLOW, tooSlow);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.TOTAL, total);
        values.put(summaryDBHelper.FeedReaderContract.FeedEntry.FACTSPEED, factSpeed);

        db.insert(summaryDBHelper.FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        return true;
    }


    // Gets todays resently sent message text
    public String getMessageTxt(String[] nameDate) {

        // Open SQL database
        SQLiteDatabase db = this.getReadableDatabase();

        // get email message stored in DB
        Cursor result = db.rawQuery("SELECT * " + " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME +
                    " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?" +
                    " AND " + FeedReaderContract.FeedEntry.TIMEDATE + " = ?", nameDate);

        String message = "";

        if (result != null) {
            result.moveToLast();
            try {
                message = result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.MESSAGETXT));
            }catch (Exception e){
                message = "";
            }
            System.out.print(message);
            result.close();
        }

        db.close();
        return message;
    }


    // Gets todays resently sent message text
    public int getNumOfEmailsRemain() {

        // Open SQL database
        SQLiteDatabase db = this.getReadableDatabase();

        int num = -999;

        try {
            // get email message stored in DB
            Cursor result = db.rawQuery("SELECT * FROM " +
                    FeedReaderContract.FeedEntry.TABLE_NAME + " order by " + FeedReaderContract.FeedEntry._ID + " asc", null);

            if (result != null) {
                result.moveToLast();
                num = result.getInt(result.getColumnIndex(FeedReaderContract.FeedEntry.NUMOFEMAIL));

            }
            result.close();
        }catch (Exception e) {
            System.out.println("Unable to get number of remaining E-mails...");

        }

        db.close();
        System.out.print("Number of Emails = " + String.valueOf(num));

        return num;
    }

    // Gets todays resently sent message text
    public boolean exists() {

        boolean exists = false;
        // Open SQL database
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            // get email message stored in DB
            Cursor result = db.rawQuery("SELECT * FROM " +
                    FeedReaderContract.FeedEntry.TABLE_NAME + " order by " + FeedReaderContract.FeedEntry._ID + " asc", null);

            if (result.getCount()>0) {
                exists = true;
            }

            result.close();
        }catch (Exception e) {
            System.out.println("Hit an Exception on trying to figure out if the summary DB exists");
        }

        db.close();
        System.out.print("Do Entries Exist? " + String.valueOf(exists));


        return exists;
    }

    }