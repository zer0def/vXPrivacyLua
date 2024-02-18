package eu.faircode.xlua.ui;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.R;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.settings.LuaSettingExtended;

public class AlertMessage {
    public static void displayMessageBatch(Context context, List<LuaSettingExtended> successful, List<LuaSettingExtended> failed, AppGeneric application) {
        String title = "Batch Settings Update Finished";
        StringBuilder sb = new StringBuilder()
                .append("successful count: ").append(successful.size())
                .append("\n")
                .append("failed count: ").append(failed.size());

        if(application != null) {
            sb.append("\n")
                    .append("name=").append(application.getName())
                    .append(" pkg=").append(application.getPackageName())
                    .append(" uid=").append(application.getUid());
        }

        if(failed.size() > 1) {
            sb.append("\n\n").append("failed:\n");
            for(LuaSettingExtended s : failed)
                sb.append(s.getName()).append("\n");
        }

        if(successful.size() > 1) {
            sb.append("\n\n").append("successful:\n");
            for(LuaSettingExtended s : successful)
                sb.append(s.getName()).append("\n");
        }

        displayMessage(context, title, sb.toString());
    }

    public static void displayMessageFailed(Context context, LuaSettingExtended setting, XResult result) {
        String title = "Failed: " + (setting == null ? "nil" : setting.getName());
        displayMessage(context, title, result == null ? "result: nil" : result.toString());
    }

    public static void displayMessageException(Context context, LuaSettingExtended setting, Exception e) {
        String title = "Ex: " + (setting == null ? "nil" : setting.getName());
        StringBuilder sb = new StringBuilder()
                .append("setting:\n")
                .append(setting == null ? "nil" : setting.toString())
                .append("\n")
                .append("Exception:\n")
                .append(e == null ? "nil" : e.getMessage())
                .append("\n")
                .append("stack:\n")
                .append(e == null ? "nil" : Log.getStackTraceString(e));

        displayMessage(context, title, sb.toString());
    }

    public static void displayMessage(Context context, String title, String message) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View alert = inflater.inflate(R.layout.exception, null, false);
        TextView tvException = alert.findViewById(R.id.tvException);
        StringBuilder sb = new StringBuilder()
                .append("<b>")
                .append(Html.escapeHtml(title))
                .append("</b><br><br>");

        for (String line : message.split("\n")) {
            sb.append(Html.escapeHtml(line));
            sb.append("<br>");
        }

        sb.append("<br>");
        tvException.setText(Html.fromHtml(sb.toString()));
        new AlertDialog.Builder(context)
                .setView(alert)
                .create()
                .show();
    }
}
