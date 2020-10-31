package com.nfc.connect.nfc.Retailer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nfc.connect.nfc.Constants;
//import com.example.peng.nfcreadwrite.Farm.ProductLoad_RetailerPieceActivity;

import com.example.connect.nfc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;


import com.nfc.connect.nfc.Farm.DBFarmHelper;
import com.nfc.connect.nfc.Farm.DBFarmHelper1;
import com.nfc.connect.nfc.Farm.Farmer_Data;
import com.nfc.connect.nfc.Farm.ProductLoad_FarmPieceActivity;
import com.nfc.connect.nfc.GpsTracker;
import com.nfc.connect.nfc.LocationTrack;
import com.nfc.connect.nfc.LocationTrack1;
import com.nfc.connect.nfc.ScannerActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class ProductLoad_RetailerPieceActivity extends AppCompatActivity {
    Button refuse_last,reset_load,finish_load,scan;
    TextView total,available,refused,tvNFCContent;
    ImageView farm_back;
    Context context;
    private static final int REQUEST = 112;
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    String type;
    String classification;
    private static final String encryptionKey           = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding       = "UTF-8";
    private static final String cipherTransformation    = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";
//    public static int x = 0;
//    public static int x1 = 0;
    static SharedPreferences sharedPreferences;
    HoloCircleSeekBar picker;
    private DBRetailHelper mydb ;
    private DBRetailHelper1 mydb1 ;
    private DBFarmHelper mydb2 ;
    private DBFarmHelper1 mydb3 ;
    LocationTrack1 locationTrack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_load__retailer);
        context = this;
        transparentStatusAndNavigation();
        mydb = new DBRetailHelper(ProductLoad_RetailerPieceActivity.this);
        mydb1 = new DBRetailHelper1(ProductLoad_RetailerPieceActivity.this);
        mydb2 = new DBFarmHelper(ProductLoad_RetailerPieceActivity.this);
        mydb3 = new DBFarmHelper1(ProductLoad_RetailerPieceActivity.this);
        locationTrack = new LocationTrack1(ProductLoad_RetailerPieceActivity.this);
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
//        Constants.retailer_sub_batch.clear();
//        Constants.retailer_data.clear();
//        x=0;
        picker = (HoloCircleSeekBar) findViewById(R.id.pp);
//        HoloCircleSeekBar pp=(HoloCircleSeekBar)findViewById(R.id.pic)
//        HoloSeekBar picker = (HoloSeekBar) findViewById(R.id.picker);
        picker.getValue();
        picker.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) {
                 }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                if(picker.getValue()== mydb.numberOfRows()){

                }else {
                    picker.setValue(mydb.numberOfRows());
                }
            }
        });
        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
//        Toast.makeText(ProductLoad_RetailerPieceActivity.this,"type "+getIntent().getStringExtra("type"),Toast.LENGTH_LONG).show();
//        Toast.makeText(ProductLoad_RetailerPieceActivity.this,"classifiction "+getIntent().getStringExtra("classification"),Toast.LENGTH_LONG).show();
        type=getIntent().getStringExtra("type");
        classification=getIntent().getStringExtra("classification");
        refused=(TextView)findViewById(R.id.refused);
        available=(TextView)findViewById(R.id.available);
        total=(TextView)findViewById(R.id.total);
        available.setText(""+mydb.numberOfRows());
        total.setText(""+(Integer.parseInt(String.valueOf(mydb.numberOfRows()))+Integer.parseInt(String.valueOf(mydb1.numberOfRows()))));
        refused.setText(""+mydb1.numberOfRows());

        picker.setValue(mydb.numberOfRows());
        farm_back=(ImageView) findViewById(R.id.farm_back2);
        farm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ProductLoad_RetailerPieceActivity.this)
//set icon
                        .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                        .setTitle("الرجوع")
//set message
                        .setMessage("هل تريد الرجوع")
//set positive button
                        .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int ii) {
//                                Constants.farmer_id="";



                              

                                Intent mainIntent = new Intent(ProductLoad_RetailerPieceActivity.this, Product_RetailerActivity.class);
                                ProductLoad_RetailerPieceActivity.this.startActivity(mainIntent);
                                ProductLoad_RetailerPieceActivity.this.finish();
                            }
                        })
