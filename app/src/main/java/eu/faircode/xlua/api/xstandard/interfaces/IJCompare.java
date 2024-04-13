package eu.faircode.xlua.api.xstandard.interfaces;

public interface IJCompare extends IJsonSerial {
    boolean equalsPartner(Object obj);
    boolean equalsPartnerContents(Object obj);
}
