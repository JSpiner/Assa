package net.jspiner.assa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;

import net.jspiner.assa.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Copyright 2016 JSpiner. All rights reserved.
 *
 * @author JSpiner (jspiner@naver.com)
 * @project Assa
 * @since 2016. 5. 9.
 */
public class MainActivity extends AppCompatActivity {

    //로그에 쓰일 tag
    public static final String TAG = MainActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.bg_loading)
    LinearLayout bgLoading;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    void init(){

        ButterKnife.bind(this);
        bgLoading.setVisibility(View.VISIBLE);
        delayHandler.sendEmptyMessageDelayed(0, 2000);
    }

    Handler delayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            bgLoading.setVisibility(View.GONE);
            initToolbar();
            initLayout();
        }
    };

    @OnClick(R.id.btn_next)
    void onNextClick(){
        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        startActivity(intent);
    }

    void initLayout(){

    }

    void initToolbar(){


        setSupportActionBar(toolbar);

        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(toggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle.syncState();

        /*
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tvTitle.setVisibility(View.INVISIBLE);
                    changeFragment(1);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                    changeFragment(0);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });*/
        /*
        ((LinearLayout)findViewById(R.id.lv_drawer_lv1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(getBaseContext(),"dfdf",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, CheckBookActivity.class);
                startActivity(intent);

            }
        });*/
    }



}
