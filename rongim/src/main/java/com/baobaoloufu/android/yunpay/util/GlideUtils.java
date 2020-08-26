package com.baobaoloufu.android.yunpay.util;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baobaoloufu.android.yunpay.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.GrayscaleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;


/**
 * 功能包括加载图片，圆形图片，圆角图片，指定圆角图片，模糊图片，灰度图片等等。
 * https://github.com/wasabeef/glide-transformations
 */
public class GlideUtils {


    public static final int placeholderIcon = R.drawable.icon_place;
    public static final int errorIcon = R.drawable.icon_place;

    /*
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderIcon) //占位图
                .error(errorIcon)       //错误图
//                 .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    /*
     *加载图片(默认)
     */
    public static void loadImageNew(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }

        Glide.with(context)
                .load(url)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);

    }

    private static int[] getImageSizeAhead(String path, boolean isUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (isUrl) {
            try {
                BitmapFactory.decodeStream(new URL(path).openStream(), null, options);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BitmapFactory.decodeFile(path, options);

        }
        return new int[]{options.outWidth, options.outHeight};
    }

    /*
     *加载图片(默认)
     */
    public static void loadImage(Context context, String url, ImageView imageView, @DrawableRes int placeholderIcon) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderIcon) //占位图
                .error(placeholderIcon)       //错误图
//                 .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 指定图片大小;使用override()方法指定了一个图片的尺寸。
     * Glide现在只会将图片加载成width*height像素的尺寸，而不会管你的ImageView的大小是多少了。
     * 如果你想加载一张图片的原始尺寸的话，可以使用Target.SIZE_ORIGINAL关键字----override(Target.SIZE_ORIGINAL)
     *
     * @param context
     * @param url
     * @param imageView
     * @param width
     * @param height
     */
    public static void loadImageSize(Context context, String url, ImageView imageView, int width, int height) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderIcon) //占位图
                .error(errorIcon)       //错误图
                .override(width, height)
                // .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    public interface LoadCallBack {
        void onLoadFailed();

        void onLoadSuccess();
    }


    /**
     * 指定宽度适应高度
     *
     * @param context
     * @param url
     * @param imageView
     * @param width     指定的宽度
     */
    public static void loadImageWrapHeight(Context context, String url, ImageView imageView, int width, LoadCallBack loadCallBack) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderIcon) //占位图
                .skipMemoryCache(true)//禁用掉Glide的内存缓存功能
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .asBitmap()
                .load(url)
//                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        loadCallBack.onLoadFailed();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        float imgWidth = resource.getWidth();
                        float imgHeight = resource.getHeight();
                        float scale = imgHeight / imgWidth;
                        float showHeight = width * scale;
                        ViewGroup.LayoutParams para = imageView.getLayoutParams();
                        if (para != null) {
                            para.height = (int) showHeight;
                            para.width = width;
                            imageView.setLayoutParams(para);
                        }
//                        imageView.setImageBitmap(resource);
                        loadCallBack.onLoadSuccess();
                        return false;
                    }
                }).into(imageView);
    }

    /**
     * 指定高度适应宽度
     *
     * @param context
     * @param url
     * @param imageView
     * @param height    根据UI的高度
     */
    public static void loadImageFixWidth(Context context, String url, ImageView imageView, int height) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        float imgWidth = resource.getWidth();
                        float imgHeight = resource.getHeight();
                        float scale = imgWidth / imgHeight;
                        float showWidth = height * 2 * scale;
                        ViewGroup.LayoutParams para = imageView.getLayoutParams();
                        if (para != null) {
                            para.height = height * 2;
                            para.width = (int) showWidth;
                            imageView.setLayoutParams(para);
                        }
                        return false;
                    }
                }).into(imageView);
    }


    public static void loadImageWrapHeight(Context context, String url, ImageView imageView, int width) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
