package com.nfc.connect.nfc;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.reflect.Type;
import java.util.List;

public class MyApplication extends Application {
    KProgressHUD hud;
    int farmer_id;
//    private static MyApplication mInstance;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        mInstance = this;
//    }
//
//    public static synchronized MyApplication getInstance() {
//        return mInstance;
//    }
//
//    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
//        ConnectivityReceiver.connectivityReceiverListener = listener;
//    }


    public static boolean isConnected = false;
    /**
     * To receive change in network state
     */
    private BroadcastReceiver NetworkStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("Internet Connection", "Internet Connection use1");
            ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();





            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mWifi.isConnected()) {
                Toast noInternetToast = Toast.makeText(getApplicationContext(),
                        " available1 ", Toast.LENGTH_LONG);
                noInternetToast.show();
            }



            if (!isConnected) {
//                Toast noInternetToast = Toast.makeText(getApplicationContext(),
//                        "not available", Toast.LENGTH_LONG);
//                noInternetToast.show();
                Log.d("Internet Connection", "Internet Connection open1");
            }else{

//                Toast noInternetToast = Toast.makeText(getApplicationContext(),
//                        "available", Toast.LENGTH_LONG);
//                noInternetToast.show();
//                final SharedPreferences sharedPreferences = ProductLoadResult_FarmActivity.sharedPreferences;

                final SharedPreferences sharedPreferences = getSharedPreferences("USER", MODE_PRIVATE);


//retailer
            Gson gson = new Gson();
            String json = sharedPreferences.getString("Set1", "");
            if (json.isEmpty()) {
                Toast.makeText(context, "لا يوجد صناديق مخزنه عند الموزع ", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                List<String> arrPackageData = gson.fromJson(json, type);
                for (String data : arrPackageData) {


                    final String[] splitStr = data.split("-");
                    int data1=data.indexOf("latitude");

                    final String[] splitStr1 = splitStr[7].split("\\s+");


                    String url1 = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.retail/select_lastids";

                    StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s1) {

                            if (s1.equals("error")) {

                                farmer_id=1;
//                                    hud.dismiss();
                            } else {

                                farmer_id = Integer.parseInt(s1) + 1;
                            }
                            String url = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.retail/insert_retailer/"+farmer_id+"/"+splitStr[4]+"/"+splitStr[5].substring(0,splitStr[5].indexOf("اول")+3)+"/"+splitStr[1]+"/"+splitStr[0]+"/"+splitStr[2]+"/"+splitStr[3]+"/"+splitStr[5].substring(splitStr[5].indexOf("اول")+3)+"/"+splitStr[6]+"/"+splitStr1[0]+splitStr1[1]+"/"+splitStr1[2]+splitStr1[3]+splitStr1[4]+splitStr1[5];

                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    if (s.equals("false")) {

                                        Toast.makeText(getApplicationContext(),"There is error", Toast.LENGTH_LONG).show();
//                                            hud.dismiss();
                                    } else {
//
                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    System.out.println("" + volleyError);
//                                        hud.dismiss();
                                }
                            });

                            RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
                            rQueue.add(request);
                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
//                                hud.dismiss();
                        }
                    });

                    RequestQueue rQueue1 = Volley.newRequestQueue(context);
                    rQueue1.add(request1);




                }
                Toast noInternetToast1 = Toast.makeText(getApplicationContext(),
                        "تم تحميل بيانات الموزع للسيرفر", Toast.LENGTH_LONG);
                noInternetToast1.show();
                Constants.retailer_sub_batch.clear();
                Constants.retailer_data.clear();
                Constants.retailer_data_refused.clear();
                Constants.x1=0;
                sharedPreferences.edit().remove("set1");

            }





//farmer

                Gson gson1 = new Gson();

                String json1 = sharedPreferences.getString("Set", "");
                if (json1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "لا يوجد صناديق مخزنه عند المزرعه ", Toast.LENGTH_LONG).show();
                } else {
                    Type type = new TypeToken<List<String>>() {
                    }.getType();
//                   String x= gson1.fromJson(json1, type);
                    List<String> arrPackageData = gson1.fromJson(json1, type);
                    for (final String data : arrPackageData) {
                        Log.d("readSharedPreference", "onClick: readSharedPreference farmer" + data);

                        final String[] splitStr = data.split("-");
                        int data1=data.indexOf("latitude");


                        String url1 = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.farmer/select_lastids";

                        StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s1) {

                                if (s1.equals("error")) {

                                    farmer_id=1;
//                                    hud.dismiss();
                                } else {

                                    farmer_id = Integer.parseInt(s1) + 1;
                                }
                                String url = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.farmer/insert_farmer/"+farmer_id+"/"+splitStr[4]+"/"+splitStr[5].substring(0,splitStr[5].indexOf("اول")+3)+"/"+splitStr[1]+"/"+splitStr[0]+"/"+splitStr[2]+"/"+splitStr[3]+"/"+splitStr[5].substring(splitStr[5].indexOf("اول")+3);

                                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {

                                        if (s.equals("false")) {

                                            Toast.makeText(getApplicationContext(),"There is error", Toast.LENGTH_LONG).show();
//                                            hud.dismiss();
                                        } else {
//
                                        }

                                    }

                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                        System.out.println("" + volleyError);
//                                        hud.dismiss();
                                    }
                                });

                                RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
                                rQueue.add(request);
                           }

                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                System.out.println("" + volleyError);
//                                hud.dismiss();
                            }
                        });

                        RequestQueue rQueue1 = Volley.newRequestQueue(context);
                        rQueue1.add(request1);




                    }
                    Toast noInternetToast1 = Toast.makeText(getApplicationContext(),
                            "تم تحميل بيانات المزرعه للسيرفر", Toast.LENGTH_LONG);
                    noInternetToast1.show();
                    Constants.sub_batch.clear();
                    Constants.data.clear();
                    Constants.data_refused.clear();
                    Constants.x=0;
                    sharedPreferences.edit().remove("set");
                }














//



                sharedPreferences.edit().clear().commit();

            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        /** Register for CONNECTIVITY_ACTION broadcasts */
        registerReceiver(NetworkStatusReceiver, new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(NetworkStatusReceiver);
    }
}
