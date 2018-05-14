package example.jni.com.coffeeseller.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by WH on 2018/3/20.
 */

public class ImageUtil {
    /*
    * 获取图片倒影
    * */
    public static Bitmap getReverseBitmapById(int resId, Context context) {
        Bitmap sourceBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        Matrix matrix = new Matrix();
        matrix.setScale(1, -1);
        Bitmap inverseBitmap = Bitmap.createBitmap(sourceBitmap, 0, sourceBitmap.getHeight() / 2, sourceBitmap.getWidth(), sourceBitmap.getHeight() / 3, matrix, false);
        Bitmap groupbBitmap = Bitmap.createBitmap(sourceBitmap.getWidth(), sourceBitmap.getHeight() + sourceBitmap.getHeight() / 3 + 60, sourceBitmap.getConfig());
        Canvas gCanvas = new Canvas(groupbBitmap);
        gCanvas.drawBitmap(sourceBitmap, 0, 0, null);
        gCanvas.drawBitmap(inverseBitmap, 0, sourceBitmap.getHeight() + 50, null);
        Paint paint = new Paint();
        Shader.TileMode tileMode = Shader.TileMode.CLAMP;
        LinearGradient shader = new LinearGradient(0, sourceBitmap.getHeight() + 50, 0,
                groupbBitmap.getHeight(), Color.BLACK, Color.TRANSPARENT, tileMode);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        gCanvas.drawRect(0, sourceBitmap.getHeight() + 50, sourceBitmap.getWidth(), groupbBitmap.getHeight(), paint);
        return groupbBitmap;
    }

    /*
    * 设置图片灰度
    * */
    public static Bitmap setGrayscale(Bitmap bitmap, float saturation) {

        if (bitmap != null) {
            int width, heigth;
            Paint paint = new Paint();
            heigth = bitmap.getHeight();
            width = bitmap.getWidth();
            Bitmap bm = Bitmap.createBitmap(width, heigth, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bm);
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(saturation);
            ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(cmcf);
            canvas.drawBitmap(bitmap, 0, 0, paint);
            return bm;
        }
        return bitmap;
    }

    /*
    * 将图片设置成圆角
    * */
    public static Bitmap getCornerBitmap(Bitmap bitmap, int corner) {
        if (bitmap == null) return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap drawTopRoundRect(Bitmap bitmap, int corner) {

        if (bitmap == null) return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        Rect rect2 = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight() - 10);
        canvas.drawRect(rect2, paint);
        canvas.drawBitmap(bitmap, rect2, rect2, paint);
        return output;
    }
}
