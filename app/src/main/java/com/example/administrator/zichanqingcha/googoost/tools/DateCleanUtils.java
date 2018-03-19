package com.example.administrator.zichanqingcha.googoost.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * DATE 继承于 java.util.Date，一个强大的日期工具类。
 */
public class DateCleanUtils {

	private DateCleanUtils(){
		throw  new Error("Do not need instantiate!");
	}

	/** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
	public static void cleanInternalCache(Context context) {
		// deleteFilesByDirectory(new File(getFilePath(context) + "/cache"));
		deleteFilesByDirectory(context.getCacheDir());
	}

	/** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
	public static void cleanDatabases(Context context) {
		deleteFilesByDirectory(new File("/data/data/"				+ context.getPackageName() + "/databases"));
	}

	/**
	 * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
	 * context
	 */
	public static void cleanSharedPreference(Context context) {
		deleteFilesByDirectory(new File("/data/data/"				+ context.getPackageName() + "/shared_prefs"));
	}

	/** * 按名字清除本应用数据库 * * @param context * @param dbName */
	public static void cleanDatabaseByName(Context context, String dbName) {
		context.deleteDatabase(dbName);
	}

	/** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
	public static void cleanFiles(Context context) {
		deleteFilesByDirectory(context.getFilesDir());
	}

	/**
	 * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
	 * context
	 */
	@SuppressLint("NewApi")
	public static void cleanExternalCache(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			deleteFilesByDirectory(context.getExternalCacheDir());
		}
	}

	/** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
	public static void cleanCustomCache(String filePath) {
		deleteFilesByDirectory(new File(filePath));
	}

	/** * 清除本应用所有的数据 * * @param context * @param filepath */
	public static void cleanApplicationData(Context context, String... filepath) {
		cleanInternalCache(context);
		cleanExternalCache(context);
		cleanDatabases(context);
		cleanSharedPreference(context);
		cleanFiles(context);
		for (String filePath : filepath) {
			cleanCustomCache(filePath);
		}
	}

	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
	public static void deleteFilesByDirectory(File directory) {
		if (directory != null && directory.exists() && directory.isDirectory()) {
			for (File item : directory.listFiles()) {
				item.delete();
			}
		}
	}
	/** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件夹，将不做处理 * * @param directory */
	public static void deleteFiles(File[] directory) {
		if(directory!=null){
			for(File item : directory){
				if (item.exists() && !item.isDirectory()) {
						item.delete();
				}
			}
		}
	}


	/**
	 * 获取文件 Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	 * 目录，一般放一些长时间保存的数据 Context.getExternalCacheDir() -->
	 * SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	 */
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {

			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				}

				else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}


	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}

	}
	/**
	 * 创建文件夹
	 */
	public static void CreateFile(String path) {
		File destDir = new File(path);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}
	}

}
