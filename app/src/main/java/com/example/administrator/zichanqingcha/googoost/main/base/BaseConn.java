package com.example.administrator.zichanqingcha.googoost.main.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.administrator.zichanqingcha.googoost.tools.OkHttpHelper;
import com.example.administrator.zichanqingcha.googoost.tools.Validate;


/**
 * Created by gh on 2017/12/14.
 */

public abstract class BaseConn extends Thread {
    private Handler handler;
    private String rev;
    public Message message;
    public Bundle bundle;

    public void run() {
        Looper.prepare();
        bundle = new Bundle();
        message = new Message();
        handler = new Handler() {// 接收回传的message，处理并跳转
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0x1://连接异常
                        rev = msg.obj + "";
                        message.what = 1;
                        if (Validate.noNull(msg.obj + "")) {// 异常信息
                            rev = msg.obj.toString();
                            bundle.putString("msg", rev);// 把失败原因传回去，有利于查找导致失败的原因的代码
                        } else {// 未与后台建立任何连接导致
                            bundle.putString("msg", "建立连接失败！");// 把失败原因传回去，有利于查找导致失败的原因的代码
                        }
                        message.setData(bundle);
                        getHandler().sendMessage(message);
                        break;
                    case 0x2://连接成功
                        if (Validate.noNull(msg.obj + "")) {//返回了json
                            rev = msg.obj.toString();
                            process(rev);
                        } else {// 后台没有传值过来导致失败
                            message.what = 1;
                            bundle.putString("msg", "服务器传参异常！");
                            message.setData(bundle);
                            getHandler().sendMessage(message);
                        }
                        break;
                }
            }
        };
        System.out.println("path=" + getPath() + "action=" + getAction() + "?key=" + getGson());
        if (isTrue()) {
            OkHttpHelper.getInstance().postConn(getPath(), getAction(), getGson(), handler, "String");
        } else {
            process(setData());
        }
        Looper.loop();
    }

    /**
     * @method 网络请求
     **/
    public abstract String getAction();

    /**
     * @method 参数
     **/
    public abstract String getGson();

    /**
     * @method 回传handler
     **/
    public abstract Handler getHandler();

    /**
     * @method 网络请求路径
     **/
    public abstract String getPath();

    /**
     * @method 是否真数据
     **/
    public abstract boolean isTrue();

    /**
     * @method 假数据封装
     **/
    public abstract String setData();

    /**
     * @method 回传数据
     **/
    public abstract void process(String rev);
}
