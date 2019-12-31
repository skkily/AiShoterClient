package com.skkily.aishoterclient.LoginUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.skkily.aishoterclient.FaceCheck.LoadingActivity;
import com.skkily.aishoterclient.LoginUtil.User;
import com.skkily.aishoterclient.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Register extends AppCompatActivity {
    private EditText name,passward,secondpass,email;
    private Button registerbut;
    private Handler handler=null;
    private Button faceLogUp=null;
    private String str=" ";
    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mContext=getApplicationContext();

        faceLogUp=findViewById(R.id.btn_face_signIn);
        name=findViewById(R.id.inputreNmae);
        passward=findViewById(R.id.inputrePass);
        secondpass=findViewById(R.id.secondPass);
        email=findViewById(R.id.inputreEmail);
        registerbut=findViewById(R.id.registerbut);

        faceLogUp.setOnClickListener(new BindFace());
        registerbut.setOnClickListener(new RebutListener());
    }
    private class BindFace implements OnClickListener {
        public void onClick(View v) {
            Intent intent=new Intent(Register.this, LoadingActivity.class);
            intent.putExtra("type","2");
            startActivity(intent);
        }
    }
    private class RebutListener implements OnClickListener {
        public void onClick(View v) {
            if (input_judge()) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Socket client = new Socket("10.133.9.49", 7654);
                            PrintStream out = new PrintStream(client.getOutputStream());
                            User user = new User(0,name.getText().toString(), passward.getText().toString(), email.getText().toString());
                            out.print(TranslateTojson(user));
                            BufferedReader msg = new BufferedReader(new InputStreamReader(client.getInputStream()));
                            str = msg.readLine();
                            out.close();
                            msg.close();
                            client.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                    }
                }.start();
                handler = new Handler() {
                    @Override
                    public void handleMessage(Message mg) {
                        super.handleMessage(mg);
                        if(str.indexOf("0")!=-1) {
                            mContext = getApplicationContext();
                            Toast.makeText(mContext, "注册成功！", Toast.LENGTH_LONG).show();
                        }else if(str.indexOf("3")!=-1){
                            mContext=getApplicationContext();
                            Toast.makeText(mContext, "用户已存在！请重新输入！", Toast.LENGTH_LONG).show();
                        }
                    }
                };
            }
        }
    }
    private Boolean input_judge(){
        Boolean flag=true;
        if(TextUtils.isEmpty(name.getText())||TextUtils.isEmpty(passward.getText())||TextUtils.isEmpty(secondpass.getText())||TextUtils.isEmpty(email.getText())){
            mContext=getApplicationContext();
            Toast.makeText(mContext,"不能有空项！",Toast.LENGTH_LONG).show();
            flag=false;
        }else {
            if(!TextUtils.equals(passward.getText(),secondpass.getText())){
                mContext=getApplicationContext();
                Toast.makeText(mContext,"两次密码不一致！",Toast.LENGTH_LONG).show();
                flag=false;
            }
        }
        return flag;
    }
    private String TranslateTojson(User user){
        Gson gson=new Gson();
        String obj=gson.toJson(user);
        return obj;
    }
}
