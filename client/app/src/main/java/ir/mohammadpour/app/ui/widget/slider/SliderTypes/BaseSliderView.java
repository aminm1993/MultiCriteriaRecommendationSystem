package ir.mohammadpour.app.ui.widget.slider.SliderTypes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import ir.mohammadpour.app.R;

/*
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
*/

/**
 * When you want to make your own slider view, you must extends from this class.
 * BaseSliderView provides some useful methods. I provide two example:
 * {@link DefaultSliderView} and
 * {@link TextSliderView} if you want to
 * show progressbar, you just need to set a progressbar id as @+id/loading_bar.
 */
public abstract class BaseSliderView {

    protected Context mContext;

    private Bundle mBundle;

    /**
     * Error place holder image.
     */
    private int mErrorPlaceHolderRes;

    /**
     * Empty imageView placeholder.
     */
    private int mEmptyPlaceHolderRes;

    private String mUrl;
    private File mFile;
    private int mRes;

    protected OnSliderClickListener mOnSliderClickListener;

    private boolean mErrorDisappear;

    private ImageLoadListener mLoadListener;

    private String mDescription;

    /**
     * Scale type of the image.
     */
    private ScaleType mScaleType = ScaleType.FitCenterCrop;

    /*
     * Universal Image Loader Option
     */
    DisplayImageOptions options;

    public enum ScaleType {
        CenterCrop, CenterInside, Fit, FitCenterCrop
    }

    protected BaseSliderView(Context context) {
        mContext = context;
        this.mBundle = new Bundle();
    }

    /**
     * the placeholder image when loading image from url or file.
     *
     * @param resId
     *            Image resource id
     * @return
     */
    public BaseSliderView empty(int resId) {
        mEmptyPlaceHolderRes = resId;
        return this;
    }

    /**
     * determine whether remove the image which failed to download or load from
     * file
     *
     * @param disappear
     * @return
     */
    public BaseSliderView errorDisappear(boolean disappear) {
        mErrorDisappear = disappear;
        return this;
    }

    /**
     * if you set errorDisappear false, this will set a error placeholder image.
     *
     * @param resId
     *            image resource id
     * @return
     */
    public BaseSliderView error(int resId) {
        mErrorPlaceHolderRes = resId;
        return this;
    }

    /**
     * the description of a slider image.
     *
     * @param description
     * @return
     */
    public BaseSliderView description(String description) {
        mDescription = description;
        return this;
    }

    /**
     * set a url as a image that preparing to load
     *
     * @param url
     * @return
     */
    public BaseSliderView image(String url) {
        if (mFile != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function,"
                    + "you only have permission to call it once");
        }
        mUrl = url;
        return this;
    }

    /**
     * set a file as a image that will to load
     *
     * @param file
     * @return
     */
    public BaseSliderView image(File file) {
        if (mUrl != null || mRes != 0) {
            throw new IllegalStateException("Call multi image function,"
                    + "you only have permission to call it once");
        }
        mFile = file;
        return this;
    }

    public BaseSliderView image(int res) {
        if (mUrl != null || mFile != null) {
            throw new IllegalStateException("Call multi image function,"
                    + "you only have permission to call it once");
        }
        mRes = res;
        return this;
    }

    public String getUrl() {
        return mUrl;
    }

    public boolean isErrorDisappear() {
        return mErrorDisappear;
    }

    public int getEmpty() {
        return mEmptyPlaceHolderRes;
    }

    public int getError() {
        return mErrorPlaceHolderRes;
    }

    public String getDescription() {
        return mDescription;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * set a slider image click listener
     *
     * @param l
     * @return
     */
    public BaseSliderView setOnSliderClickListener(OnSliderClickListener l) {
        mOnSliderClickListener = l;
        return this;
    }

    /**
     * When you want to implement your own slider view, please call this method
     * in the end in `getView()` method
     *
     * @param v
     *            the whole view
     * @param targetImageView
     *            where to place image
     * @paramcontext
     */
    protected void bindEventAndShow(final View v, final ImageView targetImageView) {
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSliderClickListener != null) {
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });

        mLoadListener.onStart(me);

        Log.v("tag", "bindEventAndShow");
		/*
		 * set option values
		 */
		/*
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).considerExifParams(true)
                .displayer(new FadeInBitmapDisplayer(300, true, true, true))
                .delayBeforeLoading(100).resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        if (!ImageLoader.getInstance().isInited()) {
            initImageLoader(mContext);
        }
        */

         ImageLoader imageLoader = ImageLoader.getInstance();
         imageLoader.displayImage(getUrl(), targetImageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted() {
                        Log.v("tag", "onLoadingStarted");
                        Log.v("tagurl",getUrl());
                    }

                    @Override
                    public void onLoadingFailed(FailReason failReason) {
                        // arrayKala.get(position).imgUrl="http://taxnet.ir/images/shopdefault.png";
                        //imageLoader.displayImage("http://taxnet.ir/images/shopdefault.png",relativeLayout);
                        Log.v("tag", "onLoadingFailed"+failReason.toString());
                    }

                    @Override
                    public void onLoadingComplete(Bitmap bitmap) {
                        Log.v("tag", "onLoadingCompleted");
                        //Drawable dr = new BitmapDrawable(bitmap);

                        // relativeLayout.setBackground(dr);
                        //Blurry.with(c).radius(25).sampling(2).onto(relativeLayout);


                        if (bitmap != null)
                        {
                            Log.v("tag", "onLoadingCompleted2");
                            targetImageView.setImageBitmap(bitmap);

                        }
                        if (v.findViewById(R.id.loading_bar) != null) {
                            v.findViewById(R.id.loading_bar).setVisibility(
                                    View.INVISIBLE);

                            Log.v("tag", "onLoadingCompleted3");
                        }
                    }

                    @Override
                    public void onLoadingCancelled() {
                        Log.v("tag", "onLoadingCanceled");
                    }
                }
        );
        //showImage(v, getUrl(), targetImageView);


    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public BaseSliderView setScaleType(ScaleType type) {
        mScaleType = type;
        return this;
    }

    public ScaleType getScaleType() {
        return mScaleType;
    }

    /**
     * the extended class have to implement getView(), which is called by the
     * adapter, every extended class response to render their own view.
     *
     * @return
     */
    public abstract View getView();

    /**
     * set a listener to get a message , if load error.
     *
     * @param l
     */
    public void setOnImageLoadListener(ImageLoadListener l) {
        mLoadListener = l;
    }

    public interface OnSliderClickListener {
        public void onSliderClick(BaseSliderView slider);
    }

    /**
     * when you have some extra information, please put it in this bundle.
     *
     * @return
     */
    public Bundle getBundle() {
        return mBundle;
    }

    public interface ImageLoadListener {
        public void onStart(BaseSliderView target);

        public void onEnd(boolean result, BaseSliderView target);
    }

    public static void initImageLoader(Context context) {
/*
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024)
                // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();

        ImageLoader.getInstance().init(config);
        */
    }

    private void showImage(final View v, String imageURL,
                           final ImageView imageView) {
        /*
        ImageLoader.getInstance().displayImage(imageURL, imageView, options,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view,
                                                  Bitmap loadedImage) {

                        imageView.setImageBitmap(loadedImage);
                        if (v.findViewById(R.id.loading_bar) != null) {
                            v.findViewById(R.id.loading_bar).setVisibility(
                                    View.INVISIBLE);
                        }

                    }
                }, new ImageLoadingProgressListener() {
                    @Override
                    public void onProgressUpdate(String imageUri, View view,
                                                 int current, int total) {

                    }
                });
                */
    }
}