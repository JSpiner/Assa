package net.jspiner.assa.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.jspiner.assa.Model.CalData;
import net.jspiner.assa.R;

import java.util.ArrayList;

/**
 * Created by JSpiner on 2015. 2. 21..
 */
public class CalendarAdapter extends BaseAdapter {

    Context context;
    ArrayList<CalData> calList;

    public CalendarAdapter(Context context, ArrayList<CalData> calList) {
        this.context = context;
        this.calList = calList;
    }

    @Override
    public int getCount() {
        return calList.size();
    }

    @Override
    public Object getItem(int position) {
        return calList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            v = View.inflate(context, R.layout.item_view_calendar, null);
        }

        TextView tv = ((TextView) v.findViewById(R.id.tv_myoffice_calendar));

        int day = calList.get(position).getDay();
        tv.setText(String.valueOf(day));

        v.setVisibility(day == 0 ? View.INVISIBLE : View.VISIBLE);

     ((LinearLayout) v.findViewById(R.id.ln_calendar_btn)).
                setSelected(calList.get(position).isSelected());

        return v;
    }

    public void updateList(ArrayList<CalData> calList) {
        this.calList = calList;
        notifyDataSetChanged();
    }

}
