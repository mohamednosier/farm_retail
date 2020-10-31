package com.nfc.connect.nfc.Truck;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nfc.connect.nfc.Farm.DBFarmHelper;
import com.nfc.connect.nfc.Farm.DBFarmHelper1;
import com.nfc.connect.nfc.Farm.FarmActivity;
import com.example.connect.nfc.R;

import com.nfc.connect.nfc.Farm.Farmer_Data;
import com.nfc.connect.nfc.Retailer.DBRetailHelper;
import com.nfc.connect.nfc.Retailer.DBRetailHelper1;
import com.nfc.connect.nfc.Retailer.ProductLoad_RetailerPieceActivity;
import com.nfc.connect.nfc.Retailer.Retail_Data;
import com.nfc.connect.nfc.Retailer.RetailerActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nfc.connect.nfc.ScannerActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.List;

public class LoadAndUnLoadActivity extends AppCompatActivity {
Button load,unload,check_Data;
    String text ="";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    static SharedPreferences sharedPreferences;
    TextView test_data;
    public static int Counter_person=0;
    boolean writeMode;
    Tag myTag;
    private DBRetailHelper mydb ;
    private DBRetailHelper1 mydb1 ;
    private DBFarmHelper mydb2 ;
    private DBFarmHelper1 mydb3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_and_un_load);
        transparentStatusAndNavigation();
        mydb = new DBRetailHelper(LoadAndUnLoadActivity.this);
        mydb1 = new DBRetailHelper1(LoadAndUnLoadActivity.this);
        mydb2 = new DBFarmHelper(LoadAndUnLoadActivity.this);
        mydb3 = new DBFarmHelper1(LoadAndUnLoadActivity.this);
        load=(Button)findViewById(R.id.load);
        unload=(Button)findViewById(R.id.unload);
        check_Data=(Button)findViewById(R.id.check_Data);
        test_data=(TextView)findViewById(R.id.test_data);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load.setBackgroundColor(Color.GREEN);
                Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, FarmActivity.class);
                LoadAndUnLoadActivity.this.startActivity(mainIntent);
                LoadAndUnLoadActivity.this.finish();
            }
        });
        unload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unload.setBackgroundColor(Color.GREEN);
                Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, RetailerActivity.class);
                LoadAndUnLoadActivity.this.startActivity(mainIntent);
                LoadAndUnLoadActivity.this.finish();
            }
        });
        check_Data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new IntentIntegrator(LoadAndUnLoadActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();

            }
        });
