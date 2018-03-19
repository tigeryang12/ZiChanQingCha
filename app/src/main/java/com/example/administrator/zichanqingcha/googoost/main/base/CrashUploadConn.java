package com.example.administrator.zichanqingcha.googoost.main.base;

import android.content.Context;
import android.util.Log;


import com.example.administrator.zichanqingcha.googoost.tools.AppUtils;
import com.example.administrator.zichanqingcha.googoost.tools.DateCleanUtils;
import com.example.administrator.zichanqingcha.googoost.tools.Validate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CrashUploadConn extends Thread {
    private String crashpath;// 图片路径
    private String sclj;// 上传路径

    private final static int CONNECT_TIMEOUT = 60;
    private final static int READ_TIMEOUT = 100;
    private final static int WRITE_TIMEOUT = 60;
    private Context mContext;
    private String uname = "undefault";

    public CrashUploadConn(String img_path, String _sclj, Context context, String uname) {
        this.crashpath = img_path;
        this.sclj = _sclj;
        this.mContext=context;
        this.uname = uname;
    }

    public void run() {
        // 进行上传线程
        upload_one_file(sclj, new File(crashpath),mContext);
    }

    /**
     * 上传文件到服务器
     *
     * @param file 图片
     * @param url  上传路径
     * @通用性 通用
     */
    public void upload_one_file(String url, final File file, Context mContext) {
        OkHttpClient client = new OkHttpClient();
        setTimeOut(client);
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);
        body.addFormDataPart("appName", AppUtils.getAppName(mContext));
        if (Validate.isNull(uname)){
            uname = "undefault";
        }
        body.addFormDataPart("userId", uname);
        body.addFormDataPart("Content-Disposition", file.getName(), RequestBody.create(null, file));
        Request request = new Request.Builder().post(body.build()).url(url).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("","连接失败----Error" + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("","连接成功---Success: ");
                if (response.isSuccessful()) {
                    String result = response.body().string();
                    try {
                        JSONObject jo = new JSONObject(result);
                        if (jo.getBoolean("success")) {
                            //如果上传成功，则删除已经上传的日志文件
                            DateCleanUtils.RecursionDeleteFile(new File(CrashHandler.path_crash+ File.separator+ file.getName()));
                            Log.i("","Log日志已成功上传");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * 设置超时时间
     */
    private void setTimeOut(OkHttpClient client) {
        client = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)//设置连接超时时间
                .build();

    }
}