//set negative button
                        .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //set what should happen when negative button is clicked
                            }
                        })
                        .show();
            }
        });







        refuse_last=(Button) findViewById(R.id.refuse_last);
        refuse_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               
                    AlertDialog alertDialog = new AlertDialog.Builder(ProductLoad_RetailerPieceActivity.this)
//set icon
                            .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                            .setTitle("هل تريد حذف اخر صندوق ")
//set message
                            .setMessage("هذا الصندوق لن يكون متاح ")
//set positive button
                            .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int ii) {
                                    if (mydb.numberOfRows()==0) {


                                        Toast.makeText(ProductLoad_RetailerPieceActivity.this, "لايوجد صناديق متاحه", Toast.LENGTH_LONG).show();
                                    } else {
                                        Boolean exist=false;

                                        List<Retail_Data> contacts = mydb.getAllFarms();
                                        for (Retail_Data cn : contacts) {
                                            if (cn.getBox_id().equals(tvNFCContent.getText().toString())) {
                                                exist = true;
                                            }
                                        }
                                        if (exist==true) {

                                            if(mydb1.numberOfRows()==0){
                                                mydb = new DBRetailHelper(ProductLoad_RetailerPieceActivity.this);
                                                List<Retail_Data> contactsq = mydb.getAllFarms();
                                                mydb.deleteFarm1(contactsq.get(contactsq.size()-1));
                                                mydb1.addFarm(new Retail_Data1(tvNFCContent.getText().toString()));


                                                Toast.makeText(ProductLoad_RetailerPieceActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();
                                                refused.setText("" + mydb1.numberOfRows());

                                                available.setText("" + mydb.numberOfRows());
                                                picker.setValue(Float.parseFloat(String.valueOf(mydb.numberOfRows())));
                                                refuse_last.setClickable(false);
                                                refuse_last.setEnabled(false);

                                            } else {
                                                Boolean exist1=false;
                                                List<Retail_Data1> contacts1 = mydb1.getAllFarms();
                                                for (Retail_Data1 cn : contacts1) {
                                                    if (cn.getRejected().equals(tvNFCContent.getText().toString())) {
                                                        exist1 = true;
                                                    }
                                                }
                                                if (exist1==true) {
//                                            if (Constants.data_refused.contains(tvNFCContent.getText().toString())) {
                                                    Toast.makeText(ProductLoad_RetailerPieceActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();
                                                } else {
//                                                    Toast.makeText(ProductLoad_RetailerPieceActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();

                                                    mydb = new DBRetailHelper(ProductLoad_RetailerPieceActivity.this);
                                                    List<Retail_Data> contactsq = mydb.getAllFarms();
                                                    mydb.deleteFarm1(contactsq.get(contactsq.size()-1));
                                                    mydb1.addFarm(new Retail_Data1(tvNFCContent.getText().toString()));


                                                    Toast.makeText(ProductLoad_RetailerPieceActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();
                                                    refused.setText("" + mydb1.numberOfRows());

                                                    available.setText("" + mydb.numberOfRows());
                                                    picker.setValue(Float.parseFloat(String.valueOf(mydb.numberOfRows())));
                                                    refuse_last.setClickable(false);
                                                    refuse_last.setEnabled(false);
                                                }
                                                System.out.println("The ArrayList is not empty");
                                            }


                                        } else {
                                            Toast.makeText(ProductLoad_RetailerPieceActivity.this, "لم يخزن الصندوف او تم رفضه من قبل ", Toast.LENGTH_LONG).show();
                                        }
                                        System.out.println("The ArrayList is not empty");
                                    }
                                }
                            })
//set negative button
                            .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what should happen when negative button is clicked
                                }
                            })
                            .show();

                
            }


        });
        reset_load=(Button) findViewById(R.id.reset_load);
        reset_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Constants.farmer_id="";
//                Constants.truck_id="";
//                Constants.retailer_id="";
//                Constants.check_farm=false;
//                Constants.date="";
//                Constants.retailer_date="";
//                Constants.general_master_batch_id="";
//                Constants.retailer_general_master_batch_id="";

                Constants.retailer_sub_batch.clear();
                Constants.retailer_data.clear();
                Constants.retailer_data_refused.clear();
                Constants.x1=0;
                available.setText("0");
                total.setText("0");
                refused.setText("0");

                picker.setValue(0);