//                .placeholder(placeholderIcon) //占位图
//                .error(errorIcon)       //错误图
//                 .priority(Priority.HIGH)//优先级
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        float imgWidth = resource.getWidth();
                        float imgHeight = resource.getHeight();
                        float scale = imgHeight / imgWidth;
                        float showHeight = width * scale;
                        Log.e("fdsfdsgvds", "showHeight==" + showHeight + "imgWidth===" + imgWidth);
                        ViewGroup.LayoutParams para = imageView.getLayoutParams();
                        if (para != null) {
                            para.height = (int) showHeight;
                            imageView.setLayoutParams(para);
                        }
                        return false;
                    }
                }).into(imageView);
    }


    /**
     * 禁用内存缓存功能
     * diskCacheStrategy()方法基本上就是Glide硬盘缓存功能的一切，它可以接收五种参数：
     * <p>
     * DiskCacheStrategy.NONE： 表示不缓存任何内容。
     * DiskCacheStrategy.DATA： 表示只缓存原始图片。
     * DiskCacheStrategy.RESOURCE： 表示只缓存转换过后的图片。
     * DiskCacheStrategy.ALL ： 表示既缓存原始图片，也缓存转换过后的图片。
     * DiskCacheStrategy.AUTOMATIC： 表示让Glide根据图片资源智能地选择使用哪一种缓存策略（默认选项）。
     */

    public static void loadImageSizekipMemoryCache(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderIcon) //占位图
                .error(errorIcon)       //错误图S
                .skipMemoryCache(true)//禁用掉Glide的内存缓存功能
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }


    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(placeholderIcon)
                .error(errorIcon)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * 加载圆形图片
     */
    public static void loadCircleImage(Context context, Bitmap bitmap, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(placeholderIcon)
                .error(errorIcon)
                //.priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(bitmap).apply(options).into(imageView);
    }

    /**
     * 预先加载图片
     * 在使用图片之前，预先把图片加载到缓存，调用了预加载之后，我们以后想再去加载这张图片就会非常快了，
     * 因为Glide会直接从缓存当中去读取图片并显示出来
     */
    public static void preloadImage(Context context, String url) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        Glide.with(context)
                .load(url)
                .preload();

    }

    /**
     * 加载圆角图片
     */
    public static void loadRoundCircleImage(Context context, String url, ImageView imageView, int radius) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .circleCrop()//设置圆形
                .placeholder(placeholderIcon) //占位图
                .error(errorIcon)
                //.priority(Priority.HIGH)
                .bitmapTransform(new RoundedCornersTransformation(radius, 0, RoundedCornersTransformation.CornerType.ALL))
                .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners(radius)))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);

    }

    private static boolean isDestory(Activity activity) {
        if (activity == null
                || activity.isFinishing()
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed())) {
            return true;
        }

        return false;
    }

    /**
     * 加载圆角图片-指定任意部分圆角（图片上、下、左、右四个角度任意定义）
     *
     * @param context
     * @param url
     * @param imageView
     * @param type
     */
    public static void loadCustRoundCircleImage(Context context, String url, ImageView imageView, RoundedCornersTransformation.CornerType type) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderIcon)
                .error(errorIcon)
                //.priority(Priority.HIGH)
                .bitmapTransform(new RoundedCornersTransformation(8, 0, type))
                .diskCacheStrategy(DiskCacheStrategy.ALL);

        Glide.with(context).load(url).apply(options).into(imageView);
    }


    /**
     * 加载模糊图片（自定义透明度）
     *
     * @param context
     * @param url
     * @param imageView
     * @param blur      模糊度，一般1-100够了，越大越模糊
     */
    public static void loadBlurImage(Context context, String url, ImageView imageView, int blur) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderIcon)
                .error(errorIcon)
                //.priority(Priority.HIGH)
                .bitmapTransform(new BlurTransformation(blur))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /*
     *加载灰度(黑白)图片（自定义透明度）
     */
    public static void loadBlackImage(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(placeholderIcon)
                .error(errorIcon)
                //.priority(Priority.HIGH)
                .bitmapTransform(new GrayscaleTransformation())
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(context).load(url).apply(options).into(imageView);
    }

    /**
     * Glide.with(this).asGif()    //强制指定加载动态图片
     * 如果加载的图片不是gif，则asGif()会报错， 当然，asGif()不写也是可以正常加载的。
     * 加入了一个asBitmap()方法，这个方法的意思就是说这里只允许加载静态图片，不需要Glide去帮我们自动进行图片格式的判断了。
     * 如果你传入的还是一张GIF图的话，Glide会展示这张GIF图的第一帧，而不会去播放它。
     *
     * @param context
     * @param url       例如：https://image.niwoxuexi.com/blog/content/5c0d4b1972-loading.gif
     * @param imageView
     */
    private void loadGif(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }
        RequestOptions options = new RequestOptions()
                .placeholder(placeholderIcon)
                .error(errorIcon);
        Glide.with(context)
                .load(url)
                .apply(options)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(imageView);

    }

    public static void downloadImage(final Context context, final String url) {
        if (context == null) {
            return;
        }

        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (isDestory(activity)) {
                return;
            }
        }

        //String url = "http://www.guolin.tech/book.png";
        Glide.with(context)
                .asFile()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
//                        Uri contentUri = Uri.fromFile(resource);
//                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,contentUri);
//                        context.sendBroadcast(mediaScanIntent);
                        try {

                            MediaStore.Images.Media.insertImage(context.getContentResolver(), resource.getAbsolutePath(), resource.getName(), "");
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                            Uri contentUri = Uri.fromFile(resource);
                            intent.setData(contentUri);
                            context.sendBroadcast(intent);
                        } catch (Exception e) {
                            Log.e("aaa", "aa");
                        }

                    }
                });
    }

    /**
     * 获取Glide造成的缓存大小
     *
     * @return CacheSize
     */
    public static String getCacheSize(Context context) {
        try {
            return getFormatSize(getFolderSize(new File(context.getCacheDir() + "/"+ InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取指定文件夹内所有文件大小的和
     *
     * @param file file
     * @return size
     * @throws Exception
     */
    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }



    /**
     * 格式化单位
     *
     * @param size size
     * @return size
     */
    private static String getFormatSize(double size) {

        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);

        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB";
    }
}