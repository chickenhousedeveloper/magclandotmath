package com.chickenhouse.mathfacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jesse on 10/20/2017.
 */

public class FactDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "Fact.db";
    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public FeedReaderContract() {}

        public abstract class FeedEntry implements BaseColumns {
            public static final String TABLE_NAME = "FactDB";
            public static final String COLUMN_NAME_TITLE = "Name";
            public static final String FACT = "Fact";
            public static final String CORRECT = "Correct";
            public static final String TIMEDATE = "TimeDate";
            public static final String FACTSPEED = "FactSpeed";
        }
    }
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.FACT + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.CORRECT + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.TIMEDATE + INT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.FACTSPEED + INT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    private static final String SQL_DELETE_USER =
            "DELETE FROM" + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";


    public FactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void removeUser (String[] name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?", name);
    }

    public boolean insertFact  (String name, String fact, String correct, String timedate, String factSpeed)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, name);
        values.put(FeedReaderContract.FeedEntry.FACT, fact);
        values.put(FeedReaderContract.FeedEntry.CORRECT, correct);
        values.put(FeedReaderContract.FeedEntry.TIMEDATE, timedate);
        values.put(FeedReaderContract.FeedEntry.FACTSPEED, factSpeed);

        db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
        return true;
    }
/*
    public ArrayList FactStats(String[] factInfo) {
        // TODO Auto-generated method stub
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select * from FactDB order by Time desc where Name = ? AND Fact =?", factInfo );
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.TIMEDATE)));
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.CORRECT)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            array_list.add(dummy);
        }
        result.close();
        return array_list;
    }

 */

    public ArrayList activeDates() {
        // TODO Auto-generated method stub
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery("SELECT DISTINCT " + FeedReaderContract.FeedEntry.TIMEDATE + " from " + FeedReaderContract.FeedEntry.TABLE_NAME, null);
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.TIMEDATE)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            array_list.add(dummy);
        }
        result.close();
        return array_list;
    }


    public ArrayList DailyStats(String[] factInfo) {
        // TODO Auto-generated method stub
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select * from " + FeedReaderContract.FeedEntry.TABLE_NAME +
                " where " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ? AND "+
                FeedReaderContract.FeedEntry.TIMEDATE + "  = ? AND " + FeedReaderContract.FeedEntry.CORRECT + " = ?", factInfo );
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.FACT)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            array_list.add(dummy);
        }
        result.close();
        return array_list;
    }

    public double todaysSpeed(String[] factInfo) {
        // TODO Auto-generated method stub
        ArrayList array_list = new ArrayList();
        double sum = 0.0;
        double count = 0.0;
        double avgSpd = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "select * from " + FeedReaderContract.FeedEntry.TABLE_NAME +
                " where " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ? AND "+
                FeedReaderContract.FeedEntry.TIMEDATE + "  = ?", factInfo );

