package com.nfc.connect.nfc.Truck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.connect.nfc.R;

public class TruckLoginActivity extends AppCompatActivity {
EditText username,password;
Button check,login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_login);

        username=(EditText)findViewById(R.id.usernam);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.login);
        check=(Button)findViewById(R.id.check);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("mohamed")&&password.getText().toString().equals("123456789")){
                    Intent mainIntent = new Intent(TruckLoginActivity.this,TruckLoginActivity.class);
                    TruckLoginActivity.this.startActivity(mainIntent);
                    TruckLoginActivity.this.finish();
                }
            }
        });

    }
}
