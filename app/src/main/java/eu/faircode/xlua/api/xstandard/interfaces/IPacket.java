package eu.faircode.xlua.api.xstandard.interfaces;

public interface IPacket {
    int getSecretKey();
    void readSelectionArgs(String[] selection, int flags);
}
