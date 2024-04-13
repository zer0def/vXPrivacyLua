package eu.faircode.xlua.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import eu.faircode.xlua.R;
import eu.faircode.xlua.api.configs.MockConfig;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.ui.ConfigQue;
import eu.faircode.xlua.ui.interfaces.IConfigUpdate;

public class ConfigDeleteDialog extends AppCompatDialogFragment {
    //    <string name="msg_delete_config" translatable="false">Do you really want ot delete this Config ?</string>

    private MockConfig config;
    private IConfigUpdate onCallback;

    private Context context;
    private ConfigQue que;
    private int adapterPosition;


    public ConfigDeleteDialog setAdapterPosition(int adapterPosition) { this.adapterPosition = adapterPosition; return this; }
    public ConfigDeleteDialog setConfig(MockConfig config) { this.config = config; return this;  }
    public ConfigDeleteDialog setCallback(IConfigUpdate onCallback) { this.onCallback = onCallback; return this; }
    public ConfigDeleteDialog setQue(ConfigQue que) { this.que = que; return this; }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.configdelete, null);
        builder.setView(view)
                .setTitle(R.string.title_delete_config)
                .setNegativeButton(R.string.option_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { XLog.i("Configs Deletion Was cancelled!");}})
                .setPositiveButton(R.string.option_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(que != null && config != null) {
                            que.sendConfig(
                                    getContext(),
                                    -1,
                                    config,
                                    true,
                                    false,
                                    onCallback);
                        }
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
