package com.skkily.aishoterclient.Control;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.skkily.aishoterclient.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;

import android.os.Message;

import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kongqw.rockerlibrary.view.RockerView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;

public class Control extends AppCompatActivity {

    private SurfaceHolder holder;
    private Canvas canvas;
    URL videoUrl;
    private int w;
    HttpURLConnection conn;
    Bitmap bmp;

    boolean sendFlag=false;
    boolean thread_flag=true;
    Integer ang=0;
    private RockerView rockerView;
    private TextView textView;
    Socket client;
    private Handler handler=null;
    private Boolean ff=false;
    private Boolean flagf;


    class subthread implements Runnable{
        @Override
        public void run() {
            ff=false;
            String str = " ";
            try {
                client = new Socket();
                client.connect(new InetSocketAddress("10.133.9.49", 7654), 5000);
                //向主线程发送what为0的message，代表连接成功
                Message connect=handler.obtainMessage();
                connect.what=0;
                handler.sendMessage(connect);
                //连接失败时,会执行try catch语句发送what为1的message，等待message送到，以防message未送到,未更新Ui,便执行剩余代码
                //连接失败的message主线程接受成功后，会将ff赋值为true，flagf赋为false，跳出此while循环，执行下一步骤代码
                while (ff==false){

                }
                //连接服务器成功的message主线程接受后，flagf赋为true
                if (flagf==true) {
                    //code为2000，表示发送的是app连接服务器
                    //code为2001时，表示发送的是选择小车
                    //code为2002时，表示发送的是角度
                    PrintStream out = new PrintStream(client.getOutputStream());
                    ControlObj controlObj = new ControlObj(2000, "APP");
                    out.print(TranslateTojson(controlObj));

                    ControlObj controlObj2 = new ControlObj(2001, "小车0");
                    out.print(TranslateTojson(controlObj2));
                    BufferedReader msg = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    str = msg.readLine();
                    //将服务器返回的信息str发送给主线程，主线程更改Toast
                    Message connextChe = handler.obtainMessage();
                    connextChe.what = 2;
                    connextChe.obj = str;
                    handler.sendMessage(connextChe);
                    //连接小车若成功，执行发送角度的代码
                    if (str.indexOf("success") != -1) {
                        try {
                            boolean f=true;
                            while (thread_flag) {
                                if (sendFlag) {  //假如点击
                                    f=true;
                                    ControlObj controlObj3 = new ControlObj(2002, String.valueOf(ang));
                                    out.print(TranslateTojson(controlObj3));
                                    out.flush();
                                    Thread.sleep(100);
                                }else if (sendFlag==false){ //假如松开
                                    if (f) {  //只有f为true时才向服务器发送松开的角度
                                        ControlObj controlObj3 = new ControlObj(2002, "0");
                                        out.print(TranslateTojson(controlObj3));
                                        out.flush();
                                        Thread.sleep(100);
                                    }
                                    f=false;   //每次执行完松开的代码后，将f赋为false，防止松开后持续发送松开的角度
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    out.close();
                    msg.close();
                    client.close();
                }
            } catch (SocketTimeoutException e) {
                Message connect2=new Message();    //连接服务器失败的message
                connect2.what=1;
                handler.sendMessage(connect2);
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

//        //
//        Intent intent=getIntent();
//        String Car=intent.getStringExtra("Car");

        rockerView = findViewById(R.id.rockerView);
        textView=findViewById(R.id.text);

        rockerView.setVisibility(View.INVISIBLE);

        w = getWindowManager().getDefaultDisplay().getWidth();
        SurfaceView surface = findViewById(R.id.video);
        holder = surface.getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                new Thread(){
                    @Override
                    public void run() {
                        while (thread_flag){
                            draw();
                        }
                    }
                }.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });

        new Thread(new subthread()).start();
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0:
                        ff=true;
                        flagf=true;
                        Toast.makeText(Control.this,"连接服务器成功！",Toast.LENGTH_SHORT).show();break;
                    case 1:
                        ff=true; //向子线程传递message传到主线程的信息
                        flagf=false;
                        Toast.makeText(Control.this,"连接服务器失败！",Toast.LENGTH_SHORT).show();break;
                    case 2: {
                        if (msg.obj.toString().indexOf("success") != -1) {
                            Toast.makeText(Control.this, "连接小车成功！", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(Control.this,"连接小车失败！",Toast.LENGTH_SHORT).show();
                        }
                        break;
                    }
                }
            }
        };
        rockInt();
    }

    private void draw(){
        try {
            InputStream inputstream;
            //创建一个URL对象
            String url = "http://" + "192.168.43.21" + ":8083/?action=snapshot";
            videoUrl = new URL(url);
            //利用HttpURLConnection对象从网络中获取网页数据
            conn = (HttpURLConnection) videoUrl.openConnection();  //创建实例
            //设置输入流
            conn.setDoInput(true);
            //连接
            conn.connect();
            //得到网络返回的输入流
            inputstream = conn.getInputStream();
            //创建图像
            bmp = BitmapFactory.decodeStream(inputstream);
            canvas = holder.lockCanvas();     //锁定屏幕surfaceview为画布
            canvas.drawColor(Color.WHITE);  //设置画布颜色为白色
            RectF rectf = new RectF(0, 0, w, w * 3 / 4);
            canvas.drawBitmap(bmp, null, rectf, null);  //将bmp位图按照要求重画
            holder.unlockCanvasAndPost(canvas);  //结束锁定画布，提交改变
            //关闭HttpURLConnection连接
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void rockInt(){
        rockerView.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
        rockerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()==0) {
                    Toast.makeText(Control.this,"已按下",Toast.LENGTH_SHORT).show();
                    sendFlag=true;
                }
                if(event.getAction()==1) {
                    Toast.makeText(Control.this,"已松开",Toast.LENGTH_SHORT).show();
                    sendFlag=false;
                    textView.setText("拖动角度：0");
                }
                return false;
            }
        });

        rockerView.setOnAngleChangeListener(new RockerView.OnAngleChangeListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void angle(double v) {
                Double dou=v;
                ang=dou.intValue();
                textView.setText("拖动角度："+ang);
            }
            @Override
            public void onFinish() {

            }
        });
    }
    private String TranslateTojson(ControlObj controlObj){
        Gson gson=new Gson();
        String obj=gson.toJson(controlObj);
        return obj;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
