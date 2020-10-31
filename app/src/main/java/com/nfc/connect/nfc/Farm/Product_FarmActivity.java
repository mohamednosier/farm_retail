package com.nfc.connect.nfc.Farm;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.nfc.connect.nfc.Constants;
import com.example.connect.nfc.R;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.ArrayList;
import java.util.List;

public class Product_FarmActivity extends AppCompatActivity {
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
    Button product_load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        transparentStatusAndNavigation();

        product_load=(Button)findViewById(R.id.product_load);
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

                AlertDialog alertDialog = new AlertDialog.Builder(Product_FarmActivity.this)
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

                                Intent i=new Intent(Product_FarmActivity.this, FarmActivity.class);
                                startActivity(i);
                                Product_FarmActivity.this.finish();
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
//                Intent mainIntent = new Intent(Product_FarmActivity.this,FarmActivity.class);
//                Product_FarmActivity.this.startActivity(mainIntent);
//                Product_FarmActivity.this.finish();
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
                    Toast.makeText(Product_FarmActivity.this, "من فضلك اختار النوع ولاصنف", Toast.LENGTH_LONG).show();
                } else {
//                if(piece.isChecked()){
                    Intent mainIntent = new Intent(Product_FarmActivity.this, ProductLoad_FarmPieceActivity.class);
                    mainIntent.putExtra("type",type);
                    mainIntent.putExtra("classification",classification);
                    Product_FarmActivity.this.startActivity(mainIntent);
                    Product_FarmActivity.this.finish();
//                }else if (total.isChecked()){
//                    Toast.makeText(Product_FarmActivity.this,"هذه الميزه لم تطبق بعد",Toast.LENGTH_LONG).show();
//                    Intent mainIntent = new Intent(Product_FarmActivity.this, ProductLoad_FarmTotalActivity.class);
//                    mainIntent.putExtra("type",type);
//                    mainIntent.putExtra("classification",classification);
//                    Product_FarmActivity.this.startActivity(mainIntent);
//                    Product_FarmActivity.this.finish();
//                }
//                else{
//                    Toast.makeText(Product_FarmActivity.this,"من فضلك اختار قطعه او الكل",Toast.LENGTH_LONG).show();
//                    }


                }
            }
        });
    }
    public void connect() {
        Log.d("sadas", "connect: ");
        hud1 = KProgressHUD.create(Product_FarmActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        list2.add(1 + "");
        list.add("طماطم");
        list2.add(2 + "");
        list.add("خيار");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(Product_FarmActivity.this,
                android.R.layout.simple_spinner_item);
        dataAdapter.add("نوع المنتج");
        dataAdapter.addAll(list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapter);

        list4.add(1 + "");
        list3.add("فرز اول");
        list4.add(2 + "");
        list3.add("فرز تاني");


        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(Product_FarmActivity.this,
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

                    Intent i=new Intent(Product_FarmActivity.this, FarmActivity.class);
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
