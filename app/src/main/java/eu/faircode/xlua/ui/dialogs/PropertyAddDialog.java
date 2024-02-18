package eu.faircode.xlua.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import eu.faircode.xlua.R;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.utilities.StringUtil;

public class PropertyAddDialog extends AppCompatDialogFragment  {
    private static final String TAG = "XLua.PropertyAddDialog";
    private EditText edSettingName;
    private EditText edPropertyName;

    private IPropertyDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.propadd, null);

        edSettingName = view.findViewById(R.id.etPropSettingAddSettingName);
        edPropertyName = view.findViewById(R.id.etPropSettingAddName);

        builder.setView(view)
                .setTitle("Property Map Builder")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "Add Property Dialog Was Cancelled");
                    }
                }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String propertyName = edPropertyName.getText().toString();
                        String settingName = edSettingName.getText().toString();
                        if(!StringUtil.isValidString(settingName) || !StringUtil.isValidString(propertyName))
                            return;

                        MockPropPacket packet = MockPropPacket.create(propertyName, settingName, null, MockPropPacket.CODE_INSERT_UPDATE_PROP_MAP);
                        Log.i(TAG, "Finishing Packet Build=" + packet);
                        listener.pushMockPropPacket(packet);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try { listener = (IPropertyDialogListener) context;
        }catch (Exception e) {
            Log.e(TAG, "onAttach Error: " + e + "\n" + Log.getStackTraceString(e));
        }
    }
}
