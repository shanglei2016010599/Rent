package com.zwu.rent;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MulticastSocket;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Rent extends AppCompatActivity {

    public final static String HOUSE_ID = "house_id";
    public final static String HOUSE_DETAIL = "house_detail";
    private String id;
    private String type = "";
    private String detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        Intent intent = getIntent();
        id = intent.getStringExtra(HOUSE_ID);
        detail = intent.getStringExtra(HOUSE_DETAIL);
        final EditText year_1 = findViewById(R.id.year_1);
        final  EditText month_1 = findViewById(R.id.month_1);
        final EditText day_1 = findViewById(R.id.day_1);
        final EditText year_2 = findViewById(R.id.year_2);
        final EditText month_2 = findViewById(R.id.month_2);
        final EditText day_2 = findViewById(R.id.day_2);
        final Button Rent = findViewById(R.id.Rent);
        Rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String year1 = year_1.getText().toString();
                String month1 = month_1.getText().toString();
                String day1 = day_1.getText().toString();
                String year2 = year_2.getText().toString();
                String month2 = month_2.getText().toString();
                String day2 = day_2.getText().toString();

                final String address = "http://106.15.190.83/server.aspx?" +
                        "servertype=renthouse" +
                        "&ID=" + id +
                        "&RenterID=" + LoginActivity.account +
                        "&IDCard=" + Registered.IDcard +
                        "&RenterPwd=" + LoginActivity.password +
                        "&RentStart=" + year1 + "-" + month1 + "-" + day1 +
                        "&RentEnd=" + year2 + "-" + month2 + "-" + day2;

                HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                    @Override
                    public void onFinish(InputStream inputStream) {
                        try{
                            DocumentBuilderFactory documentBuilderFactory =  DocumentBuilderFactory.newInstance();
                            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                            Document document = documentBuilder.parse(inputStream);
                            //获取根标签
                            Element element = document.getDocumentElement();

                            type = element.getAttribute("type");
                            Log.d("Rent",type);
                            Log.d("Rent", address);
                        } catch (Exception e){
                            Log.d("Rent", "Error");
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(Exception e) {
                        Log.d("Rent", "ERROR");
                    }
                });

                if (type.equals("租房成功")){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(Rent.this);
                    dialog.setTitle("租房成功");
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Rent.this,
                                    My_Reservation.class);
                            intent.putExtra(My_Reservation.TYPE, "租房");
                            intent.putExtra(My_Reservation.HOUSE_MESSAGE, detail);
                            startActivity(intent);
                        }
                    });
                    dialog.show();
                }
            }
        });
    }

}
