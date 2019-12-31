package com.skkily.aishoterclient;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.skkily.aishoterclient.FaceCheck.FaceNetUtil;
import com.skkily.aishoterclient.FaceCheck.LoadingActivity;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();
    }

    public void faceUtil(View v){
        Intent intent=new Intent(MainActivity.this, LoadingActivity.class);
        switch (v.getId()){

            case R.id.btn_faceCheck:
                intent.putExtra("type","1");startActivity(intent);
                break;
            case R.id.btn_faceSignIn:
                intent.putExtra("type","2");startActivity(intent);
                break;
            case R.id.btn_faceSignUp:
                intent.putExtra("type","3");startActivity(intent);
                break;
            case R.id.btn_faceCreate:
                //返回json信息，具体内容自己在官方api表里面查
                FaceNetUtil faceNetUtil=new FaceNetUtil();
                Toast.makeText(this,faceNetUtil.faceCreate(),Toast.LENGTH_LONG).show();
                break;
        }

    }

    public Boolean checkPermission() {
        boolean isGranted = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //如果没有写sd卡权限
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //如果没有相机权限
                isGranted = false;
            }
            if (this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                isGranted = false;
            }
            Log.i("cbs","isGranted == "+isGranted);
            if (!isGranted) {
                this.requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission
                                .ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        102);
            }
        }
        return isGranted;
    }

}
