package httputil;

import com.android.volley.toolbox.ImageLoader.ImageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;


public class LrUBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

	public LrUBitmapCache(int maxSize) {
		super(maxSize);
	}

	public LrUBitmapCache(Context ctx) {
		this(getCacheSize(ctx));
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight()/1000;//KB
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

	public static int getCacheSize(Context ctx) {
		final DisplayMetrics displayMetrics = ctx.getResources().getDisplayMetrics();
		final int screenWidth = displayMetrics.widthPixels;
		final int screenHeight = displayMetrics.heightPixels;
		// 4 bytes per pixel
		final int screenBytes = screenWidth * screenHeight * 4;

		return screenBytes*4;
	}
}
