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

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.R;
import eu.faircode.xlua.api.xmock.call.ClearAppDataCommand;
import eu.faircode.xlua.logger.XLog;

public class ClearAppDataDialog extends AppCompatDialogFragment {
    private AppGeneric application;
    private Context context;

    public ClearAppDataDialog setApplication(AppGeneric application) { this.application = application; return this; }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)  {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.clearappdata, null);
        builder.setView(view)
                .setTitle(R.string.title_delete_appdata)
                .setNegativeButton(R.string.option_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { XLog.i("Clear App Data action was Cancelled!");}})
                .setPositiveButton(R.string.option_clear, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClearAppDataCommand.invokeEx(context, application.getPackageName(), application.getUid());
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
