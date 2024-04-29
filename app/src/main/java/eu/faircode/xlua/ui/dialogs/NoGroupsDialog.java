package eu.faircode.xlua.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import eu.faircode.xlua.R;
import eu.faircode.xlua.XUtil;

public class NoGroupsDialog extends AppCompatDialogFragment {
    private Context context;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.uhohnogroups, null);
        builder.setView(view)
                .setTitle(R.string.title_no_groups)
                .setPositiveButton(R.string.option_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            PackageManager pm = Objects.requireNonNull(getActivity()).getPackageManager();
                            Intent companion = pm.getLaunchIntentForPackage(XUtil.PRO_PACKAGE_NAME);
                            if (companion == null) {
                                Intent browse = new Intent(Intent.ACTION_VIEW);
                                browse.setData(Uri.parse("https://lua.xprivacy.eu/pro/"));
                                if (browse.resolveActivity(pm) == null) Toast.makeText(context, R.string.msg_no_browser, Toast.LENGTH_LONG).show();
                                else startActivity(browse);
                            } else startActivity(companion);
                        }catch (Exception e) { }
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) { super.onAttach(context); this.context = context; }
}
