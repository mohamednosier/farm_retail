package com.nfc.connect.nfc.Farm;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
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
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nfc.connect.nfc.Constants;
import com.nfc.connect.nfc.GpsTracker;
import com.nfc.connect.nfc.LocationTrack;
import com.nfc.connect.nfc.ScannerActivity;
import com.nfc.connect.nfc.Truck.LoadAndUnLoadActivity;
import com.example.connect.nfc.R;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FarmActivity extends AppCompatActivity {
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    TextView test;
    LocationTrack locationTrack;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
    //    TextView tvNFCContent;
//    TextView message;
//    Button btnWrite;

    Button check_farm;
    ImageView farm_back;
    public static int Counter_person = 0;
    private GpsTracker gpsTracker;

    double latitude;
    double longitude;
    private static final String encryptionKey           = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding       = "UTF-8";
    private static final String cipherTransformation    = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farm);

        context = this;
        transparentStatusAndNavigation();
//        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
//        message = (TextView) findViewById(R.id.edit_message);
//        btnWrite = (Button) findViewById(R.id.button);

        test = (TextView) findViewById(R.id.test);
        check_farm = (Button) findViewById(R.id.check_farm);
        farm_back = (ImageView) findViewById(R.id.farm_back);
        farm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(FarmActivity.this, LoadAndUnLoadActivity.class);
                FarmActivity.this.startActivity(mainIntent);
                FarmActivity.this.finish();
            }
        });
        check_farm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(FarmActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();


            }
        });
//        btnWrite.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (myTag == null) {
//                        Toast.makeText(context, ERROR_DETECTED, Toast.LENGTH_LONG).show();
//                    } else {
//                        write(message.getText().toString(), myTag);
//                        Toast.makeText(context, WRITE_SUCCESS, Toast.LENGTH_LONG ).show();
//                    }
//                } catch (IOException e) {
//                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
//                    e.printStackTrace();
//                } catch (FormatException e) {
//                    Toast.makeText(context, WRITE_ERROR, Toast.LENGTH_LONG ).show();
//                    e.printStackTrace();
//                }
//            }
//        });

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

//        tvNFCContent.setText("NFC Content: " + text);
        test.setText("" + text);
        if (text.startsWith("F")) {
            Constants.farmer_id = "" + text;
            Counter_person = Counter_person + 1;
        }
