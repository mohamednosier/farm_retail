package com.nfc.connect.nfc.Farm;

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
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Date;

public class ProductLoad_FarmTotalActivity extends AppCompatActivity {
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
    public static int x = 0;
    static SharedPreferences sharedPreferences;
    HoloCircleSeekBar picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_load__farm_total);
        context = this;
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        picker = (HoloCircleSeekBar) findViewById(R.id.pp);
//        HoloCircleSeekBar pp=(HoloCircleSeekBar)findViewById(R.id.pic)
//        HoloSeekBar picker = (HoloSeekBar) findViewById(R.id.picker);
        picker.getValue();
        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
//        Toast.makeText(ProductLoad_FarmTotalActivity.this,"type "+getIntent().getStringExtra("type"),Toast.LENGTH_LONG).show();
//        Toast.makeText(ProductLoad_FarmTotalActivity.this,"classifiction "+getIntent().getStringExtra("classification"),Toast.LENGTH_LONG).show();
        type=getIntent().getStringExtra("type");
        classification=getIntent().getStringExtra("classification");
        refused=(TextView)findViewById(R.id.refused);
        available=(TextView)findViewById(R.id.available);
        total=(TextView)findViewById(R.id.total);
        farm_back=(ImageView) findViewById(R.id.farm_back2);
        farm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProductLoad_FarmTotalActivity.this, Product_FarmActivity.class);
                ProductLoad_FarmTotalActivity.this.startActivity(mainIntent);
                ProductLoad_FarmTotalActivity.this.finish();
            }
        });


        refuse_last=(Button) findViewById(R.id.refuse_last);
        refuse_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                boolean ans = Constants.data.isEmpty();
                if (ans == true) {
                    System.out.println("The ArrayList is empty");

                    Toast.makeText(ProductLoad_FarmTotalActivity.this, "لا يوجد صناديق متاحه", Toast.LENGTH_LONG).show();
                } else {
                    if (Constants.data.contains(tvNFCContent.getText().toString())) {


                        boolean ans1 = Constants.data_refused.isEmpty();
                        if (ans1 == true) {
                            Constants.data_refused.add(tvNFCContent.getText().toString());
                            for (int i=0;i<Constants.sub_batch.size();i++){
                                if(Constants.sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                            Toast.makeText(ProductLoad_FarmTotalActivity.this,"hi"+Constants.sub_batch.get(i),Toast.LENGTH_LONG);
                                    Constants.sub_batch.remove(i );
                                }
                            }


                            Toast.makeText(ProductLoad_FarmTotalActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();
                            refused.setText("" + Constants.data_refused.size());

                            available.setText("" + (Constants.data.size() - Constants.data_refused.size()));
                            picker.setValue(Float.parseFloat(String.valueOf((Constants.data.size() - Constants.data_refused.size()))));
//                                    finish.setVisibility(View.VISIBLE);
                        } else {
                            if (Constants.data_refused.contains(tvNFCContent.getText().toString())) {
                                Toast.makeText(ProductLoad_FarmTotalActivity.this, "تم رفض الصندوق من قبل", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ProductLoad_FarmTotalActivity.this, "تم اضافه الصندوق الي قائمه الرفض", Toast.LENGTH_LONG).show();

                                Constants.data_refused.add(tvNFCContent.getText().toString());
//                                        Constants.sub_batch.remove( tvNFCContent.getText().toString() );
                                for (int i=0;i<Constants.sub_batch.size();i++){
                                    if(Constants.sub_batch.get(i).contains(tvNFCContent.getText().toString())){
//                                                Toast.makeText(ProductLoad_FarmTotalActivity.this,"hi"+Constants.sub_batch.get(i),Toast.LENGTH_LONG);

                                        Constants.sub_batch.remove(i );
                                    }
                                }
                                refused.setText("" + Constants.data_refused.size());
                                boolean ans2 = Constants.data_refused.isEmpty();

                                available.setText("" + (Constants.data.size() - Constants.data_refused.size()));
                                picker.setValue(Float.parseFloat(String.valueOf((Constants.data.size() - Constants.data_refused.size()))));
//                                        finish.setVisibility(View.VISIBLE);
                            }
                            System.out.println("The ArrayList is not empty");
                        }


                    } else {
                        Toast.makeText(ProductLoad_FarmTotalActivity.this, "لا يوجد صناديق متاحه", Toast.LENGTH_LONG).show();
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
                Constants.check_farm=false;
//                Constants.date="";
//                Constants.retailer_date="";
//                Constants.general_master_batch_id="";
//                Constants.retailer_general_master_batch_id="";

                Constants.sub_batch.clear();
                Constants.data.clear();
                Constants.data_refused.clear();
                x=0;
                available.setText("0");
                total.setText("0");
                refused.setText("0");
                sharedPreferences.edit().clear().commit();
            }
        });
        finish_load=(Button) findViewById(R.id.finish_load);
        finish_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(available.getText().toString().equals("0")){
                    Toast.makeText(ProductLoad_FarmTotalActivity.this,"من فضلك اختار بعض الصناديق",Toast.LENGTH_LONG).show();
                }else {
                    Date date = new Date();
                    System.out.println(new Timestamp(date.getTime()));


                    Gson gson = new Gson();
                    String json = gson.toJson(Constants.sub_batch);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Set", json);
                    editor.commit();
                    Intent mainIntent = new Intent(ProductLoad_FarmTotalActivity.this, ProductLoadResult_FarmActivity.class);
                    mainIntent.putExtra("available", "" + available.getText().toString());
                    mainIntent.putExtra("type", "" + type);
                    mainIntent.putExtra("classification", "" + classification);

                    ProductLoad_FarmTotalActivity.this.startActivity(mainIntent);
                }
