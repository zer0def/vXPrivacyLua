package eu.faircode.xlua.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.R;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropSetting;

public class PropertyDeleteDialog extends AppCompatDialogFragment {
    private static final String TAG = "XLua.PropertyDeleteDialog";
    private MockPropSetting setting;
    private AppGeneric application;
    private IPropertyDialogListener listener;

    public void addSetting(MockPropSetting propertySetting) { this.setting = propertySetting; }
    public void addApplication(AppGeneric application) { this.application = application; }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.propdelete, null);

        builder.setView(view)
                .setTitle("Property Deleter")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Delete Property Dialog Was Cancelled");
                    }
                }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //setting.isNullOrEmptyCode()

                        MockPropPacket packet = MockPropPacket.create(
                                application.getUid(),
                                application.getPackageName(),
                                setting.getName(),
                                setting.getSettingName(),
                                null,
                                MockPropPacket.CODE_DELETE_PROP_MAP_AND_SETTING);

                        //CODE_DELETE_PROP_SETTING

                        Log.i(TAG, "Finishing Packet Build=" + packet);
                        listener.pushMockPropPacket(packet);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (IPropertyDialogListener) context;
        }catch (Exception e) {
            Log.e(TAG, "onAttach Error: " + e + "\n" + Log.getStackTraceString(e));
        }
    }
}
