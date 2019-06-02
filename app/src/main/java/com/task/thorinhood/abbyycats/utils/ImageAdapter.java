package com.task.thorinhood.abbyycats.utils;

import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.task.thorinhood.abbyycats.R;
import com.task.thorinhood.abbyycats.models.Cat;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;

public class ImageAdapter {

    private LruCache<String, Bitmap> cache;
    private HashMap<String, HashSet<ImageView>> waitingForImage;
    private HashMap<String, ImageLoader> imageLoaders;

    public ImageAdapter(ActivityManager am) {
        int memClassBytes = am.getMemoryClass() * 1024 * 1024;
        int cacheSize = memClassBytes / 8;
        cache = new LruCache<>(cacheSize);
        imageLoaders = new HashMap<>();
        waitingForImage = new HashMap<>();
    }

    public Bitmap add(String key, Bitmap bmp, int size) {
        return add(key, scaleBitmap(bmp, size));
    }

    public Bitmap add(String key, Bitmap bmp) {
        return cache.put(key, bmp);
    }

    public void loadImage(Cat cat) {
        Bitmap bmp = get(cat.getName());
        if (bmp != null) {
            return;
        }

        final ImageLoader imageLoader;
        if (imageLoaders.containsKey(cat.getName())) {
            return;
        }

        waitingForImage.put(cat.getName(), new HashSet<ImageView>());
        imageLoaders.put(cat.getName(), new ImageLoader(cat));
        imageLoader = imageLoaders.get(cat.getName());
        imageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void setImage(ImageView imageView, Cat cat) {

        imageView.setImageResource(R.drawable.cat);
        Bitmap bmp = get(cat.getName());
        if (bmp != null) {
            if (imageView.getContentDescription().equals(cat.getName())) {
                imageView.setImageBitmap(bmp);
            }
            return;
        }

        final ImageLoader imageLoader;
        if (imageLoaders.containsKey(cat.getName())) {
            waitingForImage.get(cat.getName()).add(imageView);
            return;
        }

        waitingForImage.put(cat.getName(), new HashSet<ImageView>());
        waitingForImage.get(cat.getName()).add(imageView);
        imageLoaders.put(cat.getName(), new ImageLoader(cat));
        imageLoader = imageLoaders.get(cat.getName());
        imageLoader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public Bitmap get(String key, int size) {
        return cache.get(key + "_" + String.valueOf(size));
    }

    public Bitmap get(String key) {
        return cache.get(key);
    }

    private Bitmap scaleBitmap(Bitmap bitmap, int size) {
        int scale = getScale(bitmap.getWidth(), bitmap.getHeight(), size);
        if (scale <= 0) {
            return bitmap;
        }
        int newWidth = bitmap.getWidth() / scale;
        int newHeight = bitmap.getHeight() / scale;
        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
    }

    private int getScale(int width, int height, int size) {
        if (size <= 0) {
            return 0;
        }
        return Math.max(width/size, height/size);
    }

    public void clearCache() {
        for (String key : cache.snapshot().keySet()) {
            cache.remove(key);
        }

        imageLoaders.clear();
        waitingForImage.clear();
    }

    public class ImageLoader extends AsyncTask<Void, Void, Bitmap> {

        private Cat cat;
        private long time;
        public ImageLoader(Cat cat) {
            this.cat = cat;
        }

        private InputStream getStream(String link) throws IOException {
            URLConnection conn = new URL(link).openConnection();
            conn.connect();
            return conn.getInputStream();
        }

        @Override
        protected Bitmap doInBackground( Void... params ) {
            time = System.currentTimeMillis();
            Bitmap bmp;
            try {
                bmp = BitmapFactory.decodeStream(getStream(cat.getLink()));
                if (bmp != null) {
                    return bmp;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                bitmap = add(cat.getName(), bitmap, 600);
                if (waitingForImage.containsKey(cat.getName())) {
                    if (waitingForImage.get(cat.getName()) != null) {
                        for (ImageView imageView : waitingForImage.get(cat.getName())) {
                            if (imageView.getContentDescription().equals(cat.getName())) {
                                imageView.setImageBitmap(bitmap);
                                imageView.postInvalidate();
                            }
                        }
                    }
                }
            }
            waitingForImage.remove(cat.getName());
            imageLoaders.remove(cat.getName());
        }

    }
}