//                sharedPreferences.edit().clear().commit();




                mydb = new DBRetailHelper(ProductLoad_RetailerPieceActivity.this);
//                ArrayList array_list = mydb.getAllFarm();
                List<Retail_Data> contacts = mydb.getAllFarms();
//                Toast.makeText(ProductLoad_RetailerPieceActivity.this,"befor  "+mydb.numberOfRows(),Toast.LENGTH_LONG).show();

                for (Retail_Data cn : contacts) {
                    String log = "Id: " + cn.getFarm_id() + " ,type: " + cn.getType() + " ,classification: " +
                            cn.getQuality()+ " ,box_id: " +
                            cn.getBox_id();
                    // Writing Contacts to log
                    Log.d("onClick: mydb: ", log);
                    mydb.deleteFarm1(cn);

                }



                mydb1 = new DBRetailHelper1(ProductLoad_RetailerPieceActivity.this);
//                ArrayList array_list = mydb.getAllFarm();
                List<Retail_Data1> contacts1 = mydb1.getAllFarms();
//                Toast.makeText(ProductLoad_RetailerPieceActivity.this,"befor  "+mydb.numberOfRows(),Toast.LENGTH_LONG).show();

                for (Retail_Data1 cn : contacts1) {
                    String log = "Id: " + cn.getFarm_id() + " ,rejected: " + cn.getRejected();
                    // Writing Contacts to log
                    Log.d("onClick: mydb: ", log);
                    mydb1.deleteFarm1(cn);

                }
                refuse_last.setClickable(false);
                refuse_last.setEnabled(false);
                
            }
        });
        finish_load=(Button) findViewById(R.id.finish_load);
        finish_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(available.getText().toString().equals("0")){
                    Toast.makeText(ProductLoad_RetailerPieceActivity.this,"من فضلك اختار بعض الصناديق",Toast.LENGTH_LONG).show();
                }else {
               
                    Intent mainIntent = new Intent(ProductLoad_RetailerPieceActivity.this, ProductLoadResult_RetailerActivity.class);
                    mainIntent.putExtra("available", "" + available.getText().toString());
                    mainIntent.putExtra("type", "" + type);
                    mainIntent.putExtra("classification", "" + classification);

                    ProductLoad_RetailerPieceActivity.this.startActivity(mainIntent);
                }
//                ProductLoad_RetailerPieceActivity.this.finish();
//                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ProductLoad_RetailerPieceActivity.this, R.style.myDialog));


            }
//                else if (process.equals("retailer1")) {
//                    Gson gson = new Gson();
//                    String json = gson.toJson(Constants.retailer_sub_batch);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("Set1", json);
//                    editor.commit();
//                    reset.setVisibility(View.VISIBLE);
//                }





        });

        scan=(Button)findViewById(R.id.scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new IntentIntegrator(ProductLoad_RetailerPieceActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
            }
        });
        refuse_last.setClickable(false);
        refuse_last.setEnabled(false);

        if(mydb.numberOfRows()>0){

            picker.setMax(Integer.parseInt(String.valueOf(mydb.numberOfRows()))+10);
            picker.setValue(Float.parseFloat(String.valueOf(mydb.numberOfRows())));
        }else{
            picker.setMax(10);
        }



//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (nfcAdapter == null) {
//            // Stop here, we definitely need NFC
//            Toast.makeText(this, "هذا الجهاز لا يدعم NFC", Toast.LENGTH_LONG).show();
//            finish();
//        }
//
//        readFromIntent(getIntent());
//
//        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
//        writeTagFilters = new IntentFilter[]{tagDetected};
    }
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

            buildTagViews(msgs);



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












