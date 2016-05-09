package net.jspiner.assa.Activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AnalogClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.jspiner.assa.Adapter.CalendarAdapter;
import net.jspiner.assa.Model.CalData;
import net.jspiner.assa.R;
import net.jspiner.assa.View.ExpandableGridView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Copyright 2015 JSpiner. All rights reserved.
 *
 * @author JSpiner (jspiner@naver.com)
 * @project MySmartRestaurant
 * @since 2015. 11. 14.
 */
public class OrderActivity extends AppCompatActivity {

    //로그에 쓰일 tag
    public static final String TAG = OrderActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_toolbar_title)
    TextView tvTitle;
    @Bind(R.id.searchView)
    SearchView searchView;
    @Bind(R.id.layout_order)
    LinearLayout layout;
    @Bind(R.id.sv_order)
    ScrollView scrollView;

//    @Bind(R.id.grid_myoffice_score_calendar)
    ExpandableGridView grid_calendar;
//    @Bind(R.id.btn_prev_year)
    ImageView btn_prev_year;
//    @Bind(R.id.btn_next_year)
    ImageView btn_next_year;
//    @Bind(R.id.btn_prev_month)
    ImageView btn_prev_month;
//    @Bind(R.id.btn_next_month)
    ImageView btn_next_month;
//    @Bind(R.id.tv_cal_year)
    TextView tv_cal_year;
//    @Bind(R.id.tv_cal_month)
    TextView tv_cal_month;
//    private FragmentManager fragmentManager;
//    private int fragmentIndex;

    ImageView persons[];
    TextView tvPerson;


    LayoutInflater inflater;
    CalendarAdapter calendarAdapter;
    int lastSelected = 0;
    AnimationSet animset;

    int peopleCount=0;

    public int selectedYear;
    public int selectedMonth;
    public int selectedDay = -1;

    Handler delayHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {


            scrollView.fullScroll(View.FOCUS_DOWN);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        init();
    }

    void init(){

        ButterKnife.bind(this);

        initToolbar();
        inflater = LayoutInflater.from(getBaseContext());

        animset = new AnimationSet(true);
        TranslateAnimation anime = new TranslateAnimation(1300,0,0,0);
        anime.setDuration(600);
        anime.setFillAfter(true); //HERE
        AlphaAnimation anime2 = new AlphaAnimation(0.3f,1f);
        anime2.setDuration(500);
        animset.addAnimation(anime);
        animset.addAnimation(anime2);

        initPerson();
    }

    void initPerson(){

        View v = inflater.inflate(R.layout.item_card_people,null);
        layout.addView(v);
        v.startAnimation(animset);

        peopleCount=1;

        ((ImageView)v.findViewById(R.id.btn_cardperson_prev)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (peopleCount <= 1) {
                    Toast.makeText(getBaseContext(), "1명 이상 예약 가능합니다", Toast.LENGTH_LONG).show();
                    return;
                }
                peopleCount--;
                updatePerson();
            }
        });
        ((ImageView)v.findViewById(R.id.btn_cardperson_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (peopleCount >= 5) {
                    Toast.makeText(getBaseContext(), "5명 이하 예약 가능합니다", Toast.LENGTH_LONG).show();
                    return;
                }
                peopleCount++;
                updatePerson();
            }
        });

        tvPerson = ((TextView)findViewById(R.id.tv_cardperson_num));

        persons = new ImageView[]{
                ((ImageView)v.findViewById(R.id.iv_cardperson_person1)),
                ((ImageView)v.findViewById(R.id.iv_cardperson_person2)),
                ((ImageView)v.findViewById(R.id.iv_cardperson_person3)),
                ((ImageView)v.findViewById(R.id.iv_cardperson_person4)),
                ((ImageView)v.findViewById(R.id.iv_cardperson_person5))
        };

        updatePerson();

        ((LinearLayout)v.findViewById(R.id.btn_card_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initCalendar();
            }
        });
    }

    void initCalendar(){
        View v = inflater.inflate(R.layout.item_card_calendar,null);
        layout.addView(v);
        v.startAnimation(animset);

        //
        grid_calendar = ((ExpandableGridView)v.findViewById(R.id.grid_myoffice_score_calendar));
        tv_cal_month = ((TextView)v.findViewById(R.id.tv_cal_month));
        tv_cal_year = ((TextView)v.findViewById(R.id.tv_cal_year));
        btn_next_month = ((ImageView)v.findViewById(R.id.btn_next_month));
        btn_next_year = ((ImageView)v.findViewById(R.id.btn_next_year));
        btn_prev_month = ((ImageView)v.findViewById(R.id.btn_prev_month));
        btn_prev_year = ((ImageView)v.findViewById(R.id.btn_prev_year));

        //

        btn_prev_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedYear--;
                updateCalendar();
            }
        });
        btn_next_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedYear++;
                updateCalendar();
            }
        });
        btn_prev_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMonth--;
                if (selectedMonth < 1) {
                    selectedMonth = 12;
                    selectedYear--;
                }
                updateCalendar();
            }
        });
        btn_next_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedMonth++;
                if (selectedMonth > 12) {
                    selectedMonth = 1;
                    selectedYear++;
                }
                updateCalendar();
            }
        });
        ((LinearLayout)v.findViewById(R.id.btn_card_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTime();
            }
        });

        //선택 포커스
        grid_calendar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "position : " + position);

                ((CalData) calendarAdapter.getItem(lastSelected)).setSelected(false);
                ((CalData) calendarAdapter.getItem(position)).setSelected(true);
                lastSelected = position;

                CalData selectedDate = ((CalData) calendarAdapter.getItem(position));

