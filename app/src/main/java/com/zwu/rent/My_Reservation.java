package com.zwu.rent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class My_Reservation extends AppCompatActivity {

    public static final String RESERVATION_PASSWORD = "reservation_password";

    public static final String HOUSE_MESSAGE = "house_message";

    public static final String TYPE = "type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__reservation);
        TextView message = findViewById(R.id.reservation_message);
        Intent intent = getIntent();
        String type = intent.getStringExtra(TYPE);
        if (type.equals("预约")){
            String detail = intent.getStringExtra(HOUSE_MESSAGE);
            String password = intent.getStringExtra(RESERVATION_PASSWORD);
            message.setText("您的预约:\r\n" +detail + "\r\n" + "password:" + password);
        }
        else if (type.equals("租房")){
            String detail = intent.getStringExtra(HOUSE_MESSAGE);
            message.setText("您的租房:\r\n" + detail);
        }

    }
}
