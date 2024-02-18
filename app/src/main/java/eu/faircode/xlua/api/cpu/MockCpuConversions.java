package eu.faircode.xlua.api.cpu;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

public class MockCpuConversions {
    private static final String TAG = "XLua.MockCpuConversions";

    public static MockCpu fromBundle(Bundle b) {
        if(b == null)
            return null;

        if(b.containsKey("result")) {
            Log.e(TAG, "MockCpu From Bundle Contains a 'result' return ....");
            if(!b.getBoolean("result", false))
                return null;
        }


        MockCpu cpu = new MockCpu();
        cpu.fromBundle(b);
        return cpu;
    }

    public static Collection<MockCpu> fromBundleArray(Bundle bundle) {
        String[] names = bundle.getStringArray("names");
        String[] models = bundle.getStringArray("models");
        String[] manufacturers = bundle.getStringArray("manufacturers");
        String[] contents = bundle.getStringArray("contents");
        boolean[] selected = bundle.getBooleanArray("selected");

        Collection<MockCpu> items = new ArrayList<>();
        if(names == null || models == null || manufacturers == null || contents == null || selected  == null)
            return items;

        int nLen = names.length;
        if(nLen < 1)
            return items;

        if(models.length != nLen || manufacturers.length != nLen || contents.length != nLen || selected.length != nLen)
            return items;


        Log.i(TAG, "MockCpu.fromBundleArray(Bundle)=" + names.length);

        for (int i = 0; i < names.length; i++)
            items.add(new MockCpu(names[i], models[i], manufacturers[i], contents[i], selected[i]));

        return items;
    }

    public static Bundle toBundleArray(Collection<MockCpu> maps) {
        Bundle b = new Bundle();
        if(maps == null)
            return b;

        //Bundle b = new Bundle();
        String[] names = new String[maps.size()];
        String[] models = new String[maps.size()];
        String[] manufacturers = new String[maps.size()];
        String[] contents = new String[maps.size()];
        boolean[] selected = new boolean[maps.size()];

        int i = 0;
        for(MockCpu map : maps) {
            names[i] = map.name;
            models[i] = map.model;
            manufacturers[i] = map.manufacturer;
            contents[i] = map.contents;
            selected[i] = map.selected;
            i++;
        }

        b.putStringArray("names", names);
        b.putStringArray("models", models);
        b.putStringArray("manufacturers", manufacturers);
        b.putStringArray("contents", contents);
        b.putBooleanArray("selected", selected);

        return  b;
    }

}
