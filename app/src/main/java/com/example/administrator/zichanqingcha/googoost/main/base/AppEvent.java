package com.example.administrator.zichanqingcha.googoost.main.base;

import org.greenrobot.eventbus.EventBus;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2017/2/8.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */
public class AppEvent {
    private Object data;
    private String msg;

    public AppEvent(){

    }

    public AppEvent(String msg, Object data){
        this.msg = msg;
        this.data = data;
    }

    public Object Data() {
        return data;
    }
    public String what() {
        return msg;
    }

    public static void post(String msg, Object data){
        EventBus.getDefault().postSticky(new AppEvent(msg, data));
    }
}
