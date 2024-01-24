package eu.faircode.xlua.utilities;

import java.util.Collection;

public class CollectionUtil {
    public static boolean isValid(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}
