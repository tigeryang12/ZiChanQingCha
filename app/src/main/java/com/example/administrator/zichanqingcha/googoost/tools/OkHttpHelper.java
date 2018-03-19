package com.example.administrator.zichanqingcha.googoost.tools;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static com.example.administrator.zichanqingcha.googoost.main.base.BaseApplication.map;

/**
 * 类名：OkHttpHelper.class
 * 描述：网络请求实现类
 * Created by：刘帅 on 2017/6/26.
 * --------------------------------------
 * 修改内容：修改网络超时可用
 * 备注：
 * Modify by：
 */
public class OkHttpHelper {

    private final static int CONNECT_TIMEOUT = 100;
    private final static int READ_TIMEOUT = 60;
    private final static int WRITE_TIMEOUT = 60;
    private static OkHttpHelper httpHelper;
    private OkHttpClient client;

    private OkHttpHelper() {
    }

    public static OkHttpHelper getInstance() {
//        if (httpHelper == null)
//            httpHelper = new OkHttpHelper();
        return new OkHttpHelper();
    }


    /**
     * get请求
     *
     * @param url     请求路径
     * @param handler
     * @param type    需要解析的类型 只能是String InputStream byte[] 三种
     */
    public void getConn(String url, final Handler handler, final String type) {
        //创建okhttpclient对象
        WebApi();
        //根据请求url创建一个request对象
        Request request = new Request.Builder().url(url).build();
        //根据request对象发起get同步http请求 execute 是同步请求，会阻塞线程
        //Response response = client.newCall(request).execute();
        //根据request对象发起get异步http请求 enqueue是异步请求方法,不会造成线程阻塞
//        msg = new Message();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()){

                }else {
                    Message msg = new Message();
                    msg.what = 0x2;//网络请求失败
                    msg.obj = e.toString();
                    msg.obj = "网络请求错误";
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg = new Message();
                    switch (type) {
                        case "String":
                            String result = response.body().string();
                            msg.obj = result;
                            break;
                        case "InputStream":
                            InputStream inputStream = response.body().byteStream();//可用于下载文件
                            msg.obj = inputStream;
                            break;
                        case "Byte":
                            byte[] bytes = response.body().bytes();
                            msg.obj = bytes;
                            break;
                        default:
                            String _default = response.body().string();
                            msg.obj = _default;
                            break;
                    }
                    msg.what = 0x1;//网络请求成功
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * post请求
     *
     * @param url     请求路径
     * @param action  接口名
     * @param json    数据
     * @param handler
     * @param type    需要解析的类型 只能是String InputStream byte[] 三种
     */
    public void postConn(String url, String action, String json, final Handler handler, final String type) {
        //创建okhttpclient对象
        WebApi();
        String urlnew = url + action;
        //FormBody对象添加多个请求参数键值对
        FormBody body = new FormBody.Builder().add("key", json).build();
        Request request = new Request.Builder().url(urlnew).post(body).build();
//        msg = new Message();
        final Call mycall = client.newCall(request);
//        map.put(tag, mycall);
        mycall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()){

                }else {
                    Message msg = new Message();
                    msg.what = 0x1;//网络请求失败
                    msg.obj = e.toString();
                    msg.obj = "网络请求错误";
                    handler.sendMessage(msg);
                }
//                map.remove(tag);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg = new Message();
                    switch (type) {
                        case "String":
                            String result = response.body().string();
                            msg.obj = result;
                            break;
                        case "InputStream":
                            InputStream inputStream = response.body().byteStream();//可用于下载文件
                            msg.obj = inputStream;
                            break;
                        case "Byte":
                            byte[] bytes = response.body().bytes();
                            msg.obj = bytes;
                            break;
                        default:
                            String _default = response.body().string();
                            msg.obj = _default;
                            break;
                    }
                    msg.what = 0x2;//网络请求成功
                    handler.sendMessage(msg);
//                    map.remove(tag);
                }
            }
        });
    }
    /**
     * post请求
     *
     * @param url     请求路径
     * @param action  接口名
     * @param json    数据
     * @param handler
     * @param type    需要解析的类型 只能是String InputStream byte[] 三种
     */
    public void postConn(String url, String action, String json, final Handler handler, final String type, final String tag) {
        //创建okhttpclient对象
        WebApi();
        String urlnew = url + action;
        //FormBody对象添加多个请求参数键值对
        FormBody body = new FormBody.Builder().add("key", json).build();
        Request request = new Request.Builder().url(urlnew).post(body).build();
//        msg = new Message();
        Call pre_call = map.get(tag);
        if (pre_call!=null) {
            if (pre_call.isExecuted()) {
                pre_call.cancel();
                map.remove(tag);
            }
        }
        final Call mycall = client.newCall(request);
        map.put(tag, mycall);
        mycall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()){
                    Log.e("PostConn", "postConn: call.isCanceled()");
                }else {
                    Message msg = new Message();
                    msg.what = 0x1;//网络请求失败
                    msg.obj = e.toString();
                    msg.obj = "网络请求错误";
                    handler.sendMessage(msg);
                }
                map.remove(tag);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg = new Message();
                    switch (type) {
                        case "String":
                            String result = response.body().string();
                            msg.obj = result;
                            break;
                        case "InputStream":
                            InputStream inputStream = response.body().byteStream();//可用于下载文件
                            msg.obj = inputStream;
                            break;
                        case "Byte":
                            byte[] bytes = response.body().bytes();
                            msg.obj = bytes;
                            break;
                        default:
                            String _default = response.body().string();
                            msg.obj = _default;
                            break;
                    }
                    msg.what = 0x2;//网络请求成功
                    handler.sendMessage(msg);
                    map.remove(tag);
                }
            }
        });
    }

    /**
     * 单文件上传
     *
     * @param url     上传路径
     * @param file    文件
     * @param handler
     */
    public void upload_one_file(String url, File file, final Handler handler) {
        WebApi();
//        msg = new Message();
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
//        body.addFormDataPart("action", "action");
        body.addFormDataPart("Content-Disposition", file.getName(), RequestBody.create(null, file));
        Request request = new Request.Builder().post(body.build()).url(url).build();
        final Call mycall = client.newCall(request);
        mycall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 0x2;//网络请求失败
                msg.obj = e.toString();
                msg.obj = "网络请求错误";
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg = new Message();
                    String result = response.body().string();
                    msg.obj = result;
                    msg.what = 0x1;//网络请求成功
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 多文件上传
     *
     * @param url     上传路径
     * @param files   文件集合
     * @param handler
     */
    public void upload_more_file(String url, List<File> files, final Handler handler) {
        WebApi();
//        msg = new Message();
        //多文件表单上传构造器
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (File file : files) {
            if (file.exists()) {
//                KL.d(file.getName());
                body.addFormDataPart(file.getName(), file.getName(), RequestBody.create(null, file));
            }
        }
        //构造文件上传时的请求对象Request
        Request request = new Request.Builder().url(url).post(body.build()).build();
        final Call mycall = client.newCall(request);
        mycall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 0x2;//网络请求失败
                msg.obj = e.toString();
                msg.obj = "网络请求错误";
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg = new Message();
                    String result = response.body().string();
                    msg.obj = result;
                    msg.what = 0x1;//网络请求成功
                    handler.sendMessage(msg);
                }
            }
        });
    }

    /**
     * 下载文件
     *
     * @param url      下载路径
     * @param path     文件存储路径
     * @param fileName 文件名称
     * @param handler
     */
    public void getFile(final String url, final String path, final String fileName, final Handler handler) {
        WebApi();
//        msg = new Message();
        Request request = new Request.Builder().url(url).get().build();
        final Call mycall = client.newCall(request);
        mycall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message msg = new Message();
                msg.what = 0x2;//网络请求失败
                msg.obj = e.toString();
                msg.obj = "网络请求错误";
                handler.sendMessage(msg);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Message msg = new Message();
                    InputStream is = response.body().byteStream();
                    File file = new File(path);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File downLoad = new File(file, fileName);
//                    KL.d(downLoad.toString());
                    FileOutputStream fos = new FileOutputStream(downLoad);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    is.close();
                    msg.what = 0x1;//网络请求成功
                    msg.obj = "文件下载成功";
                    handler.sendMessage(msg);
                }
            }
        });
    }

//    /**
//     * 设置超时时间
//     */
//    private void setTimeOut(OkHttpClient client) {
//        client = new OkHttpClient.Builder()
//                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
//                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
//                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
//                .build();
//
//    }

    public void WebApi(){
        client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();
    }
}
