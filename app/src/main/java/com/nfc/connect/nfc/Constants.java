package com.nfc.connect.nfc;

import android.nfc.Tag;

import java.util.ArrayList;

public class Constants {
    public static Boolean check_farm=false;
    public static String farmer_id="";
    public static String truck_id="";
    public static String truck="";
    public static String retailer_id="";

    public static String date="";
    public static String retailer_date="";
    public static Double farm_latitude= 0.0;
    public static Double farm_longitude= 0.0;
    public static Double retailer_latitude= 0.0;
    public static Double retailer_longitud= 0.0;
    public static  String general_master_batch_id="";
    public static  String retailer_general_master_batch_id="";

    public static ArrayList<String> sub_batch = new ArrayList<String>();
    public static ArrayList<String> retailer_sub_batch = new ArrayList<String>();

    public static ArrayList<String> data = new ArrayList<String>();
    public static ArrayList<String> retailer_data = new ArrayList<String>();

    public static ArrayList<String> data_refused = new ArrayList<String>();
    public static ArrayList<String> retailer_data_refused = new ArrayList<String>();
    public static Tag myTag1;

    public static ArrayList<String> download = new ArrayList<String>();
    public static int x=0;
    public static int x1=0;
    public static int farm_another=0;
    public static int retailer_another=0;
}
