package eu.faircode.xlua.api.xlua.xcall;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.objects.CallCommandHandler;
import eu.faircode.xlua.api.objects.CallPacket;
import eu.faircode.xlua.api.xlua.XAppProvider;
import eu.faircode.xlua.utilities.BundleUtil;

public class GetVersionCommand extends CallCommandHandler {
    public static GetVersionCommand create() { return new GetVersionCommand(); };
    public GetVersionCommand() {
        name = "getVersion";
        requiresPermissionCheck = false;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        int res = XAppProvider.getVersion(commandData.getContext());
        if(res == -55)
            return null;

        return BundleUtil.
                createSingleInt("version", res);
    }

    public static Bundle invoke(Context context) {
        return XProxyContent.luaCall(
                context,
                "getVersion");
    }
}
