package eu.faircode.xlua.api.standard.database;

public class DatabaseUtil {
    public static String fieldArgsToQuery(String[] fieldNames) { return fieldArgsToQuery(fieldNames, false); }
    public static String fieldArgsToQuery(String[] fieldNames, boolean useOr) {
        StringBuilder sb = new StringBuilder();
        int check = fieldNames.length - 1;
        String state = useOr ? " OR" : " AND";

        for (int i = 0; i < fieldNames.length; i++) {
            String f = fieldNames[i];
            sb.append(f).append(" = ?");
            if (i != check)
                sb.append(state);

        }

        return sb.toString();
    }
}