//        Constants.general_master_batch_id=Constants.farmer_id+"-"+Constants.truck_id+"-"+Constants.date;
//        boolean ans = Constants.retailer_data.isEmpty();
//        if (ans == true) {
//            System.out.println("The ArrayList is empty");
////                Constants.retailer_data.add(text);
////                Constants.retailer_sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
////                Constants.retailer_data.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
//
//            Constants.retailer_sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification+" latitude "+Constants.farm_latitude+" longitude "+Constants.farm_longitude);
//            Constants.retailer_data.add( text );
//            x = x + 1;
//            available.setText("" + x);
//            total.setText("" + x);
//            picker.setValue(Float.parseFloat(String.valueOf(x)));
//            tvNFCContent.setText("" + text);
//
//        } else {
//            if (Constants.retailer_data.contains(text)) {
//                tvNFCContent.setText("" + text);
//            } else {
//
//
////                    Constants.retailer_data.add(text);
////                    Constants.retailer_sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
////                    Constants.retailer_data.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
//                Constants.retailer_sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification+" latitude "+Constants.farm_latitude+" longitude "+Constants.farm_longitude);
//                Constants.retailer_data.add(text );
//
//                x = x + 1;
//                available.setText("" + x);
//                tvNFCContent.setText("" + text);
//                boolean ans2 = Constants.retailer_data_refused.isEmpty();
//                if (ans2 == true) {
//                    total.setText("" + x);
//                    picker.setValue(Float.parseFloat(String.valueOf(x)));
//                } else {
//                    total.setText("" + (x - Constants.retailer_data_refused.size()));
//                    picker.setValue(Float.parseFloat(String.valueOf((x - Constants.retailer_data_refused.size()))));
//                }
//            }
//            System.out.println("The ArrayList is not empty");
//
//        }












        if(text.startsWith("B")) {
            Constants.retailer_another=0;
            final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString("Set", "");


            boolean ans = Constants.retailer_data.isEmpty();
            if (ans == true) {
                System.out.println("The ArrayList is empty");
//                Constants.retailer_data.add(text);
//                Constants.retailer_sub_batch.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date);
//                Constants.retailer_data.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date);
//                for (int i=0;i<Constants.retailer_sub_batch.size();i++){
//                    if(Constants.retailer_sub_batch.get(i).contains(text)){
//                        Constants.retailer_sub_batch.add(Constants.retailer_sub_batch.get(i) +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//                    }
//                }


                if (json.isEmpty()) {
                    Toast.makeText(ProductLoad_RetailerPieceActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
                } else {
                    Type type1 = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrPackageData = gson.fromJson(json, type1);
                    for (String data : arrPackageData) {
                        if (data.contains(text) && data.contains(type) && data.contains(classification)) {
                            Constants.retailer_sub_batch.add(data + "-" + Constants.retailer_id + "-" + Constants.retailer_date + " latitude " + Constants.retailer_latitude + " longitude " + Constants.retailer_longitud);
                            Constants.retailer_data.add(text);
                            Constants.x1 = Constants.x1 + 1;
                            available.setText("" + Constants.x1);
                            total.setText("" + Constants.x1);
                            picker.setValue(Float.parseFloat(String.valueOf(Constants.x1)));
                            tvNFCContent.setText("" + text);
                        } else {
//                            Toast.makeText(ProductLoad_RetailerPieceActivity.this, "لم يتم اضافه هذا الصندوق في المزرعه من قبل", Toast.LENGTH_LONG).show();

                        }
                        Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                    }
                }


//                Constants.retailer_sub_batch.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//                Constants.retailer_data.     add(text);
//
//                Constants.retailer_sub_batch.add(text +"h");
//                Constants.retailer_data.     add(text +"h");


//                x1 = x1 + 1;
//                counter_no.setText("" + x1);
//                counter_no_general.setText("" + x1);
//                tvNFCContent.setText("" + text);

            } else {
                if (Constants.retailer_data.contains(text)) {
                    Toast.makeText(ProductLoad_RetailerPieceActivity.this,"هذا الصندوق مضاف من قبل ",Toast.LENGTH_LONG).show();

                    tvNFCContent.setText("" + text);
                } else {
//                    Constants.retailer_data.add(text);
//                    Constants.retailer_sub_batch.add( text+"-"+Constants.retailer_id+"-"+Constants.retailer_date);
//                    Constants.retailer_data.add(text +"-"+Constants.retailer_id+"-"+Constants.retailer_date);

//                    for (int i=0;i<Constants.retailer_sub_batch.size();i++){
//                        if(Constants.retailer_sub_batch.get(i).contains(text)){
//                            Constants.retailer_sub_batch.add(Constants.retailer_sub_batch.get(i) +"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//                        }
//                    }


                    if (json.isEmpty()) {
                        Toast.makeText(ProductLoad_RetailerPieceActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
                    } else {
                        Type type1 = new TypeToken<List<String>>() {
                        }.getType();
                        List<String> arrPackageData = gson.fromJson(json, type1);
                        for (String data : arrPackageData) {
                            if (data.contains(text) && data.contains(type) && data.contains(classification)) {
                                Constants.retailer_sub_batch.add(data + "-" + Constants.retailer_id + "-" + Constants.retailer_date + " latitude " + Constants.retailer_latitude + " longitude " + Constants.retailer_longitud);

                                Constants.retailer_data.add(text);
                                Constants.x1 = Constants.x1 + 1;
                                available.setText("" + Constants.x1);
                                tvNFCContent.setText("" + text);
                                boolean ans2 = Constants.retailer_data_refused.isEmpty();
                                if (ans2 == true) {
                                    total.setText("" + Constants.x1);
                                    picker.setValue(Float.parseFloat(String.valueOf(Constants.x1)));
                                } else {
                                    total.setText("" + Constants.x1);
                                    available.setText("" + (Constants.x1 - Constants.retailer_data_refused.size()));
                                    picker.setValue(Float.parseFloat(String.valueOf((Constants.x1 - Constants.retailer_data_refused.size()))));
                                }
                            } else {
//                                Toast.makeText(ProductLoad_RetailerPieceActivity.this, "لم يتم اضافه هذا الصندوق في المزرعه من قبل", Toast.LENGTH_LONG).show();

                            }
                            Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                        }
                    }

                }
                System.out.println("The ArrayList is not empty");

            }

        }
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
            Toast.makeText(ProductLoad_RetailerPieceActivity.this,"لا نستطيع القراءه",Toast.LENGTH_LONG).show();
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


    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(Intent intent) {

        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        }

    }

    @Override
    public void onPause() {
        super.onPause();
//        WriteModeOff();
    }

    @Override
    public void onResume() {
        super.onResume();
//        WriteModeOn();

    }


    @Override
    public void onBackPressed() {


        AlertDialog alertDialog = new AlertDialog.Builder(ProductLoad_RetailerPieceActivity.this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("الرجوع")
//set message
                .setMessage("هل تريد الرجوع")
//set positive button
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
//                                Constants.farmer_id="";


                        Intent mainIntent = new Intent(ProductLoad_RetailerPieceActivity.this, Product_RetailerActivity.class);
                        ProductLoad_RetailerPieceActivity.this.startActivity(mainIntent);
                        ProductLoad_RetailerPieceActivity.this.finish();
                    }
                })
