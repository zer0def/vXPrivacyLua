package eu.faircode.xlua.api.standard.interfaces;

public interface IPacket {
    int getSecretKey();
    void readSelectionArgs(String[] selection, int flags);
}
