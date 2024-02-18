package eu.faircode.xlua.api.standard.interfaces;

public interface IExpandable {
    String getKey();
    boolean isExpanded();
    void setIsExpanded(boolean isExpanded);
}
