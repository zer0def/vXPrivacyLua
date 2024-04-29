package eu.faircode.xlua.ui;

import android.content.Context;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import eu.faircode.xlua.R;
import eu.faircode.xlua.utilities.PrefUtil;

public class HookWarnings {
    private static final List<String> WARNING_GROUPS = Arrays.asList("Hide.Environment", "Intercept.Shell", "Spoof.Embed.Paypal", "Spoof.GSM.Status", "Spoof.UserAgent", "Spoof.SOC.EX", "Intercept.Properties", "Spoof.Language", "Spoof.VPN.State", "Spoof.Features", "Get.Location", "Spoof.Location", "Get.App");
    private static final HashMap<String, Boolean> localPrefs = new HashMap<>();
    public static boolean hasWarning(Context context, String group) {
        if(!WARNING_GROUPS.contains(group)) return false;
        Boolean show = localPrefs.get(group);
        if(show == null) {
            show = PrefUtil.getBoolean(context, "warning_" + group.hashCode() + "_show", true, true);
            localPrefs.put(group, show);
        }

        return show;
    }

    public static void setWarnFlag(Context context, String group, boolean warn) {
        if(WARNING_GROUPS.contains(group))
            PrefUtil.setBoolean(context,"warning_" + group.hashCode() + "_show" , warn);
    }

    public static String getWarningMessage(Context context, String group) {
        Boolean show = localPrefs.get(group);
        if(show != null || WARNING_GROUPS.contains(group)) {
            if(show == null) {
                show = PrefUtil.getBoolean(context, "warning_" + group.hashCode() + "_show", true, true);
                localPrefs.put(group, show);
            }

            if(show) {
                switch (group) {
                    case "Spoof.UserAgent":
                        return context.getString(R.string.warning_hook_user_agent);
                    case "Spoof.SOC.EX":
                        return context.getString(R.string.warning_hook_soc_ex);
                    case "Intercept.Properties":
                        return context.getString(R.string.warning_hook_properties);
                    case "Spoof.Language":
                        return context.getString(R.string.warning_hook_language);
                    case "Spoof.VPN.State":
                        return context.getString(R.string.warning_hook_vpn_state);
                    case "Spoof.Features":
                        return context.getString(R.string.warning_hook_spoof_features);
                    case "Get.Location":
                    case "Spoof.Location":
                        return context.getString(R.string.warning_hook_get_location);
                    case "Get.App":
                        return context.getString(R.string.warning_hook_get_app);
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
