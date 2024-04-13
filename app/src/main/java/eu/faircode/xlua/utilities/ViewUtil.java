package eu.faircode.xlua.utilities;

import android.view.View;
import android.widget.ImageView;

import java.util.Map;

public class ViewUtil {
    public static void setViewsVisibility(ImageView img, boolean expanded, View... views) {
        int vv = expanded ?  View.VISIBLE : View.GONE;
        if(img != null) img.setImageLevel(expanded ? 1 : 0);
        for(View v : views) {
            if(v != null) v.setVisibility(vv);
        }
    }

    public static void internalUpdateExpanded(Map<String, Boolean> expandedMaps, String key, Map<String, Boolean> expandedMapQuery, boolean enforceExpand) {
        //tho this function will still not solve the issue if they hide it and the query is cleaned :P
        //soo how about when query is cleared reset all expanded ? has to be done in parent caller

        //for the init check if it already exists if not then call with enforce flag
        if(expandedMaps == null) return;
        if(enforceExpand && expandedMapQuery != null) {
            expandedMaps.put(key, true);
            expandedMapQuery.put(key, true);
            return;
        }

        if(expandedMapQuery != null && !expandedMaps.containsKey(key) && expandedMapQuery.containsKey(key)) {
            //update the expanded maps with the key from the expanded_query maps the
            //if null the just continue execution as if it didnt exist in the expanded_query maps
            Boolean e = expandedMapQuery.get(key);
            if(e != null) {
                expandedMaps.put(key, e);
                return;
            }
        }

        if(!expandedMaps.containsKey(key)) {
            //if expandedMaps does not have the key then by default set it to false expanded
            //this will be inverted with the code below so use this only on UI transactions
            expandedMaps.put(key, false);
        }

        //get if is expanded
        boolean isExpanded = expandedMaps.containsKey(key) && Boolean.FALSE.equals(expandedMaps.get(key));
        if(expandedMapQuery != null && expandedMapQuery.containsKey(key)) {
            //Since this is not called from the Adapter View Init but instead a UI Transaction we will ensure the two maps match
            //ofc if the expanded_query map is not null
            expandedMapQuery.put(key, isExpanded);
        }

        //Finally update the main map
        expandedMaps.put(key, isExpanded);
    }

    public static void internalUpdateExpanded(Map<String, Boolean> expandedMaps, String key) { internalUpdateExpanded(expandedMaps, key, false); }
    public static void internalUpdateExpanded(Map<String, Boolean> expandedMaps, String key, boolean enforceExpand) {
        if(!expandedMaps.containsKey(key))
            expandedMaps.put(key, false);

        if(enforceExpand) { expandedMaps.put(key, true);
        }else expandedMaps.put(key, Boolean.FALSE.equals(expandedMaps.get(key)));
    }
}
