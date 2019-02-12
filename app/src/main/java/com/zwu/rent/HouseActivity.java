package com.zwu.rent;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class HouseActivity extends AppCompatActivity {

    public static final String HOUSE_NAME = "house_name";

    public static final String HOUSE_DETAIL = "house_detail";

    public static final String HOUSE_URL = "house_Url";

    public static final String HOUSE_ID = "house_id";

    public static final String HOUSE_STATE = "house_state";

    public String password = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house);
        final Intent intent = getIntent();
        String houseName = intent.getStringExtra(HOUSE_NAME);
        final String houseDetail = intent.getStringExtra(HOUSE_DETAIL);
        String address = intent.getStringExtra(HOUSE_URL);
        final String id = intent.getStringExtra(HOUSE_ID);
        final String state = intent.getStringExtra(HOUSE_STATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        ImageView houseImageView = findViewById(R.id.house_image_view);
        TextView houseContentText = findViewById(R.id.house_content_text);

        final Button reservation = findViewById(R.id.reservation);
        final Button rent = findViewById(R.id.rent);

        if (state.equals("3")){
            reservation.setEnabled(false);
            reservation.setText("已预约");
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbarLayout.setTitle(houseName);
        Glide.with(this).load(address).into(houseImageView);
        houseContentText.setText(houseDetail);

        reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String address = "http://106.15.190.83/server.aspx?" +
                        "servertype=Subscribehouse" +
                        "&ID=" + id +
                        "&RenterID=" + LoginActivity.account +
                        "&IDCard=" + Registered.IDcard +
                        "&RenterPwd=" + LoginActivity.password;

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            URL url=new URL(address);
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

                            password = element.getAttribute("password");
                            Log.d("HouseActivity",password);

                        } catch (Exception e){
                            Log.d("HouseActivity", "Error");
                            e.printStackTrace();
                        }
                    }
                }).start();
                AlertDialog.Builder dialog = new AlertDialog.Builder(HouseActivity.this);
                dialog.setTitle("预约成功");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reservation.setEnabled(false);
                        reservation.setText("已预约");
                        Intent intent = new Intent(HouseActivity.this,
                                My_Reservation.class);
                        intent.putExtra(My_Reservation.TYPE, "预约");
                        intent.putExtra(My_Reservation.HOUSE_MESSAGE, houseDetail);
                        intent.putExtra(My_Reservation.RESERVATION_PASSWORD, password);
                        startActivity(intent);
                    }
                });
                dialog.show();
            }
        });

        rent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HouseActivity.this, Rent.class);
                intent.putExtra(Rent.HOUSE_ID, id);
                intent.putExtra(Rent.HOUSE_DETAIL, houseDetail);
                startActivity(intent);
            }
        });


    }



    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
