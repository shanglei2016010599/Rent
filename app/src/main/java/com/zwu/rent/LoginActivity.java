package com.zwu.rent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoginActivity extends BaseActivity {

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    private EditText accountEdit;

    private EditText passwordEdit;

    private Button login;

    private Button registered;

    private CheckBox rememberPass;

    private  String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        accountEdit = findViewById(R.id.account);
        passwordEdit = findViewById(R.id.password);
        rememberPass = findViewById(R.id.remember_pass);
        registered = findViewById(R.id.registered);
        login = findViewById(R.id.login);

        boolean isRemember = preferences.getBoolean("remember_password", false);
        if (isRemember){
            //  将账号和密码都设置到文本框中
            String account = preferences.getString("account", "");
            String password = preferences.getString("password", "");
            accountEdit.setText(account);
            passwordEdit.setText(password);
            rememberPass.setChecked(true);
        }
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = accountEdit.getText().toString();
                String password = passwordEdit.getText().toString();
                String address = "http://106.15.190.83/server.aspx?" +
                        "servertype=login" +
                        "&id=" + account +
                        "&password=" + password;
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
                            Log.d("LoginActivity", "error");
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        e.printStackTrace();
                        Log.d("LoginActivity", "Error");
                    }
                });

                if ( type.equals("登录成功") ){
                    editor = preferences.edit();
                    if (rememberPass.isChecked()){
                        //检查复选框是否被选中
                        editor.putBoolean("remember_password", true);
                        editor.putString("account", account);
                        editor.putString("password", password);
                    }
                    else{
                        editor.clear();
                    }
                    editor.apply();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.
                            class);
                    startActivity(intent);
                    finish();
                }
                else if ( type.equals("登录失败") ){
                    Toast.makeText(LoginActivity.this, "account or password is " +
                                    "invalid", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent RegisteredIntent = new Intent(LoginActivity.this,
                        Registered.class);
                startActivity(RegisteredIntent);
            }
        });
    }
}
