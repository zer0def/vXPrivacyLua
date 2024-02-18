package eu.faircode.xlua.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.R;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.utilities.StringUtil;

public class SettingDeleteDialog extends AppCompatDialogFragment {
    private static final String TAG = "XLua.SettingDeleteDialog";

    private LuaSettingExtended setting;
    private AppGeneric application;

    private ISettingDialogListener listener;

    private CheckBox cbDeleteSetting;
    private CheckBox cbDeleteDefaultMap;
    private CheckBox getCbDeleteSettingForceKill;


    public SettingDeleteDialog(LuaSettingExtended setting, AppGeneric app) {
        setSetting(setting);
        setApplication(app);
    }

    public void setApplication(AppGeneric application) { if(application != null) this.application = application; }
    public void setSetting(LuaSettingExtended setting) { if(setting != null) this.setting = setting; }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.settingdelete, null);

        cbDeleteSetting = view.findViewById(R.id.cbDeleteSettingDeleteSetting);
        cbDeleteDefaultMap = view.findViewById(R.id.cbDeleteSettingDeleteDefault);
        getCbDeleteSettingForceKill = view.findViewById(R.id.cbDeleteSettingForceKill);

        builder.setView(view)
                .setTitle("Setting Deleter")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Add Setting Dialog Was Cancelled");
                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(application == null)
                            application = AppGeneric.DEFAULT;

                        boolean deleteSetting = cbDeleteSetting.isChecked();
                        boolean deleteDefault = StringUtil.isValidString(setting.getDescription()) && cbDeleteDefaultMap.isChecked();
                        boolean forceKill = getCbDeleteSettingForceKill.isChecked();
                        if(!deleteSetting && !deleteDefault)
                            return;

                        LuaSettingPacket packet = setting.createPacket(LuaSettingPacket.getCodeForDeletion(deleteSetting, deleteDefault), forceKill);
                        packet.identificationFromApplication(application);
                        Log.i(TAG, "Delete packet=" + packet);
                        listener.pushSettingPacket(packet);
                    }
                });

        return builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ISettingDialogListener) context;
        }catch (Exception e) {
            Log.e(TAG, "onAttach Error: " + e + "\n" + Log.getStackTraceString(e));
        }
    }
}
