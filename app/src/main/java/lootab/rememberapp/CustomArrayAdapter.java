package lootab.rememberapp;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by ichung-gi on 2016. 1. 26..
 */
public class CustomArrayAdapter extends ArrayAdapter<String>{
    protected LayoutInflater inflater;
    protected int layout;
    TextView tv_title;

    public CustomArrayAdapter(Activity activity, int resourceId, List<String> objects){
        super(activity, resourceId, objects);
        layout = resourceId;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(layout, parent, false);
        tv_title = (TextView)v.findViewById(R.id.item_label);
        tv_title.setText(getItem(position));
        return v;
    }
}