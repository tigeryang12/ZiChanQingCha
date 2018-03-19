package com.example.administrator.zichanqingcha.googoost.main.base;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.example.administrator.zichanqingcha.googoost.tools.AppUtils;
import com.example.administrator.zichanqingcha.googoost.tools.CockroachUtil;
import com.umeng.message.PushAgent;

import org.litepal.LitePalApplication;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * 类名：com.house.base.BaseApplication.class
 * 描述：继承Application类来实现应用程序级的全局变量，这种全局变量方法相对静态类更有保障，直到应用的所有Activity全部被destory掉之后才会被释放掉。
 * Created by 刘帅 on 2016/8/9.
 */
public class BaseApplication extends LitePalApplication {
    /**
     * 注意：这里的boolean，属于全局配置，一次配置，全局使用
     * **/

    /**
     * 该类的实例
     **/
    private static BaseApplication application;
    private List<Activity> activityList = new LinkedList<Activity>();

    /**
     * Activity之间的传值可用此方法
     **/
    public Map<String, Object> mCache = new HashMap<String, Object>();

    /**
     * app名称
     **/
    public static String APP_NAME;

    /**
     * 是否输出日志信息
     **/
    public static final boolean isDebug = true;

    /**
     * 是否开启异常捕获
     **/
    public static final boolean allowCrash = true;
    /**
     * 是否开启异常捕获上传
     **/
    public static final boolean allowCrashConn = false;

    /**
     * 是否允许检测更新
     **/
    public static final boolean allowCheckUpdate = false;

    /**
     * 是否允许静默安装apk，不允许则需要手动更新
     **/
    public static final boolean allowAutoInstall = true;
    private Intent mIntent;

    public static Map<String, Call> map;

    PushAgent mPushAgent;
    Handler handler;
    public static final String UPDATE_STATUS_ACTION = "com.umeng.message.example.action.UPDATE_STATUS";

    // 构造方法
    public synchronized static BaseApplication getInstance() {
        if (null == application) {
            application = new BaseApplication();
        }
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        map = new HashMap<String, Call>();
        APP_NAME = AppUtils.getAppName(this);
//        Config.DEBUG = true; //友盟的
//        UMShareAPI.get(this);//友盟的---这里不需要
        //配置数据库
        GreenDaoManager.getInstance(getApplicationContext());
        //设置该CrashHandler为程序的默认处理器
        initCrash();
        MustInit();
        //友盟推送
        push();

    }

    private void initCrash() {
//        CockroachUtil.install_debug(getApplicationContext());//崩溃无反应，带Toast debug模式//
        CockroachUtil.install_release(getApplicationContext());//崩溃无反应  推荐使用//
//        CockroachUtil.install_debug_second(getApplicationContext());//崩溃跳转到调试页面  debug模式//
//        CockroachUtil.install_release_second(getApplicationContext());//崩溃自动恢复APP
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (isDebug)
            Log.e("BaseApplication", "onLowMemory");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (isDebug)
            Log.e("BaseApplication", "onTerminate");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 统一注册
     */
    private void MustInit() {
        /**
         * 非常Nice的图片异步加载框架
         */
        // 注册图片异步加载框架
//        ImageLoaderInit.initImageLoader(getApplicationContext());
    }


    /**
     * 可选注册
     */
    public void ChooseInit() {
        // 注册异常处理
        if (allowCrash) {
            CrashHandler crashHandler = CrashHandler.getInstance();
            crashHandler.init(this);
            if (allowCrashConn) {
                String upload_url = "http://googosoft.ngrok.cc/bkrz/upload";// Crash上传路径
                crashHandler.sendPreviousReportsToServer(upload_url);
            }
        }

    }

    // Activity之间的传值可用此方法
    public Object getObject(String value) {
        if (mCache.containsKey(value)) {
            Object reference = mCache.get(value);
            if (reference == null) {
                mCache.remove(value);
            } else {
                return reference;
            }
        }
        return null;
    }

    //友盟分享的配置
//    {
//        PlatformConfig.setWeixin("wx4d091f3dde1dadf6", "2fcccd61ae459461af4900a6f5e44c23");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
//    }

    public BaseApplication() {
        application = this;
    }

    /**
     * 注册友盟推送
     * by zjy 2017-12-09
     */
    private void push() {
        mPushAgent = PushAgent.getInstance(this);
        handler = new Handler(getMainLooper());

        mPushAgent.setNotificaitonOnForeground(true);
        /**
         * 设置推送免打扰时间
         * 为免过度打扰用户，SDK默认在“23:00”到“7:00”之间收到通知消息时不响铃，不振动，不闪灯。如果需要改变默认的静音时间，可以使用以下接口：
         */
        mPushAgent.setNoDisturbMode(0, 0, 0, 0);
        /**
         * 默认情况下，同一台设备在1分钟内收到同一个应用的多条通知时，不会重复提醒，同时在通知栏里新的通知会替换掉旧的通知。可以通过如下方法来设置冷却时间：
         */
        mPushAgent.setMuteDurationSeconds(0);
        /**
         * 通知栏可以设置最多显示通知的条数，当有新通知到达时，会把旧的通知隐藏。
         */
        mPushAgent.setDisplayNotificationNumber(5);
//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//            /**
//             * 自定义消息的回调方法
//             */
//            @Override
//            public void dealWithCustomMessage(final Context context, final UMessage msg) {
//
//                handler.post(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // TODO Auto-generated method stub
//                        // 对自定义消息的处理方式，点击或者忽略
//                        boolean isClickOrDismissed = true;
//                        if (isClickOrDismissed) {
//                            //自定义消息的点击统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
//                        }
//                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
//                    }
//                });
//            }
//
//            /**
//             * 自定义通知栏样式的回调方法
//             */
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                switch (msg.builder_id) {
//                    case 1:
//                        Notification.Builder builder = new Notification.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
//                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
//                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
//                        builder.setContent(myNotificationView)
//                                .setSmallIcon(getSmallIconId(context, msg))
//                                .setTicker(msg.ticker)
//                                .setAutoCancel(true);
//
//                        return builder.getNotification();
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
//            }
//        };

//        mPushAgent.setMessageHandler(messageHandler);

        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {

//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token
//                System.out.print("注册成功！" + deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                Log.e("注册失败1", "onFailure: =====" + s);
//                Log.e("注册失败2", "onFailure: =====" + s1);
//
//            }
//        });
    }
}


