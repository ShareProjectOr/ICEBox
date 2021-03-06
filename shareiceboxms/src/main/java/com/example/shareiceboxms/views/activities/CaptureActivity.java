package com.example.shareiceboxms.views.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shareiceboxms.R;
import com.example.shareiceboxms.models.helpers.CameraManager;
import com.example.shareiceboxms.models.helpers.CameraPreview;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.io.IOException;
import java.lang.reflect.Field;


/**
 * @author pc
 * @version 1.0 Android6.0+在使用重要权限如打开相机、访问通讯录等操作时，需要动态注册权限。
 *          要实现OnRequestPermissionsResultCallback接口
 */
public class CaptureActivity extends Activity implements
        OnRequestPermissionsResultCallback {

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;
    private CameraManager mCameraManager;
    private TextView scanResult;
    private FrameLayout scanPreview;
    private Button scanRestart;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;
    private ImageView scanLine;
    private Rect mCropRect = null;
    private boolean barcodeScanned = false;
    private boolean previewing = true;
    private static int SHOWLOAD = 0;//显示加载框
    private static int HIDELOAD = 1;//隐藏加载框
    private ImageScanner mImageScanner = null;
    public static int CAMERA_REQUEST_CODE = 1;


    static {
        System.loadLibrary("iconv");
    }

    private Handler mHandler;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        findViewById();
        addEvents();

        // 判断是否有相机权限（主要用于兼容Android 6.0+）
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {// 有相机权限
            initViews();
        } else {
            //这里是拒绝给APP摄像头权限，给个提示什么的说明一下都可以。
            Toast.makeText(this, "您拒绝了系统调用摄像头权限,请手动打开相机权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void findViewById() {
        scanPreview = (FrameLayout) findViewById(R.id.capture_preview);
        scanResult = (TextView) findViewById(R.id.capture_scan_result);
        scanRestart = (Button) findViewById(R.id.capture_restart_scan);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);

    }

    private void addEvents() {
        scanRestart.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (barcodeScanned) {
                    barcodeScanned = false;
                    scanResult.setText("Scanning...");
                    mCamera.setPreviewCallback(previewCb);
                    mCamera.startPreview();
                    previewing = true;
                    mCamera.autoFocus(autoFocusCB);
                }
            }
        });
    }

    private void initViews() {

        mImageScanner = new ImageScanner();
        mImageScanner.setConfig(0, Config.X_DENSITY, 3);
        mImageScanner.setConfig(0, Config.Y_DENSITY, 3);

        autoFocusHandler = new Handler();
        mCameraManager = new CameraManager(this);
        try {
            mCameraManager.openDriver();
        } catch (IOException e) {
            e.printStackTrace();
        }
        HandlerThread thread = new HandlerThread("Ceamera3");
        thread.start();
        mHandler = new Handler(thread.getLooper());
        mCamera = mCameraManager.getCamera();
        mPreview = new CameraPreview(this, mCamera, previewCb, autoFocusCB);
        scanPreview.addView(mPreview);

    }

    @SuppressLint("Override")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {// 申请权限后的回调操作
        // 判断请求码
        if (requestCode == CAMERA_REQUEST_CODE) {
            // grantResults授权结果
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //    initViews();
            } else {
                // 授权失败
                showTip();
            }
        }
    }

    /**
     * 提示没有权限
     */
    public void showTip() {
        new AlertDialog.Builder(this).setTitle(R.string.opencameraerror).setIcon(null)
                .setMessage(R.string.aplaycamerapermission_msg)
                .show();
    }

    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            previewing = false;
            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;
        }
    }

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

    private int SCANNIN_GREQUEST_CODE;
    PreviewCallback previewCb = new PreviewCallback() {

        public void onPreviewFrame(byte[] data, Camera camera) {

            Size size = camera.getParameters().getPreviewSize();

            // 这里需要将获取的data翻转一下，因为相机默认拿的的横屏的数据
            byte[] rotatedData = new byte[data.length];
            for (int y = 0; y < size.height; y++) {
                for (int x = 0; x < size.width; x++)
                    rotatedData[x * size.height + size.height - y - 1] = data[x
                            + y * size.width];
            }

            // 宽高也要调整
            int tmp = size.width;
            size.width = size.height;
            size.height = tmp;
            initCrop();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(rotatedData);
            barcode.setCrop(mCropRect.left, mCropRect.top, mCropRect.width(),
                    mCropRect.height());

            int result = mImageScanner.scanImage(barcode);
            String resultStr = null;

            if (result != 0) {
                SymbolSet syms = mImageScanner.getResults();
                for (Symbol sym : syms) {
                    resultStr = sym.getData();
                }
            }
            //  mHandler.sendEmptyMessage(HIDELOAD);
            if (!TextUtils.isEmpty(resultStr)) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                scanResult.setText("barcode result " + resultStr);
                barcodeScanned = true;
                Intent intent = new Intent();
                intent.putExtra("QR_CODE", resultStr);
                setResult(-1, intent);
                finish();
                // onBackPressed();
                //     startActivityForResult(getIntent().putExtra("QR_CODE",resultStr),1);
            } else {

            }
        }
    };

    // Mimic continuous auto-focusing
    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = mCameraManager.getCameraResolution().y;
        int cameraHeight = mCameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
