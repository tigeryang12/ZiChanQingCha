package com.example.administrator.zichanqingcha.googoost.tools;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.zichanqingcha.googoost.main.activity.MainActivity;
import com.wanjian.cockroach.Cockroach;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;


/**
 * 类名：.class
 * 描述：
 * Created by：刘帅 on 2018/3/2 0002.
 * --------------------------------------
 * 修改内容：
 * 备注：
 * Modify by：
 */

public class CockroachUtil {
    public static void install_debug(final Context context) {
        install(context, true);
    }

    public static void install_release(final Context context) {
        install(context, false);
    }

    private static void install(final Context context, final boolean isToast) {
        Cockroach.install(new Cockroach.ExceptionHandler() {

            // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException

            @Override
            public void handlerException(final Thread thread, final Throwable throwable) {
                //开发时使用Cockroach可能不容易发现bug，所以建议开发阶段在handlerException中用Toast谈个提示框，
                //由于handlerException可能运行在非ui线程中，Toast又需要在主线程，所以new了一个new Handler(Looper.getMainLooper())，
                //所以千万不要在下面的run方法中执行耗时操作，因为run已经运行在了ui线程中。
                //new Handler(Looper.getMainLooper())只是为了能弹出个toast，并无其他用途
                //建议使用下面方式在控制台打印异常，这样就可以在Error级别看到红色log

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.e("AndroidRuntime", "--->CockroachException:" + thread + "<---", throwable);
                            if (isToast) {
                                Toast.makeText(context, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Throwable e) {

                        }
                    }
                });
            }
        });
    }

    private static void unstall() {
        Cockroach.uninstall();
    }


    public static void install_debug_second(final Context context) {
        install_second(false, context);
    }

    public static void install_release_second(final Context context) {
        install_second(true, context);
    }

    public static void install_second(boolean jingmohuifu, Context context) {
        Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity.class)
                .recoverEnabled(true)
                .callback(new RecoveryCallback() {
                    @Override
                    public void stackTrace(String stackTrace) {
                    }

                    @Override
                    public void cause(String cause) {
                    }

                    @Override
                    public void exception(String throwExceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
                    }

                    @Override
                    public void throwable(Throwable throwable) {
                    }
                })
                .silent(jingmohuifu, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(context);
    }
}
