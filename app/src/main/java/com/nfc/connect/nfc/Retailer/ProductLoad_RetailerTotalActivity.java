package com.nfc.connect.nfc.Retailer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nfc.connect.nfc.Constants;
import com.example.connect.nfc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class ProductLoad_RetailerTotalActivity extends AppCompatActivity {
    Button refuse_last,reset_load,finish_load;
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
    //    public static int x = 0;
//    public static int x1 = 0;
    static SharedPreferences sharedPreferences;
    HoloCircleSeekBar picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_load__retailer_total);
        context = this;
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
//        Constants.retailer_sub_batch.clear();
//        Constants.retailer_data.clear();
//        x=0;
        picker = (HoloCircleSeekBar) findViewById(R.id.pp);
//        HoloCircleSeekBar pp=(HoloCircleSeekBar)findViewById(R.id.pic)
//        HoloSeekBar picker = (HoloSeekBar) findViewById(R.id.picker);
        picker.getValue();
        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
//        Toast.makeText(ProductLoad_RetailerTotalActivity.this,"type "+getIntent().getStringExtra("type"),Toast.LENGTH_LONG).show();
//        Toast.makeText(ProductLoad_RetailerTotalActivity.this,"classifiction "+getIntent().getStringExtra("classification"),Toast.LENGTH_LONG).show();
        type=getIntent().getStringExtra("type");
        classification=getIntent().getStringExtra("classification");
        refused=(TextView)findViewById(R.id.refused);
        available=(TextView)findViewById(R.id.available);
        total=(TextView)findViewById(R.id.total);
        int available_size= Constants.x1-Constants.retailer_data_refused.size();
        available.setText(""+available_size);
        total.setText(""+Constants.x1);
        refused.setText(""+Constants.retailer_data_refused.size());
        farm_back=(ImageView) findViewById(R.id.farm_back2);
        farm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProductLoad_RetailerTotalActivity.this, Product_RetailerActivity.class);
                ProductLoad_RetailerTotalActivity.this.startActivity(mainIntent);
                ProductLoad_RetailerTotalActivity.this.finish();
            }
        });







        refuse_last=(Button) findViewById(R.id.refuse_last);
        refuse_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                boolean ans = Constants.retailer_data.isEmpty();
                if (ans == true) {
                    System.out.println("The ArrayList is empty");

                    Toast.makeText(ProductLoad_RetailerTotalActivity.this, "لا يوجد صناديق متاحه", Toast.LENGTH_LONG).show();
                } else {
                    if (Constants.retailer_data.contains(tvNFCContent.getText().toString())) {


                        boolean ans1 = Constants.retailer_data_refused.isEmpty();
                        if (ans1 == true) {
                            Constants.retailer_data_refused.add(tvNFCContent.getText().toString());
                            for (int i=0;i<Constants.retailer_sub_batch.size();i++){
                                if(Constants.retailer_sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                            Toast.makeText(ProductLoad_RetailerTotalActivity.this,"hi"+Constants.retailer_sub_batch.get(i),Toast.LENGTH_LONG);
                                    Constants.retailer_sub_batch.remove(i );
                                }
                            }


                            Toast.makeText(ProductLoad_RetailerTotalActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();
                            refused.setText("" + Constants.retailer_data_refused.size());

                            available.setText("" + (Constants.retailer_data.size() - Constants.retailer_data_refused.size()));
                            picker.setValue(Float.parseFloat(String.valueOf((Constants.retailer_data.size() - Constants.retailer_data_refused.size()))));
//                                    finish.setVisibility(View.VISIBLE);
                        } else {
                            if (Constants.retailer_data_refused.contains(tvNFCContent.getText().toString())) {
                                Toast.makeText(ProductLoad_RetailerTotalActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ProductLoad_RetailerTotalActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();

                                Constants.retailer_data_refused.add(tvNFCContent.getText().toString());
//                                        Constants.retailer_sub_batch.remove( tvNFCContent.getText().toString() );
                                for (int i=0;i<Constants.retailer_sub_batch.size();i++){
                                    if(Constants.retailer_sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                                Toast.makeText(ProductLoad_RetailerTotalActivity.this,"hi"+Constants.retailer_sub_batch.get(i),Toast.LENGTH_LONG);

                                        Constants.retailer_sub_batch.remove(i );
                                    }
                                }
                                refused.setText("" + Constants.retailer_data_refused.size());
                                boolean ans2 = Constants.retailer_data_refused.isEmpty();

                                available.setText("" + (Constants.retailer_data.size() - Constants.retailer_data_refused.size()));
                                picker.setValue(Float.parseFloat(String.valueOf((Constants.retailer_data.size() - Constants.retailer_data_refused.size()))));
//                                        finish.setVisibility(View.VISIBLE);
                            }
                            System.out.println("The ArrayList is not empty");
                        }


                    } else {
                        Toast.makeText(ProductLoad_RetailerTotalActivity.this, "لا يوجد صناديق متاحه", Toast.LENGTH_LONG).show();
                    }
                    System.out.println("The ArrayList is not empty");
                }
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
                int available_size=Constants.x1-Constants.retailer_data_refused.size();
                picker.setValue(available_size);
//                sharedPreferences.edit().clear().commit();
            }
        });
        finish_load=(Button) findViewById(R.id.finish_load);
        finish_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(available.getText().toString().equals("0")){
                    Toast.makeText(ProductLoad_RetailerTotalActivity.this,"من فضلك اخار بعض الصناديق",Toast.LENGTH_LONG).show();
                }else {
                    Date date = new Date();
                    System.out.println(new Timestamp(date.getTime()));


                    Gson gson = new Gson();
                    String json = gson.toJson(Constants.retailer_sub_batch);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Set1", json);
                    editor.commit();
                    Intent mainIntent = new Intent(ProductLoad_RetailerTotalActivity.this, ProductLoadResult_RetailerActivity.class);
                    mainIntent.putExtra("available", "" + available.getText().toString());
                    mainIntent.putExtra("type", "" + type);
                    mainIntent.putExtra("classification", "" + classification);

                    ProductLoad_RetailerTotalActivity.this.startActivity(mainIntent);
                }
