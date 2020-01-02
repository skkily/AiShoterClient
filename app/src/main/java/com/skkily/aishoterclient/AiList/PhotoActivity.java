package com.skkily.aishoterclient.AiList;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.skkily.aishoterclient.AiListActivity;
import com.skkily.aishoterclient.FaceCheck.util.ConUtil;
import com.skkily.aishoterclient.R;

import java.io.File;
import java.io.FileFilter;


public class PhotoActivity extends AppCompatActivity {

    private Button photo_back;
    private Button photo_pre;
    private Button photo_next;
    private TextView photo_tips;
    private ImageView photo_image;
    private File[] files;
    private  int index;
    private EditText photo_edit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(PhotoActivity.this);
        //这个也是哦（去除标题栏的框框）
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        photo_image=findViewById(R.id.photo_image);
        photo_tips=findViewById(R.id.photo_tips);
        photo_pre=findViewById(R.id.photo_pre);
        photo_next=findViewById(R.id.photo_next);
        photo_edit=findViewById(R.id.photo_edit);

        photo_image.setVisibility(View.VISIBLE);
        photo_edit.setText("/storage/emulated/0/DCIM/Camera");
        //获取路径
        String path=photo_edit.getText().toString();
        //如果路径不为空
        if(!"".equals(path.trim())){
            //加载数据
            files=getImages(path);
            index=0;
            if(null!=files&&files.length>0){
                photo_image.setImageURI(Uri.fromFile(files[index]));
                photo_tips.setText((index+1)+"/"+files.length);
            }
            else{
                photo_tips.setText("此路径下无图片");
                photo_image.setVisibility(View.INVISIBLE);
            }
        }
        //上一张
        photo_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=files){
                    int len=files.length;
                    if(index==0){
                        index=len-1;
                    }else{
                        index--;
                    }
                    photo_image.setImageURI(Uri.fromFile(files[index]));
                    photo_tips.setText((index+1)+"/"+len);
                }
            }
        });
        //下一张
        photo_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(null!=files){
                    int len=files.length;
                    if(index==len-1){
                        index=0;
                    }else{
                        index++;
                    }
                    photo_image.setImageURI(Uri.fromFile(files[index]));
                    photo_tips.setText((index+1)+"/"+len);
                }
            }
        });
        //返回主页面
        photo_back=findViewById(R.id.photo_back);
        photo_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PhotoActivity.this, AiListActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }
    //加载图片
    private File[] getImages(String folderPath) {
        File folder=new File(folderPath);
        if(folder.isDirectory()){
            File[] fs=folder.listFiles(imageFilter);
            return fs;
        }
        return null;
    }
    //检索图片
    private FileFilter imageFilter=new FileFilter() {
        @Override
        public boolean accept(File file) {
            String name =file.getName();
            return name.endsWith(".jpg")||name.endsWith(".jpeg")||name.endsWith(".png");
        }
    };


    //结束进程
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
