package com.example.administrator.zichanqingcha.googoost.main.base;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by xjy on 2017/10/25
 */
@Entity
public class CacheDB {
    @Id
    private String activity_action;
    private String activity;
    private String action;
    private String json;
    @Generated(hash = 204720724)
    public CacheDB(String activity_action, String activity, String action,
                   String json) {
        this.activity_action = activity_action;
        this.activity = activity;
        this.action = action;
        this.json = json;
    }
    @Generated(hash = 773821969)
    public CacheDB() {
    }
    public String getActivity() {
        return this.activity;
    }
    public void setActivity(String activity) {
        this.activity = activity;
    }
    public String getAction() {
        return this.action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getJson() {
        return this.json;
    }
    public void setJson(String json) {
        this.json = json;
    }
    public String getActivity_action() {
        return this.activity_action;
    }
    public void setActivity_action(String activity_action) {
        this.activity_action = activity_action;
    }

}
