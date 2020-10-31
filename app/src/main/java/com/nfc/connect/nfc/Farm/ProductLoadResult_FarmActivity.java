package com.nfc.connect.nfc.Farm;

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

import com.nfc.connect.nfc.Constants;
import com.example.connect.nfc.R;
import com.nfc.connect.nfc.SplashActivity;
import com.jesusm.holocircleseekbar.lib.HoloCircleSeekBar;

import java.util.ArrayList;

public class ProductLoadResult_FarmActivity extends AppCompatActivity {
    public static SharedPreferences sharedPreferences;
    Button exit,another_load;
TextView download;
    ArrayList<String> al=new ArrayList<String>();
    public static boolean isConnected = false;
    int farmer_id;
    int count=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_load_result);
        sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
        download=(TextView)findViewById(R.id.download) ;
        final HoloCircleSeekBar picker = (HoloCircleSeekBar) findViewById(R.id.pp1);
        exit=(Button)findViewById(R.id.exit);
        exit=(Button)findViewById(R.id.exit);
        another_load=(Button)findViewById(R.id.another_load);
        final int x=Integer.parseInt(getIntent().getStringExtra("available"))*20;
//        final int x=19740;
//        download.setText("تم تحميل "+x+" كج  "+getIntent().getStringExtra("type")+" "+getIntent().getStringExtra("classification"));
        download.setText("تم تحميل "+x+" كج  ");
//        Gson gson = new Gson();
//        String json = ProductLoad_FarmPieceActivity.sharedPreferences.getString("Set", "");
//        if (json.isEmpty()) {
//            Toast.makeText(ProductLoadResult_FarmActivity.this, "There is something error", Toast.LENGTH_LONG).show();
//        } else {
//            Type type = new TypeToken<List<String>>() {
//            }.getType();
//            List<String> arrPackageData = gson.fromJson(json, type);
//            for (String data : arrPackageData) {
//                Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
//int index_latitiude=data.indexOf("latitude");
//int index1=data.indexOf("-",4);
//String tt=data.substring(index1,index_latitiude);
//
//                if(al.contains(tt)){
//
//                }else {
//                    al.add(tt);
//                }
//                Log.d("readSharedPreference", "onClick: readSharedPreference tt" + tt);
////                        String new_data=data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude;
////                        Constants.sub_batch.add(data+"-"+Constants.retailer_id+"-"+Constants.retailer_date+" latitude "+latitude+" longitude "+longitude);
//
//            }
//        }
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

//                //شغالين قبل النت
//                Gson gson = new Gson();
//                String json = ProductLoad_FarmPieceActivity.sharedPreferences.getString("Set", "");
//                if (json.isEmpty()) {
//                    Toast.makeText(ProductLoadResult_FarmActivity.this, "يوجد خطأ", Toast.LENGTH_LONG).show();
//                } else {
//                    Type type = new TypeToken<List<String>>() {
//                    }.getType();
//                    List<String> arrPackageData = gson.fromJson(json, type);
//                    for (String data : arrPackageData) {
//                        Log.d("readSharedPreference", "onClick: readSharedPreference" + data);
//
//                    }
//                }






                ConnectivityManager connMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();





                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                if (mWifi.isConnected()) {

                }



                if (!isConnected) {
                    Log.d("Internet Connection", "Internet Connection open1");
                }else {
//
//
//
//                    final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);
//
//
////farmer
//
//                    Gson gson1 = new Gson();
//
//                    String json1 = sharedPreferences.getString("Set", "");
//                    if (json1.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "لا يوجد صناديق مخزنه عند المزرعه", Toast.LENGTH_LONG).show();
//                    } else {
//                        Type type = new TypeToken<List<String>>() {
//                        }.getType();
////                   String x= gson1.fromJson(json1, type);
//                        List<String> arrPackageData = gson1.fromJson(json1, type);
//                        for (final String data : arrPackageData) {
//                            Log.d("readSharedPreference", "onClick: readSharedPreference farmer" + data);
//
//                            final String[] splitStr = data.split("-");
//                            int data1=data.indexOf("latitude");
//
//
//                            String url1 = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.farmer/select_lastids";
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
//                                    String url = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.farmer/insert_farmer/"+farmer_id+"/"+splitStr[4]+"/"+splitStr[5].substring(0,splitStr[5].indexOf("اول")+3)+"/"+splitStr[1]+"/"+splitStr[0]+"/"+splitStr[2]+"/"+splitStr[3]+"/"+splitStr[5].substring(splitStr[5].indexOf("اول")+3);
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
//                                "تم تحميل بيانات المزرعه للسيرفر", Toast.LENGTH_LONG);
//                        noInternetToast1.show();
//                        Constants.sub_batch.clear();
//                        Constants.data.clear();
//                        Constants.data_refused.clear();
//                        Constants.x=0;
//                        sharedPreferences.edit().remove("set");
//
//                        DBFarmHelper mydb;
//                        mydb = new DBFarmHelper(ProductLoadResult_FarmActivity.this);
//                        List<Farmer_Data> contacts = mydb.getAllFarms();
//                        for (Farmer_Data cn : contacts) {
//                            String log = "Id: " + cn.getFarm_id() + " ,type: " + cn.getType() + " ,classification: " +
//                                    cn.getQuality()+ " ,box_id: " +
//                                    cn.getBox_id();
//                            // Writing Contacts to log
//                            Log.d("onClick: mydb: ", log);
//                            mydb.deleteFarm1(cn);
//
//                        }
//                    }




                }



                Intent mainIntent = new Intent(ProductLoadResult_FarmActivity.this, SplashActivity.class);
                ProductLoadResult_FarmActivity.this.startActivity(mainIntent);
                ProductLoadResult_FarmActivity.this.finish();
            }
        });
        another_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.farm_another=1;
                Intent mainIntent = new Intent(ProductLoadResult_FarmActivity.this, Product_FarmActivity.class);
                ProductLoadResult_FarmActivity.this.startActivity(mainIntent);
                ProductLoadResult_FarmActivity.this.finish();
            }
        });
    }


}
