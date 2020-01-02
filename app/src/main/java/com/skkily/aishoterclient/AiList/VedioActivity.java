package com.skkily.aishoterclient.AiList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import android.widget.Toast;
import android.widget.VideoView;

import com.skkily.aishoterclient.AiListActivity;
import com.skkily.aishoterclient.R;

import java.io.File;


public class VedioActivity extends AppCompatActivity implements View.OnClickListener{

    private Button vedio_back;
    private VideoView videoView;
    private Button play;
    private Button pause;
    private Button replay;
    private Button choice;
    private static final int FILE_SELECT_CODE=1;
    private static final String TAG="VideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio);

        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(VedioActivity.this);
        //这个也是哦（去除标题栏的框框）
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        videoView=findViewById(R.id.vedioView);
        play=findViewById(R.id.play);
        pause=findViewById(R.id.pause);
        replay=findViewById(R.id.replay);
        choice=findViewById(R.id.choice) ;//按钮的初始化
        choice.setOnClickListener(this);
        play.setOnClickListener(this);
        pause.setOnClickListener(this);
        replay.setOnClickListener(this);//给按钮加监听
        if(ContextCompat.checkSelfPermission(VedioActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(VedioActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);//判断你是否授权
        }
        else {
            inintVideoPath();
        }

        //返回主页面
        vedio_back=findViewById(R.id.vedio_back);
        vedio_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VedioActivity.this, AiListActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void inintVideoPath(){
        File file=new File(Environment.getExternalStorageDirectory(),"snow.mp4");//打开软件直接播放的视频名字是snow.mp4
        videoView.setVideoPath(file.getPath());//指定视频文件的路径
    }
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    inintVideoPath();
                }
                else {
                    Toast.makeText(this,"拒绝权限将无法访问程序",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    public void onClick(View view){//各按钮的功能
        switch (view.getId()){
            case R.id.play:
                if(!videoView.isPlaying()){//播放
                    videoView.start();
                }
                break;
            case R.id.pause:
                if(videoView.isPlaying()){//暂停
                    videoView.pause();
                }
                break;
            case R.id.replay:
                if(videoView.isPlaying()){
                    videoView.resume();//重新播放
                }
                break;
            case R.id.choice://选择文件
                Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
                //  intent.setType("*/*");//设置类型，这是任意类型
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
        }
    }
    public void onDestroy(){//释放资源
        super.onDestroy();
        if(videoView!=null){
            videoView.suspend();
        }
    }
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        if(resultCode== Activity.RESULT_OK){
            Uri uri=data.getData();
            videoView.setVideoURI(uri);//将选择的文件路径给播放器
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
        if (requestCode == FILE_SELECT_CODE) {
            Uri uri = data.getData();
            Log.i(TAG, "------->" + uri.getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void choseFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
        }

    }


}
