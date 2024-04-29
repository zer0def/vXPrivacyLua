package eu.faircode.xlua.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.R;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.xlua.call.ClearSettingsCommand;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.ui.interfaces.ISettingsReset;

public class SettingsResetDialog extends AppCompatDialogFragment {
    private AppGeneric application;
    private Context context;
    private ISettingsReset callback;

    public SettingsResetDialog setApplication(AppGeneric application) { this.application = application; return this; }
    public SettingsResetDialog setCallback(ISettingsReset callback) { this.callback = callback; return this; }


    public SettingsResetDialog() { }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.warningresetsettings, null);

        builder.setView(view)
                .setTitle(R.string.title_settings_reset)
                .setNegativeButton(R.string.option_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { XLog.i("Setting Delete was cancelled!"); }
                })
                .setPositiveButton(R.string.option_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            XResult res = ClearSettingsCommand.invoke(context, application.getPackageName());
                            if(callback != null) callback.onFinish(res);
                        }catch (Exception e) {
                            XLog.e("Failed to Batch Delete Settings!", e, true);
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
