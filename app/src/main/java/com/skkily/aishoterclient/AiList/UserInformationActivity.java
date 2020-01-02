package com.skkily.aishoterclient.AiList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skkily.aishoterclient.AiListActivity;
import com.skkily.aishoterclient.R;

public class UserInformationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(UserInformationActivity.this);
        if (getSupportActionBar() != null){     //这个也是哦（去除标题栏的框框）
            getSupportActionBar().hide();
        }
        Button back=(Button)findViewById(R.id.back);
        Button save=(Button)findViewById(R.id.save);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserInformationActivity.this, AiListActivity.class);
                startActivity(intent);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast myToast=Toast.makeText(UserInformationActivity.this,"保存成功！",Toast.LENGTH_SHORT);
                myToast.setGravity(Gravity.BOTTOM,10,10);
                myToast.show();
                Intent intent=new Intent(UserInformationActivity.this, AiListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    //结束进程
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
