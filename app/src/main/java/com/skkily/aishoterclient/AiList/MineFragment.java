package com.skkily.aishoterclient.AiList;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.skkily.aishoterclient.R;

import java.util.ArrayList;
import java.util.List;

public class MineFragment extends Fragment {

    private MineViewModel mViewModel;
    private String[] data={"PhotoActivity","Vedio","Share","Update","AccordActivity","Out"};
    private List<Personal> personalList=new ArrayList<>();
    private TextView user_ID;
    private ListView listView;

    public static MineFragment newInstance() {
        return new MineFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.mine_fragment, container, false);

        PersonalAdapter adapter = new PersonalAdapter(getActivity(), R.layout.personal_item, personalList);
        ArrayAdapter<String> array = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        listView = view.findViewById(R.id.personal);
        listView.setAdapter(adapter);
        initPersonal();
        user_ID = view.findViewById(R.id.user_ID);
        user_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),UserInformationActivity.class);
                startActivity(intent);

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Personal personal = personalList.get(position);
                switch (position) {
                    case 0:
                        startActivity(new Intent(getActivity(), PhotoActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), VedioActivity.class));
                        break;
                    case 2:
                        break;
                    case 3:
                        Toast myToast = Toast.makeText(getActivity(), "已是最新版本！", Toast.LENGTH_SHORT);
                        myToast.setGravity(Gravity.BOTTOM, 10, 10);
                        myToast.show();
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(), AccordActivity.class));
                        break;
                    case 5:
                        Dialog dialog = new AlertDialog.Builder(getActivity())
                                .setTitle("退出")
                                .setMessage("您真的要退出吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        System.exit(0);
                                    }

                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                    }
                                }).create();
                        dialog.show();
                        break;
                }

            }

        });


        return view;
    }

    //item设置
    private void initPersonal() {
        Personal photo = new Personal(" 我的照片", R.drawable.photo_pic);
        personalList.add(photo);
        Personal vedio = new Personal(" 我的视频", R.drawable.vedio_pic);
        personalList.add(vedio);
        Personal share = new Personal(" 分享", R.drawable.share_pic);
        personalList.add(share);
        Personal update = new Personal(" 检查版本更新", R.drawable.update_pic);
        personalList.add(update);
        Personal accord = new Personal(" 关于", R.drawable.accord_pic);
        personalList.add(accord);
        Personal out = new Personal(" 退出", R.drawable.out_pic);
        personalList.add(out);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MineViewModel.class);
        // TODO: Use the ViewModel


    }

}
