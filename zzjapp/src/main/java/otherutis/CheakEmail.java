package otherutis;

/**
 * Created by WH on 2017/8/7.
 */

public class CheakEmail {
    public static String patternString = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

    public static boolean isLegalEmail(String email) {
        return email.matches(patternString);
    }
}
