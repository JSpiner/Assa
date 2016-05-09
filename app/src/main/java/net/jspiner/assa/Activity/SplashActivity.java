package net.jspiner.assa.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import net.jspiner.assa.R;

/**
 * Copyright 2016 JSpiner. All rights reserved.
 *
 * @author JSpiner (jspiner@naver.com)
 * @project Assa
 * @since 2016. 5. 9.
 */

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        init();
    }

    void init(){

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);


                finish();

            }
        };

        handler.sendEmptyMessageDelayed(0,2000);

    }

}
