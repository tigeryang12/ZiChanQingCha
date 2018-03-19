package com.example.administrator.zichanqingcha.googoost.main.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.zichanqingcha.R;
import com.example.administrator.zichanqingcha.googoost.main.base.AppEvent;
import com.example.administrator.zichanqingcha.googoost.main.base.BaseActivityManager;
import com.example.administrator.zichanqingcha.googoost.main.base.BaseApplication;
import com.example.administrator.zichanqingcha.googoost.tools.AppUtils;
import com.orhanobut.logger.Logger;
import com.umeng.message.PushAgent;
import com.zhy.autolayout.AutoLayoutActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import static com.example.administrator.zichanqingcha.googoost.main.base.BaseApplication.map;


/**
 * 类名：com.house.base.BaseActivity.class
 * 描述：应用程序基类
 * Created by 刘帅 on 2016/8/9.
 */
public abstract class BaseActivity extends AutoLayoutActivity {
    /**
     * 注意：这里的boolean，每个activity都继承BaseActivity，当activity初始化时，
     * BaseActivity便完成一次初始化 ，这样就可以实现每个activity都可以单独的进行显示状态的设置
     * **/

    /**
     * 是否沉浸状态栏
     **/
    private boolean isSetStatusBar = false;

    /**
     * 是否允许全屏
     **/
    private boolean allowFullScreen = false;

    /**
     * 是否横屏
     **/
    private boolean allowScreenLandSpace = false;

    /**
     * 是否允许快速点击
     **/
    private boolean allowQuickClick = false;

    /**
     * 是否允许注册EventBus事件总线
     **/
    private boolean allowRegister = true;

    /**
     * 当前activity渲染的视图View
     **/
    private View mContextView = null;

    /**
     * 获取当前包名的TAG
     **/
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * 存放当前使用action的名字
     */
    protected List<String> actionmap;

    /**
     * 定义全局Handler
     **/
    public UIHandler handler = new UIHandler(Looper.getMainLooper());

    /**
     * 定义全局Contex
     **/
    public Activity cont = this;

    /**
     * 快速点击周期
     **/
    private long lastClick = 0;

    /***************************************************************************
     *
     * 打印Activity生命周期
     *
     ***************************************************************************/

    /**
     * onCreate 创建
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (isSetStatusBar) // 是否沉浸状态栏
            steepStatusBar();
//      getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        super.onCreate(savedInstanceState);
        actionmap  = new ArrayList<String>();
        $Log(TAG + "        ------------------> Activity:onCreat <------------------");
        Bundle bundle = getIntent().getExtras();
        mContextView = LayoutInflater.from(this).inflate(initLayout(), null);
        Logger.i("        -----> 当前所在类:" + TAG + " <-----");
        if (allowFullScreen) {
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        }


        if (allowScreenLandSpace) // 是否横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        else
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            initParms(bundle);
            setContentView(mContextView);
            BaseActivityManager.getAppManager().addActivity(this);
            setHandler();
            initQtData();
            doBusiness(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //友盟推送
        PushAgent.getInstance(this).onAppStart();

    }

    /**
     * onStart 启动
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (BaseApplication.isDebug)
            $Log(TAG + "        ------------------> Activity:onStart <------------------");
    }

    /**
     * onResume 恢复
     */
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent();
        intent.setAction("FloatWindowService");
        intent.putExtra("xq",1);
        intent.putExtra("msg", 2);
        sendBroadcast(intent);

