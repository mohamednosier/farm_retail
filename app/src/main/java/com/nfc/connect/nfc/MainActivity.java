package com.nfc.connect.nfc;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;

import androidx.core.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.connect.nfc.R;
import com.nfc.connect.nfc.Truck.TruckActivity;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

public class MainActivity extends Activity {
    private static final int REQUEST = 112;
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Tag myTag1;

    Context context;
    public static int x = 0;
    public static int x1 = 0;
    TextView tvNFCContent;
    TextView message, counter_no, counter_no_efused, counter_no_general;
    Button btnWrite, refuse, calculate_id, retrieve, save, finish,load,reset;

    final List<String> list = new ArrayList<String>();
    final List<String> list2 = new ArrayList<String>();
    final List<String> list3 = new ArrayList<String>();
    final List<String> list4 = new ArrayList<String>();
    KProgressHUD hud1;
    private Spinner spinner1, spinner2;
    String type = "0";
    String classification = "0";
    LinearLayout linearLayout;
    String process = "";
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;
    String encodedString;
    private GpsTracker gpsTracker;

    double latitude;
    double longitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
//Toast.makeText(MainActivity.this,"getIntent "+getIntent().getStringExtra("type"),Toast.LENGTH_LONG).show();
        process = getIntent().getStringExtra("type");

        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
        message = (TextView) findViewById(R.id.edit_message);
        btnWrite = (Button) findViewById(R.id.button);
        finish = (Button) findViewById(R.id.finish);
        refuse = (Button) findViewById(R.id.refuse);
        calculate_id = (Button) findViewById(R.id.calculate_id);
        retrieve = (Button) findViewById(R.id.retrieve);
        save = (Button) findViewById(R.id.save);
        load=(Button)findViewById(R.id.load);
        reset=(Button)findViewById(R.id.reset);
        counter_no = (TextView) findViewById(R.id.counter_no);
        counter_no_efused = (TextView) findViewById(R.id.counter_no_efused);
        counter_no_general = (TextView) findViewById(R.id.counter_no_general);

        linearLayout = (LinearLayout) findViewById(R.id.relativeLayout);


        spinner2 = (Spinner) findViewById(R.id.spinner2);
        if(process.equals("retailer1")){
            Constants.sub_batch.clear();
            Constants.data.clear();
            x=0;
            load.setVisibility(View.VISIBLE);
//            Constants.check_farm=false;
        }else {
            Constants.check_farm=true;
        }


