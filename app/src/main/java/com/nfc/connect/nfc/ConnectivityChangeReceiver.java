package com.nfc.connect.nfc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.nfc.connect.nfc.Farm.ProductLoadResult_FarmActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.lang.reflect.Type;
import java.util.List;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ConnectivityChangeReceiver extends BroadcastReceiver {

    KProgressHUD hud;
    int farmer_id;
    @Override
    public void onReceive(final Context context, Intent intent) {

//        // Explicitly specify that which service class will handle the intent.
//        ComponentName comp = new ComponentName(context.getPackageName(),
//                YourService.class.getName());
//        intent.putExtra("isNetworkConnected",isConnected(context));
//        startService(context, (intent.setComponent(comp)));
        Log.d("Internet Connection", "Internet Connection use");

        boolean isConnected = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
        if(isConnected){
            Toast.makeText(context, "Internet Connection Lost", Toast.LENGTH_LONG).show();

            Log.d("Internet Connection", "Internet Connection Lost");








            final SharedPreferences sharedPreferences = ProductLoadResult_FarmActivity.sharedPreferences;





//            Gson gson = new Gson();
//            String json = sharedPreferences.getString("Set1", "");
//            if (json.isEmpty()) {
//                Toast.makeText(context, "يوجد خطأ", Toast.LENGTH_LONG).show();
//            } else {
//                Type type = new TypeToken<List<String>>() {
//                }.getType();
//                List<String> arrPackageData = gson.fromJson(json, type);
//                for (String data : arrPackageData) {
//                    Log.d("readSharedPreference", "onClick: readSharedPreference retailer" + data);
//                    Toast.makeText(context,""+data,Toast.LENGTH_LONG).show();
//
//
//
//
//                }
//
//            }







            Gson gson1 = new Gson();
            String json1 = sharedPreferences.getString("Set", "");
            if (json1.isEmpty()) {
                Toast.makeText(context, "يوجد خطأ", Toast.LENGTH_LONG).show();
            } else {
                Type type = new TypeToken<List<String>>() {
                }.getType();
                List<String> arrPackageData = gson1.fromJson(json1, type);
                for (final String data : arrPackageData) {
                    Log.d("readSharedPreference", "onClick: readSharedPreference farmer" + data);

                    final String[] splitStr = data.split("-");
                    int data1=data.indexOf("latitude");
//                    check_data_farm_id.setText("Farmer iD is "+splitStr[0]);
//                    check_data_truck_id.setText("truck id is"+splitStr[1]);
//                    check_data_farm_date.setText("Farm Date is "+splitStr[2]);
////            check_data_farm_id.setText(""+splitStr[3]);
//                    check_data_type.setText(" Type is "+splitStr[4]);
//                    check_data_clasificarion.setText("Classification is "+splitStr[5].substring(0,splitStr[5].indexOf("اول")+3));
//
//                    check_data_farm_location.setText("Farm location is "+splitStr[5].substring(splitStr[5].indexOf("اول")+3));


                    hud = KProgressHUD.create(context)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setDimAmount(0.5f)
                            .show();



                    String url1 = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.farmer/select_lastids";

                    StringRequest request1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s1) {

                            if (s1.equals("error")) {

                                farmer_id=1;
                                hud.dismiss();
                            } else {

                                farmer_id = Integer.parseInt(s1) + 1;
                            }
//                            Toast.makeText(context,"restaurant is "+restaurant_id+"  branche  "+branch_id+" classification "+classification_id,Toast.LENGTH_LONG).show();










//                                        String url = "http://192.168.1.6:8080/camarera/webresources/com.camarera.client/insert_client/"+classification_id+"/"+GetUsername+"/"+GetPhone+"/"+GetEmail+"/"+GetType;
                            String url = IPAddress.ip +":8080/malyan_nfc/webresources/malyan_nfc.farmer/insert_farmer/"+farmer_id+"/"+splitStr[4]+"/"+splitStr[5].substring(0,splitStr[5].indexOf("اول")+3)+"/"+splitStr[1]+"/"+splitStr[0]+"/"+splitStr[2]+"/"+splitStr[3]+"/"+splitStr[5].substring(splitStr[5].indexOf("اول")+3);

                            StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {

                                    if (s.equals("false")) {

                                        Toast.makeText(context,"cannot insert classification", Toast.LENGTH_LONG).show();
                                        hud.dismiss();
                                    } else {
                                        hud.dismiss();


//                                        Intent i=new Intent(NewFoodclassificationActivity.this,NewSubCategory.class);
//                                        i.putExtra("foodClassification","foodClassification");
//
////                                  i.putExtra("foods","foods");
//                                        i.putExtra("restaurant_id",restaurant_id);
//                                        i.putExtra("branch_id",branch_id);
//                                        i.putExtra("classification_id",""+classification_id);
//                                        Toast.makeText(NewFoodclassificationActivity.this,"classification_id "+classification_id,Toast.LENGTH_LONG).show();
//
//                                        startActivity(i);





                                    }

                                }

                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    System.out.println("" + volleyError);
                                    hud.dismiss();
                                }
                            });

                            RequestQueue rQueue = Volley.newRequestQueue(context);
                            rQueue.add(request);











                        }

                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            hud.dismiss();
                        }
                    });

                    RequestQueue rQueue1 = Volley.newRequestQueue(context);
                    rQueue1.add(request1);




                }
            }




//            Constants.retailer_sub_batch.clear();
//            Constants.retailer_data.clear();
//            Constants.retailer_data_refused.clear();
//            Constants.x1=0;
//
//            Constants.sub_batch.clear();
//            Constants.data.clear();
//            Constants.data_refused.clear();
//            Constants.x=0;
//
//            sharedPreferences.edit().clear().commit();
//            Intent mainIntent = new Intent(ProductLoadResult_RetailerActivity.this, SplashActivity.class);
//            ProductLoadResult_RetailerActivity.this.startActivity(mainIntent);
//            ProductLoadResult_RetailerActivity.this.finish();














        }
        else{
            Log.d("Internet Connection", "Internet Connection open");
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();
        }
    }

    public  boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

}