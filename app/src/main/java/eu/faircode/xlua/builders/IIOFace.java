package eu.faircode.xlua.builders;

import android.content.ContentValues;
import android.os.Bundle;

import org.json.JSONObject;

import eu.faircode.xlua.builders.objects.Contenter;

public interface IIOFace {
    IIOFace wString(String key, String value);
    IIOFace wString(String key, String value, String def);

    String rString(String key);
    String rString(String key, String def);

    IIOFace wLong(String key, Long value);
    IIOFace wLong(String key, Long value, Long def);

    Long rLong(String key);
    Long rLong(String key, Long def);

    IIOFace wInt(String key, Integer value);
    IIOFace wInt(String key, Integer value, Integer def);

    Integer rInt(String key);
    Integer rInt(String key, Integer def);

    IIOFace wBool(String key, Boolean value);
    IIOFace wBool(String key, Boolean value, Boolean def);

    Boolean rBool(String key);
    Boolean rBool(String key, Boolean def);

    Bundle rBundle(String key);
    IIOFace wBundle(String key, Bundle bundle);

    Bundle toBundle();
    JSONObject toJson();
    ContentValues toContentValues();

    IIOFace writeIfNull(boolean writeIfNull);
    int getFlags();
}
