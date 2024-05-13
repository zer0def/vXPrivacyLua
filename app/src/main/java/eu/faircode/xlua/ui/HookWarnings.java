package eu.faircode.xlua.ui;

import android.content.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import eu.faircode.xlua.R;
import eu.faircode.xlua.utilities.PrefUtil;

public class HookWarnings {
    private static final List<String> WARNING_GROUPS = Arrays.asList("Get.User", "Get.Apps.Ex", "Spoof.Status.Cell", "Hide.VPN.State", "ID.User", "AD.Analytics.Spoof", "AD.Analytics", "Hide.Environment", "Intercept.Shell", "Spoof.Embed.Paypal", "Spoof.GSM.Status", "Spoof.UserAgent", "Spoof.SOC.EX", "Intercept.Properties", "Spoof.Language", "Spoof.VPN.State", "Spoof.Features", "Get.Location", "Spoof.Location", "Get.Apps", "Get.App");
    //private static final List<String> WARNING_GROUPS = Arrays.asList("ID.User", "AD.Analytics.Spoof", "AD.Analytics", "Hide.Environment", "Intercept.Shell", "Spoof.Embed.Paypal", "Spoof.GSM.Status", "Spoof.UserAgent", "Spoof.SOC.EX", "Intercept.Properties", "Spoof.Language", "Spoof.VPN.State", "Spoof.Features", "Get.Location", "Spoof.Location", "Get.App");
    private static final HashMap<String, Boolean> localPrefs = new HashMap<>();
    public static boolean hasWarning(Context context, String group) {
        if(!WARNING_GROUPS.contains(group)) return false;
        Boolean show = localPrefs.get(group);
        if(show == null) {
            show = PrefUtil.getBoolean(context, "warning_" + group.hashCode() + "_show", true, true);
            localPrefs.put(group, show);
        } return show;
    }

    public static void setWarnFlag(Context context, String group, boolean warn) {
        if(WARNING_GROUPS.contains(group)) PrefUtil.setBoolean(context,"warning_" + group.hashCode() + "_show" , warn);
    }

    /*public static String getWarningMessage(Context p0,String p1){
        Boolean uBoolean;
        if ((uBoolean = HookWarnings.localPrefs.get(p1)) != null || HookWarnings.WARNING_GROUPS.contains(p1)) {
            boolean b = true;
            if (uBoolean == null) {
                uBoolean = PrefUtil.getBoolean(p0, "warning_"+p1.hashCode()+"_show", Boolean.valueOf(b), b);
                HookWarnings.localPrefs.put(p1, uBoolean);
            }
            if (uBoolean.booleanValue()) {
                switch (p1.hashCode()){
                    case 0x8c6108de:
                        if (p1.equals("ID.User")) {
                            b = 0;
                        }else {
                            label_0106 :
                            b = -1;
                        }
                        break;
                    case 0xa31bda12:
                        if (p1.equals("Spoof.VPN.State")) {
                            b = 7;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0xb5b7f14b:
                        if (p1.equals("Hide.VPN.State")) {
                            b = 8;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0xb9d2a6f7:
                        if (p1.equals("Get.Apps.Ex")) {
                            b = 16;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0xc2af76cd:
                        if (p1.equals("Spoof.Embed.Paypal")) {
                            b = 13;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0xc4fde9e4:
                        if (p1.equals("Spoof.GSM.Status")) {
                            b = 11;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0xcca919c4:
                        if (p1.equals("Intercept.Shell")) {
                            b = 14;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x16a768dd:
                        if (p1.equals("Spoof.Language")) {
                            b = 6;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x4f5c2d3f:
                        if (p1.equals("Spoof.SOC.EX")) {
                            b = 4;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x51d40e59:
                        if (p1.equals("Spoof.Status.Cell")) {
                            b = 12;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x5eac7749:
                        if (p1.equals("Get.App")) {
                            b = 10;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x608fdf67:
                        if (p1.equals("Hide.Environment")) {
                            b = 15;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x6581b9c2:
                        if (p1.equals("Spoof.Features")) {
                            b = 9;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x7655e915:
                        if (p1.equals("Spoof.UserAgent")) {
                            b = 3;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x7753ee1f:
                        if (p1.equals("Intercept.Properties")) {
                            b = 5;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    case 0x7895875b:
                        if (!p1.equals("AD.Analytics")) {
                        }
                        break;
                    case 0x7f307a96:
                        if (p1.equals("AD.Analytics.Spoof")) {
                            b = 2;
                        }else {
                      goto label_0106 ;
                        }
                        break;
                    default:
                   goto label_0106 ;
                }
                switch (b){
                    case 0:
                        return p0.getString(0x7f100137);
                    case 1:
                        return p0.getString(0x7f10012f);
                    case 2:
                        return p0.getString(0x7f100130);
                    case 3:
                        return p0.getString(0x7f10013d);
                    case 4:
                        return p0.getString(0x7f10013b);
                    case 5:
                        return p0.getString(0x7f10013a);
                    case 6:
                        return p0.getString(0x7f100139);
                    case 7:
                    case 8:
                        return p0.getString(0x7f10013e);
                    case 9:
                        return p0.getString(0x7f10013c);
                    case 10:
                        return p0.getString(0x7f100132);
                    case 11:
                    case 12:
                        return p0.getString(0x7f100135);
                    case 13:
                        return p0.getString(0x7f100131);
                    case 14:
                        return p0.getString(0x7f100138);
                    case 15:
                        return p0.getString(0x7f100136);
                    case 16:
                        return p0.getString(0x7f100133);
                    default:
                }
            }
        }
        return null;
    }*/

