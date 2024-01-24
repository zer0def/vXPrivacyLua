package eu.faircode.xlua.utilities;

public class StringUtil {

    public static boolean isValidString(String str) {
        return str != null && !str.equals(" ") && !str.isEmpty();
    }
}
