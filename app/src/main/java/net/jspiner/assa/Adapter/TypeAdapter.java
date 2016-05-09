package net.jspiner.assa.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.jspiner.assa.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Copyright 2016 JSpiner. All rights reserved.
 *
 * @author JSpiner (jspiner@naver.com)
 * @project EnerBnB
 * @since 2016. 3. 26.
 */
public class TypeAdapter extends PagerAdapter {

    //로그에 쓰일 tag
    public static final String TAG = TypeAdapter.class.getSimpleName();

    private LayoutInflater mInflater;
    Context context;

    int res[] = {R.drawable.sejong_1, R.drawable.sejong_2, R.drawable.sejong_3, R.drawable.sejong_1, R.drawable.sejong_2, R.drawable.sejong_3, R.drawable.sejong_1};

    public TypeAdapter(Context c){
        super();
        this.context = c;
        mInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object instantiateItem(View pager, int position) {
        View v = null;

        v = mInflater.inflate(R.layout.item_type_image_row, null);

        ViewBinder binder = new ViewBinder(v, position);

        ((ViewPager)pager).addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(View pager, int position, Object view) {
        ((ViewPager)pager).removeView((View)view);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
        return pager == obj;
    }

    @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
    @Override public Parcelable saveState() { return null; }
    @Override public void startUpdate(View arg0) {}
    @Override public void finishUpdate(View arg0) {}

    public class ViewBinder{

        String[] strs = {"친구와 함께먹기", "친구에게 사주기", "친구에게 얻어먹기"};

        @Bind(R.id.imv_seller_image_row)
        ImageView imvRow;

        @Bind(R.id.tv_type_type)
        TextView tvType;

        int position;

        public ViewBinder(View view, int position){
            ButterKnife.bind(this, view);

            this.position = position;

            init();
        }

        void init(){

            Log.d(TAG,"init image");

            tvType.setText(strs[position]);

            Picasso.with(context)
                    .load(res[position])
                    .fit()
                    .into(imvRow);
        }
    }
}
