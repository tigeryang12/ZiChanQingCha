package com.example.administrator.zichanqingcha.googoost.main.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import static com.example.administrator.zichanqingcha.googoost.main.base.BaseApplication.map;
/**
 * 类名：com.house.base.BaseFragement.class
 * 描述：应用程序基类
 * Created by 刘帅 on 2016/8/9.
 * modified by 刘月 on 2017/04/14
 */
public abstract class BaseFragement extends Fragment implements View.OnKeyListener{
    private View mContextView = null;

    /**
     * 注意：这里的boolean，每个fragement都继承BaseFragement，当fragement初始化时，BaseFragement便完成一次初始化
     * ，这样就可以实现每个fragement都可以单独的进行显示状态的设置
     * **/

    /**
     * 是否允许快速点击
     **/
    public boolean allowQuick = false;

    /**
     * 快速点击周期
     **/
    private long lastClick = 0;

    /**
     * 获取当前包名的TAG
     **/
    protected final String TAG = this.getClass().getSimpleName();
    /**
     * 存放当前使用action的名字
     */
    protected List<String> actionmap ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        $Log(TAG+"-----------> Fragment:onAttach <--------------");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionmap = new ArrayList<String>();
        $Log(TAG+"-----------> Fragment:onCreate <--------------");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        $Log(TAG+"-----------> Fragment:onCreateView <--------------");
        if (mContextView == null) {
            Log.e(TAG, "onCreateView111111111: ================================" );
            mContextView = inflater.inflate(initLayout(), container, false);
            doBusiness(getActivity(),mContextView);
        }
        return mContextView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        $Log(TAG+"-----------> Fragment:onActivityCreated <--------------");
    }

    @Override
    public void onStart() {
        super.onStart();
        $Log(TAG+"-----------> Fragment:onStart <--------------");
    }

    @Override
    public void onResume() {
        super.onResume();
        $Log(TAG+"-----------> Fragment:onResume <--------------");
    }

    @Override
    public void onPause() {
        super.onPause();
        $Log(TAG+"-----------> Fragment:onResume <--------------");
    }

    @Override
    public void onStop() {
        super.onStop();
        $Log(TAG+"-----------> Fragment:onStop <--------------");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ButterKnife.unbind(this);
        $Log(TAG+"-----------> Fragment:onDestroyView <--------------");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            if (call!=null) {
                call.cancel();
            }
            map.remove(TAG);
        }

        $Log(TAG+"-----------> Fragment:onDestroy <--------------");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        $Log(TAG+"-----------> Fragment:onDetach <--------------");
    }

    /********************************************************************************
     *
     * [1.是否允许快速点击转 2.日志输出 3.防止快速点击
     * 4.物理键监听 ]
     *
     ********************************************************************************/


    /**
     * [是否允许快速点击转]
     *
     * @param allowQuick
     */
    public void setAllowQuick(boolean allowQuick) {
        this.allowQuick = allowQuick;
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
    private boolean fastClick() {
        if (!allowQuick) {// 不允许快速点击
            if (System.currentTimeMillis() - lastClick <= 1000) {
                return false;
            }
            lastClick = System.currentTimeMillis();
            return true;
        }
        return true;
    }
    /**
     * [物理键监听]
     *
     * @return
     */
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return $onKey(view,i,keyEvent);
    }


/********************************************************************************
     *
     * [1.绑定布局 2.物理件监听 3.业务操作]
     *
     ********************************************************************************/


    /**
     * [绑定布局]
     *
     * @return
     */
    public abstract int initLayout();

    /**
     * [业务操作]
     *
     * @param mContext
     */
    public abstract void doBusiness(Context mContext, View view);

    /**
     * [物理件监听]
     * @param view
     * @param i
     * @param keyEvent
     * @return
     */
    public abstract boolean $onKey(View view, int i, KeyEvent keyEvent);


}
