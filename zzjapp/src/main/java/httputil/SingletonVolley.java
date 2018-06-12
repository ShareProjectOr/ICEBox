package httputil;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class SingletonVolley {
	private static SingletonVolley sInstance;// ����һֱ����һ��ʵ����ֻҪ����Ϊnull,��ʹactivity��ɱ����������ת��Ļ����Ȼ����
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private LrUBitmapCache mcache;
	private Context mCtx;

	private SingletonVolley(Context context) {
		mCtx = context;
		mRequestQueue = getRequestQueue();
		mcache = new LrUBitmapCache(context);
		mImageLoader = new ImageLoader(mRequestQueue, mcache);// ���õ���Context�������գ���Ϊû�г�������
	}

	public static synchronized SingletonVolley getInstance(Context context) {
		if (sInstance == null) {
			sInstance = new SingletonVolley(context);
		}
		return sInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			// Ӧ�ó��������� ����һֱ���ڣ���֤��RequestQueueһֱ����
			mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
			mCtx = null;
		}
		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req) {
		getRequestQueue().add(req);
	}

	public ImageLoader getImageLoader() {
		return mImageLoader;
	}
}
