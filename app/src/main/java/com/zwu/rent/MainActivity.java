package com.zwu.rent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;

    private List<House> houseList = new ArrayList<>();

    private RecyclerView recyclerView;
    private GridLayoutManager layoutManager;
    private HouseAdapter houseAdapter;



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
    /*ToolBar图标功能控制*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }

    private SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBar actionBar = getSupportActionBar();
        swipeRefresh = findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        /* 信息初始化 */
        initHouses(LoginActivity.houses);

        recyclerView = findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        houseAdapter = new HouseAdapter(houseList);
        recyclerView.setAdapter(houseAdapter);

        /*左侧菜单栏元件功能控制*/
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_quit:
                        Intent LogIntent = new Intent(MainActivity.this,
                                LoginActivity.class);
                        startActivity(LogIntent);
                        break;
                    case R.id.nav_NFC:
                        Intent NFCIntent = new Intent(MainActivity.this,
                                NFC_Activity.class);
                        startActivity(NFCIntent);
                        break;
                    case R.id.nav_task:
                        Intent intent = new Intent(MainActivity.this,
                                My_Reservation.class);
                        startActivity(intent);
                     default:
                         mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });
         /*下拉刷新*/
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshHouses();
            }
        });

    }

    /*刷新卡片式布局*/
    private void refreshHouses(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    LoginActivity.Init();
                    Thread.sleep(2000);
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initHouses(LoginActivity.houses);
                        houseAdapter.notifyDataSetChanged();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    public void initHouses(House[] houses){
        houseList.clear();
        for ( int i = 0; i < houses.length; i++ ){
            if (!(houses[i].getState().equals("0"))){
                houseList.add(houses[i]);
            }
        }
    }

}
