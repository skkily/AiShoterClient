package com.skkily.aishoterclient.AiList;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.skkily.aishoterclient.Control.Control;
import com.skkily.aishoterclient.R;

public class SelectFragment extends Fragment {

    private SelectViewModel mViewModel;
    private ImageView select_image1;
    private ImageView select_image2;

    public static SelectFragment newInstance() {
        return new SelectFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.select_fragment, container, false);
        select_image1=view.findViewById(R.id.select_image1);
        select_image2=view.findViewById(R.id.select_image2);


        //跳转到Control界面
        select_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转界面
                Intent intent=new Intent(getActivity(), Control.class);
                Bundle bundle = new Bundle();
                bundle.putString("Car","小车0"); //放入所需要传递的值
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
        select_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转界面
                Intent intent=new Intent(getActivity(),Control.class);
                Bundle bundle = new Bundle();
                bundle.putString("Car","小车1"); //放入所需要传递的值
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });




        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SelectViewModel.class);
        // TODO: Use the ViewModel
    }

}
