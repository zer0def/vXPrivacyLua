package eu.faircode.xlua.api.xmock;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.XDataBase;
import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;
import eu.faircode.xlua.database.DatabaseHelperEx;
import eu.faircode.xlua.database.DatabaseQuerySnake;
import eu.faircode.xlua.utilities.CollectionUtil;

public class XMockCpuProvider {
    private static boolean makeSureOneSelected = false;

    private static List<MockCpu> selectedMapObjects = new ArrayList<>();
    private static List<String> allMapNames = new ArrayList<>();

    private static final Object lock = new Object();

    private static final String TAG = "XLua.XMockCpuApi";

    public static boolean putCpuMap(XDataBase db, String cpuMapName, boolean selected) {
        synchronized (lock) {
            if(selected) {
                if(selectedMapObjects.size() > 0) {
                    if(makeSureOneSelected) {
                        for(MockCpu map : selectedMapObjects)
                            map.setSelected(false);

                        if(!XMockCpuDatabase.putCpuMaps(db, selectedMapObjects))
                            return false;

                        selectedMapObjects.clear();
                        //we can work with content values like
                        //createContentValues(QuerySnake)
                        //  if(snake.onlyFields.contains(fieldName))
                    }
                }

                MockCpu map = XMockCpuDatabase.getMap(db, cpuMapName, true);
                map.setSelected(true);
                if(!XMockCpuDatabase.insertCpuMap(db, map))
                    return false;

                selectedMapObjects.add(map);
                return true;
            }else {
                for(int i = 0; i < selectedMapObjects.size(); i++) {
                    MockCpu map = selectedMapObjects.get(i);
                    if(map.getName().equals(cpuMapName)) {
                        map.setSelected(false);
                        if(!XMockCpuDatabase.insertCpuMap(db, map))
                            return false;

                        selectedMapObjects.remove(map);
                        return true;
                    }
                }

                MockCpu map = XMockCpuDatabase.getMap(db, cpuMapName, true);
                map.setSelected(false);
                return XMockCpuDatabase.insertCpuMap(db, map);
            }
        }
    }

    public static MockCpu getSelectedCpuMap(Context context, XDataBase db) {
        //synchronized (lock) {

        //}
        if (makeSureOneSelected && selectedMapObjects.size() > 0)
            return selectedMapObjects.get(0);
        else if (!makeSureOneSelected && selectedMapObjects.size() > 0) {
            if (selectedMapObjects.size() == 1)
                return selectedMapObjects.get(0);

            return selectedMapObjects.get(ThreadLocalRandom.current().nextInt(0, selectedMapObjects.size()));
        }

        //Check cache first
        if (allMapNames.size() == 0) {
            initCache(context, db);
            if(allMapNames.isEmpty())
                return MockCpu.EMPTY_DEFAULT;

        }else {
            //Cache NOT empty but selected IS empty
            //PS when cache is Init we make sure ONLY one is selected IF flag is SET
            //So do not worry about the many that can be selected ?
            //Sure , we can make it even better to constantly check but that can use a lot of resources so lets avoid ?
            //Lets just leave it up to a 'update' function to 'update' cache

            Log.i(TAG, "CPU Map is not selected Randomizing...");
            String randomName = allMapNames.get(ThreadLocalRandom.current().nextInt(0, allMapNames.size()));
            Log.i(TAG, "Random CPU Map Selected, name=" + randomName);
            return XMockCpuDatabase.getMap(db, randomName, true);
        }

        return getSelectedCpuMap(context, db);
    }

    public static void initCache(Context context, XDataBase db) {
        selectedMapObjects.clear();
        allMapNames.clear();
        //Here is the bug
        Collection<MockCpu> localMaps = XMockCpuDatabase.getCpuMaps(context, db);
        Collection<MockCpu> reset = new ArrayList<>();
        if (localMaps.size() < XMockCpuDatabase.COUNT)
            return;
            //return MockCpu.EMPTY_DEFAULT;//Failed to init cache for some reason

        for (MockCpu map : localMaps) {
            if (map.getSelected()) {
                if (selectedMapObjects.size() == 1 && makeSureOneSelected) {
                    map.setSelected(false);
                    reset.add(map);
                } else {
                    selectedMapObjects.add(map);
                }
            }

            allMapNames.add(map.getName());
        }

        if (!reset.isEmpty()) {
            Log.i(TAG, "Too many 'selected' CPU Maps, resetting them.. size=" + reset.size());
            if (!DatabaseHelperEx.insertItems(db, MockCpu.Table.name, reset))
                Log.e(TAG, "Failed to Reset CPU Maps Selected .....");
        }
    }
}
