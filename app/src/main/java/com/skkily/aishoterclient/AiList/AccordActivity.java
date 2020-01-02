package com.skkily.aishoterclient.AiList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.skkily.aishoterclient.AiListActivity;
import com.skkily.aishoterclient.R;

public class AccordActivity extends AppCompatActivity {

    private TextView accord_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accord);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(AccordActivity.this);
        if (getSupportActionBar() != null){     //这个也是哦（去除标题栏的框框）
            getSupportActionBar().hide();
        }
        TextView accord_text=(TextView)findViewById(R.id.accord_text);
        Button accord_back=(Button)findViewById(R.id.accord_back);
        accord_text.setText("       Alpha智能摄影师是运用TensorFlow、Bigtable、Android等技术，模仿专业摄影师研发的智能照相系统。系统由智能小车、可控伸缩杆、平衡仪、照相机等四部分组成。项目的目标市场初步锁定在企事业单位，应用场景为各类大中型会议。系统通过预先设置，对当前场景中人物、位置、背景进行识别，实现对主持人、嘉宾、主讲人、观众等角色进行智能化的拍照或录像。智能小车和可控伸缩杆用来调整相机的位置和高度，以满足特写、全景等需求，平衡仪用来降低照相过程中的抖动。相机的智能化参数设置是本系统的关键技术，通过TensorFlow建立的专用神经网络，用已标注的海量优质照片作为训练集（已经收集了近6亿张照片），将获得的不同场景的参数用于拍摄，以拍出较高质量的照片，而拍摄出来的照片会在云端自动进行二次美化，最终输出高质量的照片，以此来达到提高非专业人员照片拍摄水平的目的。\n" +
                "       日本、德国和美国等发达国家早在上世纪六十年代就开始了对于智能相机的研究，到上世纪九十年代，随着光电子技术和计算机技术的发展，智能相机取得了广泛的应用，其市场潜力十分巨大。随着2017年12月20日国务院正式印发《新一代人工智能发展规划》，国家鼓励人工智能和各行业的融合创新，与此同时，涌现出了一股股基于人工智能的近期取得的丰硕成果。\n" +
                "       人工智能是解决人类重复性劳动问题的利器。在常见的会议、表演现场中，会场正中央总会树立一架摄像机和照相机，而摄影师的背影常常是最煞风景的一幕；又考虑到现今社会中对摄影师的需求量和不同资历摄影师的薪资对比（见图1，图2），制作出一款Alpha智能摄影师的想法也就顺势而生。\n" +
                "       不仅仅是会议摄影、拍照，还有晚会、慕课等，这一系列对摄影架子的稳定性、以及拍摄角度稳定不变的场景是数不胜数，而通常这样的场景拍摄都会需要大量的人力物力来是实现拍摄效果。\n");
        accord_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(AccordActivity.this, AiListActivity.class);
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
