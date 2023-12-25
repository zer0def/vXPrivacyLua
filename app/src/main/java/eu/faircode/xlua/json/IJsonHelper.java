package eu.faircode.xlua.json;

import org.json.JSONException;
import org.json.JSONObject;

import eu.faircode.xlua.database.IDatabaseHelper;

public interface IJsonHelper extends IDatabaseHelper  {
    void fromJSONAssets(JSONObject jsonObject, String path) throws JSONException;

}
