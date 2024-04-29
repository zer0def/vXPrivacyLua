package eu.faircode.xlua.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import eu.faircode.xlua.R;
import eu.faircode.xlua.Str;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.configs.MockConfig;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.ui.interfaces.IConfigUpdate;
import eu.faircode.xlua.ui.transactions.ConfigTransactionResult;

public class RenameDialogEx extends AppCompatDialogFragment {
    private EditText renamedConfig;
    private TextView tvConfigOldName;
    private MockConfig config;
    private IConfigUpdate callback;

    public RenameDialogEx setConfig(MockConfig config) { this.config = config; return this; }
    public RenameDialogEx setCallback(IConfigUpdate callback) { this.callback = callback; return this; }


    private Context context;

    public RenameDialogEx() { }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.configrename, null);

        renamedConfig = view.findViewById(R.id.etConfigName);
        tvConfigOldName = view.findViewById(R.id.tvOldName);

        tvConfigOldName.setText(config.getName());

        builder.setView(view)
                .setTitle(getString(R.string.title_config_rename_config))
                .setNegativeButton(R.string.option_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Log.i(TAG, "Add Setting Dialog Was Cancelled");
                    }
                }).setPositiveButton(R.string.option_rename, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(Str.isValidNotWhitespaces(renamedConfig.getText())) {
                            config.setName(renamedConfig.getText().toString());
                            //Check if duplicate names
                            ConfigTransactionResult res = new ConfigTransactionResult();
                            res.code = -1;
                            res.configs.add(config);
                            res.succeeded.add(config);
                            res.result = XResult.create().setSucceeded("Renamed Config");
                            callback.onConfigUpdate(res);
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