//set negative button
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what should happen when negative button is clicked
                    }
                })
                .show();

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "تم الغاء الفحص", Toast.LENGTH_LONG).show();
            } else {
                connect(result.getContents());
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void connect(String result) {
        Log.d("result  ", "result  " + result);
        final String[] parts = result.split(" / ");
        int replace = result.length() - result.replace(" / ", "").length();
        System.out.println("replace = " + parts.length);
        double latitude=0.0;
        double longitude=0.0;
        locationTrack = new LocationTrack1(ProductLoad_RetailerPieceActivity.this);
        Log.d("locationTrack)", "connect: locationTrack.getLatitude()1"+locationTrack.getLatitude() + "," + locationTrack.getLongitude());

        if (locationTrack.canGetLocation()) {


            Log.d("locationTrack)", "connect: locationTrack.getLatitude()" + locationTrack.getLatitude() + "," + locationTrack.getLongitude());

            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();




            GpsTracker gpsTracker = new GpsTracker(this);

            if (gpsTracker.canGetLocation())
            {

                latitude=gpsTracker.getLatitude();
                longitude=gpsTracker.getLongitude();
                Log.d("locationTrack)", "connect: locationTrack.getLatitude()5 " + gpsTracker.getLatitude() + "," + gpsTracker.getLongitude());

                if (parts.length == 4) {
            if ( parts[1].contains("http://bit.ly/2xnoFZR") ) {

                String decrypted_text=decrypt(parts[3]);
                if (parts[2].equals("2011") || parts[2].equals("2012") || parts[2].equals("2013")) {
                    Toast.makeText(ProductLoad_RetailerPieceActivity.this, "من فضلك اتاكد من الكود ", Toast.LENGTH_LONG).show();

                } else {
                    if (isNumeric(decrypted_text)) {
                        String text = result;
                        tvNFCContent.setText(text);
                        mydb = new DBRetailHelper(ProductLoad_RetailerPieceActivity.this);
                        if (mydb.numberOfRows()==0) {
                            Boolean exist=false;
                            List<Retail_Data1> contacts1 = mydb1.getAllFarms();
                            for (Retail_Data1 cn : contacts1) {
                                if (cn.getRejected().equals(tvNFCContent.getText().toString())) {
                                    exist = true;
                                }
                            }
                            if (exist==true) {
                                Toast.makeText(ProductLoad_RetailerPieceActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();

                            }else {



                                Boolean exist2=false;

                                List<Farmer_Data> contacts2 = mydb2.getAllFarms();
                                for (Farmer_Data cn : contacts2) {
                                    if (cn.getBox_id().equals(text)) {
                                        exist2 = true;
                                    }
                                }
                                if (exist2==false) {




                                    Toast.makeText(ProductLoad_RetailerPieceActivity.this, "هذا الصندوق ليس مضاف من قبل المزرعه او تم رفضه من المزرعه ", Toast.LENGTH_LONG).show();
                                    tvNFCContent.setText("" + text);
                                } else {

                                    Boolean exist3=false;

                                    List<Farmer_Data> contacts3 = mydb2.getAllFarms();
                                    for (Farmer_Data cn : contacts3) {
                                        if (cn.getBox_id().equals(text)) {
                                            exist3 = true;
                                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                            Date date = new Date();
                                            System.out.println(dateFormat.format(date));
                                            Log.d("cn.getType()", "onActivityResult:cn.getType() "+cn.getType());
                                            Log.d("cn.getType()", "onActivityResult:cn.getType()1 "+cn.getQuantity());
                                            Log.d("cn.getType()", "onActivityResult:cn.getType()2 "+cn.getFarmer_id());
                                            Log.d("cn.getType()", "onActivityResult:cn.getType()3 "+cn.getLoading_date());
                                            Log.d("cn.getType()", "onActivityResult:cn.getType()4 "+cn.getLoading_date());
                                            mydb.addFarm(new Retail_Data(cn.getFarmer_id(),cn.getType(), cn.getQuantity(),
                                                    cn.getLoading_date(), cn.getTruck_id(), cn.getLocation() , text,
                                                    "20",Constants.retailer_id,dateFormat.format(date),latitude + "," + longitude));
                                            available.setText("" + mydb.numberOfRows());
                                            tvNFCContent.setText("" + text);

                                            total.setText("" + (Integer.parseInt(String.valueOf(mydb.numberOfRows())) + Integer.parseInt(String.valueOf(mydb1.numberOfRows()))));
                                            picker.setMax(Integer.parseInt(String.valueOf(mydb.numberOfRows()))+10);
                                            picker.setValue(Float.parseFloat(String.valueOf(mydb.numberOfRows())));
                                            refuse_last.setClickable(true);
                                            refuse_last.setEnabled(true);
                                        }
                                    }




                            }}
                        } else {

                            Boolean exist=false;

                            List<Retail_Data> contacts = mydb.getAllFarms();
                            for (Retail_Data cn : contacts) {
                                if (cn.getBox_id().equals(text)) {
                                    exist = true;
                                }
                            }
                            if (exist==true) {




                                Toast.makeText(ProductLoad_RetailerPieceActivity.this, "هذا الصندوق مضاف  ", Toast.LENGTH_LONG).show();
                                tvNFCContent.setText("" + text);
                            } else {
                                List<Retail_Data1> contacts1 = mydb1.getAllFarms();
                                for (Retail_Data1 cn : contacts1) {
                                    if (cn.getRejected().equals(tvNFCContent.getText().toString())) {
                                        exist = true;
                                    }
                                }
                                if (exist==true) {
                                    Toast.makeText(ProductLoad_RetailerPieceActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();

                                }else{
                                    Boolean exist2=false;

                                    List<Farmer_Data> contacts2 = mydb2.getAllFarms();
                                    for (Farmer_Data cn : contacts2) {
                                        if (cn.getBox_id().equals(text)) {
                                            exist2 = true;
                                        }
                                    }
                                    if (exist2==false) {




                                        Toast.makeText(ProductLoad_RetailerPieceActivity.this, "هذا الصندوق ليس مضاف من قبل المزرعه او تم رفضه من المزرعه ", Toast.LENGTH_LONG).show();
                                        tvNFCContent.setText("" + text);
                                    } else {

                                        Boolean exist3=false;

                                        List<Farmer_Data> contacts3 = mydb2.getAllFarms();
                                        for (Farmer_Data cn : contacts3) {
                                            if (cn.getBox_id().equals(text)) {
                                                exist3 = true;
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Date date = new Date();
                                                System.out.println(dateFormat.format(date));
                                                Log.d("cn.getType()", "onActivityResult:cn.getType() "+cn.getType());
                                                Log.d("cn.getType()", "onActivityResult:cn.getType()1 "+cn.getQuantity());
                                                Log.d("cn.getType()", "onActivityResult:cn.getType()2 "+cn.getFarmer_id());
                                                Log.d("cn.getType()", "onActivityResult:cn.getType()3 "+cn.getLoading_date());
                                                Log.d("cn.getType()", "onActivityResult:cn.getType()4 "+cn.getLoading_date());
                                                mydb.addFarm(new Retail_Data(cn.getFarmer_id(),cn.getType(), cn.getQuantity(),
                                                        cn.getLoading_date(), cn.getTruck_id(), cn.getLocation() , text,
                                                        "20",Constants.retailer_id,dateFormat.format(date),latitude + "," + longitude));
                                                available.setText("" + mydb.numberOfRows());
                                                tvNFCContent.setText("" + text);

                                                total.setText("" + (Integer.parseInt(String.valueOf(mydb.numberOfRows())) + Integer.parseInt(String.valueOf(mydb1.numberOfRows()))));
                                                picker.setMax(Integer.parseInt(String.valueOf(mydb.numberOfRows()))+10);
                                                picker.setValue(Float.parseFloat(String.valueOf(mydb.numberOfRows())));
                                                refuse_last.setClickable(true);
                                                refuse_last.setEnabled(true);
                                            }
                                        }
                                }
                                }}
                            System.out.println("The ArrayList is not empty");

                        }



                    } else {
                        Toast.makeText(ProductLoad_RetailerPieceActivity.this, "من فضلك اتاكد من الكود ", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(ProductLoad_RetailerPieceActivity.this, "من فضلك اتاكد من الكود ", Toast.LENGTH_LONG).show();
            }
        } else

                {

                    gpsTracker.showSettingsAlert();
                }}else {
            Toast.makeText(ProductLoad_RetailerPieceActivity.this, "من فضلك اتاكد من الكود ", Toast.LENGTH_LONG).show();


        }  } else

        {

            locationTrack.showSettingsAlert();
        }
    }


    public static String decrypt(String encryptedText) {
        String decryptedText = "";
        try {
            Cipher cipher = Cipher.getInstance(cipherTransformation);
            byte[] key = encryptionKey.getBytes(characterEncoding);
            SecretKeySpec secretKey = new SecretKeySpec(key, aesEncryptionAlgorithem);
            IvParameterSpec ivparameterspec = new IvParameterSpec(key);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
            Base64.Decoder decoder = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                decoder = Base64.getDecoder();
            }
            byte[] cipherText = new byte[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                cipherText = decoder.decode(encryptedText.getBytes("UTF8"));
            }else{
                cipherText =  android.util.Base64.decode(encryptedText.getBytes("UTF8"), android.util.Base64.DEFAULT);

            }
            decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

        } catch (Exception E) {
            System.err.println("decrypt Exception : "+E.getMessage());
        }
        return decryptedText;
    }
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
    private void transparentStatusAndNavigation() {
        //make full transparent statusBar
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