//        Cursor result =  db.rawQuery( "select * from " + FeedReaderContract.FeedEntry.TABLE_NAME +
//                " where " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ? AND "+
//                FeedReaderContract.FeedEntry.TIMEDATE + " >= ?", factInfo );

        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                //array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.FACTSPEED)));
                //System.out.println(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.FACTSPEED)));
                count = count + 1.0;
                sum = sum + result.getDouble(result.getColumnIndex(FeedReaderContract.FeedEntry.FACTSPEED));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            array_list.add(dummy);
        }
        avgSpd = sum/count;

        result.close();
        return avgSpd;
    }

    public double lastFive(String[] factInfo) {
        // TODO Auto-generated method stub
        ArrayList array_list = new ArrayList();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result =  db.rawQuery( "SELECT " + FeedReaderContract.FeedEntry.CORRECT +
                " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE +
                " = ? AND " + FeedReaderContract.FeedEntry.FACT + " = ?" + " ORDER BY " +
                FeedReaderContract.FeedEntry.TIMEDATE + " ASC", factInfo ); // DESC was used before 5.2.2018
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.CORRECT)));
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            array_list.add(dummy);
        }
        result.close();

        int length = array_list.size();
        //System.out.println(" array list size = " + array_list.size());
        int corr = 0;
        double percent = 0;

        if(length>5){
            for(int i=0; i<5; i++ ){

                //System.out.println(" array list value = " + array_list.get(length-i-1));
                if(array_list.get(length-i-1).equals("1")) {
                    corr++;
                }
            }
            percent = corr/5.;
        }else{

           // if(!array_list.get(0).equals("empty")) {
           //     for (int i = 0; i < length; i++) {
           //         if (array_list.get(length - i - 1).equals("1")) {
           //             corr++;
           //         }
           //     }
           //     percent = corr / length;
           //     }

           percent = -1.0; // set percent to "-1.0" to set a flag to not allow that mathfact's status to be shown

        }

        return percent;
    }

    // Calculates percent correct for each math fact today
    public double rangePercent(String[] factInfo) {
        // Open SQL database
        SQLiteDatabase db = this.getReadableDatabase();

        double total = 100.0;
        double corr = 0.0;

        String[] dummy ={factInfo[0],factInfo[3]};

        // collect answers from a date range for a specific math fact
        // + FeedReaderContract.FeedEntry.CORRECT +
        Cursor result =  db.rawQuery( "SELECT *" +
                " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME +
                        " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ? AND " + FeedReaderContract.FeedEntry.FACT + " = ?" +
                        " AND " + FeedReaderContract.FeedEntry.TIMEDATE + " BETWEEN " + String.valueOf(factInfo[1]) + " AND " + String.valueOf(factInfo[2]) +" "
                , dummy );

        //System.out.println("Column Title = " + factInfo[0].toString() + " FACT = " + factInfo[3].toString());

        ArrayList array_list = new ArrayList();

        //System.out.println("Length of Output = " + String.valueOf(result.getCount()));
        // How long is the list = total number of attempts on that math fact in that date range
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.CORRECT)));
                //System.out.println("Date = " + result.getString(result.getColumnIndex((FeedReaderContract.FeedEntry.TIMEDATE))));
                result.moveToNext();
            }

            total =  array_list.size();

            corr = Collections.frequency(array_list,"1");
        }
        result.close();

       /* Cursor mxResult = db.rawQuery("SELECT MIN(" + FeedReaderContract.FeedEntry.TIMEDATE + ") FROM " + FeedReaderContract.FeedEntry.TABLE_NAME, null);
        mxResult.moveToFirst();
        int max = mxResult.getInt(0);
        mxResult.close();

        System.out.println("Min Time Date = " + String.valueOf(max));
        */

       double percent = 0.0;

        //System.out.println("Name = " + factInfo[0] + " Start Of Exam = " + factInfo[1] + " End Of Exam = "
        //        + factInfo[2] + " Math Fact = " + factInfo[3] + " total = " + String.valueOf(total));

        percent = corr / total;


        return percent;
    }

    // Supplies ArrayList of the student Math Fact Results from the date provided to present
    public double mathfactResult(String[] factInfo, int since) {
        // Open SQL database
        SQLiteDatabase db = this.getReadableDatabase();

        int soe = Integer.valueOf(factInfo[2])-since;

        String[] info = {factInfo[0], factInfo[1]};

        String cmdString = "SELECT * " +
                " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?" +
                " AND " + FeedReaderContract.FeedEntry.FACT + " = ?" + " AND " + FeedReaderContract.FeedEntry.TIMEDATE + " BETWEEN " +
                String.valueOf(soe) + " AND " + factInfo[2] ;

        //System.out.println(cmdString);
        // collect answers from a date range for a specific math fact
        Cursor result = db.rawQuery(cmdString, info);

        // collect answers from a date range for a specific math fact
        //Cursor result =  db.rawQuery( "SELECT * " +
        //        " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?" +
        //        " AND " + FeedReaderContract.FeedEntry.FACT + " = ?", factInfo );

        double total = 1.0;
        double correct = 0.0;

        //System.out.println("made request");
        //System.out.println("factInfo[0] = "+factInfo[0] + " " + "factInfo[1] = " + factInfo[1]);
        //System.out.println("Number of values in result = " + String.valueOf(result.getCount()));
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            int timeClmIdx = result.getColumnIndex(FeedReaderContract.FeedEntry.TIMEDATE);
            int ansClmnIdx = result.getColumnIndex(FeedReaderContract.FeedEntry.CORRECT);



            while(result.isAfterLast() == false) {
                if(Integer.valueOf(result.getString(timeClmIdx))>=since){

                    // Create an array list so that if the answer was correct the system will out put a 1 and false a 0
                    if (result.getString(ansClmnIdx).equals("1")){
                        correct++; // increment number correct
                     }
                     total++; // increment number of attempts
                }

                result.moveToNext();
            }

            if(total>1) {
                total = total - 1; // removes offset implemented to avoided undefined result
            }

        }else{
            System.out.println(" error in mathfactResult did not find mathfact, " + factInfo[1] + ", in that time period ");
        }

        return correct/total;

    }

    // Supplies ArrayList of the student Math Fact Results from the date provided to present
    public int activeFact(String[] factInfo, int since) {
        // Open SQL database
        SQLiteDatabase db = this.getReadableDatabase();

        int soe = Integer.valueOf(factInfo[2])-since;

        String[] info = {factInfo[0], factInfo[1]};

        String cmdString = "SELECT * " +
                " FROM " + FeedReaderContract.FeedEntry.TABLE_NAME + " WHERE " + FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?" +
                " AND " + FeedReaderContract.FeedEntry.FACT + " = ?" + " AND " + FeedReaderContract.FeedEntry.TIMEDATE + " BETWEEN " +
                String.valueOf(soe) + " AND " + factInfo[2] ;

        //System.out.println(cmdString);
        // collect answers from a date range for a specific math fact
        Cursor result = db.rawQuery(cmdString, info);

        int total = 0;

        if(result != null) {
            total = result.getCount();
        };

        return total;
    }


    public ArrayList<?> createcvs( String[] studentName){

        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList array_list = new ArrayList();

        // Cursor result =  db.rawQuery( "select * from FactDB where Name = ? AND Input =? ", studentName);
        Cursor result =  db.rawQuery( "select * from FactDB where Name = ? ", studentName);

        Integer a = 0;

        //System.out.println("made request");
        if(result != null && result.getCount()>0){
            result.moveToFirst();
            while(result.isAfterLast() == false) {
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.TIMEDATE)));
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.FACT)));
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.CORRECT)));
                array_list.add(result.getString(result.getColumnIndex(FeedReaderContract.FeedEntry.FACTSPEED)));
                //System.out.println("line = " + a.toString());
                a = a + 1;
                result.moveToNext();
            }
        }else{
            Object dummy = "empty";
            array_list.add(dummy);
        }
        result.close();
        db.close();
        return array_list;

    }

}
