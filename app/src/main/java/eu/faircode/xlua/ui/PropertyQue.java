package eu.faircode.xlua.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import eu.faircode.xlua.AppGeneric;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropSetting;
import eu.faircode.xlua.api.xmock.XMockCall;
import eu.faircode.xlua.logger.XLog;
import eu.faircode.xlua.ui.interfaces.IPropertyUpdate;
import eu.faircode.xlua.ui.transactions.PropTransactionResult;

public class PropertyQue {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Object lock = new Object();
    private final AppGeneric application;
    private final List<PropTransactionResult> results = new ArrayList<>();

    public PropertyQue(AppGeneric application) { this.application = application; }

    public void clearResults() { this.results.clear(); }
    public List<PropTransactionResult> getResults() { return this.results; }
    public PropTransactionResult getResultAt(int index) { return this.results.get(index); }
    public PropTransactionResult getResultFromId(int id) {
        for(PropTransactionResult res : this.results) if(res.id == id) return res;
        return null;
    }

    public void addPropertyMap(
            final Context context,
            final String propertyName,
            final String settingName,
            final IPropertyUpdate onCallback) {
        final int code = MockPropPacket.CODE_INSERT_UPDATE_PROP_MAP;
        final MockPropPacket packet = MockPropPacket.create(propertyName, settingName, null, code);
        final MockPropSetting setting = MockPropSetting.create(propertyName, settingName, null);
        try {
            XLog.i("Mock Prop Packet created: Property packet=" + packet);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        final XResult ret = XMockCall.putMockProp(context, packet);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                PropTransactionResult result = new PropTransactionResult();
                                result.context = context;
                                result.id = settingName.hashCode();
                                result.result = ret;
                                result.settings.add(setting);
                                result.adapterPosition = -1;
                                result.code = code;
                                result.packets.add(packet);
                                if(ret.succeeded()) result.succeeded.add(setting);
                                else if(ret.failed()) result.failed.add(setting);
                                results.add(result);
                                if(onCallback != null) onCallback.onPropertyUpdate(result);
                            }
                        });
                    }
                }
            });
        }catch (Exception e) {
            XLog.e("Failed to Add Property: property=" + propertyName + " setting=" + settingName, e, true);
            PropTransactionResult result = new PropTransactionResult();
            result.context = context;
            result.id = settingName.hashCode();
            result.result = XResult.create().setFailed("Failed to send property error!");
            result.settings.add(setting);
            result.adapterPosition = -1;
            result.code = code;
            result.packets.add(packet);
            result.failed.add(setting);
            results.add(result);
            try { if(onCallback != null) onCallback.onPropertyUpdate(result);
            }catch (Exception ex) {  XLog.e("Failed to execute Callback! ", e, true); }
        }
    }

    public void sendPropertySetting(
            final Context context,
            final MockPropSetting setting,
            final  int adapterPosition,
            final  int valueSet,
            final boolean hardDelete,
            final IPropertyUpdate onCallback) {

        final int packetCode = MockPropPacket.getPacketCodeForSetting(valueSet, hardDelete);
        final MockPropPacket packet =
                MockPropPacket.create(application, setting.getName(), setting.getSettingName(), valueSet, packetCode);

        try {
            XLog.i("Mock Prop Packed created: Property packet=" + packet);
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    synchronized (lock) {
                        final XResult ret = XMockCall.putMockProp(context, packet);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                PropTransactionResult result = new PropTransactionResult();
                                result.context = context;
                                result.id = setting.getName().hashCode();
                                result.result = ret;
                                result.settings.add(setting);
                                result.adapterPosition = adapterPosition;
                                result.code = packetCode;
                                result.packets.add(packet);
                                if(ret.succeeded()) result.succeeded.add(setting);
                                else if(ret.failed()) result.failed.add(setting);
                                results.add(result);
                                if(onCallback != null) onCallback.onPropertyUpdate(result);
                            }
                        });
                    }
                }
            });
        }catch (Exception e) {
            XLog.e("Failed to Add Property: property=" + setting.getName() + " setting=" + setting.getSettingName(), e, true);
            PropTransactionResult result = new PropTransactionResult();
            result.context = context;
            result.id = setting.getName().hashCode();
            result.result = XResult.create().setFailed("Failed to send property error!");
            result.settings.add(setting);
            result.adapterPosition = -1;
            result.code = packetCode;
            result.packets.add(packet);
            result.failed.add(setting);
            results.add(result);
            try { if(onCallback != null) onCallback.onPropertyUpdate(result);
            }catch (Exception ex) {  XLog.e("Failed to execute Callback! ", e, true); }
        }
    }
}
