package com.skkily.aishoterclient.AiList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.skkily.aishoterclient.R;

import java.util.List;

public class PersonalAdapter extends ArrayAdapter<Personal> {

    private  int resourceId;

    public PersonalAdapter(@NonNull Context context, int textViewResourceId, List<Personal>objects) {
        super(context, textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Personal personal=getItem(position);//获取的当前的实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView personalImage=(ImageView)view.findViewById(R.id.personal_image);
        TextView personalName=(TextView)view.findViewById(R.id.personal_name);
        personalImage.setImageResource(personal.getImageId());
        personalName.setText(personal.getName());
        return view;
    }
}
