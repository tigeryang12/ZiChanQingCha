package com.example.administrator.zichanqingcha.googoost.main.base;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.example.administrator.zichanqingcha.googoost.main.activity.MainActivity;
import com.example.administrator.zichanqingcha.googoost.tools.TimeUtils;
import com.example.administrator.zichanqingcha.googoost.tools.Validate;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,由该类来接管程序,并记录发送错误报告.
 *
 * @author ...
 *
 */
public class CrashHandler implements UncaughtExceptionHandler {
	private static final String TAG = "CrashHandler";
	private UncaughtExceptionHandler mDefaultHandler;// 系统默认的UncaughtException处理类
	private static CrashHandler INSTANCE = new CrashHandler();// CrashHandler实例
	private Context mContext;// 程序的Context对象
	private Map<String, String> info = new HashMap<String, String>();// 用来存储设备信息和异常信息
	private TextView dialog_title;
	StringBuffer bb;
	public static String path_crash;
	//	String sdpath;
	CrashUploadConn crashconn;
	Handler mhandler;
	String endPathString;
	public static SharedPreferences sp = null;
	public static final String SETTING_INFOS = "SETTING_INFO";
	public static String LOGIN_USN = "LOGIN_USN";
	String login_usn;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {

	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 *
	 * @param context
	 */
	@SuppressLint("HandlerLeak")
	public void init(Context context) {
		mContext = context;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();// 获取系统默认的UncaughtException处理器
		Thread.setDefaultUncaughtExceptionHandler(this);// 设置该CrashHandler为程序的默认处理器
		path_crash = mContext.getApplicationContext().getCacheDir().getAbsolutePath() + File.separator+ "crash";
//		sdpath = Environment.getExternalStorageDirectory() + File.separator
//				+ "crash";
		sp = mContext.getSharedPreferences(SETTING_INFOS, 0);
		login_usn = sp.getString(LOGIN_USN, "");// 取出保存的用户名
	}

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 */
	public void sendPreviousReportsToServer(String upload_url) {
		sendCrashReportsToServer(mContext,upload_url,login_usn);
	}

	/**
	 * 当UncaughtException发生时会转入该重写的方法来处理
	 */
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果自定义的没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				thread.sleep(2000);// 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
			} catch (InterruptedException e) {
				Log.e(TAG, "Error : ", e);
			}

			//关闭消息推送服务
//			Intent mqttintent = new Intent(mContext, MQTTService.class);
//			mContext.stopService(mqttintent);
			//关闭通知推送服务
//			Intent intent2 = new Intent(mContext, MQTTNoticeService.class);


//			mContext.stopService(intent2);
//			if (Build.VERSION.SDK_INT < 25) {
//				Intent kcxqintent1 = new Intent(mContext, FloatWindowService.class);
//				mContext.stopService(kcxqintent1);
//			}
//			Intent intent1 = new Intent(mContext, PlayerService.class);
//			mContext.stopService(intent1);
//			General.tximg = null;

			Intent intent = new Intent(mContext.getApplicationContext(), MainActivity.class);
			intent.putExtra("iszhuxiao", true);
			PendingIntent restartIntent = PendingIntent.getActivity(
					mContext.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
			//退出程序
			AlarmManager mgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
					restartIntent); // 1秒钟后重启应用
			BaseActivityManager.getAppManager().finishAllActivity();

			//杀死该应用进程
			android.os.Process.killProcess(android.os.Process.myPid());

		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 *
	 * @param ex
	 *            异常信息o
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	public boolean handleException(Throwable ex) {
		if (ex == null)
			return false;
		Thread a =new Thread() {
			public void run() {
				// DialogTools.dialog_alertString(mContext, "很抱歉，程序出现异常，即将退出");
				Looper.prepare();
				Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", Toast.LENGTH_LONG).show();
				Looper.loop();
			}
		};
		a.start();
		// 收集设备参数信息
		collectDeviceInfo(mContext);
		// 保存日志文件
		if(Validate.noNull(saveCrashInfo2File(ex))){
			a.interrupt();
		}
		return true;
	}

	/**
	 * 收集设备参数信息
	 *
	 * @param context
	 */
	public void collectDeviceInfo(Context context) {
		try {
			PackageManager pm = context.getPackageManager();// 获得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);// 得到该应用的信息，即主Activity
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				info.put("versionName", versionName);
				info.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		Field[] fields = Build.class.getDeclaredFields();// 反射机制
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				info.put(field.getName(), field.get("").toString());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存日志文件
	 *
	 * @param ex
	 * @return
	 */
	private String saveCrashInfo2File(Throwable ex) {
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : info.entrySet()) {
			String key = entry.getKey();
			String value = null;
			if (key != null && key.equals("TIME")) {
				value = TimeUtils.getInatance().formatDateDay(new Date(System
						.currentTimeMillis()));
			} else {
				value = entry.getValue();
			}
			sb.append(key + "=" + value + "\r\n");
		}
		Writer writer = new StringWriter();
		PrintWriter pw = new PrintWriter(writer);
		ex.printStackTrace(pw);
		Throwable cause = ex.getCause();
		// 循环着把所有的异常信息写入writer中
		while (cause != null) {
			cause.printStackTrace(pw);
			cause = cause.getCause();
		}
		pw.close();// 记得关闭
		String result = writer.toString();
		sb.append(result);
		// 保存文件
		long timetamp = System.currentTimeMillis();
		String time = TimeUtils.getInatance().formatDateForFileName(new Date(timetamp));

		String fileName =time + "-" + timetamp + ".log";

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				File dir = new File(path_crash);
//				File dirsd = new File(sdpath);

				if (!dir.exists()) {
					dir.mkdir();
				}
//				if (!dirsd.exists()) {
//					dirsd.mkdir();
//				}
				FileOutputStream fos = new FileOutputStream(new File(dir,
						fileName));
				fos.write(sb.toString().getBytes());
				fos.close();
//				FileOutputStream fossd = new FileOutputStream(new File(dirsd,
//						fileName));
//				fossd.write(sb.toString().getBytes());
//				fossd.close();
				return fileName;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 *
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx, String upload_url, String uname) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				String cr = path_crash + File.separator + fileName;
				postReport(cr,upload_url,uname);

			}
		}
	}

	@SuppressLint("HandlerLeak")
	private void postReport(String file, String upload_url, String uname) {
//		mhandler = new Handler() {
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				switch (msg.what) {
//				case 0x1://网络连接成功
//
//					break;
//
//				case 0x2://网络连接失败
//					LogUtils.i("网络连接失败");
//					break;
//				}
//				System.out.println("msg========" + msg);
//			}
//		};
		// 提交错误报告
		endPathString = file;
		crashconn = new CrashUploadConn(file,upload_url,mContext,uname);
		crashconn.start();

	}

	/**
	 * 获取错误报告文件名
	 *
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		// File filesDir = ctx.getFilesDir();
		File filesDir = new File(path_crash);
		Log.i("获取报告路径==" , filesDir.toString());
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".log");
			}
		};
		return filesDir.list(filter);
	}

}
