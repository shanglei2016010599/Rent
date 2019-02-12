package com.zwu.rent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public static String account;

    public static String password;

    public static House[] houses;
    private static  String HouseID;
    private static  String HouseState;
    private static  String HouseName;
    private static  String HousePrice;
    private static  String HouseAddress;
    private static  String HouseDetail;
    private static  String HouseUploadtime;
    private static  String HouseImageUrl = "http://106.15.190.83:8000/images/house/1.jpg";

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
        Init();

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
                account = accountEdit.getText().toString();
                password = passwordEdit.getText().toString();

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
                    Toast.makeText(LoginActivity.this, "账号或密码无效"
                                    , Toast.LENGTH_SHORT).show();
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

    public static void Init(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    URL url=new URL("http://106.15.190.83/server.aspx?servertype=getall");
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    connection.setRequestMethod("GET");
                    //设置连接超时的时间（优化）
                    connection.setConnectTimeout(5000);
                    InputStream inputStream=connection.getInputStream();
                    //解析XMLDOM解析=====================================
                    DocumentBuilderFactory documentBuilderFactory=  DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder= documentBuilderFactory.newDocumentBuilder();
                    Document document= documentBuilder.parse(inputStream);
                    //获取根标签
                    Element element=document.getDocumentElement();
                    NodeList nodeList=element.getElementsByTagName("House");
                    houses = new House[nodeList.getLength()];
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Node node = nodeList.item(i);
                        if (node instanceof Element) {
                            Element HouseEle = (Element) node;
                            HouseID = HouseEle.getAttribute("HouseID");
                            HouseState = HouseEle.getAttribute("HouseState");
                            HouseName = HouseEle.getAttribute("HouseName");
                            HousePrice = HouseEle.getAttribute("HousePrice");
                            HouseAddress = HouseEle.getAttribute("HouseAddress");
                            HouseDetail = HouseEle.getAttribute("HouseDetail");
                            HouseUploadtime = HouseEle.getAttribute("HouseUploadtime");
                            HouseImageUrl = HouseEle.getAttribute("HouseImageUrl");
                        }
                        houses[i] = new House(HouseID, HouseState, HouseName, HousePrice,
                                HouseAddress, HouseDetail, HouseUploadtime, HouseImageUrl);
                    }
                } catch (Exception e){
                    Log.d("LoginActivity", "Error");
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