//        else if(text.startsWith("T")){
//            Constants.truck_id=""+text;
//            Counter_person=Counter_person+1;
//        }else if(text.startsWith("R")){
//            Constants.retailer_id=""+text;
//            Counter_person=3;
//        }
//if (Constants.farmer_id.length()>3&&Constants.truck_id.length()>3){
////    Toast.makeText(FarmActivity.this, "Farm is "+Constants.farmer_id, Toast.LENGTH_SHORT).show();
////    Toast.makeText(FarmActivity.this, "Truck is "+Constants.truck_id, Toast.LENGTH_SHORT).show();
//    if(Constants.check_farm==false){
//
//    Date date = new Date();
//    System.out.println(new Timestamp(date.getTime()));
//    Constants.date=""+new Timestamp(date.getTime());
//    Constants.general_master_batch_id=Constants.farmer_id+"-"+Constants.truck_id+"-"+Constants.date;
//
////    Intent i=new Intent(FarmActivity.this,MainActivity.class);
////    i.putExtra("type","farm");
////    startActivity(i);
////    finish();
//
//    }else {
////    Toast.makeText(FarmActivity.this,"We access before between farm and truck",Toast.LENGTH_LONG).show();
//    }
//}
//if(Constants.truck_id.length()>3&&Constants.retailer_id.length()>3){
////    Toast.makeText(FarmActivity.this, "Retailer is "+Constants.retailer_id, Toast.LENGTH_SHORT).show();
////    Toast.makeText(FarmActivity.this, "Truck is "+Constants.truck_id, Toast.LENGTH_SHORT).show();
//    Date date = new Date();
//    System.out.println(new Timestamp(date.getTime()));
//    Constants.retailer_date=""+new Timestamp(date.getTime());
//    Constants.retailer_general_master_batch_id=Constants.retailer_id+"-"+Constants.truck_id+"-"+Constants.retailer_date;
////    Intent i=new Intent(FarmActivity.this,MainActivity.class);
////    i.putExtra("type","retailer1");
////    startActivity(i);
////    finish();
//}
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
        ndef.writeNdefMessage(message);
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

        Intent i = new Intent(FarmActivity.this, LoadAndUnLoadActivity.class);
        startActivity(i);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        String text = "";
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "تم الغاء الفحص", Toast.LENGTH_LONG).show();
            } else {
//                if(x==1){
////show dialogue with result
                text = result.getContents();

                test.setText("" + text);
//                showResultDialogue1(result.getContents());
                connect(result.getContents());
//
//                }else if(x==0){


            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showResultDialogue1(final String result) {
        Log.d("result  ", "result  " + result);
        final String[] parts = result.split(" / ");
        int replace = result.length() - result.replace(" / ", "").length();
        System.out.println("replace = " + parts.length);
//        Log.d("replace = ", "replace = : "+parts[0]);
//        Log.d("replace = ", "replace = : "+parts[1]);
//        Log.d("replace = ", "replace = : "+parts[2]);
//        Log.d("replace = ", "replace = : "+parts[3]);
//        Log.d("replace = ", "replace = : "+parts[4].substring(1));


        if (parts.length == 4) {
            if ( parts[1].contains("http://bit.ly/2xnoFZR")) {

                String url = "http://3.218.130.218:8080/nfc/webresources/phone/select_specific_user_loyality/" + parts[3].substring(1);

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String s) {


                        new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                            @Override
                            public void run() {
                                Log.d("ggggg", "run:ggggg " + s);
//                                Toast.makeText(FarmActivity.this, "s " + s, Toast.LENGTH_LONG).show();
                                if (s.equals("error")) {

                                    Toast.makeText(FarmActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();

                                } else {

//                                    if (s.equals("0")) {
//                                        Toast.makeText(FarmActivity.this, "تم تسجيل ال QR  ", Toast.LENGTH_LONG).show();
//                                    } else if (s.equals("1")) {
//                                        Toast.makeText(FarmActivity.this, "هذا QR  مسجل من قبل ", Toast.LENGTH_LONG).show();
//                                    } else if (s.equals("2")) {
//                                        Toast.makeText(FarmActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();
//                                    }
//                                    if(  result.startsWith("F")){
//                                    Constants.farmer_id = "" + result;
                                    Counter_person = Counter_person + 1;
                                    if (Constants.farmer_id.length() > 3) {

                                        // check if GPS enabled
                                        GpsTracker gps = new GpsTracker(FarmActivity.this);
                                        if (gps.canGetLocation()) {

                                            latitude = gps.getLatitude();
                                            longitude = gps.getLongitude();
                                            Constants.farm_latitude = gps.getLatitude();
                                            Constants.farm_longitude = gps.getLongitude();
                                        }
//                    Date date = new Date();
//    System.out.println(new Timestamp(date.getTime()));
                                        @SuppressLint({"NewApi", "LocalSuppress"})
                                        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = new Date();

//                    Constants.date=""+new Timestamp(date.getTime());
                                        Constants.date = "" + formatter.format(date);

                                        Intent mainIntent = new Intent(FarmActivity.this, Product_FarmActivity.class);
                                        FarmActivity.this.startActivity(mainIntent);
                                        FarmActivity.this.finish();
//                                        }else {
//                                            Toast.makeText(FarmActivity.this,"من فضلك اختار التاج الصحيح"+ Constants.farmer_id,Toast.LENGTH_LONG).show();
//
//                                        }
                                    }
//

                                }
                            }
                        });


                    }

                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("volleyError " + error);
//                    Toast.makeText(LoginActivity.this, " Please try again may be aproblem at network", Toast.LENGTH_LONG).show();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(FarmActivity.this, "خطأ في مهلة الشبكة", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(FarmActivity.this, "خطأ فشل ", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(FarmActivity.this, "خطأ في الشبكه", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(FarmActivity.this, "خطأ في الشبكه", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(FarmActivity.this, "خطأ في التحويل ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(FarmActivity.this, "من فضل حاول مره اخري", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(FarmActivity.this);
                rQueue.add(request);
            } else {
                Toast.makeText(FarmActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(FarmActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();
            ;
        }
    }

    public void connect(String result) {
        Log.d("result  ", "result  " + result);
        final String[] parts = result.split(" / ");
        int replace = result.length() - result.replace(" / ", "").length();
        System.out.println("replace = " + parts.length);
        Log.d("decrypt(parts[2]", "connect:decrypt(parts[2] farm "+ decrypt(parts[3]));
        if (parts.length == 4) {
            if ( parts[1].contains("http://bit.ly/2xnoFZR") && parts[2].contains("2011") && isNumeric(decrypt(parts[3]))) {
                Constants.farmer_id = "" + parts[3];
                Counter_person = Counter_person + 1;
                if (Constants.farmer_id.length() > 3) {

                    // check if GPS enabled
                    GpsTracker gps = new GpsTracker(FarmActivity.this);
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        Constants.farm_latitude = gps.getLatitude();
                        Constants.farm_longitude = gps.getLongitude();
                    }
                    locationTrack = new LocationTrack(FarmActivity.this);
                    if (locationTrack.canGetLocation()) {


                        longitude = locationTrack.getLongitude();
                        latitude = locationTrack.getLatitude();
                        Constants.farm_latitude = locationTrack.getLatitude();
                        Constants.farm_longitude = locationTrack.getLongitude();

//                    Date date = new Date();
//    System.out.println(new Timestamp(date.getTime()));
                    @SuppressLint({"NewApi", "LocalSuppress"})
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = new Date();

//                    Constants.date=""+new Timestamp(date.getTime());
                    Constants.date = "" + formatter.format(date);

                    Intent mainIntent = new Intent(FarmActivity.this, Product_FarmActivity.class);
                    FarmActivity.this.startActivity(mainIntent);
                    FarmActivity.this.finish();
                    }else {
                        locationTrack.showSettingsAlert();
                    }
                } else {
                    Toast.makeText(FarmActivity.this, "من فضلك اتاكد من الكود ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(FarmActivity.this, "من فضلك اتاكد من الكود ", Toast.LENGTH_LONG).show();


            }
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
