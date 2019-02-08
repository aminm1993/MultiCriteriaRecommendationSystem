package ir.mohammadpour.app.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.FileCountLimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import ir.mohammadpour.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Amin on 2016-10-02.
 */

public class App extends android.app.Application {
    private static Context context;
    public static final String TAG = "taxi";
    private RequestQueue mRequestQueue;
    private static App sInstance;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
       // super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
        MultiDex.install(this);
    }
    public static synchronized App getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        sInstance = this;

        Fresco.initialize(this);

        File cacheDir = StorageUtils.getOwnCacheDirectory(
                getApplicationContext(),
                "/sdcard/Android/data/random_folder_name_for_cache");

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(options)
                //.discCache(new FileCountLimitedDiscCache(cacheDir, 1000))
                .build();

        ImageLoader.getInstance().init(config);


        CalligraphyConfig.initDefault(
                new CalligraphyConfig.Builder()
                        .setDefaultFontPath("IRANSansMobile(FaNum).ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }


    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }


    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);

        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
