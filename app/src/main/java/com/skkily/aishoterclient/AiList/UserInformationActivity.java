package com.skkily.aishoterclient.AiList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.skkily.aishoterclient.AiListActivity;
import com.skkily.aishoterclient.FaceCheck.LoadingActivity;
import com.skkily.aishoterclient.MainActivity;
import com.skkily.aishoterclient.R;

public class UserInformationActivity extends AppCompatActivity {
    private EditText editUserName=null;
    private EditText editUserEmail=null;
    private Button btnChangePW=null;
    private Button btnFaceIn=null;
    private Button btnQQIn=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        editUserEmail=findViewById(R.id.edit_info_email);
        editUserName=findViewById(R.id.edit_info_nickname);
        btnChangePW=findViewById(R.id.btn_info_changePassword);
        btnFaceIn=findViewById(R.id.btn_info_faceLogIn);
        btnQQIn=findViewById(R.id.btn_info_qqLogIn);

        SharedPreferences pref=getSharedPreferences("User",MODE_PRIVATE);
        if(pref!=null) {
            editUserName.setText(pref.getString("name",""));
            editUserEmail.setText(pref.getString("email",""));
        }

        btnFaceIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(UserInformationActivity.this, LoadingActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
            }
        });

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
