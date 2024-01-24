package eu.faircode.xlua.api.xmock;

import android.content.Context;

import org.luaj.vm2.compiler.Constants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;
import eu.faircode.xlua.database.DatabaseHelperEx;
import eu.faircode.xlua.database.DatabaseQuerySnake;

public class XMockCpuDatabase {
    private static final String TAG = "XLua.XMockCpuDatabase";
    public static final int COUNT = 43;
    public static final String JSON = "cpumaps.json";

    public static boolean insertCpuMap(XDataBase db, MockCpu map) {
        return DatabaseHelperEx.insertItem(
                db,
                MockCpu.Table.name,
                map);
    }

    public static boolean updateCpuMap(XDataBase db, String name, boolean selected) {
        MockCpu map = new MockCpu(name, null, null, null, selected);
        DatabaseQuerySnake snake = DatabaseQuerySnake.create(db, MockCpu.Table.name)
                .whereColumn("name", name)
                .whereColumn("selected", Boolean.toString(!selected));

        return DatabaseHelperEx.updateItem(snake, map);
    }

    public static boolean putCpuMaps(XDataBase db, Collection<MockCpu> maps) {
       return DatabaseHelperEx.insertItems(db, MockCpu.Table.name, maps);
    }

    public static MockCpu getSelectedMap(XDataBase db, boolean getContents) {
        DatabaseQuerySnake snake = DatabaseQuerySnake.create(db, MockCpu.Table.name)
                .whereColumn("selected", "true");

        if(!getContents)
            avoidContents(snake);

        return snake.queryGetFirstAs(MockCpu.class, true);
    }

    public static MockCpu getMap(XDataBase db, String name, boolean getContents) {
        DatabaseQuerySnake snake = DatabaseQuerySnake
                .create(db, MockCpu.Table.name)
                .whereColumn("name", name);

        if(!getContents)
            snake.onlyReturnColumns("name", "model", "manufacturer", "selected");

        return snake.queryGetFirstAs(MockCpu.class, true);
    }

    public static void enforceOneSelected(XDataBase db, String keepMapName, boolean keepFirstSelected) {
        DatabaseQuerySnake selectedSnake = DatabaseQuerySnake.create(db, MockCpu.Table.name)
                .whereColumn("selected", "true")
                .onlyReturnColumns("name", "selected");

        List<MockCpu> selected = new ArrayList(selectedSnake.queryAs(MockCpu.class, true));
        if(selected.size() > 1) {
            if(keepMapName != null) {
                for(int i = 0; i < selected.size(); i++) {
                    MockCpu map = selected.get(i);
                    if(map.getName().equals(keepMapName)) {
                        selected.remove(map);
                        break;
                    }
                }
            }
            else if(keepFirstSelected)
                selected.remove(selected.get(0));

            for (MockCpu m : selected)
                m.setSelected(false);

            DatabaseHelperEx.updateItems(db, MockCpu.Table.name, selected, selectedSnake);
        }
    }

    public static Collection<MockCpu> getSelectedMaps(XDataBase db) {
        DatabaseQuerySnake selectedSnake = DatabaseQuerySnake.create(db, MockCpu.Table.name)
                .whereColumn("selected", "true")
                .onlyReturnColumns("name", "selected");

        return selectedSnake.queryAs(MockCpu.class, true);
    }

    public static Collection<String> getSelectedMapNames(XDataBase db) {
        DatabaseQuerySnake selectedSnake = DatabaseQuerySnake.create(db, MockCpu.Table.name)
                .whereColumn("selected", "true")
                .onlyReturnColumns("name", "selected");

        return selectedSnake.queryAsStringList("name", true);
    }

    public static Collection<MockCpu> getCpuMaps(Context context, XDataBase db) {
        return DatabaseHelperEx.initDatabase(
                context,
                db,
                MockCpu.Table.name,
                MockCpu.Table.columns,
                JSON,
                true,
                MockCpu.class,
                COUNT);
    }

    private static void avoidContents(DatabaseQuerySnake snake) {
        snake.onlyReturnColumns("name", "model", "manufacturer", "selected");
    }
}
