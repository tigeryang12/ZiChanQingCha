package com.example.administrator.zichanqingcha.googoost.main.base;


import android.content.Context;

/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2017/10/25 0025.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */
public class GreenDaoManager {
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private static volatile GreenDaoManager mInstance = null;

    private GreenDaoManager(Context context) {
        if (mInstance == null) {
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "datacatch.db");
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }

    public static GreenDaoManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager(context);
                }
            }
        }
        return mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }

    public DaoSession getSession() {
        return mDaoSession;
    }

    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }

    /**
     * 获取实体类
     * @param activity activity名字
     * @param action action名字
     * @param json json字段
     * @return
     */
    public  CacheDB getEntity(String activity, String action, String json){
        CacheDB user = new CacheDB();
        user.setActivity(activity);
        user.setActivity_action(activity+action);
        user.setAction(action);
        user.setJson(json);
        return  user;
    }


}