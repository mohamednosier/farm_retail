package com.nfc.connect.nfc.Retailer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DBRetailHelper1 extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDBFRetail1.db";
    public static final String Farm_TABLE_NAME = "retail1";
    public static final String Farm_COLUMN_farm_id1 = "farm_id1";
    public static final String Farm_COLUMN_TYPE = "rejected";


    private HashMap hp;


    public DBRetailHelper1(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table retail1 " +
                        "(farm_id1 integer primary key, rejected text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS retail1");
        onCreate(db);
    }

    public boolean insertFarm ( String rejected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rejected", rejected);

       
        db.insert("retail1", null, contentValues);
        return true;
    }
    // code to add the new contact
    public void addFarm(Retail_Data1 truck1) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("rejected",truck1.getRejected() );

       
        db.insert("retail1", null, contentValues);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from retail1 where farm_id1="+id+"", null );
        return res;
    }
    // code to get the single contact
    public Retail_Data1 getFarm(String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from retail1 where Farm_COLUMN_TYPE='"+data+"'", null );

        if (res != null) {
            res.moveToFirst();

            Retail_Data1 contact = new Retail_Data1(Integer.parseInt(res.getString(0)),
                    res.getString(1));
            // return contact
            return contact;
        }else {
            return null;
        }
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, Farm_TABLE_NAME);
        return numRows;
    }

    public boolean updateFarm (Integer  farm_id1, String rejected, String rejected1) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rejected", rejected);


        db.update("retail1", contentValues, "farm_id1 = ? ", new String[] { Integer.toString(farm_id1) } );
        return true;
    }

    public Integer deleteFarm (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("retail1",
                "farm_id1 = ? ",
                new String[] { Integer.toString(id) });
    }
    // Deleting single contact
    public void deleteFarm1(Retail_Data1 contact) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
        db.delete("retail1",
                "farm_id1 = ? ",
                new String[] { Integer.toString(contact.getFarm_id()) });
        db.close();
    }

    public ArrayList<String> getAllFarm() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from retail1", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(Farm_COLUMN_farm_id1)));
            res.moveToNext();
        }
        return array_list;
    }
    // code to get all contacts in a list view
    public List<Retail_Data1> getAllFarms() {
        List<Retail_Data1> contactList = new ArrayList<Retail_Data1>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Farm_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Retail_Data1 contact = new Retail_Data1();
                contact.setFarm_id(Integer.parseInt(cursor.getString(0)));
                contact.setRejected(cursor.getString(1));


                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }
    // Getting contacts Count
    public int getFarmCount() {
        String countQuery = "SELECT  * FROM " + Farm_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
//        cursor.close();

        // return count
        return cursor.getCount();
    }
}
