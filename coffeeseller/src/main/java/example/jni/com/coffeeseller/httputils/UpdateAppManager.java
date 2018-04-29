package example.jni.com.coffeeseller.httputils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog.Builder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import example.jni.com.coffeeseller.R;
import example.jni.com.coffeeseller.contentprovider.SharedPreferencesManager;
import example.jni.com.coffeeseller.utils.SecondToDate;

public class UpdateAppManager {
    // 分位符
    private static final String FILE_SEPARATOR = "/";
    // 外存sdcard存放路径
    private static final String FILE_PATH = Environment
            .getExternalStorageDirectory()
            + FILE_SEPARATOR
            + "CoffeeSeller"
            + FILE_SEPARATOR;
    // 软件路径
    private static final String FILE_NAME = FILE_PATH + "CoffeeSeller.apk";
    // 更新应用版本标记
    private static final int UPDARE_TOKEN = 0x29;
    // 准备安装新版本应用标记
    private static final int INSTALL_TOKEN = 0x31;
    private Context context;
    private String message = "检测到本程序有新版本发布，建议您更新!";
    private String spec;
    // 下载应用的对话框
    private Dialog dialog;
    // 下载应用的进度条
    private ProgressBar progressBar;
    // 进度条的当前刻度值
    private int curProgress;
    // 用户是否取消下载

    private boolean isCancel;

    public UpdateAppManager(Context context) {
        this.context = context;
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDARE_TOKEN:
                    progressBar.setProgress(curProgress);
                    break;

                case INSTALL_TOKEN:
                    installApp();
                    break;
            }
        }

    };

    public void cheakUpdateInfo(String downloadFileUrl) {
        spec = downloadFileUrl;
        showNoticeDialog();
    }

    OnKeyListener keylistener = new OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                return true;
            } else {
                return false;
            }
        }
    };

    private void showNoticeDialog() {
        new Builder(context).setTitle("软件版本更新").setMessage(message)
                .setOnKeyListener(keylistener).setCancelable(false)
                .setPositiveButton("下载", new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        showDownloadDialog();

                    }

                })
                // .setNegativeButton("以后再说", new OnClickListener() {
                //
                // @Override
                // public void onClick(DialogInterface dialog, int which) {
                // dialog.dismiss();
                //
                // }
                // })
                .create().show();

    }

    private void showDownloadDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.progressbar,
                null);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        Builder builder = new Builder(context);
        builder.setTitle("软件版本更新");
        builder.setView(view);
        builder.setNegativeButton("取消", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                isCancel = true;

            }
        });
        dialog = builder.create();
        dialog.show();
        downloadApp();
    }

    private void downloadApp() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                URL url = null;
                InputStream in = null;
                FileOutputStream out = null;
                HttpURLConnection con = null;
                try {
                    url = new URL(spec);
                    con = (HttpURLConnection) url.openConnection();
                    con.connect();
                    long fileLength = con.getContentLength();
                    File filePath = new File(FILE_PATH);
                    in = con.getInputStream();
                    if (!filePath.exists()) {
                        filePath.mkdir();

                    }
                    out = new FileOutputStream(new File(FILE_NAME));
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    long readedLength = 0l;
                    while ((len = in.read(buffer)) != -1) {
                        // 用户点击“取消”按钮，下载中断
                        if (isCancel) {
                            break;
                        }
                        out.write(buffer, 0, len);
                        readedLength += len;
                        curProgress = (int) (((float) readedLength / fileLength) * 100);
                        handler.sendEmptyMessage(UPDARE_TOKEN);
                        if (readedLength >= fileLength) {
                            dialog.dismiss();
                            handler.sendEmptyMessage(INSTALL_TOKEN);
                            break;
                        }
                    }
                    out.flush();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }

        }).start();

    }

    private void installApp() {
        SharedPreferencesManager.getInstance(context).setLastAppUpdateTime(SecondToDate.getDateToString(System.currentTimeMillis()));
        File appFile = new File(FILE_NAME);
        if (!appFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + appFile.toString()),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
