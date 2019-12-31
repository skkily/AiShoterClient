package com.skkily.aishoterclient.FaceCheck;



import android.os.Bundle;

import com.skkily.aishoterclient.R;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import android.hardware.Camera.PreviewCallback;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.HandlerThread;


import android.widget.RelativeLayout;
import android.widget.TextView;

import com.skkily.aishoterclient.FaceCheck.mediacodec.MediaHelper;
import com.skkily.aishoterclient.FaceCheck.util.CameraMatrix;
import com.skkily.aishoterclient.FaceCheck.util.ConUtil;

import com.skkily.aishoterclient.FaceCheck.util.ICamera;

import com.skkily.aishoterclient.FaceCheck.util.OpenGLUtil;
import com.skkily.aishoterclient.FaceCheck.util.PointsMatrix;
import com.skkily.aishoterclient.FaceCheck.util.Screen;
import com.skkily.aishoterclient.FaceCheck.util.SensorEventUtil;
import com.megvii.facepp.sdk.Facepp;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class OpenglActivity extends Activity
        implements PreviewCallback, Renderer, SurfaceTexture.OnFrameAvailableListener {

    private boolean isShowFaceRect;
    private String trackModel;
    private GLSurfaceView mGlSurfaceView;
    private ICamera mICamera;
    private Camera mCamera;
    private Intent intent;

    private HandlerThread mHandlerThread = new HandlerThread("facepp");
    private Handler mHandler;
    private Facepp facepp;

    private int min_face_size = 200;
    private int detection_interval = 25;
    private HashMap<String, Integer> resolutionMap;
    private SensorEventUtil sensorUtil;


    private MediaHelper mMediaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screen.initialize(this);
        setContentView(R.layout.activity_opengl);
        intent=getIntent();
        init();


        ConUtil.toggleHideyBar(this);//隐藏通知栏

    }

    //初始化
    private void init() {
        trackModel = "Fast";

        min_face_size = 40;
        detection_interval = 30;
        resolutionMap = null;

        facepp = new Facepp();

        sensorUtil = new SensorEventUtil(this);

        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        mGlSurfaceView = findViewById(R.id.opengl_layout_surfaceview);
        mGlSurfaceView.setEGLContextClientVersion(2);// 创建一个OpenGL ES 2.0
        // context
        mGlSurfaceView.setRenderer(this);// 设置渲染器进入gl
        // RENDERMODE_CONTINUOUSLY不停渲染
        // RENDERMODE_WHEN_DIRTY懒惰渲染，需要手动调用 glSurfaceView.requestRender() 才会进行更新
        mGlSurfaceView.setRenderMode(mGlSurfaceView.RENDERMODE_WHEN_DIRTY);// 设置渲染器模式

        mICamera = new ICamera();


    }

    private int Angle;

    @Override
    protected void onResume() {
        super.onResume();
        ConUtil.acquireWakeLock(this);
        startTime = System.currentTimeMillis();
        mCamera = mICamera.openCamera(false, this, resolutionMap);
        //相机的设置和facepp的初始化
        if (mCamera != null) {
            Angle = 360 - mICamera.Angle;

            RelativeLayout.LayoutParams layout_params = mICamera.getLayoutParam();
            mGlSurfaceView.setLayoutParams(layout_params);

            int width = mICamera.cameraWidth;
            int height = mICamera.cameraHeight;

            int left = 0;
            int top = 0;
            int right = width;
            int bottom = height;


            String errorCode = facepp.init(this, ConUtil.getFileContent(this, R.raw.megviifacepp_0_5_2_model), 1);

            //sdk内部其他api已经处理好，可以不判断
            if (errorCode!=null){
                Intent intent=new Intent();
                intent.putExtra("errorcode",errorCode);
                setResult(101,intent);
                finish();
                return;
            }

            Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
            faceppConfig.interval = detection_interval;
            faceppConfig.minFaceSize = min_face_size;
            faceppConfig.roi_left = left;
            faceppConfig.roi_top = top;
            faceppConfig.roi_right = right;
            faceppConfig.roi_bottom = bottom;
            String[] array = getResources().getStringArray(R.array.trackig_mode_array);
            if (trackModel.equals(array[0]))
                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_FAST;
            else if (trackModel.equals(array[1]))
                faceppConfig.detectionMode = Facepp.FaceppConfig.DETECTION_MODE_TRACKING_ROBUST;
            else if (trackModel.equals(array[2])) {
                faceppConfig.detectionMode = Facepp.FaceppConfig.MG_FPP_DETECTIONMODE_TRACK_RECT;
                isShowFaceRect = true;
            }
            facepp.setFaceppConfig(faceppConfig);
        }
        mMediaHelper = new MediaHelper(mICamera.cameraWidth, mICamera.cameraHeight, true, mGlSurfaceView);
    }

    //facepp的初始化
    private void setConfig(int rotation) {
        Facepp.FaceppConfig faceppConfig = facepp.getFaceppConfig();
        if (faceppConfig.rotation != rotation) {
            faceppConfig.rotation = rotation;
            facepp.setFaceppConfig(faceppConfig);
        }
    }

    boolean isSuccess = false;
    float confidence;
    long startTime;
    int rotation = Angle;

    long matrixTime;


    //人脸检测接口
    int check_num=0;
    @Override
    public void onPreviewFrame(final byte[] imgData, final Camera camera) {
        //检测操作放到主线程，防止贴点延迟
        int width = mICamera.cameraWidth;
        int height = mICamera.cameraHeight;
        long faceDetectTime_action = System.currentTimeMillis();
        final int orientation = sensorUtil.orientation;
        if (orientation == 0)
            rotation = Angle;
        else if (orientation == 1)
            rotation = 0;
        else if (orientation == 2)
            rotation = 180;
        else if (orientation == 3)
            rotation = 360 - Angle;

        //图片旋转角度
        setConfig(rotation);

        //检测图片中人脸
        final Facepp.Face[] faces = facepp.detect(imgData, width, height, Facepp.IMAGEMODE_NV21);
        final long algorithmTime = System.currentTimeMillis() - faceDetectTime_action;
        if (faces != null) {
            long actionMaticsTime = System.currentTimeMillis();
            ArrayList<ArrayList> pointsOpengl = new ArrayList<ArrayList>();
            ArrayList<FloatBuffer> rectsOpengl = new ArrayList<FloatBuffer>();
            if (faces.length > 0) {
                for (int c = 0; c < faces.length; c++) {

                    facepp.getLandmarkRaw(faces[c], Facepp.FPP_GET_LANDMARK81);

                    confidence = faces[c].confidence;


                    //0.4.7之前（包括）jni把所有角度的点算到竖直的坐标，所以外面画点需要再调整回来，才能与其他角度适配
                    //目前getLandmarkOrigin会获得原始的坐标，所以只需要横屏适配好其他的角度就不用适配了，因为texture和preview的角度关系是固定的
                    ArrayList<FloatBuffer> triangleVBList = new ArrayList<FloatBuffer>();
                    for (int i = 0; i < faces[c].points.length; i++) {
                        float x = (faces[c].points[i].x / width) * 2 - 1;
                        float y = (faces[c].points[i].y / height) * 2-1;
                        float[] pointf = new float[]{y, x, 0.0f};
                        FloatBuffer fb = mCameraMatrix.floatBufferUtil(pointf);
                        triangleVBList.add(fb);
                    }
                    pointsOpengl.add(triangleVBList);
                    if(faces[c].rect.bottom>287&&faces[c].rect.bottom<375)
                        if(faces[c].rect.top>90&&faces[c].rect.top<167)
                            if(faces[c].rect.left>147&&faces[c].rect.left<244)
                                if(faces[c].rect.right>363&&faces[c].rect.right<432){
                                    check_num++;
                                    if(check_num==10){
                                        System.out.println("ok!");
                                        //进行图片旋转剪裁
                                        camera.takePicture(null, null, new Camera.PictureCallback() {
                                            @Override
                                            public void onPictureTaken(byte[] data, Camera camera) {
                                                //将字节数组
                                                Bitmap bitmap= BitmapFactory.decodeByteArray(data,0,data.length);
                                                //输出流保存数据
                                                try {
                                                    Bitmap bm=adjustPhotoRotation(bitmap,270);
                                                    bm=imageCrop(bm,1050);

                                                    FileOutputStream fileOutputStream=new FileOutputStream("/data/data/com.skkily.aishoterclient/aaa.png");
                                                    bm.compress(Bitmap.CompressFormat.JPEG,80,fileOutputStream);
                                                    camera.stopPreview();
                                                    intnet_send_face(imgData);
                                                    //camera.startPreview();
                                                    System.out.println("save ok");
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });



                                    }
                                }

                }
            }

            //最大框
            Rect rect=new Rect();
            rect.bottom=375;
            rect.top=90;
            rect.left=147;
            rect.right=432;
            FloatBuffer buffers = calRectPostion(rect, mICamera.cameraWidth, mICamera.cameraHeight);
            rectsOpengl.add(buffers);


            synchronized (mPointsMatrix) {
                mPointsMatrix.bottomVertexBuffer = null;
                mPointsMatrix.points = pointsOpengl;
                mPointsMatrix.faceRects = rectsOpengl;
            }

            matrixTime = System.currentTimeMillis() - actionMaticsTime;

        }

        if (isSuccess)
            return;
        isSuccess = true;


    }

    //调整图片角度
    Bitmap adjustPhotoRotation(Bitmap bm, final int orientationDegree) {

        android.graphics.Matrix m = new android.graphics.Matrix();
        m.setRotate(orientationDegree, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);

        try {
            Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);

            return bm1;

        } catch (OutOfMemoryError ex) {
        }
        return null;

    }

    //图片裁剪
    Bitmap imageCrop(Bitmap bitmap, int width) {
        // 得到图片的宽，高
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        //width最大不能超过长方形的短边
        if (w < width || h < width) {
            width = w > h ? h : w;
        }

        int retX = (w - width) / 2;
        int retY = (h - width) / 2;

        return Bitmap.createBitmap(bitmap, retX, retY, width, width, null, false);
    }

    //网络发送
    public int intnet_send_face(final byte[] buff){

        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceNetUtil sendSome=new FaceNetUtil();
                switch (intent.getStringExtra("type")){
                    case "1":
                        final String str1=sendSome.faceCheck();
                        System.out.println(str1);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView=findViewById(R.id.text_log);
                                textView.setText("人脸识别:"+str1);
                            }
                        });
                        break;
                    case "2":
                        final String str2=sendSome.faceSinIn();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView=findViewById(R.id.text_log);
                                textView.setText("人脸注册:"+str2);
                            }
                        });
                        System.out.println(str2);
                        break;
                    case "3":
                        final String str3=sendSome.faceSignUp();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView textView=findViewById(R.id.text_log);
                                textView.setText("人脸登录:"+str3);
                            }
                        });
                        System.out.println(str3);
                        break;
                }
            }
        }).start();


        return 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
        ConUtil.releaseWakeLock();
        mICamera.closeCamera();
        mCamera = null;
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mMediaHelper!=null)
            mMediaHelper.stopRecording();
        super.onDestroy();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                facepp.release();
            }
        });

    }

    private int mTextureID = -1;
    private SurfaceTexture mSurface;
    private CameraMatrix mCameraMatrix;
    private PointsMatrix mPointsMatrix;

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        // TODO Auto-generated method stub

        mGlSurfaceView.requestRender();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // 黑色背景
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        surfaceInit();
    }

    private void surfaceInit() {
        mTextureID = OpenGLUtil.createTextureID();

        mSurface = new SurfaceTexture(mTextureID);

        // 这个接口就干了这么一件事，当有数据上来后会进到onFrameAvailable方法
        mSurface.setOnFrameAvailableListener(this);// 设置照相机有数据时进入
        mCameraMatrix = new CameraMatrix(mTextureID);
        mPointsMatrix = new PointsMatrix(false);
        mPointsMatrix.isShowFaceRect = isShowFaceRect;
        mICamera.startPreview(mSurface);// 设置预览容器
        mICamera.actionDetect(this);

    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // 设置画面的大小
        GLES20.glViewport(0, 0, width, height);

        float ratio = 1; // 这样OpenGL就可以按照屏幕框来画了，不是一个正方形了

        Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 3, 7);

    }

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjMatrix = new float[16];
    private final float[] mVMatrix = new float[16];

    @Override
    public void onDrawFrame(GL10 gl) {

        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);// 清除屏幕和深度缓存
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx);
        mCameraMatrix.draw(mtx);
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1f, 0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);

        mPointsMatrix.draw(mMVPMatrix);

        mSurface.updateTexImage();// 更新image，会调用onFrameAvailable方法


    }



    private FloatBuffer calRectPostion(Rect rect, float width, float height) {
        float top = 1 - (rect.top * 1.0f / height) * 2;
        float left = (rect.left * 1.0f / width) * 2 - 1;
        float right = (rect.right * 1.0f / width) * 2 - 1;
        float bottom = 1 - (rect.bottom * 1.0f / height) * 2;

        // 左上角
        float x1 = -top;
        float y1 = left;

        // 右下角
        float x2 = -bottom;
        float y2 = right;



        float[] tempFace = {
                x1, y2, 0.0f,
                x1, y1, 0.0f,
                x2, y1, 0.0f,
                x2, y2, 0.0f,
        };

        FloatBuffer buffer = mCameraMatrix.floatBufferUtil(tempFace);
        return buffer;
    }



}

