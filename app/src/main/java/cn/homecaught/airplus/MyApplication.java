/**
 * 
 */
package cn.homecaught.airplus;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.homecaught.airplus.bean.UserBean;
import cn.homecaught.airplus.util.HttpData;
import cn.homecaught.airplus.util.SharedPreferenceManager;

/**
 * MyApplication
 * 
 * @author minking
 */
public class MyApplication extends Application {
	public static MyApplication instance;

	private List<Activity> activitys = new ArrayList<Activity>();
	public final static String app_canche_camera = Environment
			.getExternalStorageDirectory() + "/VEGETABLE/images/";

    public final static String REFRESH_DATA_NOTIFICATION = "cn.homecaught.airplus.refreshdata";
    public final static String REFRESH_DATA_FINISHED_NOTIFICATION = "cn.homecaught.airplus.refreshdata.finished";

	public MyApplication() {
		instance = this;
	}

	public static MyApplication getInstance() {
		return instance;
	}

    private BroadcastReceiver dynamicReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(REFRESH_DATA_NOTIFICATION)){
                new Thread(new GetPMDataThread()).start();
            }
        }

        @Override
        public IBinder peekService(Context myContext, Intent service) {
            return super.peekService(myContext, service);
        }

    };

    @Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getApplicationContext()));
		initImageLoader(getApplicationContext());

        // 注册自定义动态广播消息
        IntentFilter filter_dynamic = new IntentFilter();
        filter_dynamic.addAction(REFRESH_DATA_NOTIFICATION);
        registerReceiver(dynamicReceiver, filter_dynamic);

        new Thread(new GetPMDataThread()).start();

	}

	public boolean isLogin() {
		if (!(UserBean.getInstance().getUid() == null)
				&& (!(UserBean.getInstance().getUid().isEmpty()))) {
			return true;
		}
		return false;
	}
//
//	@Override
//	public void uncaughtException(Thread thread, Throwable ex) {
//		System.out.println("uncaughtException");
//		ex.printStackTrace();
//		StackTraceElement[] es = ex.getStackTrace();
//		for (StackTraceElement e : es) {
//			SystemUtils.print(e.toString());
//		}
//		SystemUtils.print(ex.getLocalizedMessage());
//	}

	/**
	 * 添加activity
	 *
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activitys.add(activity);
	}

	public void removeActivity(Activity activity) {
		activitys.remove(activity);
	}

	public void clearActivity() {
		for (Activity activity : activitys) {
			activity.finish();
		}
		activitys.clear();
	}

	public static void initImageLoader(Context context) {
		@SuppressWarnings("static-access")
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				instance.app_canche_camera);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPoolSize(2)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.diskCache(new UnlimitedDiskCache(cacheDir))
						// .memoryCacheExtraOptions(480, 800)
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheFileCount(100).diskCacheSize(10 * 1024 * 1024)
				.memoryCache(new WeakMemoryCache())
				.memoryCacheSize(10 * 1024 * 1024)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 退出程序
	 */
	public void exit() {
		for (Activity activity : activitys) {
			activity.finish();
		}
		activitys.clear();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static Context getContext() {
		return getInstance();
	}

    public class GetPMDataThread implements Runnable {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {

                try {
                    SharedPreferenceManager preferenceManager = new SharedPreferenceManager(getApplicationContext(),
                            SharedPreferenceManager.PREFERENCE_FILE);
                    preferenceManager.setPMData(HttpData.getIndoorData());
                    Intent intent = new Intent();
                    intent.setAction(REFRESH_DATA_FINISHED_NOTIFICATION);
                    sendBroadcast(intent);

                    Thread.sleep(3 * 60 * 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


}
