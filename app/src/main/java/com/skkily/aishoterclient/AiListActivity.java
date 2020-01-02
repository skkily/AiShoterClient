package com.skkily.aishoterclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skkily.aishoterclient.AiList.StatusBarUtil;
import com.skkily.aishoterclient.FaceCheck.util.ConUtil;

public class AiListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_list);


        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(AiListActivity.this);
        if (getSupportActionBar() != null){     //这个也是哦（去除标题栏的框框）
            getSupportActionBar().hide();
        }

//        //判断系统版本
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
//            }
//        }

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigationView);
        NavController navController= Navigation.findNavController(this,R.id.fragment);
        AppBarConfiguration configuration=new AppBarConfiguration.Builder(bottomNavigationView.getMenu()).build();
        NavigationUI.setupActionBarWithNavController(this,navController,configuration);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);
    }
}