//                ProductLoad_FarmTotalActivity.this.finish();
//                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(ProductLoad_FarmTotalActivity.this, R.style.myDialog));


            }
//                else if (process.equals("retailer1")) {
//                    Gson gson = new Gson();
//                    String json = gson.toJson(Constants.sub_batch);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("Set1", json);
//                    editor.commit();
//                    reset.setVisibility(View.VISIBLE);
//                }





        });


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




        Constants.general_master_batch_id=Constants.farmer_id+"-"+Constants.truck_id+"-"+Constants.date;
        boolean ans = Constants.data.isEmpty();
        if (ans == true) {
            System.out.println("The ArrayList is empty");
//                Constants.data.add(text);
//                Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
//                Constants.data.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);

            Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification+" latitude "+Constants.farm_latitude+" longitude "+Constants.farm_longitude);
            Constants.data.add( text );
            x = x + 1;
            available.setText("" + x);
            total.setText("" + x);
            picker.setValue(Float.parseFloat(String.valueOf(available.getText().toString())));
//                picker.setValue(1);
            tvNFCContent.setText("" + text);

        } else {
            if (Constants.data.contains(text)) {
                tvNFCContent.setText("" + text);
            } else {


//                    Constants.data.add(text);
//                    Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
//                    Constants.data.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification);
                Constants.sub_batch.add(Constants.general_master_batch_id + "-" + text + "-" + type + "-" + classification+" latitude "+Constants.farm_latitude+" longitude "+Constants.farm_longitude);
                Constants.data.add(text );

                x = x + 1;
                available.setText("" + x);
                tvNFCContent.setText("" + text);
                boolean ans2 = Constants.data_refused.isEmpty();
                if (ans2 == true) {
                    total.setText("" + x);
                    picker.setValue(Float.parseFloat(String.valueOf(available.getText().toString())));
                } else {
                    total.setText(""+x);
                    available.setText("" + (x - Constants.data_refused.size()));
                    picker.setValue(Float.parseFloat(String.valueOf((x - Constants.data_refused.size()))));
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
            Toast.makeText(ProductLoad_FarmTotalActivity.this,"لا نستطيع القراءه",Toast.LENGTH_LONG).show();
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
}

