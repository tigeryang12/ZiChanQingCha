package com.example.administrator.zichanqingcha.googoost.main.base;

import com.orhanobut.logger.Logger;

/**
 * Created by xjy on 2017/4/8.
 * Fragment 加载使用，当Fragment可见时加载数据
 */

public abstract class LazyFragment extends BaseFragement {
    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Logger.d("走了吗？??======" );
        if(getUserVisibleHint()) {
            isVisible = true;
            Logger.d("走了吗？======" + isVisible);
            onVisible();
        } else {
            isVisible = false;
            Logger.d("走了吗？？======" + isVisible);
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected abstract void lazyLoad();
    protected void onInvisible(){}
}