        // check if GPS enabled
        GpsTracker gps = new GpsTracker(MainActivity.this);
        if(gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }






        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Gson gson = new Gson();
                String json = sharedPreferences.getString("Set", "");
                if (json.isEmpty()) {
                    Toast.makeText(MainActivity.this, "فيه خطأ", Toast.LENGTH_LONG).show();
                } else {
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrPackageData = gson.fromJson(json, type);
                    for (String data : arrPackageData) {
                        Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                        String new_data=data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude;
                        Constants.sub_batch.add(data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
                    }
                }
                Gson gson1 = new Gson();
                String json1 = gson1.toJson(Constants.sub_batch);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Set1", json1);
                editor.commit();
                reset.setVisibility(View.VISIBLE);

            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                        Constants.farmer_id="";
                        Constants.truck_id="";
                        Constants.retailer_id="";
                        Constants.check_farm=false;
                        Constants.date="";
                        Constants.retailer_date="";
                        Constants.general_master_batch_id="";
                        Constants.retailer_general_master_batch_id="";

                        Constants.sub_batch.clear();
                        Constants.data.clear();
                        Constants.data_refused.clear();

                        sharedPreferences.edit().clear().commit();
Intent i=new Intent(MainActivity.this, TruckActivity.class);
startActivity(i);
finish();
            }
        });



        // Spinner click listener
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemSelected", "onItemSelected: " + position);
                if (position == 0) {
                    type = "0";
                } else {
                    type = list.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner1 = (Spinner) findViewById(R.id.spinner);
        connect();
        // Spinner click listener
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("onItemSelected", "onItemSelected: " + position);
                if (position == 0) {
                    classification = "0";
                } else {
                    classification = list3.get(position - 1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        calculate_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (process.equals("farm")) {


                    Log.d("onClick: subbatch", "onClick: subbatchsize" + Constants.sub_batch.size());
                    for (int i = 0; i < Constants.sub_batch.size(); i++) {

                        Log.d("onClick: subbatch", "onClick: subbatch" + Constants.sub_batch.get(i));
                    }
                } else if (process.equals("retailer1")) {

                    Log.d("onClick: subbatch", "onClick: subbatchsize" + Constants.sub_batch.size());
                    for (int i = 0; i < Constants.sub_batch.size(); i++) {

                        Log.d("onClick: subbatch", "onClick: subbatch" + Constants.sub_batch.get(i));
                    }
                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                System.out.println(new Timestamp(date.getTime()));

                showPictureDialog();

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date = new Date();
                System.out.println(new Timestamp(date.getTime()));
                if (process.equals("farm")) {

                    Gson gson = new Gson();
                    String json = gson.toJson(Constants.sub_batch);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Set", json);
                    editor.commit();
                } else if (process.equals("retailer1")) {
                    Gson gson = new Gson();
                    String json = gson.toJson(Constants.sub_batch);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Set1", json);
                    editor.commit();
                    reset.setVisibility(View.VISIBLE);
                }


            }
        });
        retrieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (process.equals("farm")) {

                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("Set", "");
                    if (json.isEmpty()) {
                        Toast.makeText(MainActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
                    } else {
                        Type type = new TypeToken<List<String>>() {
                        }.getType();
                        List<String> arrPackageData = gson.fromJson(json, type);
                        for (String data : arrPackageData) {
                            Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                        }
                    }
                } else if (process.equals("retailer1")) {
                    Gson gson = new Gson();
                    String json = sharedPreferences.getString("Set1", "");
                    if (json.isEmpty()) {
                        Toast.makeText(MainActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
                    } else {
                        Type type = new TypeToken<List<String>>() {
                        }.getType();
                        List<String> arrPackageData = gson.fromJson(json, type);
                        for (String data : arrPackageData) {
                            Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                        }
                    }
                }

            }
        });
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (process.equals("farm")) {
                    if (type.equals("0") || classification.equals("0")) {
                        Toast.makeText(MainActivity.this, "من فضلك اختار النوع والصنف", Toast.LENGTH_LONG).show();
                    } else {
                        boolean ans = Constants.data.isEmpty();
                        if (ans == true) {
                            System.out.println("The ArrayList is empty");

                            Toast.makeText(MainActivity.this, "لايوجد صناديق متاحه", Toast.LENGTH_LONG).show();
                        } else {
                            if (Constants.data.contains(tvNFCContent.getText().toString())) {


                                boolean ans1 = Constants.data_refused.isEmpty();
                                if (ans1 == true) {
                                    Constants.data_refused.add(tvNFCContent.getText().toString());
                                    for (int i=0;i<Constants.sub_batch.size();i++){
                                        if(Constants.sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                            Toast.makeText(MainActivity.this,"hi"+Constants.sub_batch.get(i),Toast.LENGTH_LONG);
                                            Constants.sub_batch.remove(i );
                                        }
                                    }


                            Toast.makeText(MainActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();
                                    counter_no_efused.setText("" + Constants.data_refused.size());

                                    counter_no_general.setText("" + (Constants.data.size() - Constants.data_refused.size()));
finish.setVisibility(View.VISIBLE);
                                } else {
                                    if (Constants.data_refused.contains(tvNFCContent.getText().toString())) {
                                        Toast.makeText(MainActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();
                                    } else {
                                Toast.makeText(MainActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();

                                        Constants.data_refused.add(tvNFCContent.getText().toString());
//                                        Constants.sub_batch.remove( tvNFCContent.getText().toString() );
                                        for (int i=0;i<Constants.sub_batch.size();i++){
                                            if(Constants.sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                                Toast.makeText(MainActivity.this,"hi"+Constants.sub_batch.get(i),Toast.LENGTH_LONG);

                                                Constants.sub_batch.remove(i );
                                            }
                                        }
                                        counter_no_efused.setText("" + Constants.data_refused.size());
                                        boolean ans2 = Constants.data_refused.isEmpty();

                                        counter_no_general.setText("" + (Constants.data.size() - Constants.data_refused.size()));
                                        finish.setVisibility(View.VISIBLE);
                                    }
                                    System.out.println("The ArrayList is not empty");
                                }


                            } else {
                                Toast.makeText(MainActivity.this, "there is no available boxes", Toast.LENGTH_LONG).show();
                            }
                            System.out.println("The ArrayList is not empty");
                        }
                    }
                }
                if (process.equals("retailer1")) {

                    if (type.equals("0") || classification.equals("0")) {
                        Toast.makeText(MainActivity.this, "please select Type and Classification", Toast.LENGTH_LONG).show();
                    } else {
                        boolean ans = Constants.data.isEmpty();
                        if (ans == true) {

                            Toast.makeText(MainActivity.this, "there is no available boxes", Toast.LENGTH_LONG).show();
                        } else {
                            if (Constants.data.contains(tvNFCContent.getText().toString())) {


                                boolean ans1 = Constants.data_refused.isEmpty();
                                if (ans1 == true) {
                                    Constants.data_refused.add(tvNFCContent.getText().toString());
//                                    Constants.sub_batch.remove( tvNFCContent.getText().toString() );

                                    for (int i=0;i<Constants.sub_batch.size();i++){
                                        if(Constants.sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                            Toast.makeText(MainActivity.this,"hi"+Constants.sub_batch.get(i),Toast.LENGTH_LONG);
                                            Constants.sub_batch.remove(i );
                                        }
                                    }
                                    Toast.makeText(MainActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();
                                    counter_no_efused.setText("" + Constants.data_refused.size());

                                    counter_no_general.setText("" + (Constants.data.size() - Constants.data_refused.size()));
                                    finish.setVisibility(View.VISIBLE);
                                } else {
                                    if (Constants.data_refused.contains(tvNFCContent.getText().toString())) {
                                        Toast.makeText(MainActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();

                                        Constants.data_refused.add(tvNFCContent.getText().toString());
//                                        Constants.sub_batch.remove(tvNFCContent.getText().toString() );
                                        for (int i=0;i<Constants.sub_batch.size();i++){
                                            if(Constants.sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                            Toast.makeText(MainActivity.this,"hi"+Constants.sub_batch.get(i),Toast.LENGTH_LONG);
                                                Constants.sub_batch.remove(i );
                                            }
                                        }
                                        counter_no_efused.setText("" + Constants.data_refused.size());
                                        boolean ans2 = Constants.data_refused.isEmpty();

                                        counter_no_general.setText("" + (Constants.data.size() - Constants.data_refused.size()));
                                        finish.setVisibility(View.VISIBLE);
                                    }
                                    System.out.println("The ArrayList is not empty");
                                }


                            } else {
                                Toast.makeText(MainActivity.this, "there is no available boxes", Toast.LENGTH_LONG).show();
                            }
                            System.out.println("The ArrayList is not empty");
                        }
                    }
                }
//                else if (process.equals("retailer")) {
//
//                    if (type.equals("0") || classification.equals("0")) {
//                        Toast.makeText(MainActivity.this, "please select Type and Classification", Toast.LENGTH_LONG).show();
//                    } else {
//                        boolean ans = Constants.retailer_data.isEmpty();
//                        if (ans == true) {
//                            System.out.println("The ArrayList is empty");
//
//                            Toast.makeText(MainActivity.this, "there is no available boxes", Toast.LENGTH_LONG).show();
//                        } else {
//                            if (Constants.retailer_data.contains(tvNFCContent.getText().toString())) {
//
//
//                                boolean ans1 = Constants.retailer_data_refused.isEmpty();
//                                if (ans1 == true) {
//                                    Constants.retailer_data_refused.add(tvNFCContent.getText().toString());
//                                    Constants.retailer_sub_batch.remove(Constants.retailer_general_master_batch_id + "-" + tvNFCContent.getText().toString() + "-" + type + "-" + classification);
////                            Toast.makeText(MainActivity.this, "تم اضافه الصندوق الي قائمه الرفض1", Toast.LENGTH_LONG).show();
//                                    counter_no_efused.setText("" + Constants.retailer_data_refused.size());
//
//                                    counter_no_general.setText("" + (Constants.retailer_data.size() - Constants.retailer_data_refused.size()));
//
//                                } else {
//                                    if (Constants.retailer_data_refused.contains(tvNFCContent.getText().toString())) {
//                                        Toast.makeText(MainActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();
//                                    } else {
////                                Toast.makeText(MainActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();
//
//                                        Constants.retailer_data_refused.add(tvNFCContent.getText().toString());
//                                        Constants.retailer_sub_batch.remove(Constants.retailer_general_master_batch_id + "-" + tvNFCContent.getText().toString() + "-" + type + "-" + classification);
//                                        counter_no_efused.setText("" + Constants.retailer_data_refused.size());
//                                        boolean ans2 = Constants.retailer_data_refused.isEmpty();
//
//                                        counter_no_general.setText("" + (Constants.retailer_data.size() - Constants.retailer_data_refused.size()));
//
//                                    }
//                                    System.out.println("The ArrayList is not empty");
//                                }
//
//
//                            } else {
//                                Toast.makeText(MainActivity.this, "there is no available boxes", Toast.LENGTH_LONG).show();
//                            }
//                            System.out.println("The ArrayList is not empty");
//                        }
//                    }
//
//
//                }
            }
        });
        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (myTag == null) {
                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
                    } else {
                        write(message.getText().toString(), myTag);
                        Log.d("onClick: myTag   ", "onClick: myTag   " +myTag);
                        Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (FormatException e) {
                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
        }

        readFromIntent(getIntent());

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writeTagFilters = new IntentFilter[]{tagDetected};

    }


    /******************************************************************************
     **********************************Read From NFC Tag***************************
     ******************************************************************************/
    private void readFromIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            if (type.equals("0") || classification.equals("0")) {
                Toast.makeText(MainActivity.this, "please select Type and Classification", Toast.LENGTH_LONG).show();
            } else {
                buildTagViews(msgs);


            }
        }
        }

    private void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) return;

        String text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }
        if (process.equals("farm")) {




            boolean ans = Constants.data.isEmpty();
            if (ans == true) {
                System.out.println("The ArrayList is empty");
//                Constants.data.add(text);
//                Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
//                Constants.data.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);

                Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification+" latitude "+latitude+" longitude "+longitude);
                Constants.data.add( text );
                      x = x + 1;
                counter_no.setText("" + x);
                counter_no_general.setText("" + x);
                tvNFCContent.setText("" + text);

            } else {
                if (Constants.data.contains(text)) {
                    tvNFCContent.setText("" + text);
                } else {


//                    Constants.data.add(text);
//                    Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
//                    Constants.data.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
                    Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification+" latitude "+latitude+" longitude "+longitude);
                    Constants.data.add(text );

                    x = x + 1;
                    counter_no.setText("" + x);
                    tvNFCContent.setText("" + text);
                    boolean ans2 = Constants.data_refused.isEmpty();
                    if (ans2 == true) {
                        counter_no_general.setText("" + x);
                    } else {
                        counter_no_general.setText("" + (x - Constants.data_refused.size()));
                    }
                }
                System.out.println("The ArrayList is not empty");

            }
        }

        else if (process.equals("retailer1")) {
            final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("Set", "");


            boolean ans = Constants.data.isEmpty();
            if (ans == true) {
                System.out.println("The ArrayList is empty");
//                Constants.data.add(text);
//                Constants.sub_batch.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date);
//                Constants.data.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date);
//                for (int i=0;i<Constants.sub_batch.size();i++){
//                    if(Constants.sub_batch.get(i).contains(text)){
//                        Constants.sub_batch.add(Constants.sub_batch.get(i) +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//                    }
//                }


                if (json.isEmpty()) {
                    Toast.makeText(MainActivity.this, "There is something error", Toast.LENGTH_LONG).show();
                } else {
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrPackageData = gson.fromJson(json, type);
                    for (String data : arrPackageData) {
                        if(data.contains(text)){
                        Constants.sub_batch.add(data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
                            Constants.data.     add(text);
                            x1 = x1 + 1;
                            counter_no.setText("" + x1);
                            counter_no_general.setText("" + x1);
                            tvNFCContent.setText("" + text);
                    }else {
                            Toast.makeText(MainActivity.this, "لم يتم اضافه هذا الصندوق في المزرعه من قبل", Toast.LENGTH_LONG).show();

                        }
                        Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                    }
                }


//                Constants.sub_batch.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//                Constants.data.     add(text);
//
//                Constants.sub_batch.add(text +"h");
//                Constants.data.     add(text +"h");


//                x1 = x1 + 1;
//                counter_no.setText("" + x1);
//                counter_no_general.setText("" + x1);
//                tvNFCContent.setText("" + text);

            } else {
                if (Constants.data.contains(text)) {
                    tvNFCContent.setText("" + text);
                } else {
//                    Constants.data.add(text);
//                    Constants.sub_batch.add( text+"-"+Constants.retailer_id+"-"+Constants.retailer_date);
//                    Constants.data.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date);

//                    for (int i=0;i<Constants.sub_batch.size();i++){
//                        if(Constants.sub_batch.get(i).contains(text)){
//                            Constants.sub_batch.add(Constants.sub_batch.get(i) +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//                        }
//                    }



                    if (json.isEmpty()) {
                        Toast.makeText(MainActivity.this, "There is something error", Toast.LENGTH_LONG).show();
                    } else {
                        Type type = new TypeToken<List<String>>() {
                        }.getType();
                        List<String> arrPackageData = gson.fromJson(json, type);
                        for (String data : arrPackageData) {
                            if(data.contains(text)){
                                Constants.sub_batch.add(data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);

                                Constants.data.     add(text );
                                x1 = x1 + 1;
                                counter_no.setText("" + x1);
                                tvNFCContent.setText("" + text);
                                boolean ans2 = Constants.data_refused.isEmpty();
                                if (ans2 == true) {
                                    counter_no_general.setText("" + x1);
                                } else {
                                    counter_no_general.setText("" + (x1 - Constants.data_refused.size()));
                                }
                            }else{
                                Toast.makeText(MainActivity.this, "لم يتم اضافه هذا الصندوق في المزرعه من قبل", Toast.LENGTH_LONG).show();

                            }
                            Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                        }
                    }


//                Constants.sub_batch.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//                Constants.data.     add(text );
//
////                    Constants.sub_batch.add(text +"h");
////                    Constants.data.     add(text +"h");
//                    x1 = x1 + 1;
//                    counter_no.setText("" + x1);
//                    tvNFCContent.setText("" + text);
//                    boolean ans2 = Constants.data_refused.isEmpty();
//                    if (ans2 == true) {
//                        counter_no_general.setText("" + x1);
//                    } else {
//                        counter_no_general.setText("" + (x1 - Constants.data_refused.size()));
//                    }
                }
                System.out.println("The ArrayList is not empty");

            }
        }

//        else if (process.equals("retailer")) {
//
//
//            boolean ans = Constants.retailer_data.isEmpty();
//            if (ans == true) {
//                System.out.println("The ArrayList is empty");
//                Constants.retailer_data.add(text);
//                Constants.retailer_sub_batch.add(Constants.retailer_general_master_batch_id + "-" + text + "-" + type + "-" + classification);
////                Constants.sub_batch.add(text+"-"+Constants.retailer_id+Constants.retailer_date);
//
//                x = x + 1;
//                counter_no.setText("" + x);
//                counter_no_general.setText("" + x);
//                tvNFCContent.setText("" + text);
//
//            } else {
//                if (Constants.retailer_data.contains(text)) {
//                    tvNFCContent.setText("" + text);
//                } else {
//                    Constants.retailer_data.add(text);
//                    Constants.retailer_sub_batch.add(Constants.retailer_general_master_batch_id + "-" + text + "-" + type + "-" + classification);
////                    Constants.sub_batch.add(text+"-"+Constants.retailer_id+Constants.retailer_date);
//
//                    x = x + 1;
//                    counter_no.setText("" + x);
//                    tvNFCContent.setText("" + text);
//                    boolean ans2 = Constants.retailer_data_refused.isEmpty();
//                    if (ans2 == true) {
//                        counter_no_general.setText("" + x);
//                    } else {
//                        counter_no_general.setText("" + (x - Constants.retailer_data_refused.size()));
//                    }
//                }
//                System.out.println("The ArrayList is not empty");
//            }
//
//
//        }
//        if (myTag == null) {
//                pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//                IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//                tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
//                writeTagFilters = new IntentFilter[]{tagDetected};
//            Toast.makeText(context, "try again", Toast.LENGTH_LONG).show();
//        } else {
//            write_nfc(Constants.general_master_batch_id + "-" + tvNFCContent.getText().toString() + "-" + type + "-" + classification + "-" + tvNFCContent.getText().toString());
//        }

//        WriteModeOn();
//        write_nfc(Constants.general_master_batch_id + "-" + tvNFCContent.getText().toString() + "-" + type + "-" + classification+"-"+ tvNFCContent.getText().toString());

    }


    /******************************************************************************
     **********************************Write to NFC Tag****************************
     ******************************************************************************/
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = {createRecord(text)};
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message

        if(ndef.isWritable()){
            ndef.writeNdefMessage(message);
        }else {
           Toast.makeText(MainActivity.this,"we cannot read",Toast.LENGTH_LONG).show();
        }

        // Close the connection
        ndef.close();
    }

    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang = "en";
        byte[] textBytes = text.getBytes();
        byte[] langBytes = lang.getBytes("US-ASCII");
        int langLength = langBytes.length;
        int textLength = textBytes.length;
        byte[] payload = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload);

        return recordNFC;
    }


    @Override
    protected void onNewIntent(Intent intent) {

        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        }






