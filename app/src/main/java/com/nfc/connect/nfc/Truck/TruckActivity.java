package com.nfc.connect.nfc.Truck;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Base64;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.kaopiz.kprogresshud.KProgressHUD;
import com.nfc.connect.nfc.Constants;
import com.example.connect.nfc.R;
import com.nfc.connect.nfc.ScannerActivity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class TruckActivity extends AppCompatActivity {
    String text ="";
    KProgressHUD hud1;
    public static final String ERROR_DETECTED = "No NFC tag detected!";
    public static final String WRITE_SUCCESS = "Text written to the NFC tag successfully!";
    public static final String WRITE_ERROR = "Error during writing, is the NFC tag close enough to your device?";
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter writeTagFilters[];
    boolean writeMode;
    Tag myTag;
    Context context;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");
//    TextView tvNFCContent;
//    TextView message;
//    Button btnWrite;
    EditText truck_username,truck_password;
    Button check,login;
public static int Counter_person=0;
    private static final String encryptionKey           = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding       = "UTF-8";
    private static final String cipherTransformation    = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);


        context = this;
        transparentStatusAndNavigation();
//        tvNFCContent = (TextView) findViewById(R.id.nfc_contents);
//        message = (TextView) findViewById(R.id.edit_message);
//        btnWrite = (Button) findViewById(R.id.button);


        truck_username=(EditText)findViewById(R.id.truck_usernam);
        truck_password=(EditText)findViewById(R.id.truck_password);
        login=(Button)findViewById(R.id.login);
        check=(Button)findViewById(R.id.check);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);

        if (firstStart) {
//            showStartDialog();
            check.setVisibility(View.INVISIBLE);
            check.setEnabled(false);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(truck_username.getText().toString().equals("")||truck_password.getText().toString().equals("")){
    Toast.makeText(TruckActivity.this,"الاسم و كلمة المرور مطلوبين",Toast.LENGTH_LONG).show();

}else {

                    String truck_use_name1=truck_username.getText().toString().toLowerCase();
                    String pattern="[\\s]";
                    String replace="";

                    Pattern p= Pattern.compile(pattern);
                    Matcher m=p.matcher(truck_use_name1);

                    truck_use_name1=m.replaceAll(replace);
                    int truck_password1=Integer.parseInt(truck_password.getText().toString());

                    if(truck_use_name1.equals("mohamed")&&truck_password1==123){

//                        if(text.startsWith("T")) {
//                        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = prefs.edit();
//                        editor.putBoolean("firstStart", false);
//                        editor.apply();
//                        Constants.truck_id = text;
//                        Intent mainIntent = new Intent(TruckActivity.this, LoadAndUnLoadActivity.class);
//                        mainIntent.putExtra("truck", text);
//                        TruckActivity.this.startActivity(mainIntent);
//                        TruckActivity.this.finish();
//
//                    }else {
//                        Toast.makeText(TruckActivity.this,"من فضلك اتاكد من التاج ",Toast.LENGTH_LONG).show();
//
//                    }

                        new IntentIntegrator(TruckActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                    }else {
                 Toast.makeText(TruckActivity.this,"خطأ في اسم المستخدم او كلمة المرور ",Toast.LENGTH_LONG).show();
                }
                }
//
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("truck", "onClick: truck "+text);
                new IntentIntegrator(TruckActivity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
//                if(text.startsWith("T")){
//                    Constants.truck_id=text;
////          Counter_person=Counter_person+1;
////                }
////if(Constants.truck_id.length()>3){
//    Intent mainIntent = new Intent(TruckActivity.this,LoadAndUnLoadActivity.class);
//                    mainIntent.putExtra("truck" ,text);
//    TruckActivity.this.startActivity(mainIntent);
//    TruckActivity.this.finish();
//                }
            }
        });


//        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
//        if (nfcAdapter == null) {
//            // Stop here, we definitely need NFC
//            Toast.makeText(this, "هذا الجهاز لايدعم NFC.", Toast.LENGTH_LONG).show();
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

         text = "";
