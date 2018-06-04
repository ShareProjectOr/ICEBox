package example.jni.com.coffeeseller.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * 榨汁机APP日志管理类
 *
 * @author LHT
 */
public class LogFileOperator {

    static LogFileOperator instance = null;

    final private static int MAX_DAYS_OF_FILE = 30;
    final private static String DEFAULT_LOGFILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/APPlogs/";
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    final SimpleDateFormat msg_sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    int deletedFilesNumber = 0;
    String lastCreateFileTime = "";//保存从本地读取的文件最后创建时间,主要用于APP重新打开的时候

    Context mContext;
    LocalDataManager mLocalDataManager;

    /**
     * @param context 操作所在环境
     */
    private LogFileOperator(Context context) {
        //读取本地存储的时间
        mContext = context;
        mLocalDataManager = LocalDataManager.getInstance(mContext);
        lastCreateFileTime = mLocalDataManager.getLastCreateLogfileTime();//方便第一次启动APP的时候使用
        Log.d("test", "constructor -- the time which was readed from xml is " + lastCreateFileTime);
    }

    /**
     * 获取LogFileOperator实例
     *
     * @param context 操作所在环境
     */
    public static LogFileOperator getInstance(Context context) {
        if (instance == null) {
            instance = new LogFileOperator(context);
        }
        return instance;
    }

    public static LogFileOperator getInstance() {

        return instance;
    }


    /**
     * 进行日志的写入
     *
     * @param msg需要写入的日志信息
     */
    public void writeLogToFile(String msg) {
        try {
            String date = sdf.format(new Date());
            //如果当前时间和保存的最后一次创建时间相同，则不进行文件创建，反之。
            File file = null;
            if (date.equals(lastCreateFileTime)) {
                Log.d("test", "date equals lastCreateFileTime");
                file = new File(DEFAULT_LOGFILE_PATH + lastCreateFileTime);
                if (!file.exists()) {
                    Log.d("test", "file:" + date + "dosen't exist , it will be recreated");
                    file = createFile(lastCreateFileTime);
                }
            } else {
                Log.d("test", "date dosen't equals lastCreateFileTime");
                file = createFile(getFileName());
            }
            //数据写入
            msg = msg_sdf.format(new Date()) + "---" + msg;
            BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
            bw.write(msg + "\r\n");
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 获取文件名称
     *
     * @return 文件名称
     */
    private String getFileName() {
        String fileName = sdf.format(new Date());

        //存取最后一次创建文件的时间
        lastCreateFileTime = fileName;
        return fileName;
    }

    /**
     * 创建文件
     *
     * @param fileName 需要创建的文件名（按照自定义号的格式进行命名）
     * @return
     */
    private File createFile(String fileName) {
        //每一次创建文件前检查是否创建了指定文件夹
        File logFolder = new File(DEFAULT_LOGFILE_PATH);
        if (logFolder.exists()) {

            if (logFolder.isDirectory()) {

            } else {

                logFolder.mkdir();
            }
        } else {

            logFolder.mkdir();
        }

        File file = null;
        try {
            //创建之前先删除过期文件
            deleteLogFiles();
            file = new File(DEFAULT_LOGFILE_PATH + fileName);
            file.createNewFile();
            mLocalDataManager.setLastCreateLogfileTime(fileName);
            lastCreateFileTime = fileName;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return file;
    }

    /**
     * 获取制定文件夹下的所有文件
     *
     * @param dirPath 指定的文件夹路径
     * @return
     */
    private File[] getFiles(String dirPath) {

        File dir = new File(dirPath);
        if (!dir.exists() || !dir.isDirectory()) {

            return null;
        }
        File[] files = dir.listFiles();
        return files;
    }

    /**
     * 检测过期文件并删除过期文件
     *
     * @param files 需要进行过期检测的一些文件
     */


    private void deleteLogFiles() {

        Runnable mRun = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                deleteOODFiles(getFiles(DEFAULT_LOGFILE_PATH));
            }
        };
        Thread t = new Thread(mRun);
        t.start();

    }

    private void deleteOODFiles(File[] files) {

        try {
            Calendar calendar = Calendar.getInstance();
            deletedFilesNumber = 0;
            for (int i = 0; i < files.length; i++) {
                int daysExist;

                String date = files[i].getName();
                Calendar cal = Calendar.getInstance();//Calendar非单例类
                cal.setTime(sdf.parse(date));
                if (cal.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {//如果文件日期的年份与当前的相同
                    daysExist = calendar.get(Calendar.DAY_OF_YEAR) - cal.get(Calendar.DAY_OF_YEAR);

                } else {//如果文件日期的年份与当前的不相同
                    daysExist = cal.get(Calendar.DAY_OF_YEAR) - cal.getActualMaximum(Calendar.DAY_OF_YEAR)
                            + calendar.get(Calendar.DAY_OF_YEAR);
                }
                if (daysExist >= MAX_DAYS_OF_FILE) {//文件存在天数大于30天,进行删除

                    deleteFile(files[i]);
                }
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * 删除文件
     *
     * @param file 需要被删除的文件
     */
    private void deleteFile(File file) {

        if (!file.exists()) {

            return;
        } else {
            if (file.delete()) {

                deletedFilesNumber += 1;
            } else {


            }
        }
    }
}
