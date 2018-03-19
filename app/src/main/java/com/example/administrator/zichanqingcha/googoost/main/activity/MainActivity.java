package com.example.administrator.zichanqingcha.googoost.main.activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.zichanqingcha.R;
import com.example.administrator.zichanqingcha.googoost.main.base.MyBaseActivity;
import com.example.administrator.zichanqingcha.googoost.main.fragment.aboutFragment;
import com.example.administrator.zichanqingcha.googoost.main.fragment.zhuyeFragment;
import com.example.administrator.zichanqingcha.googoost.tools.ClickListener;

public class MainActivity extends MyBaseActivity {
    // 用于对Fragment进行管理
    private FragmentManager fragementManager;
    private zhuyeFragment zyFragment;//主页
    private aboutFragment aboutfragment;
    private LinearLayout zyFragmentLl;//主页
    private LinearLayout aboutFragmentLl;//主页
    private Context context;
    private ClickListener clicklistener;
    private ImageView about_img,zhuye_img;
    private TextView about_tv,zhuye_tv;

    @Override
    public int intiLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {

        fragementManager = getSupportFragmentManager();

        zyFragmentLl =  findViewById(R.id.zhuye_ll);//主页Fragment
        aboutFragmentLl = findViewById(R.id.about_ll);//关于
        zhuye_img = findViewById(R.id.zhuye_img);
        about_img = findViewById(R.id.about_img);

        zhuye_tv = findViewById(R.id.zhuye_tv);
        about_tv = findViewById(R.id.about_tv);



    }

    @Override
    public void initData() {

        initListener();
        setTabSelection(0);//初始化界面


    }


    //    初始化监听事件
    public void initListener() {
        clicklistener = new ClickListener() {
            @Override
            protected void onNewClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.zhuye_ll://首页
                        setTabSelection(0);
                        break;
                    case R.id.about_ll://关于
                        setTabSelection(1);
                        break;
                    default:
                        break;
                }
            }
        };
        zyFragmentLl.setOnClickListener(clicklistener);
        aboutFragmentLl.setOnClickListener(clicklistener);
    }

    private void setTabSelection(int i) {
        clearSelection();// 每次选中之前先清楚掉上次的选中状态

        fragementManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragementManager.beginTransaction();
        switch (i){
            case 0:
                zhuye_img.setSelected(true);
                zhuye_tv.setSelected(true);

                if (zyFragment==null){
                    zyFragment = new zhuyeFragment();
                }
                transaction.replace(R.id.main_content,zyFragment);
                break;
            case 1:
                about_img.setSelected(true);
                about_tv.setSelected(true);

                if (aboutfragment==null){
                    aboutfragment = new aboutFragment();
                }
                transaction.replace(R.id.main_content,aboutfragment);
                break;

        }
        transaction.commit();

    }
    /**
     * 清除掉所有的选中状态。
     */
    private void clearSelection() {
        zhuye_img.setSelected(false);
        about_img.setSelected(false);

    }

}
