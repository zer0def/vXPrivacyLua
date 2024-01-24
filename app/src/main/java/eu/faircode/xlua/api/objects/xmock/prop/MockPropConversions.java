package eu.faircode.xlua.api.objects.xmock.prop;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;

import eu.faircode.xlua.api.objects.xmock.cpu.MockCpu;

public class MockPropConversions {
    private static final String TAG = "XLua.MockPropConversions";

    public static MockProp fromBundle(Bundle b) {
        if(b == null)
            return null;

        if(b.containsKey("result")) {
            Log.e(TAG, "MockProp From Bundle Contains a 'result' return ....");
            if(!b.getBoolean("result", false))
                return null;
        }

        MockProp prop = new MockProp();
        prop.fromBundle(b);
        return prop;
    }

    public static Collection<MockProp> fromBundleArray(Bundle bundle) {
        String[] names = bundle.getStringArray("names");
        String[] values = bundle.getStringArray("values");
        String[] defValues = bundle.getStringArray("defaultValues");
        boolean[] enValues = bundle.getBooleanArray("enabledValues");

        Collection<MockProp> ps = new ArrayList<>();
        if(names == null || values == null || defValues == null || enValues == null)
            return ps;

        int nLen = names.length;
        if(values.length != nLen || defValues.length != nLen || enValues.length != nLen)
            return ps;


        Log.i(TAG, "MockProp.fromBundleArray(Bundle)=" + names.length);

        for (int i = 0; i < names.length; i++)
            ps.add(new MockProp(names[i], values[i], defValues[i], enValues[i]));

        return ps;
    }

    public static Bundle toBundleArray(Collection<MockProp> props) {
        Bundle b = new Bundle();
        if(props == null)
            return b;

        String[] names = new String[props.size()];
        String[] values = new String[props.size()];
        String[] defValues = new String[props.size()];
        boolean[] enValues = new boolean[props.size()];

        int i = 0;
        for(MockProp prop : props) {
            names[i] = prop.getName();
            values[i] = prop.getValue();
            defValues[i] = prop.getDefaultValue();
            enValues[i] = prop.isEnabled();
            i++;
        }

        b.putStringArray("names", names);
        b.putStringArray("values", values);
        b.putStringArray("defaultValues", defValues);
        b.putBooleanArray("enabledValues", enValues);

        return  b;
    }

    public static Collection<MockProp> fromCursor(Cursor cursor, boolean marshall) {
        //make sure this supports json / marshall
        Collection<MockProp> ps = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            if(marshall) {
                byte[] marshaled = cursor.getBlob(0);
                Parcel parcel = Parcel.obtain();
                parcel.unmarshall(marshaled, 0, marshaled.length);
                parcel.setDataPosition(0);
                MockProp prop = MockProp.CREATOR.createFromParcel(parcel);
                parcel.recycle();
                ps.add(prop);
            }else {
                //add
            }
        }

        return ps;
    }

    public static Cursor toCursor(Collection<MockProp> props, boolean marshall, boolean close) {
        Log.i(TAG, "toCursor");
        MatrixCursor result = new MatrixCursor(new String[]{ marshall ? "blob" : "json"});
        try {
            for (MockProp prop : props)
                if (marshall) {
                    Parcel parcel = Parcel.obtain();
                    prop.writeToParcel(parcel, 0);
                    result.newRow().add(parcel.marshall());
                    parcel.recycle();
                } else
                    result.addRow(new Object[]{prop.toJSON()});
        }catch (Exception ex) {
            Log.e(TAG, "[MockProp][toCursor] Failed=\n" + ex.getMessage());
        }
        return result;
    }
}
