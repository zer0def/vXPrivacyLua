package eu.faircode.xlua.api.standard.interfaces;

public interface IDividerKind {
    String getDividerID(int position);
    String getLongID(int position);
    boolean isSearching();
    boolean hasChanged();
    void resetHashChanged();
}