        //      极光推送服务会恢复正常工作
//        JPushInterface.onResume(cont);
        if (BaseApplication.isDebug)
            $Log(TAG + "        ------------------> Activity:onResume <------------------");
    }

    /**
     * onPause 暂停
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (AppUtils.isApplicationInBackground(cont)){
            Intent intent = new Intent();
            intent.setAction("FloatWindowService");
            intent.putExtra("xq",1);
            intent.putExtra("msg", 2);
            sendBroadcast(intent);
        }
        if (BaseApplication.isDebug)
            $Log(TAG + "        ------------------> Activity:onPause <------------------");
    }

    /**
     * onRestart 重启
     */

    @Override
    protected void onRestart() {
        super.onRestart();
        if (BaseApplication.isDebug)
            $Log(TAG + "        ------------------> Activity:onRestart <------------------");
    }

    /**
     * onStop 停止
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (BaseApplication.isDebug)
            $Log(TAG + "        ------------------> Activity:onStop<------------------");
    }

    /**
     * onDestroy 销毁
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
//        ButterKnife.unbind(this);
        if (actionmap.size()>0){
            for (int i=0;i<actionmap.size();i++){
                Call call = map.get(actionmap.get(i));
                if (call!=null) {
                    call.cancel();
                }
                map.remove(actionmap.get(i));
            }
        }else {
            Call call = map.get(TAG);
            if (call != null) {
                call.cancel();
            }
            map.remove(TAG);
        }
        if (BaseApplication.isDebug)
            $Log(TAG + "        ------------------> Activity:onDestroy()<------------------");
        if(allowRegister)
            EventBus.getDefault().unregister(this);//取消注册
    }

    /***************************************************************************
     *
     * [重写：1.是否沉浸状态栏 2.页面跳转 3.携带数据的页面跳转 4.加载主视图 5.加载布局文件
     * 6.含有Bundle通过Class打开编辑界面 7.是否允许全屏 6.是否设置沉浸状态栏
     * 9.是否允许屏幕旋转 10.日志输出  11.防止快速点击 12.重写内部类]
     *
     ***************************************************************************/

    /**
     * [沉浸状态栏]
     */
    public void steepStatusBar() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            ViewGroup contentView = ((ViewGroup) findViewById(android.R.id.content));
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
            contentView.setPadding(0, getStatusBarHeight(this), 0, 0);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            // 透明导航栏
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //设置contentview为fitsSystemWindows
            ViewGroup contentView = (ViewGroup) findViewById(android.R.id.content);
            View childAt = contentView.getChildAt(0);
            if (childAt != null) {
                childAt.setFitsSystemWindows(true);
            }
            contentView.setPadding(0, getStatusBarHeight(this), 0, 0);
            //给statusbar着色
            View view = new View(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,getStatusBarHeight(this)));
            view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            contentView.addView(view);
        }

    }
    public void setStatusBarColor(){
        try
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.titleColorSelected));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (
                Exception e)

        {
            e.printStackTrace();
        }
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * [页面跳转]
     * <p/>
     * clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }

    /**
     * [携带数据的页面跳转]
     * <p/>
     * clz bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivity(intent);
    }


//    /**
//     * [加载布局文件]
//     *
//     * @param resId
//     * @param <T>
//     * @return
//     */
//    public <T extends View> T $findViewById(int resId) {
//        return (T) super.findViewById(resId);
//    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     * <p/>
     * cls bundle requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null)
            intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    /**
     * [是否允许全屏]
     * <p/>
     * allowFullScreen
     */
    public void setAllowFullScreen(boolean allowFullScreen) {
        this.allowFullScreen = allowFullScreen;
    }

    /**
     * [是否设置沉浸状态栏]
     * <p/>
     * allowFullScreen
     */
    public void setSteepStatusBar(boolean isSetStatusBar) {
        this.isSetStatusBar = isSetStatusBar;
    }

    /**
     * [是否允许屏幕旋转]
     * <p/>
     * isAllowScreenRoate
     */
    public void setScreenRoate(boolean allowScreenLandSpace) {
        this.allowScreenLandSpace = allowScreenLandSpace;
    }

    /**
     * [是否允许快速点击转]
     * <p/>
     * isAllowScreenRoate
     */
    public void setAllowQuickClick(boolean allowQuickClick) {
        this.allowQuickClick = allowQuickClick;
    }

    /**
     * [日志输出]
     * <p/>
     * msg
     */
    protected void $Log(String msg) {
        Log.i(BaseApplication.APP_NAME, msg);
    }

    /**
     * [防止快速点击]
     *
     * @return
     */
    public boolean fastClick() {
        if (!allowQuickClick) {// 不允许快速点击
            if (System.currentTimeMillis() - lastClick <= 1000)
                return false;
            lastClick = System.currentTimeMillis();
            return true;
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e(TAG, "onKeyDown: -----------onKeyDown====================" );
        return widgetOnKey(keyCode,event);
    }

    /**
     * [内部类]
     * <p/>
     * 重写Handler
     */
    public class UIHandler extends Handler {

        private IHandler handler;// 回调接口，消息传递给注册者

        public UIHandler(Looper looper) {
            super(looper);
        }

        public UIHandler(Looper looper, IHandler handler) {
            super(looper);
            this.handler = handler;
        }

        public void setHandler(IHandler handler) {
            this.handler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (handler != null)
                handler.handleMessage(msg);// 有消息，就传递
        }
    }

    public interface IHandler {
        void handleMessage(Message msg);
    }

    private void setHandler() throws Exception {
        handler.setHandler(new IHandler() {
            public void handleMessage(Message msg) {
                widgetHandle(msg);// 有消息就提交给子类实现的方法
            }
        });
    }

    /**
     * 在当前界面注册一个订阅者
     * ——：事件总线框架EventBus
     * ——：特别注意，按需注册，EventBus 非常好用, 但是不希望太多，因为它会让代码库变得更混乱;基本的传值还是用Handler
     */
    public void registerEventBus(boolean _allowRegister){
        this.allowRegister=_allowRegister;
        if(allowRegister)
            EventBus.getDefault().register(this);
    }

    /**
     * 发布事件
     */
    public  void eventBusPost(String msg,Object obj){
        if(allowRegister)
            AppEvent.post(msg,obj);
    }

    /**
     * 接收事件
     */
    @Subscribe
    public  void onEventMainThread(AppEvent bean){
        _onEventMainThread(bean);
    }
    /********************************************************************************
     *
     * [1.绑定布局 2.初始化Handler 3.初始化Bundle参数
     * 4.初始化其他数据 5.业务操作 6.物理键监听]
     *
     ********************************************************************************/

    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int initLayout();

    /**
     * [初始化Handler]
     */
    public abstract void widgetHandle(Message msg);

    /**
     * [初始化Bundle参数]
     *
     * @param bundle
     */
    public abstract void initParms(Bundle bundle) throws Exception;

    /**
     * [初始化其他数据]
     */
    public abstract void initQtData() throws Exception;

    /**
     * [业务操作]
     *
     * @param mContext
     */
    public abstract void doBusiness(Context mContext) throws Exception;

    /**
     * [物理键监听]
     *
     * @param keyEvent
     */
    public abstract boolean widgetOnKey(int keyCode,KeyEvent keyEvent);

    /**
     * [事件接收]
     *
     * @param event
     */
    public abstract void _onEventMainThread(AppEvent event);

}