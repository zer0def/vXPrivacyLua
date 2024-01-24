package eu.faircode.xlua.database.readers;

import android.database.Cursor;

/*public class GroupCursorObject implements ICursorReader {
    //pass cursor within here to map out the indexes ?
    //
    //
    public static final int USER = 0x1;
    public static final int CATEGORY = 0x2;
    public static final int NAME = 0x4;
    public static final int VALUE = 0x5;

    public static final int CATEGORY_NAME = CATEGORY | NAME;
    public static final int ALL = USER | CATEGORY | NAME | VALUE;


    public int userId;
    private Integer userid_index;

    public String category;
    private Integer category_index;

    public String name;
    private Integer name_index;

    public String value;
    private Integer value_index;

    private int flags;

    private Cursor cursor;

    public GroupCursorObject() { }
    public GroupCursorObject(Cursor cursor, int flags) {
        if((flags & CATEGORY_NAME) == CATEGORY_NAME) {
            category_index = cursor.getColumnIndex("category");
            name_index = cursor.getColumnIndex("name");
        }
        if((flags & ALL) == ALL) {

        }
    }


    @Override
    public ICursorReader readCursor() {
        GroupCursorObject obj = new GroupCursorObject();
        if((flags & CATEGORY_NAME) == CATEGORY_NAME) {
            obj.category = cursor.getString(category_index);
            obj.name = cursor.getString(name_index);
            return obj;
        }
        else if(())
    }
}*/
