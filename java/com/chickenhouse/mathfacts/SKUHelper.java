package com.chickenhouse.mathfacts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;

/**
 * Sample SQLiteHelper for the purchase record table
 *
 */
public class SKUHelper extends SQLiteOpenHelper {

    //table name
    public static final String TABLE_PURCHASES = "purchases";
    //receipt id 
    public static final String COLUMN_RECEIPT_ID = "receipt_id";
    //amazon user id 
    public static final String COLUMN_USER_ID = "user_id";
    //purchase record status
    public static final String COLUMN_STATUS = "status";
    //database name
    private static final String DATABASE_NAME = "receipts.db";
    private static final int DATABASE_VERSION = 1;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table " + TABLE_PURCHASES
                                                  + "("
                                                  + COLUMN_RECEIPT_ID
                                                  + " text primary key not null, "
                                                  + COLUMN_USER_ID
                                                  + " text not null, "
                                                  + COLUMN_STATUS
                                                  + " text not null "
                                                  + ");";
    public final class FeedReaderContract {
        // To prevent someone from accidentally instantiating the contract class,
        // give it an empty constructor.
        public FeedReaderContract() {
        }

        public abstract class FeedEntry implements BaseColumns {
            //table name
            public static final String TABLE_PURCHASES = "purchases";
            //receipt id
            public static final String COLUMN_RECEIPT_ID = "receipt_id";
            //amazon user id
            public static final String COLUMN_USER_ID = "user_id";
            //purchase record status
            public static final String COLUMN_STATUS = "status";
        }
    }

    private static final String SQL_UPDATE_STATUS_P1 =
            "UPDATE " + FeedReaderContract.FeedEntry.TABLE_PURCHASES + " SET " +
                    FeedReaderContract.FeedEntry.COLUMN_STATUS + " = ";

    private static final String SQL_UPDATE_STATUS_P2 = " WHERE " +
                    FeedReaderContract.FeedEntry.COLUMN_RECEIPT_ID + " = ";

    public SKUHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(final SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
//        this.insert("Starter25","MAGCLANMATHADMIN","FULFILLED");
    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        Log.w(SKUHelper.class.getName(), "Upgrading database from version " + oldVersion
                                                  + " to "
                                                  + newVersion
                                                  );
        //do nothing in the sample
    }

    public boolean insert(String receiptID, String userID, String status){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SKUHelper.FeedReaderContract.FeedEntry.COLUMN_RECEIPT_ID, receiptID);
        values.put(SKUHelper.FeedReaderContract.FeedEntry.COLUMN_USER_ID, userID);
        values.put(SKUHelper.FeedReaderContract.FeedEntry.COLUMN_STATUS, status);

        db.insert(SKUHelper.FeedReaderContract.FeedEntry.TABLE_PURCHASES, null, values);
        db.close();
        return true;
    }

    public boolean updateEntryStatus(String receiptID, String userID, String status){

        SQLiteDatabase db = this.getWritableDatabase();

//        "UPDATE " + FeedReaderContract.FeedEntry.TABLE_PURCHASES + " SET " + FeedReaderContract.FeedEntry.COLUMN_STATUS + " ="
//              status
//        " WHERE " + FeedReaderContract.FeedEntry.COLUMN_RECEIPT_ID + " = "
//              receiptID

        db.execSQL(SQL_UPDATE_STATUS_P1 + status + SQL_UPDATE_STATUS_P2 + receiptID + ";");

        db.close();
        return true;
    }

    public boolean exists(String reciept){

        // Open SQL database
        SQLiteDatabase db = this.getReadableDatabase();

        int num = -999;
        boolean exists = false;

        ArrayList dummy = new ArrayList();

        try {
            // get email message stored in DB
            Cursor result = db.rawQuery("SELECT * FROM " +
                    TABLE_PURCHASES +  " ", null);

            if (result != null) {
                result.moveToFirst();
                while(result.isAfterLast() == false){
                    dummy.add(result.getString(result.getColumnIndex("receipt_id")));
                    result.moveToNext();
                }

                result.close();
                exists = dummy.contains(reciept);
            }
        }catch (Exception e) {

        }

        db.close();

        return exists;
    }
}