package com.example.administrator.zichanqingcha.googoost.tools;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.TelephonyManager;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileFilter;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * AppUtils
 * 描述：App相关工具类
 * 时间：2016年9月13日
 */
public class AppUtils {

    private AppUtils() {
        throw new AssertionError();
    }

//    /**
//     * 安装App
//     * <p>根据路径安装App</p>
//     *
//     * @param context  上下文
//     * @param filePath 文件路径
//     */
//    public static void installApp(Context context, String filePath) {
//        installApp(context, new File(filePath));
//    }
//
//    /**
//     * 安装App
//     * <p>根据文件安装App</p>
//     *
//     * @param context 上下文
//     * @param file    文件
//     */
//    public static void installApp(Context context, File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

//    /**
//     * 卸载指定包名的App
//     *
//     * @param context     上下文
//     * @param packageName 包名
//     */
//    public void uninstallApp(Context context, String packageName) {
//        Intent intent = new Intent(Intent.ACTION_DELETE);
//        intent.setData(Uri.parse("package:" + packageName));
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 得到版本名称
     * @param context
     * @return
     */
    public static String getVersionName(Context context){
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "1.0";
    }

    /**
     * 获取APP版本号
     * get app version code
     *
     * @param context
     * @return
     */
    public static int getAppVersionCode(Context context) {
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pi;
                try {
                    pi = pm.getPackageInfo(context.getPackageName(), 0);
                    if (pi != null) {
                        return pi.versionCode;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return -1;
    }

    /**
     * 获取手机系统信息
     * @param context
     * @return
     */
    public static String getPhoneInfo(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        StringBuilder sb = new StringBuilder();
        sb.append("\n设备Id(IMEI) = " + tm.getDeviceId());// 15位数字的设备id
        sb.append("\n手机型号：" + android.os.Build.MODEL);//例如GT-I9502
        sb.append("\n系统名称：" + android.os.Build.DISPLAY);
        sb.append("\n设备名称：" + android.os.Build.DEVICE);// m1metal--HM2014011--ja3gchnduos
        sb.append("\n硬件：" + android.os.Build.HARDWARE);// 一般显示处理器型号

        sb.append("\nID：" + android.os.Build.ID);// LMY47I--HM2014011--LRX22C
        sb.append("\n手机7：" + android.os.Build.TAGS);// 手机一般显示release-keys，可以区分是不是模拟器

        sb.append("\n用户类型：" + android.os.Build.TYPE);// user--user--user

        sb.append("\n手机11：" + android.os.Build.VERSION.CODENAME);// REL
        sb.append("\nandroid版本：" + android.os.Build.VERSION.RELEASE);//android版本例如 4.4.2
        sb.append("\nandroidApi版本：" + android.os.Build.VERSION.SDK_INT);//手机的android APi 19
        sb.append("\n手机14：" + android.os.Build.VERSION_CODES.BASE);// 1--1--1
        Logger.i("info:" + sb.toString());
        return sb.toString();
    }

    /**
     * 封装App信息的Bean类
     */
    public static class AppInfo {

        private String name;
        private Drawable icon;
        private String packageName;
        private String packagePath;
        private String versionName;
        private int versionCode;
        private boolean isSD;
        private boolean isUser;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        public boolean isSD() {
            return isSD;
        }

        public void setSD(boolean SD) {
            isSD = SD;
        }

        public boolean isUser() {
            return isUser;
        }

        public void setUser(boolean user) {
            isUser = user;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packagName) {
            this.packageName = packagName;
        }

        public String getPackagePath() {
            return packagePath;
        }

        public void setPackagePath(String packagePath) {
            this.packagePath = packagePath;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        /**
         * @param name        名称
         * @param icon        图标
         * @param packageName 包名
         * @param packagePath 包路径
         * @param versionName 版本号
         * @param versionCode 版本Code
         * @param isSD        是否安装在SD卡
         * @param isUser      是否是用户程序
         */
        public AppInfo(String name, Drawable icon, String packageName, String packagePath,
                       String versionName, int versionCode, boolean isSD, boolean isUser) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setPackagePath(packagePath);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSD(isSD);
            this.setUser(isUser);
        }

    }

    /**
     * 获取当前App信息
     * <p>AppInfo（名称，图标，包名，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
     *
     * @param context 上下文
     * @return 当前应用的AppInfo
     */
    public static AppInfo getAppInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(context.getApplicationContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return pi != null ? getBean(pm, pi) : null;
    }

    /**
     * 得到AppInfo的Bean
     *
     * @param pm 包的管理
     * @param pi 包的信息
     * @return AppInfo类
     */
    private static AppInfo getBean(PackageManager pm, PackageInfo pi) {
        ApplicationInfo ai = pi.applicationInfo;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String packageName = pi.packageName;
        String packagePath = ai.sourceDir;
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSD = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        boolean isUser = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != ApplicationInfo.FLAG_SYSTEM;
        return new AppInfo(name, icon, packageName, packagePath, versionName, versionCode, isSD, isUser);
    }

    /**
     * 获取所有已安装App信息
     * <p>{@link #getBean(PackageManager, PackageInfo)}（名称，图标，包名，包路径，版本号，版本Code，是否安装在SD卡，是否是用户程序）</p>
     * <p>依赖上面的getBean方法</p>
     *
     * @param context 上下文
     * @return 所有已安装的AppInfo列表
     */
    public static List<AppInfo> getAllAppsInfo(Context context) {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = context.getPackageManager();
        // 获取系统中安装的所有软件信息
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            if (pi != null) {
                list.add(getBean(pm, pi));
            }
        }
        return list;
    }

    /**
     * 根据包名获取意图
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 意图
     */
    private static Intent getIntentByPackageName(Context context, String packageName) {
        return context.getPackageManager().getLaunchIntentForPackage(packageName);
    }

    /**
     * 根据包名判断App是否安装
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 已安装<br>{@code false}: 未安装
     */
    public static boolean isInstallApp(Context context, String packageName) {
        return getIntentByPackageName(context, packageName) != null;
    }

    /**
     * 判断手机上是否装有某款app
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> info = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (info != null) {
            for (int i = 0; i < info.size(); i++) {
                String pn = info.get(i).packageName;
                pName.add(pn);
            }
        }

        return pName.contains(packageName);
    }

    /**
     * 打开指定包名的App
     *
     * @param context     上下文
     * @param packageName 包名
     * @return {@code true}: 打开成功<br>{@code false}: 打开失败
     */
    public static boolean openAppByPackageName(Context context, String packageName) {
        Intent intent = getIntentByPackageName(context, packageName);
        if (intent != null) {
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 打开指定包名的App应用信息界面
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void openAppInfo(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        context.startActivity(intent);
    }

    /**
     * 可用来做App信息分享
     *
     * @param context 上下文
     * @param info    分享信息
     */
    public static void shareAppInfo(Context context, String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, info);
        context.startActivity(intent);
    }

    /**
     * whether this process is named with processName
     * 进程中是否有指定命名的进程
     * @param context
     * @param processName
     * @return  whether this process is named with processName
     *         如果 context 为 null, return false
     *         如果 {@link ActivityManager#getRunningAppProcesses()} 为 null, return false
     *         如果有一个进程  {@link ActivityManager#getRunningAppProcesses()} is equal to processName, return
     *         true, 否则 return false
     */
    public static boolean isNamedProcess(Context context, String processName) {
        if (context == null) {
            return false;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
        if (ListUtils.isEmpty(processInfoList)) {
            return false;
        }

        for (RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo != null && processInfo.pid == pid
                    && Validate.isEquals(processName, processInfo.processName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断当前App处于前台还是后台
     * whether application is in background
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * @param context
     * @return 如果 app 在后台 return true, 否则 return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测服务是否运行
     *
     * @param context   上下文
     * @param className 类名
     * @return 是否运行的状态
     */
    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager
                = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList
                = activityManager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo si : servicesList) {
            if (className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }
        return isRunning;
    }

    /**
     * 得到CPU核心数
     *
     * @return CPU核心数
     */
    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (Pattern.matches("cpu[0-9]", pathname.getName())) {
                        return true;
                    }
                    return false;
                }
            });
            return files.length;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 获取应用签名
     *
     * @param context 上下文
     * @param pkgName 包名
     * @return 返回应用的签名
     */
    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将签名字符串转换成需要的32位签名
     *
     * @param paramArrayOfByte 签名byte数组
     * @return 32位签名字符串
     */
    private static String hexdigest(byte[] paramArrayOfByte) {
        final char[] hexDigits = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97,
                98, 99, 100, 101, 102 };
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            for (int i = 0, j = 0; ; i++, j++) {
                if (i >= 16) {
                    return new String(arrayOfChar);
                }
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[(0xF & k >>> 4)];
                arrayOfChar[++j] = hexDigits[(k & 0xF)];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 清理后台进程与服务
     *
     * @param context 应用上下文对象context
     * @return 被清理的数量
     */
    public static void gc(Context context) {
        long i = getDeviceUsableMemory(context);
        int count = 0; // 清理掉的进程数
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的service列表
        List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(100);
        if (serviceList != null) {
            for (ActivityManager.RunningServiceInfo service : serviceList) {
                if (service.pid == android.os.Process.myPid()) continue;
                try {
                    android.os.Process.killProcess(service.pid);
                    count++;
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }
    }

    /**
     * 获取设备的可用内存大小
     *
     * @param context 应用上下文对象context
     * @return 当前内存大小
     */
    public static int getDeviceUsableMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        // 返回当前系统的可用内存
        return (int) (mi.availMem / (1024 * 1024));
    }


    /**
     * 停止运行服务
     *
     * @param context   上下文
     * @param className 类名
     * @return 是否执行成功
     */
    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        boolean ret = false;
        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intent_service != null) {
            ret = context.stopService(intent_service);
        }
        return ret;
    }


    /**
     * 获取应用运行的最大内存
     *
     * @return 最大内存
     */
    public static long getMaxMemory() {

        return Runtime.getRuntime().maxMemory() / 1024;
    }
}