//                ProductLoad_RetailerTotalActivity.this.finish();
//                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ProductLoad_RetailerTotalActivity.this, R.style.myDialog));


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








//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (nfcAdapter == null) {
//            // Stop here, we definitely need NFC
//            Toast.makeText(this, "هذا الجهاز لايدعم NFC", Toast.LENGTH_LONG).show();
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
                Toast.makeText(ProductLoad_RetailerTotalActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
            } else {
                Type type1 = new TypeToken<List<String>>() {
                }.getType();
                List<String> arrPackageData = gson.fromJson(json, type1);
                for (String data : arrPackageData) {
                    if(data.contains(text)&&data.contains(type)&&data.contains(classification)){
                        Constants.retailer_sub_batch.add(data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+Constants.retailer_latitude+" longitude "+Constants.retailer_longitud);
                        Constants.retailer_data.     add(text);
                        Constants.x1 = Constants.x1 + 1;
                        available.setText("" + Constants.x1);
                        total.setText("" + Constants.x1);
                        picker.setValue(Float.parseFloat(String.valueOf(Constants.x1)));
                        tvNFCContent.setText("" + text);
                    }else {
//                            Toast.makeText(ProductLoad_RetailerTotalActivity.this, "لم يتم اضافه هذا الصندوق في المزرعه من قبل", Toast.LENGTH_LONG).show();

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
                    Toast.makeText(ProductLoad_RetailerTotalActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
                } else {
                    Type type1 = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrPackageData = gson.fromJson(json, type1);
                    for (String data : arrPackageData) {
                        if(data.contains(text)&&data.contains(type)&&data.contains(classification)){
                            Constants.retailer_sub_batch.add(data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+Constants.retailer_latitude+" longitude "+Constants.retailer_longitud);

                            Constants.retailer_data.     add(text );
                            Constants.x1 = Constants.x1 + 1;
                            available.setText("" + Constants.x1);
                            tvNFCContent.setText("" + text);
                            boolean ans2 = Constants.retailer_data_refused.isEmpty();
                            if (ans2 == true) {
                                total.setText("" + Constants.x1);
                                picker.setValue(Float.parseFloat(String.valueOf(Constants.x1)));
                            } else {
                                total.setText(""+Constants.x1);
                                available.setText("" + (Constants.x1 - Constants.retailer_data_refused.size()));
                                picker.setValue(Float.parseFloat(String.valueOf((Constants.x1 - Constants.retailer_data_refused.size()))));
                            }
                        }else{
//                                Toast.makeText(ProductLoad_RetailerTotalActivity.this, "لم يتم اضافه هذا الصندوق في المزرعه من قبل", Toast.LENGTH_LONG).show();

                        }
                        Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
                    }
                }

            }
            System.out.println("The ArrayList is not empty");

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
            Toast.makeText(ProductLoad_RetailerTotalActivity.this,"لا نستطيع القراهء",Toast.LENGTH_LONG).show();
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


    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
//    private void WriteModeOn() {
//        writeMode = true;
//        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
//    }
//
//    /******************************************************************************
//     **********************************Disable Write*******************************
//     ******************************************************************************/
//    private void WriteModeOff() {
//        writeMode = false;
//        nfcAdapter.disableForegroundDispatch(this);
//    }
    @Override
    public void onBackPressed() {

        Intent i=new Intent(ProductLoad_RetailerTotalActivity.this,RetailerActivity.class);
        startActivity(i);

    }
}

