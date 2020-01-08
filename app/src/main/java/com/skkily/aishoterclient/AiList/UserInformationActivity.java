package com.skkily.aishoterclient.AiList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.skkily.aishoterclient.AiListActivity;
import com.skkily.aishoterclient.FaceCheck.LoadingActivity;
import com.skkily.aishoterclient.LoginUtil.QQLoginManager;
import com.skkily.aishoterclient.LoginUtil.User;
import com.skkily.aishoterclient.R;
import com.skkily.aishoterclient.ServerIp;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class UserInformationActivity extends AppCompatActivity implements QQLoginManager.QQLoginListener{
    private EditText editUserName=null;
    private EditText editUserEmail=null;
    private Button btnChangePW=null;
    private Button btnFaceIn=null;
    private Button btnQQIn=null;
    private String openid=null;
    private QQLoginManager qqLoginManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);

        editUserEmail=findViewById(R.id.edit_info_email);
        editUserName=findViewById(R.id.edit_info_nickname);
        btnChangePW=findViewById(R.id.btn_info_changePassword);
        btnFaceIn=findViewById(R.id.btn_info_faceLogIn);
        btnQQIn=findViewById(R.id.btn_info_qqLogIn);

        qqLoginManager = new QQLoginManager("101842080", this);
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

        btnQQIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //QQUtil qqUtil=new QQUtil();
                //String openId=qqUtil.getOpenid();
                qqLoginManager.launchQQLogin();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        qqLoginManager.onActivityResultData(requestCode,resultCode,data);
    }

    @Override
    public void onQQLoginSuccess(JSONObject jsonObject, QQLoginManager.UserAuthInfo authInfo) {
        try {


            openid=jsonObject.getString("open_id");

            //Toast.makeText(UserInformationActivity.this,openid,Toast.LENGTH_LONG).show();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences pref=getSharedPreferences("User",MODE_PRIVATE);
                    String getstr = sendToCarServer(
                            "{\"code\":5,\"qq_token\":\""+openid+"\",\"userId\":\""+pref.getString("id","")+"\"}");
                    if(getstr.equals("3")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                }
            }).start();
        }catch (JSONException e){
           e.printStackTrace();
        }

    }



    @Override
    public void onQQLoginCancel() {

    }

    @Override
    public void onQQLoginError(UiError uiError) {

    }
    private String TranslateTojson(User user){
        Gson gson=new Gson();
        String obj=gson.toJson(user);
        return obj;
    }
    //结束进程
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    private static String sendToCarServer(String command) {
        try{
            Socket client = new Socket(ServerIp.serverIp, 666);
            PrintStream out = new PrintStream(client.getOutputStream());
            //user = new User(3,token,);
            out.println(command);
            //out.println("{\"code\":3,face_token:"+token+"}");
            BufferedReader msg = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String str = msg.readLine();
            out.close();
            msg.close();
            client.close();
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "false";
    }
}
