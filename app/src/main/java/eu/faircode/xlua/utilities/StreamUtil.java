package eu.faircode.xlua.utilities;

import java.io.InputStream;
import java.util.zip.ZipFile;

public class StreamUtil {

    public static void close(ZipFile zf) {
        if(zf == null) return;
        try { zf.close(); } catch (Exception ignore) { }
    }

    public static void close(InputStream is) {
        if(is == null) return;
        try { is.close(); } catch (Exception ignore) { }
    }
}
