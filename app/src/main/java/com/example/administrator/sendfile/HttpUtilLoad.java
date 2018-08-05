package com.example.administrator.sendfile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/8.
 */

public class HttpUtilLoad {
    private static CustomFileInputStream fStream;
    private static DataOutputStream ds;
    private static HttpURLConnection con;

    public static void endFile(){

        try {
            if (fStream != null){
                fStream.close();
            }
            if (ds != null){
                ds.flush();
            }
            if (con != null){
                con.disconnect();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String submitPostData(String urlPath, Map<String, String> params, String encode) {


        byte[] data = getRequestData(params, encode).toString().getBytes();//获得请求体
        try {
            URL url = new URL(urlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setConnectTimeout(3000);//设置连接超时时间
            httpURLConnection.setDoInput(true); //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true); //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");//设置以Post方式提交数据
            httpURLConnection.setUseCaches(false); //使用Post方式不能使用缓存
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);

            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if(response == HttpURLConnection.HTTP_OK) {
                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
         for(Map.Entry<String, String> entry : params.entrySet()) {
             stringBuffer.append(entry.getKey())
                  .append("=")
                  .append(URLEncoder.encode(entry.getValue(), encode))
                     .append("&");
               }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
            } catch (Exception e) {
            e.printStackTrace();
             }
         return stringBuffer;
    }

    public static String dealResponseResult(InputStream inputStream) {
        String resultData = null;      //存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
    /***
     *
     * @param urlPath 服务器地址
     * @param filePath 文件路径
     * @param newName 文件名字
     * @param fileSize 文件大小
     * @param listener 进度监听
     * @return
     * @throws Exception
     */
    public static String sendFile(String urlPath, String filePath,
                                  String newName, String fileSize, OnUploadListener listener) throws Exception {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        URL url = new URL(urlPath);
        con = (HttpURLConnection) url.openConnection();

//        //下载需要将setDoInput方法的参数值设为true
        con.setDoInput(true);
        //上传需要将setDoOutput方法的参数值设为true
        con.setDoOutput(true);
        //禁止HttpURLConnection使用缓存
        con.setUseCaches(false);
        //使用POST请求
        con.setRequestMethod("POST");
        //以下三行设置http请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");
        //在模拟web页面向服务器端上传文件时，每个文件的开头需要有一个分界符，
        //分界符需要在http请求头中指定。boundary是任意一个字符串，一般为******
        con.setRequestProperty("Content-Type", "multipart/form-data;boundary="
                + boundary);

        ds = new DataOutputStream(con.getOutputStream());

        ds.writeBytes(twoHyphens + boundary + end);
        //上传文件相关信息，这些信息包括请求参数名，上传文件名，文件类型，但并不限于此
        ds.writeBytes("Content-Disposition: form-data; "
                + "name=\"file1\";filename=\"" + newName + "\"" + ";size=\"" + fileSize + "\"" + end);
        ds.writeBytes(end);
        //获得文件的输入流，通过流传输文件。在这里我重写了FileInputStream，为了监听上传进度
        fStream = new CustomFileInputStream(filePath);
        fStream.setOnUploadListener(listener);
        /* 设置每次写入1024bytes */
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int length = -1;
        // 从文件读取数据至缓冲区
        while ((length = fStream.read(buffer)) != -1) {
            //将资料写入DataOutputStream中
            ds.write(buffer, 0, length);
        }
        ds.writeBytes(end);
        ds.writeBytes(twoHyphens + boundary + twoHyphens + end);

        fStream.close();
        ds.flush();

        //上传完成以后获取服务器的反馈
        InputStream is = con.getInputStream();
        int ch;
        StringBuffer b = new StringBuffer();
        while ((ch = is.read()) != -1) {
            b.append((char) ch);
        }

        ds.close();
        return b.toString();
    }
}
