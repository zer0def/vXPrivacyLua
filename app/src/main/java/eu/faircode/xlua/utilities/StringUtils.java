package eu.faircode.xlua.utilities;

public class StringUtils {

    public static boolean isValidString(String str) {
        return str != null && !str.equals(" ") && !str.isEmpty();
    }
}
