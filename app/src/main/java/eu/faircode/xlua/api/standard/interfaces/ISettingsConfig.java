package eu.faircode.xlua.api.standard.interfaces;

import java.util.Map;

public interface ISettingsConfig {
    String getName();
    Map<String, String> getSettings();
}