//        String tagId = new String(msgs[0].getRecords()[0].getType());
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16"; // Get the Text Encoding
        int languageCodeLength = payload[0] & 0063; // Get the Language Code, e.g. "en"
        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");

        try {
            // Get the Text
            text = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            Log.d("Truck", "buildTagViews: truck  "+text);
            if(text.startsWith("T")){
          Constants.truck_id=text;
////          Counter_person=Counter_person+1;
      }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "تم الغاء الفحص", Toast.LENGTH_LONG).show();
            } else {
//showResultDialogue1(result.getContents());
connect(result.getContents());
//text=result.getContents();
//                Constants.truck_id=text;
//
//
//
////                if(text.startsWith("T")) {
//                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
//                    SharedPreferences.Editor editor = prefs.edit();
//                    editor.putBoolean("firstStart", false);
//                    editor.apply();
//                    Constants.truck_id = text;
//                    Intent mainIntent = new Intent(TruckActivity.this, LoadAndUnLoadActivity.class);
//                    mainIntent.putExtra("truck", text);
//                    TruckActivity.this.startActivity(mainIntent);
//                    TruckActivity.this.finish();

//                }else {
//                    Toast.makeText(TruckActivity.this,"من فضلك اتاكد من التاج ",Toast.LENGTH_LONG).show();
//
//                }

//                    connect(result.getContents());
////                    showResultDialogue(result.getContents());
//                }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    public void connect(String result) {
        final String[] parts = result.split(" / ");
        int replace = result.length() - result.replace(" / ", "").length();
        System.out.println("replace = " + parts.length);
        Log.d("decrypt(parts[2]", "connect:decrypt(parts[2]truck "+ decrypt(parts[3]));
        Log.d("result  ", "result scan " + result  +"    decrypt  "+decrypt(parts[3]));

        if (parts.length == 4) {
            if ( parts[1].contains("http://bit.ly/2xnoFZR") && parts[2].contains("2013") && isNumeric(decrypt(parts[3]))) {
////GEtCode
//    3.218.130.218

                final String[] product_description = {""};
                final String[] product_name = {""};
                final String[] product_url = {""};
                final String[] production_profile_id = {""};



                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstStart", false);
                    editor.apply();
                    Constants.truck_id = parts[3];
                    Intent mainIntent = new Intent(TruckActivity.this, LoadAndUnLoadActivity.class);
                    mainIntent.putExtra("truck", parts[3]);
                    TruckActivity.this.startActivity(mainIntent);
                    TruckActivity.this.finish();


                } else {
                    Toast.makeText(TruckActivity.this, "من فضلك اتاكد من الكود ", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(TruckActivity.this, "من فضلك اتاكد من الكود1  ", Toast.LENGTH_LONG).show();


        }
    }
    public static String fixEncoding(String response) {
        try {
            byte[] u = response.toString().getBytes(
                    "ISO-8859-1");
            response = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }



    public void showResultDialogue1(final String result) {
        Log.d("result  ", "result  "+result);
        final String[] parts = result.split(" / ");
        int replace = result.length() - result.replace(" / ", "").length();
        System.out.println("replace = " + parts.length);
//        Log.d("replace = ", "replace = : "+parts[0]);
//        Log.d("replace = ", "replace = : "+parts[1]);
//        Log.d("replace = ", "replace = : "+parts[2]);
//        Log.d("replace = ", "replace = : "+parts[3]);
//        Log.d("replace = ", "replace = : "+parts[4].substring(1));


        if(parts.length==4) {
            if (parts[1].contains("http://bit.ly/2xnoFZR")) {

                String url = "http://3.218.130.218:8080/nfc/webresources/phone/select_specific_user_loyality/" + parts[3].substring(1);

                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String s) {


                        new Handler(Looper.getMainLooper()).post(new Runnable() { // Tried new Handler(Looper.myLopper()) also
                            @Override
                            public void run() {
                                Log.d("ggggg", "run:ggggg " + s);
//                                Toast.makeText(TruckActivity.this, "s " + s, Toast.LENGTH_LONG).show();
                                if (s.equals("error")) {

                                    Toast.makeText(TruckActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();

                                } else {

//                                    if (s.equals("0")) {
//                                        Toast.makeText(TruckActivity.this, "تم تسجيل ال QR  ", Toast.LENGTH_LONG).show();
//                                    } else if (s.equals("1")) {
//                                        Toast.makeText(TruckActivity.this, "هذا QR  مسجل من قبل ", Toast.LENGTH_LONG).show();
//                                    } else if (s.equals("2")) {
//                                        Toast.makeText(TruckActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();
//                                    }
                                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstStart", false);
                        editor.apply();
                        Constants.truck_id = result;
                        Intent mainIntent = new Intent(TruckActivity.this, LoadAndUnLoadActivity.class);
                        mainIntent.putExtra("truck", result);
                        TruckActivity.this.startActivity(mainIntent);
                        TruckActivity.this.finish();
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
                            Toast.makeText(TruckActivity.this, "خطأ في مهلة الشبكة", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(TruckActivity.this, "خطأ فشل ", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(TruckActivity.this, "خطأ في الشبكه", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(TruckActivity.this, "خطأ في الشبكه", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(TruckActivity.this, "خطأ في التحويل ", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(TruckActivity.this, "من فضل حاول مره اخري", Toast.LENGTH_LONG).show();
                        }
                    }
                });

                RequestQueue rQueue = Volley.newRequestQueue(TruckActivity.this);
                rQueue.add(request);
            }else{
                Toast.makeText(TruckActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();

            }
        }else{
            Toast.makeText(TruckActivity.this, "هذا QR غير مسجل من قبل ", Toast.LENGTH_LONG).show();
            ;
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