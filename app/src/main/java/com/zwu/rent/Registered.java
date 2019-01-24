package com.zwu.rent;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Registered extends AppCompatActivity implements View.OnClickListener{

    private EditText accountEdit;

    private EditText passwordEdit;

    private EditText againEdit;

    private Button registered;

    private Button back;

    final private String IDcard = "sl";

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        againEdit = findViewById(R.id.again);
        registered = findViewById(R.id.registered);
        back = findViewById(R.id.back);

        registered.setOnClickListener(this);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registered){
            Registered();
            Judge();
        }
        if (v.getId() == R.id.back){
            Back();
        }
    }

    public void Back(){
        Intent LoginIntent = new Intent(Registered.this,
                LoginActivity.class);
        startActivity(LoginIntent);
    }

    public void Registered(){
        String account = accountEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String again = againEdit.getText().toString();
        final String address = "http://106.15.190.83/server.aspx?" +
                "servertype=register" +
                "&id=" + account +
                "&password=" + password +
                "&idcard=" + IDcard;;
        if ( password.equals(again) ) {
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
                    } catch (Exception e){
                        e.printStackTrace();
                        Log.d("Registered", "error");
                    }
                }
                @Override
                public void onError(Exception e) {
                    Log.d("Registered", "Error");
                }
            });
        } else {
            Toast.makeText(Registered.this, "Inconsistent password input " +
                    "twice", Toast.LENGTH_SHORT).show();
        }
    }

    private void Judge(){
        if (type.equals("注册成功")){
            Toast.makeText(Registered.this, "注册成功" , Toast.LENGTH_SHORT).show();
            Intent MainIntent = new Intent(Registered.this, MainActivity.
                    class);
            startActivity(MainIntent);
        }
        else if (type.equals("非法账户")){
            Toast.makeText(Registered.this, "非法账户" , Toast.LENGTH_SHORT).show();
        }
        else if (type.equals("已有此ID或IDCard已经注册过")){
            Toast.makeText(Registered.this, "已有此ID或IDCard已经注册过"
                    , Toast.LENGTH_SHORT).show();
        }
    }

}
