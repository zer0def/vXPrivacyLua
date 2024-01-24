package eu.faircode.xlua.database;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.faircode.xlua.BuildConfig;
import eu.faircode.xlua.XDataBase;

public class DatabaseQueryBuilder {
    private static final String TAG = "XLua.DatabaseQueryBuilder";

    protected XDataBase db;
    protected String tableName;

    protected List<String> compareValues = new ArrayList<>();
    protected List<String> onlyReturn = new ArrayList<>();

    private String symbolWithCompare = " = ?";
    private boolean useOr = false;

    protected StringBuilder selectionArgsBuilder = new StringBuilder();
    protected String orderOrFieldName = null;
    protected int fCount = 0;

    public String getTableName() {
        return tableName;
    }
    public String getSelectionArgs() {
         return selectionArgsBuilder.toString();
    }

    public String[] getSelectionCompareValues() {
        if(compareValues.isEmpty())
            return new String[] { };

        return compareValues.toArray(new String[0]);
    }

    public DatabaseQueryBuilder() { }
    public DatabaseQueryBuilder(XDataBase db, String tableName) {
        this.db = db;
        this.tableName = tableName;
    }

    protected void internal_setSymbol(String symbol) {
        internal_setSymbol(symbol.replace(" ", ""), "?");
    }

    protected void internal_setSymbol(String symbol, String compareValue) {
        if(!isOperator(symbol))
            return;

        this.symbolWithCompare = " " + symbol + " " + compareValue;
    }

    protected void internal_useOr(boolean useOr) {
        this.useOr = useOr;
    }

    protected void internal_whereColumnsEquals(String... columnNames) {
        for(String f : columnNames)
            internal_whereColumnBinds(f, null);
    }

    protected void internal_anchorValuesWithFields(String... values) {
        compareValues.addAll(Arrays.asList(values));
    }

    protected void internal_whereColumnBinds(String columnName, String value) { internal_whereColumnBinds(columnName, value, null); }
    protected void internal_whereColumnBinds(String columnName, String value, String symbol) {
        if(fCount != 0)
            selectionArgsBuilder.append(useOr ? " OR " : " AND ");

        if(symbol != null) {
            symbol = symbol.replace(" ", "");
            symbol = " " + symbol + " ?";
            //eww this looks ugly fix later
        }

        selectionArgsBuilder.append(columnName);
        selectionArgsBuilder.append(symbol == null ? symbolWithCompare : symbol);
        if(value != null) compareValues.add(value);
        fCount++;
    }

    protected void internal_onlyReturnColumn(String columnName) {
        Log.i(TAG, "only setting return=" + columnName);
        if(!onlyReturn.contains(columnName))
            onlyReturn.add(columnName);
    }

    protected void internal_orderBy(String orderBy) {
        orderOrFieldName = orderBy;
    }

    protected void internal_onlyReturnColumns(String... fields) {
        for(String f : fields)
            if(!onlyReturn.contains(f))
                onlyReturn.add(f);
    }

    private static final String op_pattern = "^[=!<>]+$";
    private static boolean isOperator(String symbol) {
        return symbol.length() < 3 && symbol.matches(op_pattern);
    }
}