    public static String getWarningMessage(Context context, String group) {
        Boolean show = localPrefs.get(group);
        if(show != null || WARNING_GROUPS.contains(group)) {
            if(show == null) {
                show = PrefUtil.getBoolean(context, "warning_" + group.hashCode() + "_show", true, true);
                localPrefs.put(group, show);
            }

            if(show) {
                switch (group) {
                    case "ID.User":
                    case "Get.User":
                        return context.getString(R.string.warning_hook_id_user);
                    case "AD.Analytics":
                        return context.getString(R.string.warning_hook_ad_analytics);
                    case "AD.Analytics.Spoof":
                        return context.getString(R.string.warning_hook_ad_analytics_spoof);
                    case "Spoof.UserAgent":
                        return context.getString(R.string.warning_hook_user_agent);
                    case "Spoof.SOC.EX":
                        return context.getString(R.string.warning_hook_soc_ex);
                    case "Intercept.Properties":
                        return context.getString(R.string.warning_hook_properties);
                    case "Spoof.Language":
                        return context.getString(R.string.warning_hook_language);
                    case "Spoof.VPN.State":
                    case "Hide.VPN.State":
                        return context.getString(R.string.warning_hook_vpn_state);
                    case "Spoof.Features":
                        return context.getString(R.string.warning_hook_spoof_features);
                    case "Get.Location":
                    case "Spoof.Location":
                        return context.getString(R.string.warning_hook_get_location);
                    case "Get.App":
                    case "Get.Apps":
                        return context.getString(R.string.warning_hook_get_app);
                    case "Get.Apps.Ex":
                        return context.getString(R.string.warning_hook_get_apps_ex);
                    case "Spoof.GSM.Status":
                        return context.getString(R.string.warning_hook_gsm_status);
                    case "Spoof.Embed.Paypal":
                        return context.getString(R.string.warning_hook_embed_paypal);
                    case "Intercept.Shell":
                        return context.getString(R.string.warning_hook_intercept_shell);
                    case "Hide.Environment":
                        return context.getString(R.string.warning_hook_hide_environment);
                }
            }
        } return null;
    }
}
