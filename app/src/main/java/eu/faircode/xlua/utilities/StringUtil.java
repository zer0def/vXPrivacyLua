package eu.faircode.xlua.utilities;

import android.text.TextUtils;
import android.util.Log;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StringUtil {
    private static final String TAG = "XLua.StringUtil";

    public static final String BUILD_PROP_ENDING = "\r\n";//(0D 0A) or  \u000D\u000A
    public static final List<Character> ESCAPE_CHARS = Arrays.asList('\n', '\t', '\b', '\f', '\r', '\"', '\0');

    public static boolean listHasString(List<String> lst, String s) {
        if(lst == null || lst.isEmpty() || s == null || s.isEmpty()) return false;
        for(String sl : lst) {
            if(sl == null) continue;
            if(sl.equalsIgnoreCase(s)) return true;
        }

        return false;
    }

    public static List<String> stringToList(String s, String del) {
        if(s == null || s.isEmpty()) return new ArrayList<>();
        if(del == null || del.isEmpty()) del = ",";
        if(del.equals(".")) del = "\\.";
        if(!s.contains(del)) return Collections.singletonList(s);
        String[] ps = s.split(del);
        List<String> psList = new ArrayList<>(ps.length);
        psList.addAll(Arrays.asList(ps));
        return psList;
    }

    public static int countOccurrences(String subStr, String str) {
        if (subStr == null || str == null || subStr.isEmpty() || str.isEmpty()) { return 0; }
        int count = 0;
        int fromIndex = 0;
        while (fromIndex < str.length()) {
            int index = str.indexOf(subStr, fromIndex);
            if (index != -1) {
                count++;
                fromIndex = index + subStr.length();
            } else {
                break;
            }
        } return count;
    }

    public static String startAtString(String startAt, String str) {
        if (str == null || startAt == null) { return null; }
        int index = str.indexOf(startAt);
        return index != -1 ? str.substring(index) : str;
    }

    public static List<String> breakStringExtreme(String str, boolean keepAlpha, boolean keepNumeric, List<Character> goodChars) {
        if(str == null || TextUtils.isEmpty(str))
            return new ArrayList<>();

        String lowered = str.toLowerCase().trim();
        StringBuilder low = new StringBuilder();
        List<String> parts = new ArrayList<>();

        for (int i = 0; i < lowered.length(); i++) {
            char c = lowered.charAt(i);
            if (c == ' '  || c == '\n' || c == '\0' || c == '\t') {
                if (low.length() > 0) {
                    parts.add(low.toString());
                    low.setLength(0);
                }
            }else {
                if((keepAlpha && Character.isAlphabetic(c)) || (keepNumeric && Character.isDigit(c)) || goodChars.contains(c)) {
                    low.append(c);
                }else {
                    if (low.length() > 0) {
                        parts.add(low.toString());
                        low.setLength(0);
                    }
                }
            }
        }

        if (low.length() > 0) parts.add(low.toString());
        return parts;
    }

    public static List<String> breakStringListExtreme(List<String> lst) {
        List<String> elements = new ArrayList<>();
        for(String s : lst)
            elements.addAll(breakStringExtreme(s));

        return elements;
    }

    public static List<String> breakStringArrayExtreme(String[] array) {
        List<String> elements = new ArrayList<>();
        for (String s : array)
            elements.addAll(breakStringExtreme(s));

        return elements;
    }

    //have extract path function
    public static List<String> breakStringExtreme(String str) {
        if(str == null || TextUtils.isEmpty(str))
            return new ArrayList<>();

        String lowered = str.toLowerCase().trim();
        StringBuilder low = new StringBuilder();
        List<String> parts = new ArrayList<>();

        for (int i = 0; i < lowered.length(); i++) {
            char c = lowered.charAt(i);
            if (c == ' '  || c == '\n' || c == '\b' || c == '\r' || c == '\0' || c == '\t' || c == ',' || c == '|' || c == '>' || c == '<') {
                if (low.length() > 0) {
                    parts.add(low.toString());
                    low.setLength(0);
                }
            } else {
                low.append(c);
            }
        }

        if (low.length() > 0) parts.add(low.toString());
        return parts;
    }

    public static String capitalizeFirstLetter(String s) {
        StringBuilder sb = new StringBuilder();
        if(s.length() > 1) {
            char f = s.charAt(0);
            sb.append(Character.toUpperCase(f));
            sb.append(s.substring(1));
        }else sb.append(s);
        return sb.toString();
    }

    public static Boolean toBoolean(String str) { return toBoolean(str, null); }
    public static Boolean toBoolean(String str, Boolean defaultValue) {
        try {
            if(str == null || TextUtils.isEmpty(str)) return defaultValue;
            str = str.trim();
            if(str.equals("0"))
                return false;
            if(str.equals("1"))
                return true;
            if(str.equalsIgnoreCase("false"))
                return false;
            if(str.equalsIgnoreCase("true"))
                return true;

            return defaultValue;
        }catch (Exception ex) {
            Log.e(TAG, "Error Converting String to Boolean: " + str + " " + ex);
            return defaultValue;
        }
    }

    public static Integer toInteger(String str) { return toInteger(str, null); }
    public static Integer toInteger(String str, Integer defaultValue) {
        try {
            return Integer.parseInt(str);
        }catch (Exception ex) {
            Log.e(TAG, "Error Converting String to Integer: " + str + " " + ex);
            return defaultValue;
        }
    }

    public static String join(List<String> args) {
        if (args == null || args.size() == 0)
            return ""; // Return an empty string if the array is null or empty.

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.size(); i++)
            sb.append(args.get(i));

        return sb.toString();
    }


    public static String joinDelimiter(String delimiter, List<String> args) {
        if (args == null || args.size() == 0)
            return ""; // Return an empty string if the array is null or empty.

        StringBuilder sb = new StringBuilder();
        int len = args.size() - 1;
        for (int i = 0; i < args.size(); i++) {
            sb.append(args.get(i));
            if (i < len) { // Add a space after each element except the last one.
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static String join(String... args) {
        if (args == null || args.length == 0)
            return ""; // Return an empty string if the array is null or empty.

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++)
            sb.append(args[i]);

        return sb.toString();
    }

    public static String joinDelimiter(String delimiter, String... args) {
        if (args == null || args.length == 0)
            return ""; // Return an empty string if the array is null or empty.

        StringBuilder sb = new StringBuilder();
        int len = args.length - 1;
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if (i < len) { // Add a space after each element except the last one.
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static boolean isValidString(String str) {
        return str != null && !str.equals(" ") && !str.isEmpty();
    }

    public static String random(int minLen, int maxLen) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        //int randomLength = generator.nextInt(minLen, maxLen);
        int randomLength = ThreadLocalRandom.current().nextInt(minLen, maxLen);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    public static byte[] getUTF8Bytes(String s) {
        try {
            return s.getBytes("UTF-8");
        }catch (Exception e) {
            Log.e(TAG, "Failed to get Bytes from String returning def. e=" + e);
            return new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        }
    }

    public static String getUTF8String(byte[] bs) {
        try {
            return new String(bs, "UTF-8");
        }catch (Exception e) {
            Log.e(TAG, "Failed to get String from Bytes. e=" + e);
            return RandomStringGenerator.generateRandomLetterString(16, RandomStringGenerator.LOWER_LETTERS);
        }
    }


    public static byte[] stringToRawBytes(String drmIdString) {
        // Convert the string back to bytes
        int len = drmIdString.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(drmIdString.charAt(i), 16) << 4)
                    + Character.digit(drmIdString.charAt(i + 1), 16));
        }

        return data;
    }

    public static String rawBytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static String getBytesSHA256Hash(byte[] bs) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(bs);
            return rawBytesToHex(md.digest());
        }catch (Exception e) {
            Log.e(TAG, "Failed to Get Bytes SHA256 Hash.. e=" + e);
            return "";
        }
    }
}
