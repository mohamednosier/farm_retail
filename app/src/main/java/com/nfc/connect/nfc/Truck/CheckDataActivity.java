package com.nfc.connect.nfc.Truck;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nfc.connect.nfc.Constants;
import com.example.connect.nfc.R;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CheckDataActivity extends AppCompatActivity {
TextView check_data_farm_id,check_data_truck_id,check_data_retailer_id,check_data_type,
    check_data_clasificarion,check_data_farm_location,check_data_retailer_location,check_data_farm_date,check_data_retailer_date;
    private static final String encryptionKey           = "ABCDEFGHIJKLMNOP";
    private static final String characterEncoding       = "UTF-8";
    private static final String cipherTransformation    = "AES/CBC/PKCS5PADDING";
    private static final String aesEncryptionAlgorithem = "AES";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_data);
        transparentStatusAndNavigation();
        check_data_farm_id=(TextView)findViewById(R.id.check_data_farm_id);
        check_data_truck_id=(TextView)findViewById(R.id.check_data_truck_id);
        check_data_retailer_id=(TextView)findViewById(R.id.check_data_retailer_id);
        check_data_type=(TextView)findViewById(R.id.check_data_type);
        check_data_clasificarion=(TextView)findViewById(R.id.check_data_clasificarion);
        check_data_farm_location=(TextView)findViewById(R.id.check_data_farm_location);
        check_data_retailer_location=(TextView)findViewById(R.id.check_data_retailer_location);
        check_data_farm_date=(TextView)findViewById(R.id.check_data_farm_date);
        check_data_retailer_date=(TextView)findViewById(R.id.check_data_retailer_date);



        String type1=getIntent().getStringExtra("type1");
//        Toast.makeText(CheckDataActivity.this,"data is "+type,Toast.LENGTH_LONG).show();
//        Toast.makeText(CheckDataActivity.this,"data is "+data,Toast.LENGTH_LONG).show();
//        Toast.makeText(CheckDataActivity.this,"text is "+text,Toast.LENGTH_LONG).show();
        if(type1.equals("farm")){

//            check_data_farm_id.setText("Farmer iD is "+splitStr[0]);
//            check_data_truck_id.setText("truck id is"+splitStr[1]);

//            check_data_farm_id.setText(""+splitStr[3]);
            check_data_farm_date.setText(" تاريخ الحموله  "+getIntent().getStringExtra("loading_date"));
            check_data_type.setText(" النوع "+getIntent().getStringExtra("type"));
            check_data_clasificarion.setText("الصنف "+getIntent().getStringExtra("quantity"));

            check_data_farm_location.setText(" موقع المزرعه  "+getIntent().getStringExtra("location"));
//            check_data_type.setText(" Type is "+splitStr[4].substring(0,splitStr[4].indexOf(" ")));
//            check_data_farm_location.setText("Farm location is "+ splitStr[4].substring(splitStr[4].indexOf(" ")));

//            String[] splitStr1 = splitStr[4].split("\\s+");
//            check_data_type.setText(" Type is "+splitStr1[0]);
//            check_data_farm_location.setText("Farm location is "+splitStr1[1]);
//            check_data_retailer_id.setText("retailer id is "+splitStr[6]);
        }else if(type1.equals("retailer")){

//            check_data_farm_id.setText("Farmer iD is "+splitStr[0]);
//            check_data_truck_id.setText("truck id is"+splitStr[1]);

//            check_data_farm_id.setText(""+splitStr[3]);
            check_data_farm_date.setText(" تاريخ الحموله  "+ getIntent().getStringExtra("loading_date"));
            check_data_type.setText(" النوع "+getIntent().getStringExtra("type"));
            check_data_clasificarion.setText(" الصنف "+getIntent().getStringExtra("quantity"));

            check_data_farm_location.setText(" موقع المزرعه  "+getIntent().getStringExtra("location"));

//            check_data_retailer_id.setText("retailer id is "+splitStr[6]);

            check_data_retailer_date.setText(" تاريخ التفريغ  "+getIntent().getStringExtra("discharge_date"));

            check_data_retailer_location.setText(" موقع الموزع  "+getIntent().getStringExtra("retailer_location"));
        }
    }
    public void onBackPressed() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
//set icon
                .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                .setTitle("العوده")
//set message
                .setMessage("هل تريد العوده")
//set positive button
                .setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int ii) {

                        Intent i=new Intent(CheckDataActivity.this, LoadAndUnLoadActivity.class);
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
