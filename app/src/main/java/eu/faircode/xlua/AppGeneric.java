package eu.faircode.xlua;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.api.app.XLuaApp;
import eu.faircode.xlua.api.hook.LuaHooksGroup;
import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.xlua.XLuaCall;
import eu.faircode.xlua.api.xstandard.UserIdentityPacket;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.utilities.StringUtil;

public class AppGeneric {
    public static final AppGeneric DEFAULT = new AppGeneric(null, null);
    private static final String TAG = "XLua.AppGeneric";

    private int icon;
    private int uid;
    private String name;
    private String packageName;
    private boolean forceStop;

    public static AppGeneric from(Bundle b, Context context) {
        if(b == null || !b.containsKey("packageName")) {
            Log.i(TAG, "App From NULL (using global)");
            return DEFAULT;
            //return new AppGeneric(null, context);
        }

        String pName = b.getString("packageName");
        Log.i(TAG, "pkg=" + pName);
        return new AppGeneric(pName, context);
    }

    public AppGeneric(String packageName, Context context) {
        if(context == null || !StringUtil.isValidString(packageName) ||  packageName.equalsIgnoreCase("global")) {
            this.name = UserIdentityPacket.GLOBAL_NAMESPACE;
            this.packageName = UserIdentityPacket.GLOBAL_NAMESPACE;
            this.uid = 0;
            this.icon = -1;
        }else {
            try {
                this.packageName = packageName;
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
                this.icon = appInfo.icon;
                this.uid = appInfo.uid;
                this.name = (String) packageManager.getApplicationLabel(appInfo);
                //this.name = appInfo.loadLabel(packageManager);
                //Drawable appIcon = pm.getApplicationIcon(appInfo);
            }catch (Exception e) {
                Log.e(TAG, "Failed to grab Application Info: " + packageName + " " + e);
            }
        }
    }

    public int getIcon() { return icon; }
    public int getUid() { return uid; }
    public String getName() { return name; }
    public String getPackageName() { return packageName; }

    public boolean getForceStop() { return !isGlobal() && this.forceStop; }
    public void setForceStop(boolean forceStop) { this.forceStop = forceStop; }

    public void initView(Context context, View view, int appIcon, int textViewPackageName, int textViewPackageNameFull, int textViewPackageUid) {
        try {
            Log.i(TAG, "Initializing View [" + view.getId() + "]");
            initIcon((ImageView) view.findViewById(appIcon), context);

            TextView tvPkgName = view.findViewById(textViewPackageName);
            TextView tvPkgNameFull = view.findViewById(textViewPackageNameFull);
            TextView tvPkgUid = view.findViewById(textViewPackageUid);

            tvPkgName.setText(getName());
            tvPkgNameFull.setText(getPackageName());
            tvPkgUid.setText(String.valueOf(getUid()));
            Log.i(TAG, "Finished Initialized View [" + view.getId() + "] [pkg=" + getName() + " pkg full=" + getPackageName() + " uid=" + getUid() + "]");
        }catch (Exception e) {
            Log.e(TAG, "Failed to initView [" + e + "] (ai=" + appIcon + " tvpn=" + textViewPackageName + " tvpnf=" + textViewPackageNameFull + " tvpkgu=" + textViewPackageUid + ")");
        }
    }

    public void initIcon(ImageView ivAppIcon, Context context) {
        Log.i(TAG, "Setting icon s=" + toString());
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.listPreferredItemHeight, typedValue, true);
        int height = TypedValue.complexToDimensionPixelSize(typedValue.data, context.getResources().getDisplayMetrics());
        int iconSize = Math.round(height * context.getResources().getDisplayMetrics().density + 0.5f);

        Log.i(TAG, "ICON HEIGHT=" + height + " SIZE=" + iconSize);

        // App icon
        try {
            if (icon <= 0)
                ivAppIcon.setImageResource(android.R.drawable.sym_def_app_icon);
            else {
                Log.i(TAG, "Setting with glid app");
                Uri uri = Uri.parse("android.resource://" + packageName + "/" + icon);
                GlideApp.with(context)
                        .applyDefaultRequestOptions(new RequestOptions().format(DecodeFormat.PREFER_RGB_565))
                        .load(uri)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .override(iconSize, iconSize)
                        .into(ivAppIcon);
            }
        }catch (Exception e) {
            Log.e(TAG, "Failed to set AppIcon: " + packageName + " " + e);
        }
    }

    public boolean isGlobal() {
        if(!StringUtil.isValidString(packageName))
            return false;

        return packageName.equalsIgnoreCase("global");
    }

    @NonNull
    @Override
    public String toString() {
        return "pkg=" + packageName + " uid=" + uid + " name=" + name + " ico=" + icon;
    }
}
