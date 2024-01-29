package eu.faircode.xlua.api.objects;

import java.util.Map;

public interface ISettingsConfig {
    String getName();
    Map<String, String> getSettings();
}