//if(process.equals("farm")) {
//
//    try {
////        NdefRecord[] records = {createRecord(Constants.general_master_batch_id + "-" + tvNFCContent.getText().toString() + "-" + type + "-" + classification)};
//        NdefRecord[] records = {createRecord(Constants.general_master_batch_id + "-" + tvNFCContent.getText().toString() + "-" + type + "-" + classification+" latitude "+latitude+" longitude "+longitude)};
//
//
//        NdefMessage message1 = new NdefMessage(records);
//        // Get an instance of Ndef for the tag.
////            myTag1 = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        Ndef ndef = Ndef.get(myTag);
//        // Enable I/O
//        ndef.connect();
//        // Write the message
//
//        if (ndef.isWritable()) {
//            ndef.writeNdefMessage(message1);
//        } else {
//            Toast.makeText(MainActivity.this, "we cannot read", Toast.LENGTH_LONG).show();
//        }
//
//        // Close the connection
//        ndef.close();
//    } catch (IOException e) {
//        Log.d("IOException", "buildTagViews: IOException  " + e.toString());
//        e.printStackTrace();
//    } catch (FormatException e) {
//        Log.d("FormatException", "buildTagViews: FormatException  " + e.toString());
//
//        e.printStackTrace();
//    }
//
//}
//else if(process.equals("retailer1")){
//    try {
//
//        Toast.makeText(MainActivity.this,tvNFCContent.getText().toString() +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude,Toast.LENGTH_LONG).show();
////        NdefRecord[] records = {createRecord( tvNFCContent.getText().toString() +"-"+Constants.retailer_id+"-"+Constants.retailer_date)};
//        NdefRecord[] records = {createRecord( tvNFCContent.getText().toString() +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude)};
////        NdefRecord[] records = {createRecord( tvNFCContent.getText().toString() +"h")};
//
//        NdefMessage message1 = new NdefMessage(records);
//        // Get an instance of Ndef for the tag.
////            myTag1 = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        Ndef ndef = Ndef.get(myTag);
//        // Enable I/O
//        ndef.connect();
//        // Write the message
//
//        if (ndef.isWritable()) {
//            ndef.writeNdefMessage(message1);
//        } else {
//            Toast.makeText(MainActivity.this, "we cannot read", Toast.LENGTH_LONG).show();
//        }
//
//        // Close the connection
//        ndef.close();
//    } catch (IOException e) {
//        Log.d("IOException", "buildTagViews: IOException  " + e.toString());
//        e.printStackTrace();
//    } catch (FormatException e) {
//        Log.d("FormatException", "buildTagViews: FormatException  " + e.toString());
//
//        e.printStackTrace();
//    }
//}




    }

    @Override
    public void onPause() {
        super.onPause();
        WriteModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
        WriteModeOn();

    }


    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
    private void WriteModeOn() {
        writeMode = true;
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
    }

    /******************************************************************************
     **********************************Disable Write*******************************
     ******************************************************************************/
    private void WriteModeOff() {
        writeMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void connect() {
        Log.d("sadas", "connect: ");
        hud1 = KProgressHUD.create(MainActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        list2.add(1 + "");
        list.add("طماطم");
        list2.add(2 + "");
        list.add("خيار");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item);
        dataAdapter.add("NONE");
        dataAdapter.addAll(list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        list4.add(1 + "");
        list3.add("فرز اول");
        list4.add(2 + "");
        list3.add("فرز تاني");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item);
        dataAdapter1.add("NONE");
        dataAdapter1.addAll(list3);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);

        hud1.dismiss();
    }

    public void write_nfc(String message) {
        try {
            if (myTag == null) {
//                pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//                IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//                tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
//                writeTagFilters = new IntentFilter[]{tagDetected};
                Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
            } else {

//            }
                write(message,myTag);
                Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } catch (FormatException e) {
            Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {

                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                takePhotoFromCamera();
                                break;
//                            case 1:
//                                takePhotoFromCamera();
//                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            ImageView iv = new ImageView(this);
            iv.setImageBitmap(thumbnail);
            linearLayout.addView(iv);
            String path = saveImage(thumbnail);
//                Toast.makeText(MainActivity.this,"Path is  "+path,Toast.LENGTH_LONG).show();


            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos);
                byte[] b = baos.toByteArray();
                encodedString = Base64.encodeToString(b, Base64.DEFAULT);

            } catch (NullPointerException e) {

            } catch (OutOfMemoryError e) {

            }
            Toast.makeText(MainActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }


        File f = new File(wallpaperDirectory, Calendar.getInstance()
                .getTimeInMillis() + ".jpg");
        if (Build.VERSION.SDK_INT >= 23) {
            String[] PERMISSIONS = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if (!hasPermissions(context, PERMISSIONS)) {
                ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, REQUEST);
            } else {
                try {

                    f.createNewFile();
                    FileOutputStream fo = new FileOutputStream(f);
                    fo.write(bytes.toByteArray());
                    MediaScannerConnection.scanFile(this,
                            new String[]{f.getPath()},
                            new String[]{"image/jpeg"}, null);
                    fo.close();
                    Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
//                    Toast.makeText(MainActivity.this,"f.getAbsolutePath() is  "+f.getAbsolutePath(),Toast.LENGTH_LONG).show();

                } catch (IOException e1) {
                    e1.printStackTrace();
                    return "";
                }
            }
        } else {
            try {

                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                MediaScannerConnection.scanFile(this,
                        new String[]{f.getPath()},
                        new String[]{"image/jpeg"}, null);
                fo.close();
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
                Toast.makeText(MainActivity.this, "f.getAbsolutePath() is  " + f.getAbsolutePath(), Toast.LENGTH_LONG).show();
                return f.getAbsolutePath();
            } catch (IOException e1) {
                e1.printStackTrace();
                return "";
            }
        }


        return f.getAbsolutePath();
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

}