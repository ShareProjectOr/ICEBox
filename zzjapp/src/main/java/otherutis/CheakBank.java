package otherutis;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Comparator;

/**
 * Created by WH on 2017/8/7.
 */

public class CheakBank {
    //银行卡号前缀
    static String binArr = "(1[08]{1})|(3[057]{1}|(4[0-9]{1})|(5[01234568]{1})|(6[02589]{1})|(8[478]{1})|(9[4589]{1}))";
    static String pattern = "(^\\d+$)";

    public static boolean isLegalBank(String bank) {
        if (!firstJudge(bank)) {
            return false;
        }
//        if (!luhmJudge(bank)) {
//            return false;
//        }
        return true;
    }

    /*
    借记卡号：18-21位
    信用卡号：16位
    判断是否符合16-19位数字和前缀
    */
    public static boolean firstJudge(String bank) {
        if (TextUtils.isEmpty(bank)) {
            Log.d("debug", "isEmpty：" + TextUtils.isEmpty(bank));
            return false;
        }
        if (bank.length() < 16 || bank.length() > 21 || bank.length() == 17) {
            Log.d("debug", "bank.length()：" + bank.length());
            return false;
        }
        if (!bank.matches(pattern)) {
            Log.d("debug", "bank.matches(pattern)：" + bank.matches(pattern));
            return false;
        }
        Log.d("debug", "银行卡前缀：" + bank.substring(0, 2));
        if (!bank.substring(0, 2).matches(binArr)) {
            return false;
        }
        return true;
    }

    /*
    使用Luhm校验银行卡
    * */
    public static boolean luhmJudge(String bank) {
        int sum = 0;
        //取出校验位
        int errorCode = Integer.parseInt(String.valueOf(bank.charAt(bank.length() - 1)));
        //不包含最后一位校验位
        StringBuffer buffer = new StringBuffer();
        int length = bank.length() - 2;
        for (int i = 0; i <= length; i++) {
            buffer.append(bank.charAt(i));
        }
//        String newBank = buffer.reverse().toString();
        String newBank = bank.substring(0,18);
        Log.d("debug", newBank.length() + "====newBank===" + newBank);
        for (int i = 0; i < newBank.length(); i++) {
            String valueStr = String.valueOf(newBank.charAt(i));
            int value = Integer.parseInt(valueStr);
            if (i % 2 == 0) {
                int value2 = value * 2;
                if (value2 > 9) {
                    sum += (value2 / 10 + value2 % 10);
                } else {
                    sum += value2;
                }
            } else {
                sum += value;
            }
        }
        int culCode = Math.abs(sum % 10 - 10);
        Log.d("debug", sum + "culCode==" + culCode);
        if (!(culCode == errorCode)) {
            return false;
        }
        return true;
    }
}
