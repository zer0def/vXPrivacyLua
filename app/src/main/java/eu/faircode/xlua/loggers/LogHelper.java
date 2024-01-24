package eu.faircode.xlua.loggers;

import java.util.List;

import eu.faircode.xlua.utilities.CollectionUtil;

public class LogHelper {
    public static void writeStringList(StringBuilder sb, List<String> lst) {
        writeStringList(sb, lst, ",");
    }

    public static void writeStringList(StringBuilder sb, List<String> lst, String delimiter) {
        if(!CollectionUtil.isValid(lst))
            return;

        int ix = 0;
        int stp = lst.size() - 1;
        for(String s : lst) {
            sb.append(s);
            if(ix != stp)
                sb.append(delimiter);
            ix++;
        }
    }

    public static void writeStringArray(StringBuilder sb, String[] arr) {
        writeStringArray(sb, arr, ",");
    }

    public static void writeStringArray(StringBuilder sb, String[] arr, String delimiter) {
        if(arr == null || arr.length < 1)
            return;

        int ix = 0;
        int stp = arr.length - 1;
        for(String s : arr) {
            sb.append(s);
            if(ix != stp)
                sb.append(delimiter);
            ix++;
        }
    }
}