/*
                MyOfficeInfoActivity.scoreYear = selectedDate.getYear();
                MyOfficeInfoActivity.scoreMonth = selectedDate.getMonth();
                MyOfficeInfoActivity.scoreDay = selectedDate.getDay();
*/
                Log.d(TAG, "date : " + selectedDate.getYear() + ":" + selectedDate.getMonth() + ":" + selectedDate.getDay());

                calendarAdapter.notifyDataSetChanged();

            }
        });

        updateCalendar();

        delayHandler.sendEmptyMessageDelayed(0, 20);
    }

    void initTime(){
        View v = inflater.inflate(R.layout.item_card_time,null);
        layout.addView(v);
        v.startAnimation(animset);

        final TextView tvTime = ((TextView)v.findViewById(R.id.tvTime));

        ((AnalogClock) v.findViewById(R.id.analogClock)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(OrderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tvTime.setText(i+":"+i1);
                            }
                        }, 6, 0, false).show();
            }
        });

        ((LinearLayout)v.findViewById(R.id.btn_card_accept)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderActivity.this, MatchMakingActivity.class);
                startActivity(intent);
            }
        });
        delayHandler.sendEmptyMessageDelayed(0, 20);
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

        tvTitle.setText("예약하러 가기");

        searchView.setVisibility(View.GONE);


    }

    @Override
    protected void onResume() {
        this.overridePendingTransition(0, 0);
        super.onResume();
    }

    @Override
    public void onBackPressed() {

        this.overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    void updatePerson(){

        for(int i=0;i<5;i++){
            persons[i].setVisibility(View.GONE);
        }
        for(int i=0;i<peopleCount;i++){
            persons[i].setVisibility(View.VISIBLE);
        }
        tvPerson.setText(peopleCount+"명");

    }

    void updateCalendar() {

        boolean isFirst = false;

        if (selectedDay == -1) {
            isFirst = true;
            Calendar todayCalendar = Calendar.getInstance();
            selectedYear = todayCalendar.get(Calendar.YEAR);
            selectedMonth = todayCalendar.get(Calendar.MONTH) + 1;
            selectedDay = todayCalendar.get(Calendar.DAY_OF_MONTH);
        }

        tv_cal_year.setText(String.valueOf(selectedYear));
        tv_cal_month.setText(String.valueOf(selectedMonth));


        Calendar cal = Calendar.getInstance();
        //월이 0~11
        cal.set(selectedYear, selectedMonth - 1, selectedDay);


        ArrayList<CalData> calList = new ArrayList<CalData>();

        Calendar firstCal = Calendar.getInstance();
        firstCal.set(selectedYear, selectedMonth - 1, 1);

        int startDay = firstCal.get(Calendar.DAY_OF_WEEK) - 1;
        int lastDay = cal.getActualMaximum(Calendar.DATE);

        if (startDay == 7) startDay = 0;
        if (startDay == -1) startDay = 6;

        for (int i = 1; i <= lastDay + startDay; i++) {
            CalData calData;
            if (i < startDay) {
                calData = new CalData(selectedYear, selectedMonth, 0, i, i + "요일");
            } else {
                calData = new CalData(selectedYear, selectedMonth, i - startDay, i, i + "요일");
            }
            calList.add(calData);
        }

        if (calendarAdapter == null) {
            calendarAdapter = new CalendarAdapter(getBaseContext(), calList);
            grid_calendar.setAdapter(calendarAdapter);
        } else {
            calendarAdapter.updateList(calList);
        }


        if (isFirst) {

            /*
                grid_calendar.performItemClick(
                        calendarAdapter.getView(todayCal.get(Calendar.DAY_OF_MONTH)+startDay,null,null),
                        todayCal.get(Calendar.DAY_OF_MONTH)+startDay,
                        calendarAdapter.getItemId(todayCal.get(Calendar.DAY_OF_MONTH)+startDay));*/
            //

            Calendar todayCal = Calendar.getInstance();
            int position = todayCal.get(Calendar.DAY_OF_MONTH) + startDay - 1;
            Log.d(TAG, "position : " + position);

            ((CalData) calendarAdapter.getItem(lastSelected)).setSelected(false);
            ((CalData) calendarAdapter.getItem(position)).setSelected(true);
            lastSelected = position;

            CalData selectedDate = ((CalData) calendarAdapter.getItem(position));

/*
            MyOfficeInfoActivity.scoreYear = selectedDate.getYear();
            MyOfficeInfoActivity.scoreMonth = selectedDate.getMonth();
            MyOfficeInfoActivity.scoreDay = selectedDate.getDay();*/

            Log.d(TAG, "date : " + selectedDate.getYear() + ":" + selectedDate.getMonth() + ":" + selectedDate.getDay());

            calendarAdapter.notifyDataSetChanged();

            //


        }

    }
}
