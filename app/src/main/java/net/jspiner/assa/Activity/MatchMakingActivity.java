package net.jspiner.assa.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.victor.loading.newton.NewtonCradleLoading;

import net.jspiner.assa.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Copyright 2016 JSpiner. All rights reserved.
 *
 * @author JSpiner (jspiner@naver.com)
 * @project Assa
 * @since 2016. 5. 9.
 */
public class MatchMakingActivity extends AppCompatActivity {

    //로그에 쓰일 tag
    public static final String TAG = MainActivity.class.getSimpleName();

    public Socket socket;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_toolbar_title)
    TextView tvTitle;

    @Bind(R.id.tv_match_wait)
    TextView tvWait;

    @Bind(R.id.newton_cradle_loading)
    NewtonCradleLoading newtonCradleLoading;

    int time = 0;

    Handler timeHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            time++;
            tvWait.setText("현재 대기시간 "+String.format("%02d",time / 60)+":"+String.format("%02d",time%60));
            timeHandler.sendEmptyMessageDelayed(0,1000);
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchmaking);


        init();
        timeHandler.sendEmptyMessageDelayed(0,1000);
    }

    void init(){
        ButterKnife.bind(this);

        newtonCradleLoading.start();
        newtonCradleLoading.setLoadingColor(Color.BLACK);

        initToolbar();
        initSocket();
    }

    void initSocket(){
        try {
            socket = IO.socket("http://ubuntu@ec2-54-238-242-12.ap-northeast-1.compute.amazonaws.com:3000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        socket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG,"connect");

                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", Profile.getCurrentProfile().getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socket.on("match", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        Intent intent = new Intent(MatchMakingActivity.this, ChatActivity.class);
                        startActivity(intent);
                        Toast.makeText(getBaseContext(),"매칭이 완료되었습니다.",Toast.LENGTH_LONG).show();

                    }
                });
                socket.emit("register", obj, new Ack() {
                    @Override
                    public void call(Object... args) {
                        Log.d(TAG," register ack");
                    }
                });
                socket.emit("matchfind", obj, new Ack() {
                    @Override
                    public void call(Object... args) {
                        Log.d(TAG," matchfind ack");
                    }
                });




            }
        });
    }

    void initToolbar(){

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        tvTitle.setText("그룹 형성중");



    }

}
