package example.jni.com.coffeeseller.utils;


import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 该类用于生成二维码图像
 * @author LHT
 *
 */
public class QRMaker {
	
	public Bitmap createBitmap(String source,int QR_WIDTH, int QR_HEIGHT)
	{
		
		Bitmap bitmap = null;
        try {
            // 需要引入core包
            QRCodeWriter writer = new QRCodeWriter();
            if (source == null || "".equals(source) || source.length() < 1) {
                return null;
            }

            // 把输入的文本转为二维码
            BitMatrix martix = writer.encode(source, BarcodeFormat.QR_CODE,QR_WIDTH, QR_HEIGHT);
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");        
            BitMatrix bitMatrix = new QRCodeWriter().encode(source,BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);          
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
            	
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }

                }
            }
            bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT,Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
	
	public String ReadImage(Bitmap bitmap) {

        Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        BitmapLuminanceSource source = new BitmapLuminanceSource(bitmap);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();
        com.google.zxing.Result result;
        String text = null;
        try {
        	result = reader.decode(bitmap1, hints);
        	text = result.getText();
        }catch (com.google.zxing.NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (com.google.zxing.FormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            // 得到解析后的文字
          
         catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) 
        {
            e.printStackTrace();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return text;
    }
	
	
	

class BitmapLuminanceSource extends LuminanceSource{  
		  
		    private byte bitmapPixels[];  
		  
		    protected BitmapLuminanceSource(Bitmap bitmap) {  
		        super(bitmap.getWidth(), bitmap.getHeight());  		  
		        // 首先，要取得该图片的像素数组内容  
		        int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];  
		        this.bitmapPixels = new byte[bitmap.getWidth() * bitmap.getHeight()];  
		        bitmap.getPixels(data, 0, getWidth(), 0, 0, getWidth(), getHeight());  
		  
		        // 将int数组转换为byte数组，也就是取像素值中蓝色值部分作为辨析内容  
		        for (int i = 0; i < data.length; i++) {  
		            this.bitmapPixels[i] = (byte) data[i];  
		        }  
		    }  
		  
		    @Override  
		    public byte[] getMatrix() {  
		        // 返回我们生成好的像素数据  
		        return bitmapPixels;  
		    }  
		  
		    @Override  
		    public byte[] getRow(int y, byte[] row) {  
		        // 这里要得到指定行的像素数据  
		        System.arraycopy(bitmapPixels, y * getWidth(), row, 0, getWidth());  
		        return row;  
		    }  
		}  
}
