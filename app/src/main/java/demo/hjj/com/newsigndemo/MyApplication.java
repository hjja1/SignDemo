package demo.hjj.com.newsigndemo;

import android.app.Application;

/**
 * 作者：hjj on 2018/3/23 16:02
 * 邮箱：370997731@qq.com
 */
public class MyApplication extends Application {


    private static MyApplication instance;

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
