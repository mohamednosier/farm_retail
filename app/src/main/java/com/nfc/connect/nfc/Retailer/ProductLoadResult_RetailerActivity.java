package com.nfc.connect.nfc.Retailer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nfc.connect.nfc.Constants;
import com.nfc.connect.nfc.Farm.Product_FarmActivity;
import com.nfc.connect.nfc.IPAddress;
import com.example.connect.nfc.R;
import com.nfc.connect.nfc.SplashActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProductLoadResult_RetailerActivity extends AppCompatActivity {
    public static boolean isConnected = false;
    int farmer_id;
    Button exit,another_load;
    TextView download;
    ArrayList<String> al=new ArrayList<String>();
    static SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_load_result__retailer);
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        download=(TextView)findViewById(R.id.download) ;
        final HoloCircleSeekBar picker = (HoloCircleSeekBar) findViewById(R.id.pp1);
        exit=(Button)findViewById(R.id.exit);
        exit=(Button)findViewById(R.id.exit);
        another_load=(Button)findViewById(R.id.another_load);
        final int x=Integer.parseInt(getIntent().getStringExtra("available"))*20;
//        download.setText("تم تحميل "+x+" كج  "+getIntent().getStringExtra("type")+" "+getIntent().getStringExtra("classification"));
        download.setText("تم تحميل "+x+" كج  ");

        picker.setMax(Integer.parseInt(String.valueOf(x))*2);
        picker.setValue(Float.parseFloat(String.valueOf(x)));
        picker.setOnSeekBarChangeListener(new HoloCircleSeekBar.OnCircleSeekBarChangeListener() {
            @Override
            public void onProgressChanged(HoloCircleSeekBar holoCircleSeekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {

            }

            @Override
            public void onStopTrackingTouch(HoloCircleSeekBar holoCircleSeekBar) {
                if(picker.getValue()==x){

                }else {
                    picker.setValue(x);
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                                           //شغالين قبل النت
//                Gson gson = new Gson();
//                String json = ProductLoad_RetailerPieceActivity.sharedPreferences.getString("Set1", "");
//                if (json.isEmpty()) {
//                    Toast.makeText(ProductLoadResult_RetailerActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
//                } else {
//                    Type type = new TypeToken<List<String>>() {
//                    }.getType();
//                    List<String> arrPackageData = gson.fromJson(json, type);
//                    for (String data : arrPackageData) {
//                        Log.d("readSharedPreference", "onClick: readSharedPreference retailer" + data);
//                        Toast.makeText(ProductLoadResult_RetailerActivity.this,""+data,Toast.LENGTH_LONG).show();
//
//                    }
//
//                }
//
//
//
//
//
//
//
//                Gson gson1 = new Gson();
//                String json1 = ProductLoad_RetailerPieceActivity.sharedPreferences.getString("Set", "");
//                if (json1.isEmpty()) {
//                    Toast.makeText(ProductLoadResult_RetailerActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
//                } else {
//                    Type type = new TypeToken<List<String>>() {
//                    }.getType();
//                    List<String> arrPackageData = gson1.fromJson(json1, type);
//                    for (String data : arrPackageData) {
//                        Log.d("readSharedPreference", "onClick: readSharedPreference farmer" + data);
//
//                    }
//                }
//
//                Constants.retailer_sub_batch.clear();
//                Constants.retailer_data.clear();
//                Constants.retailer_data_refused.clear();
//                Constants.x1=0;
//
//                Constants.sub_batch.clear();
//                Constants.data.clear();
//                Constants.data_refused.clear();
//                Constants.x=0;
//
//                sharedPreferences.edit().clear().commit();

                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();





                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected()) {
//                    Toast noInternetToast = Toast.makeText(getApplicationContext(),
//                            " available1 ", Toast.LENGTH_LONG);
//                    noInternetToast.show();
                }



                if (!isConnected) {
//                    Toast noInternetToast = Toast.makeText(getApplicationContext(),
//                            "not available", Toast.LENGTH_LONG);
//                    noInternetToast.show();
                    Log.d("Internet Connection", "Internet Connection open1");
                }else {

//                    Toast noInternetToast = Toast.makeText(getApplicationContext(),
//                            "available", Toast.LENGTH_LONG);
//                    noInternetToast.show();
//
//
//                    final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
//
//
////retailer
//                    Gson gson = new Gson();
//                    String json = sharedPreferences.getString("Set1", "");
//                    if (json.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "لا يوجد صناديق مخزنه عند الموزع", Toast.LENGTH_LONG).show();
//                    } else {
//                        Type type = new TypeToken<List<String>>() {
//                        }.getType();
//                        List<String> arrPackageData = gson.fromJson(json, type);
//                        for (String data : arrPackageData) {
//
//
//                            final String[] splitStr = data.split("-");
//                            int data1=data.indexOf("latitude");
//
//                            final String[] splitStr1 = splitStr[7].split("\\s+");
//
//
//                            String url1 = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.retail/select_lastids";
//
//                            StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String s1) {
//
//                                    if (s1.equals("error")) {
//
//                                        farmer_id=1;
////                                    hud.dismiss();
//                                    } else {
//
//                                        farmer_id = Integer.parseInt(s1) + 1;
//                                    }
//                                    String url = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.retail/insert_farmer/"+farmer_id+"/"+splitStr[4]+"/"+splitStr[5].substring(0,splitStr[5].indexOf("اول")+3)+"/"+splitStr[1]+"/"+splitStr[0]+"/"+splitStr[2]+"/"+splitStr[3]+"/"+splitStr[5].substring(splitStr[5].indexOf("اول")+3)+"/"+splitStr[6]+"/"+splitStr1[0]+splitStr1[1]+"/"+splitStr1[2]+splitStr1[3]+splitStr1[4]+splitStr1[5];
//
//                                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
//                                        @Override
//                                        public void onResponse(String s) {
//
//                                            if (s.equals("false")) {
//
//                                                Toast.makeText(getApplicationContext(),"There is error", Toast.LENGTH_LONG).show();
////                                            hud.dismiss();
//                                            } else {
////
//                                            }
//
//                                        }
//
//                                    }, new Response.ErrorListener() {
//                                        @Override
//                                        public void onErrorResponse(VolleyError volleyError) {
//                                            System.out.println("" + volleyError);
////                                        hud.dismiss();
//                                        }
//                                    });
//
//                                    RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
//                                    rQueue.add(request);
//                                }
//
//                            }, new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError volleyError) {
//                                    System.out.println("" + volleyError);
////                                hud.dismiss();
//                                }
//                            });
//
//                            RequestQueue rQueue1 = Volley.newRequestQueue(getApplicationContext());
//                            rQueue1.add(request1);
//
//
//
//
//                        }
//                        Toast noInternetToast1 = Toast.makeText(getApplicationContext(),
//                                "تم تحميل بيانات الموزع للسيرفر", Toast.LENGTH_LONG);
//                        noInternetToast1.show();
//                        Constants.retailer_sub_batch.clear();
//                        Constants.retailer_data.clear();
//                        Constants.retailer_data_refused.clear();
//                        Constants.x1=0;
//                        sharedPreferences.edit().remove("set1");
//                    }
//
//
//
//
//
//
//

                }



                Intent mainIntent = new Intent(ProductLoadResult_RetailerActivity.this, SplashActivity.class);
                ProductLoadResult_RetailerActivity.this.startActivity(mainIntent);
                ProductLoadResult_RetailerActivity.this.finish();
            }
        }
        );
        another_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.retailer_another=1;
                Intent mainIntent = new Intent(ProductLoadResult_RetailerActivity.this, Product_RetailerActivity.class);
                ProductLoadResult_RetailerActivity.this.startActivity(mainIntent);
                ProductLoadResult_RetailerActivity.this.finish();
            }
        });
    }
}

