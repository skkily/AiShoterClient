package com.skkily.aishoterclient.FaceCheck;


import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.skkily.aishoterclient.FaceCheck.faceInfo.FaceCheckInfo;
import com.skkily.aishoterclient.FaceCheck.faceInfo.FaceSignInInfo;
import com.skkily.aishoterclient.FaceCheck.faceInfo.FaceSignUpInfo;
import com.skkily.aishoterclient.FaceCheck.util.Util;
import com.skkily.aishoterclient.LoginUtil.User;
import com.skkily.aishoterclient.ServerIp;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLException;

import static android.content.Context.MODE_PRIVATE;


public class FaceNetUtil {
    private Gson gson=new Gson();
    private String token="false";

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

    //检查人脸
    public String faceCheck(){
        File file = new File("/data/data/com.skkily.aishoterclient/aaa.png");
        byte[] buff = getBytesFromFile(file);
        String url = "https://api-cn.faceplusplus.com/facepp/v3/detect";
        HashMap<String, String> map = new HashMap<>();
        HashMap<String, byte[]> byteMap = new HashMap<>();
        map.put("api_key", Util.API_KEY);
        map.put("api_secret", Util.API_SECRET);
        byteMap.put("image_file", buff);
        try {
            byte[] bacd = post(url, map, byteMap);
            String str = new String(bacd);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    //注册人脸
    public String faceSinIn(String userId){
        String token2=faceSignUp(0);
        //Toast.makeText(OpenglActivity.this,"token2:"+token2,Toast.LENGTH_LONG).show();
        if(token2.equals("false")) {
            String str="false";
            if(!token.equals("false")) {
                str = token;
            }

            String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/addface";
            HashMap<String, String> map = new HashMap<>();
            map.put("api_key", Util.API_KEY);
            map.put("api_secret", Util.API_SECRET);
            map.put("outer_id", "faceHub1");
            map.put("face_tokens", str);
            try {
                System.out.println(post(url, map));
                String getstr = sendToCarServer(
                        "{\"code\":3,\"face_token\":\""+token+"\",\"userId\":\""+userId+"\"}");
                return getstr;
            }
            catch (JsonSyntaxException e){
                e.printStackTrace();
                token="false";
                return "false";

            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            String getstr = sendToCarServer(
                    "{\"code\":3,\"face_token\":\""+token2+"\",\"userId\":\""+userId+"\"}");
            return getstr;
        }
        token="false";
        return "false";
    }
    //登录人脸
    public String faceSignUp(int type){
        String str=faceCheck();
        Gson gson=new Gson();
        FaceCheckInfo faceInfo=gson.fromJson(str, FaceCheckInfo.class);//token1
        if(faceInfo.getFaces().get(0).getFace_token()!=null){
            token=faceInfo.getFaces().get(0).getFace_token();//当前照片token，用于照片注册之用
            String url = "https://api-cn.faceplusplus.com/facepp/v3/search";
            HashMap<String, String> map = new HashMap<>();
            map.put("api_key", Util.API_KEY);
            map.put("api_secret", Util.API_SECRET);
            map.put("outer_id","faceHub1");
            map.put("face_token",faceInfo.getFaces().get(0).getFace_token());
            try {
                byte[] bacd = post(url, map);
                String request = new String(bacd);
                FaceSignUpInfo faceSignUpInfo=gson.fromJson(request,FaceSignUpInfo.class);
                if(faceSignUpInfo.getResults().get(0).getConfidence()>75&&type==1){
                    String getstr = sendToCarServer(
                            "{\"code\":4,\"face_token\":\""+faceSignUpInfo.getResults().get(0).getFace_token()+"\"}");//send the get token2
                    return getstr;//需要发送登录返回的正确token
                }else if(faceSignUpInfo.getResults().get(0).getConfidence()>80) {
                    return faceSignUpInfo.getResults().get(0).getFace_token();//token2
                }else {
                    return "false";
                }
            }catch (JsonSyntaxException e){
                return "false";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "false";
    }
    //创建人脸库
    public static String faceCreate(){
        String url = "https://api-cn.faceplusplus.com/facepp/v3/faceset/create";
        HashMap<String, String> map = new HashMap<>();
        map.put("api_key",Util.API_KEY);
        map.put("api_secret", Util.API_SECRET);
        map.put("outer_id","faceHub1");
        try {
            byte[] bacd = post(url, map);
            String str = new String(bacd);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private final static int CONNECT_TIME_OUT = 30000;
    private final static int READ_OUT_TIME = 50000;
    private static String boundaryString = getBoundary();


    protected static byte[] post(String url, HashMap<String, String> map, HashMap<String, byte[]> fileMap) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        if(fileMap != null && fileMap.size() > 0){
            Iterator fileIter = fileMap.entrySet().iterator();
            while(fileIter.hasNext()){
                Map.Entry<String, byte[]> fileEntry = (Map.Entry<String, byte[]>) fileIter.next();
                obos.writeBytes("--" + boundaryString + "\r\n");
                obos.writeBytes("Content-Disposition: form-data; name=\"" + fileEntry.getKey()
                        + "\"; filename=\"" + encode(" ") + "\"\r\n");
                obos.writeBytes("\r\n");
                obos.write(fileEntry.getValue());
                obos.writeBytes("\r\n");
            }
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }


    private static String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }

    protected static byte[] post(String url, HashMap<String, String> map) throws Exception {
        HttpURLConnection conne;
        URL url1 = new URL(url);
        conne = (HttpURLConnection) url1.openConnection();
        conne.setDoOutput(true);
        conne.setUseCaches(false);
        conne.setRequestMethod("POST");
        conne.setConnectTimeout(CONNECT_TIME_OUT);
        conne.setReadTimeout(READ_OUT_TIME);
        conne.setRequestProperty("accept", "*/*");
        conne.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundaryString);
        conne.setRequestProperty("connection", "Keep-Alive");
        conne.setRequestProperty("user-agent", "Mozilla/4.0 (compatible;MSIE 6.0;Windows NT 5.1;SV1)");
        DataOutputStream obos = new DataOutputStream(conne.getOutputStream());
        Iterator iter = map.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<String, String> entry = (Map.Entry) iter.next();
            String key = entry.getKey();
            String value = entry.getValue();
            obos.writeBytes("--" + boundaryString + "\r\n");
            obos.writeBytes("Content-Disposition: form-data; name=\"" + key
                    + "\"\r\n");
            obos.writeBytes("\r\n");
            obos.writeBytes(value + "\r\n");
        }
        obos.writeBytes("--" + boundaryString + "--" + "\r\n");
        obos.writeBytes("\r\n");
        obos.flush();
        obos.close();
        InputStream ins = null;
        int code = conne.getResponseCode();
        try{
            if(code == 200){
                ins = conne.getInputStream();
            }else{
                ins = conne.getErrorStream();
            }
        }catch (SSLException e){
            e.printStackTrace();
            return new byte[0];
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[4096];
        int len;
        while((len = ins.read(buff)) != -1){
            baos.write(buff, 0, len);
        }
        byte[] bytes = baos.toByteArray();
        ins.close();
        return bytes;
    }




    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    public static byte[] getBytesFromFile(File f) {
        if (f == null) {
            return null;
        }
        try {
            FileInputStream stream = new FileInputStream(f);
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b)) != -1)
                out.write(b, 0, n);
            stream.close();
            out.close();
            return out.toByteArray();
        } catch (IOException e) {
        }
        return null;
    }
}