//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (nfcAdapter == null) {
//            // Stop here, we definitely need NFC
//            Toast.makeText(this, "هذا الجهاز لايدعم NFC", Toast.LENGTH_LONG).show();
//            finish();
//        }
//        readFromIntent(getIntent());
//
//        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
//        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
//        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
//        writeTagFilters = new IntentFilter[] { tagDetected };
    }
    @Override
    public void onBackPressed() {

        Intent i=new Intent(LoadAndUnLoadActivity.this, TruckActivity.class);
        startActivity(i);

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

        text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            test_data.setText(text);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.toString());
        }



    }


    /******************************************************************************
     **********************************Write to NFC Tag****************************
     ******************************************************************************/
    private void write(String text, Tag tag) throws IOException, FormatException {
        NdefRecord[] records = { createRecord(text) };
        NdefMessage message = new NdefMessage(records);
        // Get an instance of Ndef for the tag.
        Ndef ndef = Ndef.get(tag);
        // Enable I/O
        ndef.connect();
        // Write the message
        ndef.writeNdefMessage(message);
        // Close the connection
        ndef.close();
    }
    private NdefRecord createRecord(String text) throws UnsupportedEncodingException {
        String lang       = "en";
        byte[] textBytes  = text.getBytes();
        byte[] langBytes  = lang.getBytes("US-ASCII");
        int    langLength = langBytes.length;
        int    textLength = textBytes.length;
        byte[] payload    = new byte[1 + langLength + textLength];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langLength;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1,              langLength);
        System.arraycopy(textBytes, 0, payload, 1 + langLength, textLength);

        NdefRecord recordNFC = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,  NdefRecord.RTD_TEXT,  new byte[0], payload);

        return recordNFC;
    }



    @SuppressLint("MissingSuperCall")
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        readFromIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        }
    }

    @Override
    public void onPause(){
        super.onPause();
//        WriteModeOff();
    }

    @Override
    public void onResume(){
        super.onResume();
//        WriteModeOn();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data2) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data2);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "تم الغاء الفحص", Toast.LENGTH_LONG).show();
            } else {
//                if(x==1){
////show dialogue with result
//                    showResultDialogue1(result.getContents());
//
//                }else if(x==0){
                text=result.getContents();
//                text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
                test_data.setText(text);
//                    connect(result.getContents());
////                    showResultDialogue(result.getContents());
//                }

                String Farmer_id = null;
                String type_=null;
                String quality=null;
                String loading_date=null;
                String truck_id=null;
                String location=null;
                String box_id=null;
                String quantity=null;
                String retiler_id=null;
                String discharge_date=null;
                String retailer_location=null;

                if (mydb.numberOfRows()==0) {
                    if(mydb2.numberOfRows()==0){
                        Toast.makeText(LoadAndUnLoadActivity.this, "لا توجد صناديق مخزنه", Toast.LENGTH_LONG).show();

                    }else {





                        Boolean exist4=false;

                        List<Farmer_Data> contacts4 = mydb2.getAllFarms();
                        for (Farmer_Data cn : contacts4) {
                            if (cn.getBox_id().equals(text)) {
                                exist4 = true;
                                Farmer_id=cn.getFarmer_id();
                                quality=cn.getQuality();
                                type_=cn.getType();
                                loading_date=cn.getLoading_date();
                                truck_id=cn.getTruck_id();
                                location=cn.getLocation();
                                box_id=cn.getBox_id();
                                quantity=cn.getQuantity();

                            }
                        }
                        if (exist4==true) {

                            Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, CheckDataActivity.class);
                            mainIntent.putExtra("type1","farm");
                            mainIntent.putExtra("Farmer_id",Farmer_id);
                            mainIntent.putExtra("quality",quality);
                            mainIntent.putExtra("type",type_);
                            mainIntent.putExtra("loading_date",loading_date);
                            mainIntent.putExtra("truck_id",truck_id);
                            mainIntent.putExtra("location",location);
                            mainIntent.putExtra("box_id",box_id);
                            mainIntent.putExtra("quantity",quantity);
                            mainIntent.putExtra("retiler_id",retiler_id);
                            mainIntent.putExtra("discharge_date",discharge_date);
                            mainIntent.putExtra("retailer_location",retailer_location);


                            LoadAndUnLoadActivity.this.startActivity(mainIntent);
                            LoadAndUnLoadActivity.this.finish();


                            test_data.setText("" + text);
                        }else {
                            Toast.makeText(LoadAndUnLoadActivity.this, "هذا الصندوق لم يخزن بعد من قبل المزرعه او الموزع", Toast.LENGTH_LONG).show();

                        }










                    }
                }else{
                    Boolean exist=false;

                    List<Retail_Data> contacts = mydb.getAllFarms();
                    for (Retail_Data cn : contacts) {
                        if (cn.getBox_id().equals(text)) {
                            exist = true;
                            Farmer_id=cn.getFarmer_id();
                            quality=cn.getQuality();
                            type_=cn.getType();

                            loading_date=cn.getLoading_date();
                            truck_id=cn.getTruck_id();
                            location=cn.getLocation();
                            box_id=cn.getBox_id();
                            quantity=cn.getQuantity();
                            retiler_id=cn.getRetiler_id();
                            discharge_date=cn.getDischarge_date();
                            retailer_location=cn.getRetailer_location();


                            List<Farmer_Data> contacts1 = mydb2.getAllFarms();
                            for (Farmer_Data cn1 : contacts1)
                            {
                                if (cn1.getBox_id().equals(text)) {
                                    type_ = cn1.getType();
                                }
                            }

                            Log.d("cn.getType()", "onActivityResult:cn.Farmer_id "+Farmer_id);
                            Log.d("cn.getType()", "onActivityResult:cn.getType() "+cn.getType());
                            Log.d("cn.getType()", "onActivityResult:cn.quality "+quality);
                            Log.d("cn.getType()", "onActivityResult:cn.loading_date "+loading_date);
                            Log.d("cn.getType()", "onActivityResult:cn.truck_id "+truck_id);
                            Log.d("cn.getType()", "onActivityResult:cn.location "+location);
                            Log.d("cn.getType()", "onActivityResult:cn.box_id "+box_id);
                            Log.d("cn.getType()", "onActivityResult:cn.retiler_id "+retiler_id);
                            Log.d("cn.getType()", "onActivityResult:cn.discharge_date "+discharge_date);
                            Log.d("cn.getType()", "onActivityResult:cn.retailer_location "+retailer_location);


                        }
                    }
                    if (exist==true) {

                        Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, CheckDataActivity.class);
                        mainIntent.putExtra("type1","retailer");
                        mainIntent.putExtra("Farmer_id",Farmer_id);
                        mainIntent.putExtra("quality",quality);
                        mainIntent.putExtra("type",type_);
                        mainIntent.putExtra("loading_date",loading_date);
                        mainIntent.putExtra("truck_id",truck_id);
                        mainIntent.putExtra("location",location);
                        mainIntent.putExtra("box_id",box_id);
                        mainIntent.putExtra("quantity",quantity);
                        mainIntent.putExtra("retiler_id",retiler_id);
                        mainIntent.putExtra("discharge_date",discharge_date);
                        mainIntent.putExtra("retailer_location",retailer_location);


                        LoadAndUnLoadActivity.this.startActivity(mainIntent);
                        LoadAndUnLoadActivity.this.finish();


                         test_data.setText("" + text);
                    }else {
                        if (mydb2.numberOfRows() == 0) {
                            Toast.makeText(LoadAndUnLoadActivity.this, "لا توجد صناديق مخزنه", Toast.LENGTH_LONG).show();

                        } else {

                            Boolean exist4=false;

                            List<Farmer_Data> contacts4 = mydb2.getAllFarms();
                            for (Farmer_Data cn : contacts4) {
                                if (cn.getBox_id().equals(text)) {
                                    exist4 = true;
                                    Farmer_id=cn.getFarmer_id();
                                    quality=cn.getQuality();
                                    type_=cn.getType();
                                    loading_date=cn.getLoading_date();
                                    truck_id=cn.getTruck_id();
                                    location=cn.getLocation();
                                    box_id=cn.getBox_id();
                                    quantity=cn.getQuantity();

                                }
                            }
                            if (exist4==true) {

                                Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, CheckDataActivity.class);
                                mainIntent.putExtra("type1","farm");
                                mainIntent.putExtra("Farmer_id",Farmer_id);
                                mainIntent.putExtra("quality",quality);
                                mainIntent.putExtra("type",type_);
                                mainIntent.putExtra("loading_date",loading_date);
                                mainIntent.putExtra("truck_id",truck_id);
                                mainIntent.putExtra("location",location);
                                mainIntent.putExtra("box_id",box_id);
                                mainIntent.putExtra("quantity",quantity);
                                mainIntent.putExtra("retiler_id",retiler_id);
                                mainIntent.putExtra("discharge_date",discharge_date);
                                mainIntent.putExtra("retailer_location",retailer_location);


                                LoadAndUnLoadActivity.this.startActivity(mainIntent);
                                LoadAndUnLoadActivity.this.finish();


                                test_data.setText("" + text);
                            }else {
                                Toast.makeText(LoadAndUnLoadActivity.this, "هذا الصندوق لم يخزن بعد من قبل المزرعه او الموزع", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }



//                Gson gson = new Gson();
//                String json = sharedPreferences.getString("Set1", "");
//                if (json.isEmpty()) {
////                    Toast.makeText(LoadAndUnLoadActivity.this, "There is something error1", Toast.LENGTH_LONG).show();
//
//
//                    //farmer
//                    Gson gson1 = new Gson();
//                    String json1 = sharedPreferences.getString("Set", "");
//                    if (json1.isEmpty()) {
//                        Toast.makeText(LoadAndUnLoadActivity.this, "لا توجد صناديق مخزنه", Toast.LENGTH_LONG).show();
//                    } else {
//                        Type type1 = new TypeToken<List<String>>() {
//                        }.getType();
//                        List<String> arrPackageData1 = gson1.fromJson(json1, type1);
//                        for (String data1 : arrPackageData1) {
//                            Log.d("readSharedPreference", "onClick: readSharedPreference farmer" + data1);
//                            if(data1.contains(test_data.getText().toString())){
////                                Toast.makeText(LoadAndUnLoadActivity.this,"yes exist",Toast.LENGTH_LONG).show();
////                            Intent i=new Intent(LoadAndUnLoadActivity.this,CheckDataActivity.class);
//
//                                Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, CheckDataActivity.class);
//                                mainIntent.putExtra("type","farm");
//                                mainIntent.putExtra("data",data1);
//                                mainIntent.putExtra("text",test_data.getText().toString());
//
//                                LoadAndUnLoadActivity.this.startActivity(mainIntent);
//                                LoadAndUnLoadActivity.this.finish();
//                            }else{
//                                Toast.makeText(LoadAndUnLoadActivity.this, "هذا الصندوق لم يخزن بعد ", Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    }  //finish farm
//
//                } else {
//                    Type type = new TypeToken<List<String>>() {
//                    }.getType();
//                    List<String> arrPackageData = gson.fromJson(json, type);
//                    for (String data : arrPackageData) {
//                        Log.d("readSharedPreference", "onClick: readSharedPreference retailer" + data);
////                        Toast.makeText(LoadAndUnLoadActivity.this,""+data,Toast.LENGTH_LONG).show();
//                        if(data.contains(test_data.getText().toString())){
////                            Intent i=new Intent(LoadAndUnLoadActivity.this,CheckDataActivity.class);
////     i.putExtra("type","retailer");
////i.putExtra("data",data);
////i.putExtra("text",test_data.getText().toString());
//
//                            Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, CheckDataActivity.class);
//                            mainIntent.putExtra("type","retailer");
//                            mainIntent.putExtra("data",data);
//                            mainIntent.putExtra("text",test_data.getText().toString());
//
//                            LoadAndUnLoadActivity.this.startActivity(mainIntent);
//                            LoadAndUnLoadActivity.this.finish();
//                        }else{
//
//
//                            //farmer
//                            Gson gson1 = new Gson();
//                            String json1 = sharedPreferences.getString("Set", "");
//                            if (json1.isEmpty()) {
//                                Toast.makeText(LoadAndUnLoadActivity.this, "لاتوجد صناديق مخزنه", Toast.LENGTH_LONG).show();
//                            } else {
//                                Type type1 = new TypeToken<List<String>>() {
//                                }.getType();
//                                List<String> arrPackageData1 = gson1.fromJson(json1, type1);
//                                for (String data1 : arrPackageData1) {
//                                    Log.d("readSharedPreference", "onClick: readSharedPreference farmer" + data1);
//                                    if(data1.contains(test_data.getText().toString())){
////                 Toast.makeText(LoadAndUnLoadActivity.this,"yes exist1",Toast.LENGTH_LONG).show();
////                            Intent i=new Intent(LoadAndUnLoadActivity.this,CheckDataActivity.class);
//
//                                        Intent mainIntent = new Intent(LoadAndUnLoadActivity.this, CheckDataActivity.class);
//                                        mainIntent.putExtra("type","farm");
//                                        mainIntent.putExtra("data",data1);
//                                        mainIntent.putExtra("text",test_data.getText().toString());
//
//                                        LoadAndUnLoadActivity.this.startActivity(mainIntent);
//                                        LoadAndUnLoadActivity.this.finish();
//                                    }else{
//                                        Toast.makeText(LoadAndUnLoadActivity.this, "هذا الصندوق لم يخزن بعد ", Toast.LENGTH_LONG).show();
//                                    }
//                                }
//                            }  //finish farm
//
//                        }
//
//
//
//
//                    }
//
//                }


































            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data2);
        }
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
