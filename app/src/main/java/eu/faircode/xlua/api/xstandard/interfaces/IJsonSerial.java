package eu.faircode.xlua.api.xstandard.interfaces;

import org.json.JSONException;
import org.json.JSONObject;

public interface IJsonSerial extends IDBSerial, ISerial {
    String toJSON() throws JSONException;
    JSONObject toJSONObject() throws JSONException;

    void fromJSONObject(JSONObject obj) throws JSONException;
}
