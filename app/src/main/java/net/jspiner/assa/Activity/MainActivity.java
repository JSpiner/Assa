package net.jspiner.assa.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.Profile;
import com.nirhart.parallaxscroll.views.ParallaxListView;
import com.squareup.picasso.Picasso;

import net.jspiner.assa.Adapter.MainAdapter;
import net.jspiner.assa.Model.SellerModel;
import net.jspiner.assa.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

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
    boolean loadingMore;

    @Bind(R.id.lv_main_list)
    ParallaxListView lvList;

    @Bind(R.id.profile_image)
    ImageView profileImage;
    @Bind(R.id.tv_drawer_name)
    TextView tvName;

    MainAdapter adapter;

    @Bind(R.id.fab_room_chat)
    FloatingActionButton btnChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    @OnClick(R.id.fab_room_chat)
    void onChatClick(){
        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        startActivity(intent);
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

/*    @OnClick(R.id.btn_next)
    void onNextClick(){
        Intent intent = new Intent(MainActivity.this, OrderActivity.class);
        startActivity(intent);
    }
*/
    void initLayout(){

        initUpdateFrame();
        initParallaxScroll();

        Picasso.with(getBaseContext())
                .load(Profile.getCurrentProfile().getProfilePictureUri(400,400).toString())
                .fit()
                .into(profileImage);
        tvName.setText(Profile.getCurrentProfile().getName());
    }

    void initUpdateFrame(){
        PtrClassicFrameLayout mPtrFrame = (PtrClassicFrameLayout) findViewById(R.id.rotate_header_list_view_frame);

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                updateData();
//                reloadData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                Log.d(TAG, "refresh checkCanDoRefresh");
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);

        final MaterialHeader header = new MaterialHeader(MainActivity.this);
        int[] colors = {Color.BLUE,Color.RED,Color.GRAY,Color.CYAN};
        header.setColorSchemeColors(colors);
        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
        header.setPadding(0, 25, 0, 25);
        header.setPtrFrameLayout(mPtrFrame);

        mPtrFrame.setLoadingMinTime(1000);
        mPtrFrame.setDurationToCloseHeader(1500);
        mPtrFrame.setHeaderView(header);
        mPtrFrame.addPtrUIHandler(header);
    }

    void initParallaxScroll(){

        View header = LayoutInflater.from(this).inflate(R.layout.item_main_header,null);
        ArrayList<SellerModel> arrayList = new ArrayList<>();
        for(int i=0;i<4;i++){
            arrayList.add(new SellerModel());
        }
        adapter = new MainAdapter(MainActivity.this,arrayList );

        ViewBinder binder = new ViewBinder(header);

        lvList.addParallaxedHeaderView(header);
        lvList.setAdapter(adapter);

        lvList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (totalItemCount <= 1) return;
                int lastInScreen = firstVisibleItem + visibleItemCount;
                if ((lastInScreen == totalItemCount) && !(loadingMore)) {

//                    loadmore();
                }
            }
        });
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


    public class ViewBinder{

        @Bind(R.id.imv_header_row)
        ImageView imvRow;

        public ViewBinder(View view){
            ButterKnife.bind(this,view);

            init();
        }

        void init(){

            Picasso.with(getBaseContext())
                    .load(R.drawable.sejongbg)
                    .fit()
                    .into(imvRow);
        }
    }

}
