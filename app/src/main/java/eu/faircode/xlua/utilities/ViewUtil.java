package eu.faircode.xlua.utilities;

import android.view.View;
import android.widget.ImageView;

import java.util.Map;

public class ViewUtil {
    public static void setViewsVisibility(ImageView img, boolean expanded, View... views) {
        int vv = expanded ?  View.VISIBLE : View.GONE;
        img.setImageLevel(expanded ? 1 : 0);
        for(View v : views)
            v.setVisibility(vv);
    }

    public static void internalUpdateExpanded(Map<String, Boolean> expandedMaps, String key) {
        if(!expandedMaps.containsKey(key))
            expandedMaps.put(key, false);

        expandedMaps.put(key, Boolean.FALSE.equals(expandedMaps.get(key)));
    }
}
