package com.nfc.connect.nfc.Farm;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.nfc.connect.nfc.Farm.Farmer_Data;

public class DBFarmHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyDBFarm.db";
    public static final String Farm_TABLE_NAME = "farm";
    public static final String Farm_COLUMN_FARM_ID = "farm_id";
    public static final String Farm_COLUMN_FARMER_ID = "farmer_id";
    public static final String Farm_COLUMN_TYPE = "type";
    public static final String Farm_COLUMN_QUALITY = "quality";
    public static final String Farm_COLUMN_LOADING_DATE = "loading_date";
    public static final String Farm_COLUMN_TRUCK_ID = "truck_id";
    public static final String Farm_COLUMN_LOCATION = "location";
    public static final String Farm_COLUMN_BOX_ID = "box_id";
    public static final String Farm_COLUMN_QUANTITY = "quantity";
    private HashMap hp;


    public DBFarmHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table farm " +
                        "(farm_id integer primary key,farmer_id text, type text,quantity text, loading_date text,truck_id text,location text,box_id text,quality text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS farm");
        onCreate(db);
    }

    public boolean insertFarm ( String type, String quality, String loading_date,String truck_id,String location,String box_id,String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        contentValues.put("quality", quality);
        contentValues.put("loading_date", loading_date);
        contentValues.put("truck_id", truck_id);
        contentValues.put("location", location);
        contentValues.put("box_id", box_id);
        contentValues.put("quantity", quantity);
        db.insert("farm", null, contentValues);
        return true;
    }
    // code to add the new contact
    public void addFarm(Farmer_Data farm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("farmer_id",farm.getFarmer_id());
        contentValues.put("type",farm.getType() );
        contentValues.put("quantity", farm.getQuantity());
        contentValues.put("loading_date", farm.getLoading_date());
        contentValues.put("truck_id", farm.getTruck_id());
        contentValues.put("location", farm.getLocation());
        contentValues.put("box_id", farm.getBox_id());
        contentValues.put("quality", farm.getQuality());
        db.insert("farm", null, contentValues);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from farm where farm_id="+id+"", null );
        return res;
    }
    // code to get the single contact
    public Farmer_Data getFarm(String data) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from farm where Farm_COLUMN_BOX_ID='"+data+"'", null );

        if (res != null) {
            res.moveToFirst();

            Farmer_Data contact = new Farmer_Data(Integer.parseInt(res.getString(0)),
                    res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6), res.getString(7), res.getString(8));
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

    public boolean updateFarm (Integer  farm_id, String type, String quality, String loading_date,String truck_id,String location,String box_id,String quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("type", type);
        contentValues.put("quality", quality);
        contentValues.put("loading_date", loading_date);
        contentValues.put("truck_id", truck_id);
        contentValues.put("location", location);
        contentValues.put("box_id", box_id);
        contentValues.put("quantity", quantity);
        db.update("farm", contentValues, "farm_id = ? ", new String[] { Integer.toString(farm_id) } );
        return true;
    }

    public Integer deleteFarm (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("farm",
                "farm_id = ? ",
                new String[] { Integer.toString(id) });
    }
    // Deleting single contact
    public void deleteFarm1(Farmer_Data contact) {
        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
        db.delete("farm",
                "farm_id = ? ",
                new String[] { Integer.toString(contact.getFarm_id()) });
        db.close();
    }

    public ArrayList<String> getAllFarm() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from farm", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(Farm_COLUMN_FARM_ID)));
            res.moveToNext();
        }
        return array_list;
    }
    // code to get all contacts in a list view
    public List<Farmer_Data> getAllFarms() {
        List<Farmer_Data> contactList = new ArrayList<Farmer_Data>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + Farm_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Farmer_Data contact = new Farmer_Data();
                contact.setFarm_id(Integer.parseInt(cursor.getString(0)));
                contact.setFarmer_id(cursor.getString(1));
                contact.setType(cursor.getString(2));
                contact.setQuantity(cursor.getString(3));
                contact.setLoading_date(cursor.getString(4));
                contact.setTruck_id(cursor.getString(5));
                contact.setLocation(cursor.getString(6));
                contact.setBox_id(cursor.getString(7));
                contact.setQuality(cursor.getString(8));

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
