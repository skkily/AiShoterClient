package com.skkily.aishoterclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.skkily.aishoterclient.AiList.MineFragment;
import com.skkily.aishoterclient.AiList.StatusBarUtil;
import com.skkily.aishoterclient.FaceCheck.util.ConUtil;
import com.skkily.aishoterclient.LoginUtil.User;

public class AiListActivity extends AppCompatActivity {

    private User user=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ai_list);

        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("User");


        SharedPreferences.Editor editor=getSharedPreferences("User",MODE_PRIVATE).edit();
        editor.putString("name",user.getUsername());
        editor.putString("id",user.getUserid());
        editor.putString("email",user.getEmail());
        editor.apply();

        MineFragment mineFragment=(MineFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_userInfo);

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
