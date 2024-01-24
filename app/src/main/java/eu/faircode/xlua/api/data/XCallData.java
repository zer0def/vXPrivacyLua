package eu.faircode.xlua.api.data;

import android.content.Context;
import android.os.Bundle;

import eu.faircode.xlua.XDataBase;

/*public class XCallData {
    //list of all handler here as well and invoke them ???????

    private Context context;
    private String method;
    private Bundle extras;
    private XDataBase db;
    private boolean authSuccessful;

    public XCallData(Context context,Bundle extras, XDataBase db) {
        this.context = context;
        this.extras = extras;
        this.db = db;
    }

    public XCallData(Context context, String method, Bundle extras, XDataBase db) {
        this.context = context;
        this.method = method;
        this.extras = extras;
        this.db = db;
    }

    public Context getContext() { return context; }
    public String getMethod() { return method; }
    public Bundle getExtras() { return extras; }
    public XDataBase getDatabase() { return db; }

    public void setAuthSuccessful(boolean authSuccessful) {
        this.authSuccessful = authSuccessful;
    }

    public <T extends ICallCommand> T read(Class<T> obj) throws IllegalAccessException, InstantiationException {
        T inst = obj.newInstance();
        inst.fromBundle(extras);
        return inst;
    }

    public static XCallData create(Context context, Bundle extras, XDataBase db) {
        return new XCallData(context, extras, db);
    }

    public static XCallData create(Context context, String method, Bundle extras, XDataBase db) {
        return new XCallData(context, method, extras, db);
    }
}*/
