package com.nfc.connect.nfc.Retailer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nfc.connect.nfc.Constants;
import com.example.connect.nfc.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Product_RetailerActivity extends AppCompatActivity {
    ImageView farm_back1;
    final List<String> list = new ArrayList<String>();
    final List<String> list2 = new ArrayList<String>();
    final List<String> list3 = new ArrayList<String>();
    final List<String> list4 = new ArrayList<String>();
    KProgressHUD hud1;
    private Spinner spinner1, spinner2;
    String type = "0";
    String classification = "0";
    RadioButton piece,total;
    Button product_load,inform;
    TextView infrom_text;
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    static SharedPreferences sharedPreferences;
    boolean writeMode;
    Tag myTag;
    public static int Counter_person=0;
    String text ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product__retailer);
        transparentStatusAndNavigation();
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        product_load=(Button)findViewById(R.id.product_load);
        infrom_text=(TextView) findViewById(R.id.infrom_text);
        inform=(Button)findViewById(R.id.inform);
        piece=(RadioButton)findViewById(R.id.piece);

        total=(RadioButton)findViewById(R.id.total);
//        total.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean checked = ((RadioButton) v).isChecked();
//                // Check which radiobutton was pressed
//                if (checked){
//
//                }
//                else{
//
//                }
//            }
//        });
//        piece.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                boolean checked = ((RadioButton) v).isChecked();
//                // Check which radiobutton was pressed
//                if (checked){
//
//                }
//                else{
//
//                }
//            }
//        });
        farm_back1=(ImageView)findViewById(R.id.farm_back1);
        farm_back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog alertDialog = new AlertDialog.Builder(Product_RetailerActivity.this)
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
                                Constants.retailer_id="";
                                Intent i=new Intent(Product_RetailerActivity.this, RetailerActivity.class);
                                startActivity(i);
                                Product_RetailerActivity.this.finish();
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


//                Intent mainIntent = new Intent(Product_RetailerActivity.this, FarmActivity.class);
//                Product_RetailerActivity.this.startActivity(mainIntent);
//                Product_RetailerActivity.this.finish();
            }
        });

        spinner2= (Spinner) findViewById(R.id.spinner2);
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
        product_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("0") || classification.equals("0")) {
                    Toast.makeText(Product_RetailerActivity.this, "من فضلك اختار النوع والصنف", Toast.LENGTH_LONG).show();
                } else {
//                    if(piece.isChecked()){
                        Intent mainIntent = new Intent(Product_RetailerActivity.this, ProductLoad_RetailerPieceActivity.class);
                        mainIntent.putExtra("type",type);
                        mainIntent.putExtra("classification",classification);
                        Product_RetailerActivity.this.startActivity(mainIntent);
                        Product_RetailerActivity.this.finish();
//                    }else if (total.isChecked()){
//                        Toast.makeText(Product_RetailerActivity.this,"هذه الميزه لم تطبق بعد",Toast.LENGTH_LONG).show();
//
//                        Intent mainIntent = new Intent(Product_RetailerActivity.this, ProductLoad_RetailerTotalActivity.class);
//                        mainIntent.putExtra("type",type);
//                        mainIntent.putExtra("classification",classification);
//                        Product_RetailerActivity.this.startActivity(mainIntent);
//                        Product_RetailerActivity.this.finish();
//                    }else{
//                        Toast.makeText(Product_RetailerActivity.this,"من فضلك اختار القطه او الكل",Toast.LENGTH_LONG).show();
//                    }


                }
            }
        });
        inform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
                Gson gson = new Gson();
                String json = sharedPreferences.getString("Set", "");
//                Gson gson = new Gson();
//                String json = ProductLoad_RetailerPieceActivity.sharedPreferences.getString("Set", "");
                if (json.isEmpty()) {
                    Toast.makeText(Product_RetailerActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
                } else {
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
                    List<String> arrPackageData = gson.fromJson(json, type);
                    for (String data : arrPackageData) {
                        if(data.contains(text)){
                            int data1=data.indexOf("latitude");
                            String[]  index1= data.split("-",9);
                            int index2=data.indexOf(index1[6]);
                            String data2=data.substring(index2,data1);
//
                          infrom_text.setText(""+data2);
//                            Toast.makeText(Product_RetailerActivity.this,""+data,Toast.LENGTH_LONG).show();
                        }
//                        Log.d("readSharedPreference", "onClick: readSharedPreference retailer" + data);

//                        String new_data=data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude;
//                        Constants.sub_batch.add(data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);





                    }

                }
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
    public void connect() {
        Log.d("sadas", "connect: ");
        hud1 = KProgressHUD.create(Product_RetailerActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        list2.add(1 + "");
        list.add("طماطم");
        list2.add(2 + "");
        list.add("خيار");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Product_RetailerActivity.this,
                android.R.layout.simple_spinner_item);
        dataAdapter.add("نوع المنتج");
        dataAdapter.addAll(list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        list4.add(1 + "");
        list3.add("فرز اول");
        list4.add(2 + "");
        list3.add("فرز تاني");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Product_RetailerActivity.this,
                android.R.layout.simple_spinner_item);
        dataAdapter1.add("نوع التصنيف");
        dataAdapter1.addAll(list3);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter1);

        hud1.dismiss();
    }

//    public void onRadioButtonClicked(View view) {
//        boolean checked = ((RadioButton) view).isChecked();
//        String str="";
//        // Check which radio button was clicked
//        switch(view.getId()) {
//            case R.id.piece:
//                if(checked)
//                    str = "Android Selected";
//                break;
//            case R.id.total:
//                if(checked)
//                    str = "AngularJS Selected";
//                break;
//
//        }
//        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
//    }

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

         text = "";
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

        if(  text.startsWith("R")){
//            Constants.retailer_id=""+text;
            Counter_person=Counter_person+1;
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



    /******************************************************************************
     **********************************Enable Write********************************
     ******************************************************************************/
//    private void WriteModeOn(){
//        writeMode = true;
//        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writeTagFilters, null);
//    }
//    /******************************************************************************
//     **********************************Disable Write*******************************
//     ******************************************************************************/
//    private void WriteModeOff(){
//        writeMode = false;
//        nfcAdapter.disableForegroundDispatch(this);
//    }
    @Override
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("الرجوع")
//set message
                .setMessage("هل تريد الرجوع ")
//set positive button
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {
                        Constants.retailer_id="";
                        Intent i=new Intent(Product_RetailerActivity.this, RetailerActivity.class);
                        startActivity(i);
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

