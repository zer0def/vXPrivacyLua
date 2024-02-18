package eu.faircode.xlua.api.xmock.call;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.xlua.provider.XLuaAppProvider;

public class KillAppCommand extends CallCommandHandler {
    public static KillAppCommand create() { return new KillAppCommand(); };
    public KillAppCommand() {
        name = "killApp";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        LuaSettingPacket packet = commandData.readFullPackFrom(LuaSettingPacket.class);
        XResult res = XResult.create().setMethodName("killApp").setExtra(packet.toString());
        return res.setResult(XLuaAppProvider.forceStop(commandData.getContext(), packet.getCategory(), XUtil.getUserId(packet.getUser()), res)).toBundle();
    }

    public static XResult invokeEx(Context context, String packageName, int uid) {
        LuaSettingPacket packet = new LuaSettingPacket();
        packet.setCategory(packageName);
        packet.setUser(uid);
        return XResult.from(invoke(context, packet));
    }

    public static Bundle invoke(Context context, LuaSettingPacket packet) {
        return XProxyContent.mockCall(
                context,
                "killApp",
                packet.toBundle());
    }
}

