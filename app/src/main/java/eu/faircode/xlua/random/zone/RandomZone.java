package eu.faircode.xlua.random.zone;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.DataNullElement;
import eu.faircode.xlua.random.elements.DataNumberElement;
import eu.faircode.xlua.random.elements.DataStringElement;
import eu.faircode.xlua.random.elements.ISpinnerElement;

public class RandomZone implements IRandomizer {
    public static final int ZONE_COUNTRY = 0x1;
    public static final int ZONE_COUNTRY_ISO = 0x2;
    public static final int ZONE_LANGUAGE = 0x3;
    public static final int ZONE_LANGUAGE_ISO = 0x4;
    public static final int ZONE_LANGUAGE_TAG = 0x5;
    public static final int ZONE_REGION = 0x6;
    public static final int ZONE_OFFSET = 0x7;
    public static final int ZONE_DISPLAY_NAME = 0x8;
    public static final int ZONE_ID = 0x9;

    private final List<ISpinnerElement> options = new ArrayList<>();

    private String settingName;
    private String name;
    private String id;
    private String[] array;

    private String[] subArray;

    public static RandomZone createCountries() { return new RandomZone(ZONE_COUNTRY); }
    public static RandomZone createCountriesIso() { return new RandomZone(ZONE_COUNTRY_ISO); }
    public static RandomZone createLanguages() { return new RandomZone(ZONE_LANGUAGE); }
    public static RandomZone createLanguageIso() { return new RandomZone(ZONE_LANGUAGE_ISO); }
    public static RandomZone createLanguageTag() { return new RandomZone(ZONE_LANGUAGE_TAG); }
    public static RandomZone createRegion() { return new RandomZone(ZONE_REGION); }
    public static RandomZone createOffset() { return new RandomZone(ZONE_OFFSET); }
    public static RandomZone createDisplayName() { return new RandomZone(ZONE_DISPLAY_NAME); }
    public static RandomZone createIDs() { return new RandomZone(ZONE_ID); }

    public static final List<IRandomizer> RANDOMIZERS = Arrays.asList(
            (IRandomizer)createCountries(),
            (IRandomizer)createCountriesIso(),
            (IRandomizer)createLanguages(),
            (IRandomizer)createLanguageIso(),
            (IRandomizer)createLanguageTag(),
            (IRandomizer)createRegion(),
            (IRandomizer)createOffset(),
            (IRandomizer)createDisplayName(),
            (IRandomizer)createIDs());

    public RandomZone(int kind) {
        switch (kind) {
            case ZONE_COUNTRY:
                settingName = "zone.country";
                name = "TimeZone Country";
                id = "%zone_country%";
                array = GlobalZoneUtil.TIMEZONE_COUNTRIES;
                break;
            case ZONE_COUNTRY_ISO:
                settingName = "zone.country.iso";
                name = "TimeZone Country ISO";
                id = "%zone_country_iso%";
                array = GlobalZoneUtil.TIMEZONE_LANGUAGE_ISO;
                subArray = GlobalZoneUtil.TIMEZONE_COUNTRIES;
                break;
            case ZONE_LANGUAGE:
                settingName = "zone.language";
                name = "TimeZone Language";
                id = "%zone_language%";
                array = GlobalZoneUtil.TIMEZONE_LANGUAGES;
                break;
            case ZONE_LANGUAGE_ISO:
                settingName = "zone.language.iso";
                name = "TimeZone Language ISOs";
                id = "%zone_language_iso%";
                array = GlobalZoneUtil.TIMEZONE_LANGUAGE_ISO;
                subArray = GlobalZoneUtil.TIMEZONE_COUNTRIES;
                break;
            case ZONE_LANGUAGE_TAG:
                settingName = "zone.language.tag";
                name = "TimeZone Language Tags";
                id = "%zone_language_tag%";
                array = GlobalZoneUtil.TIMEZONE_LANGUAGE_TAGS;
                subArray = GlobalZoneUtil.TIMEZONE_COUNTRIES;
                break;
            case ZONE_REGION:
                settingName = "zone.region";
                name = "TimeZone Regions";
                id = "%zone_region%";
                array = GlobalZoneUtil.TIMEZONE_ISO_CODES;
                subArray = GlobalZoneUtil.TIMEZONE_COUNTRIES;
                break;
            case ZONE_OFFSET:
                settingName = "zone.timezone";
                name = "TimeZone Offset";
                id = "%zone_offset%";
                array = GlobalZoneUtil.TIMEZONE_OFFSETS;
                subArray = GlobalZoneUtil.TIMEZONE_COUNTRIES;
                break;
            case ZONE_DISPLAY_NAME:
                settingName = "zone.timezone.display.name";
                name = "TimeZone Display Name";
                id = "%zone_display_name%";
                array = GlobalZoneUtil.TIMEZONE_DISPLAY_NAMES;
                break;
            case ZONE_ID:
                settingName = "zone.timezone.id";
                name = "TimeZone ID";
                id = "%zone_id%";
                array = GlobalZoneUtil.TIMEZONE_IDS;
                break;
        }

        if(name != null && array != null && array.length > 1) {
            List<String> added = new ArrayList<>();
            options.add(DataNullElement.EMPTY_ELEMENT);
            for(int i = 0; i < array.length; i++) {
                String item = array[i];
                if(item == null) continue;
                if(!added.contains(item)) {
                    added.add(item);
                    if(subArray != null) options.add(DataStringElement.create(item + " (" + subArray[i] + ")", item));
                    else options.add(DataStringElement.create(item));
                }
            }
        }
    }


    @Override
    public boolean isSetting(String settingName) { return this.settingName.equalsIgnoreCase(settingName); }

    @Override
    public String getSettingName() { return this.settingName; }

    @Override
    public String getName() { return this.name; }

    @Override
    public String getID() { return this.id; }

    @Override
    public String generateString() {
        ISpinnerElement el = options.get(ThreadLocalRandom.current().nextInt(1, options.size()));
        return el.getValue();
    }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return this.options; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
