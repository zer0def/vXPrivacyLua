package eu.faircode.xlua.utilities;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListFilterUtil {
    private static final String TAG = "XLua.ListFilterUtil";

    //Do bluetooth scan results
    public static List<WifiConfiguration> filterSavedWifiNetworks(List<WifiConfiguration> results, List<String> allowList) {
        List<WifiConfiguration> allowed = new ArrayList<>();
        if(!CollectionUtil.isValid(results)) return results;
        boolean disallowAll = !CollectionUtil.isValid(allowList) || allowList.get(0).equals("*");
        if(disallowAll) return allowed;

        try {
            for(WifiConfiguration w : results) {
                String ssid = w.SSID;
                String bssid = w.BSSID;

                if(ssid == null && bssid == null)
                    continue;

                if(ssid != null) ssid = ssid.trim();
                if(bssid != null) bssid = bssid.trim();

                for(String a : allowList) {
                    String at = a.trim();
                    if(ssid != null) {
                        if(ssid.equalsIgnoreCase(at)) {
                            allowed.add(w);
                            break;
                        }
                    }

                    if(bssid != null) {
                        if(bssid.equalsIgnoreCase(at)) {
                            allowed.add(w);
                            break;
                        }
                    }
                }
            }
        }catch (Exception e) {
            Log.e(TAG, "Failed to filter Configured networks maybe dep. e=" + e);
        }

        return allowed;
    }

    public static List<ScanResult> filterWifiScanResults(List<ScanResult> results, List<String> allowList) {
        List<ScanResult> allowed = new ArrayList<>();
        if(!CollectionUtil.isValid(results)) return results;
        boolean disallowAll = !CollectionUtil.isValid(allowList) || allowList.get(0).equals("*");
        if(disallowAll) return allowed;

        for(ScanResult w : results) {
            String ssid = w.SSID;
            String bssid = w.BSSID;

            if(ssid == null && bssid == null)
                continue;

            if(ssid != null) ssid = ssid.trim();
            if(bssid != null) bssid = bssid.trim();

            for(String a : allowList) {
                String at = a.trim();
                if(ssid != null) {
                    if(ssid.equalsIgnoreCase(at)) {
                        allowed.add(w);
                        break;
                    }
                }

                if(bssid != null) {
                    if(bssid.equalsIgnoreCase(at)) {
                        allowed.add(w);
                        break;
                    }
                }
            }
        }

        return allowed;
    }


    @SuppressLint("MissingPermission")
    public static Set<BluetoothDevice> filterSavedBluetoothDevices(Set<BluetoothDevice> devices, List<String> allowList) {
        Set<BluetoothDevice> allowed = new HashSet<>();
        if(!CollectionUtil.isValid(devices)) return devices;
        boolean disallowAll = !CollectionUtil.isValid(allowList) || allowList.get(0).equals("*");
        if(disallowAll) return allowed;
        for(BluetoothDevice d : devices) {
            for(String a : allowList) {
                String at = a.trim();
                String address = d.getAddress();
                if(address.equalsIgnoreCase(at)) {
                    allowed.add(d);
                    Log.w(TAG, "Allowing Bluetooth Device: " + address);
                    break;
                }

                try {
                    String name = d.getName().trim();
                    if(name.equalsIgnoreCase(at)) {
                        allowed.add(d);
                        Log.w(TAG, "Allowing Bluetooth Device: " + name);
                        break;
                    }
                }catch (SecurityException  e) {
                    Log.e(TAG, "Most likely required permission and its missing...");
                }
            }
        }

        return allowed;
    }

}
