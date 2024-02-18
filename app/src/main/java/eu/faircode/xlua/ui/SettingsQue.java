package eu.faircode.xlua.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.settings.LuaSettingExtended;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.standard.interfaces.ISettingUpdate;
import eu.faircode.xlua.api.xlua.XLuaCall;

public class SettingsQue {
    private static final String TAG = "XLua.SettingsQue";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();
    private final AppGeneric application;

    public SettingsQue(AppGeneric application) { this.application = application; }

    public void batchUpdate(final Context context, final List<LuaSettingExtended> settings, final boolean delete, final ISettingUpdate onResult) {
        for(LuaSettingExtended sm : settings)
            sm.setIsBusy(true);

        executor.submit(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    final List<LuaSettingExtended> successful = new ArrayList<>();
                    final List<LuaSettingExtended> failed = new ArrayList<>();
                    for(final LuaSettingExtended s : settings) {
                        final LuaSettingPacket packet = createUpdatePacket(s, delete, false);
                        try {
                            final XResult ret = XLuaCall.sendSetting(context, packet);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    if(ret.failed()) {
                                        failed.add(s);
                                        if(onResult != null) onResult.onSettingUpdateFailed(context, s, ret);
                                    }else {
                                        successful.add(s);
                                        if(delete) {
                                            s.setValueForce(null);
                                            s.resetModified(true);
                                        }
                                        if(onResult != null) onResult.onSettingUpdatedSuccessfully(context, s, ret);
                                    }
                                }
                            });
                        }catch (final Exception e) {
                            Log.e(TAG, "Failed to update setting setting=" + s + " e=" + e);
                            if(onResult != null) onResult.onSettingUpdateFailed(context, s, XResult.create().setFailed(e.getMessage()));
                            failed.add(s);
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    failed.add(s);
                                    if(onResult != null) onResult.onSettingUpdateFailed(context, s, XResult.create().setFailed(e.getMessage()));
                                }
                            });
                        }
                    }
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(onResult != null) onResult.onBatchFinished(context, successful, failed);
                        }
                    });
                }
            }
        });
    }


    public void sendSetting(final Context context, final  LuaSettingExtended setting, final boolean deleteSetting, final boolean forceKill) { sendSetting(context, setting, deleteSetting, forceKill); }
    public void sendSetting(final Context context, final  LuaSettingExtended setting, final boolean deleteSetting, final boolean forceKill, final ISettingUpdate onResult) {
        if(!deleteSetting && !setting.isModified()) {
            Log.w(TAG, "Make changes to the setting before 'sending' setting=" + setting);
            return;
        }

        try {
            executor.submit(new Runnable() {
                @Override
                public void run() { synchronized (lock) {
                    final LuaSettingPacket packet = createUpdatePacket(setting, deleteSetting, forceKill);
                    final XResult ret = XLuaCall.sendSetting(context, packet);
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            if(onResult != null) {
                                if(ret.succeeded()) onResult.onSettingUpdatedSuccessfully(context, setting, ret);
                                else onResult.onSettingUpdateFailed(context, setting, ret);
                            }
                        }
                    });
                }
                }
            });
        }catch (Exception e) {
            Log.e(TAG, "Failed to wait for Setting to apply , setting=" + setting);
        }
    }

    public LuaSettingPacket createUpdatePacket(LuaSettingExtended setting, boolean deleteSetting, boolean forceKill) {
        LuaSettingPacket packet =
                LuaSettingPacket.create(setting, LuaSettingPacket.getCodeInsertOrDelete(deleteSetting), forceKill)
                        .copyIdentification(application);

        if(!deleteSetting) packet.setValueForce(setting.getModifiedValue());
        else packet.setValueForce(null);
        return packet;
    }
}
