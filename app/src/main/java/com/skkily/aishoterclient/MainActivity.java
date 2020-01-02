package com.skkily.aishoterclient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skkily.aishoterclient.Control.Control;
import com.skkily.aishoterclient.FaceCheck.FaceNetUtil;
import com.skkily.aishoterclient.FaceCheck.LoadingActivity;
import com.skkily.aishoterclient.FaceCheck.OpenglActivity;
import com.skkily.aishoterclient.LoginUtil.QQLoginManager;
import com.skkily.aishoterclient.LoginUtil.Register;
import com.skkily.aishoterclient.LoginUtil.User;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements QQLoginManager.QQLoginListener{

    private Button loginbut,qqlogin,wexinlogin,registerbut;
    private EditText name=null,password=null;
    private String userid,userpassward;
    private Handler handler=null;
    private String str=" ",openid;
    public static Context mContext;
    private QQLoginManager qqLoginManager;
    private ImageButton faceLogIn=null;
    private User user=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();


        mContext=getApplicationContext();

        faceLogIn=findViewById(R.id.facelogin);
        name=findViewById(R.id.editText1);
        password=findViewById(R.id.editText2);
        loginbut=findViewById(R.id.loginbut);
        qqlogin=findViewById(R.id.qqloign);
        wexinlogin=findViewById(R.id.wexinlogin);
        registerbut=findViewById(R.id.zhuce);


        SharedPreferences pref=getSharedPreferences("User",MODE_PRIVATE);
        if(pref!=null) {

            name.setText(pref.getString("id",""));
            password.setText(pref.getString("password",""));
        }

        Drawable drawable=getResources().getDrawable(R.drawable.qq);
        drawable.setBounds(0,0,60,60);
        qqlogin.setCompoundDrawables(drawable,null,null,null);

        Drawable drawable2=getResources().getDrawable(R.drawable.wechat);
        drawable2.setBounds(0,0,60,60);
        wexinlogin.setCompoundDrawables(drawable2,null,null,null);

        loginbut.setOnClickListener(new LoginListener());
        registerbut.setOnClickListener(new RegisterListener());
        qqlogin.setOnClickListener(new QQloginListener());

        faceLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("5555555");
                Intent intent=new Intent(MainActivity.this, LoadingActivity.class);
                intent.putExtra("type","3");
                startActivity(intent);
            }
        });

        qqLoginManager = new QQLoginManager("101842080", this);



        Button button=findViewById(R.id.wexinlogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, LoadingActivity.class);
                intent.putExtra("type","2");
                startActivity(intent);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        FaceNetUtil.faceCreate();
//                    }
//                }).start();

            }
        });
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        qqLoginManager.onActivityResultData(requestCode,resultCode,data);
    }

    @Override
    public void onQQLoginSuccess(JSONObject jsonObject, QQLoginManager.UserAuthInfo authInfo) {
        // Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        try {
            openid=jsonObject.getString("open_id");
        }catch (JSONException e){
            Log.d("333","获取失败");
        }
        //intent.putExtra("extra_data", openid);
        //startActivity(intent);
        new Thread() {
            @Override
            public void run() {
                try {
                    Socket client = new Socket(ServerIp.serverIp, 666);
                    PrintStream out = new PrintStream(client.getOutputStream());
                    User user = new User(1,openid, "", "","");
                    out.println(TranslateTojson(user));
                    out.close();
                    client.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        Toast.makeText(MainActivity.this,"登录成功！",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onQQLoginCancel() {

    }

    @Override
    public void onQQLoginError(UiError uiError) {

    }


    private class LoginListener implements View.OnClickListener {
        public void onClick(View v) {
            userid = name.getText().toString();
            userpassward = password.getText().toString();
            if (input_judge()) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Socket client = new Socket(ServerIp.serverIp, 666);
                            PrintStream out = new PrintStream(client.getOutputStream());
                            user = new User(0,userid, userpassward, "","");
                            out.println(TranslateTojson(user));
                            BufferedReader msg = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            str = msg.readLine();
                            out.close();
                            msg.close();
                            client.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Message message = new Message();
                        message.obj = str;
                        handler.sendMessage(message);
                    }
                }.start();
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message mg) {//-1未找到用户名，0登陆成功，-2是密码错误
                        super.handleMessage(mg);
                        try {
                            Gson gson=new Gson();
                            User users=gson.fromJson(mg.obj.toString(),User.class);
                            if (users.getCode()==0&&users!=null) {
                                Intent it = new Intent(MainActivity.this, AiListActivity.class);
                                it.putExtra("User",users);
                                SharedPreferences.Editor editor=getSharedPreferences("User",MODE_PRIVATE).edit();
                                editor.putString("password",user.getPassword());
                                editor.apply();
                                MainActivity.this.startActivity(it);
                                Toast.makeText(mContext, "登陆成功！", Toast.LENGTH_LONG).show();
                                finish();
                            } else if (mg.obj.toString().indexOf("-1") != -1) {
                                Toast.makeText(mContext, "未找到用户名！", Toast.LENGTH_LONG).show();
                            } else if (mg.obj.toString().indexOf("-2") != -1) {
                                Toast.makeText(mContext, "密码错误！", Toast.LENGTH_LONG).show();
                            }
                        }catch (NullPointerException e){
                            Toast.makeText(MainActivity.this,"服务器未找到",Toast.LENGTH_LONG).show();
                        }

                    }
                };
            }
        }
    }
    private Boolean input_judge(){
        Boolean flag=true;
        if(TextUtils.isEmpty(name.getText())||TextUtils.isEmpty(password.getText())){
            mContext=getApplicationContext();
            Toast.makeText(mContext,"不能有空项！",Toast.LENGTH_LONG).show();
            flag=false;
        }
        return flag;
    }
    private String TranslateTojson(User user){
        Gson gson=new Gson();
        String obj=gson.toJson(user);
        return obj;
    }
    private class RegisterListener implements View.OnClickListener {
        public void onClick(View v) {
            Intent it=new Intent(MainActivity.this, Register.class);
            MainActivity.this.startActivity(it);
        }
    }
    private class QQloginListener implements View.OnClickListener {
        public void onClick(View v) {
            qqLoginManager.launchQQLogin();
            Toast.makeText(MainActivity.this,"正在发起QQ授权登录，请稍等",Toast.LENGTH_LONG).show();
        }
    }
}


