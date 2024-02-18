package eu.faircode.xlua.interceptors.shell;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import eu.faircode.xlua.interceptors.UserContextMaps;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.ShellUtils;
import eu.faircode.xlua.utilities.StringUtil;

public class ShellInterceptionResult {
    public static ShellInterceptionResult create(ProcessBuilder pb) { return new ShellInterceptionResult(pb); }
    public static ShellInterceptionResult create(ProcessBuilder pb, UserContextMaps maps) { return new ShellInterceptionResult(pb).setUserMaps(maps); }
    public static ShellInterceptionResult create(String value) { return new ShellInterceptionResult(value); }
    public static ShellInterceptionResult create(String value, UserContextMaps maps) { return new ShellInterceptionResult(value).setUserMaps(maps); }
    public static ShellInterceptionResult create(String[] value) { return new ShellInterceptionResult(value); }
    public static ShellInterceptionResult create(String[] value, UserContextMaps maps) { return new ShellInterceptionResult(value).setUserMaps(maps); }
    public static ShellInterceptionResult create(List<String> value) { return new ShellInterceptionResult(value); }
    public static ShellInterceptionResult create(List<String> value, UserContextMaps maps) { return new ShellInterceptionResult(value).setUserMaps(maps); }

    private UserContextMaps _userContextMaps;
    public ShellInterceptionResult setUserMaps(UserContextMaps userMaps) { this._userContextMaps = userMaps; return this; }
    public UserContextMaps getUserMaps() { return this._userContextMaps; }

    private String _originalValueLine = null;
    private List<String> _cleanedValue = null;

    private boolean malicious = false;
    private List<String> originalValue;
    private String newValue;

    public ShellInterceptionResult() { }
    public ShellInterceptionResult(String originalValue) { this(Collections.singletonList(originalValue)); }
    public ShellInterceptionResult(String[] originalValue) { this(new ArrayList<>(Arrays.asList(originalValue))); }
    public ShellInterceptionResult(List<String> originalValue) { if(!CollectionUtil.isEmptyValuesOrInvalid(originalValue)) { this.originalValue = originalValue; } }
    public ShellInterceptionResult(ProcessBuilder pb) {
        if(pb != null) {
            List<String> args = pb.command();
            if(!CollectionUtil.isEmptyValuesOrInvalid(args)) {
                setOriginalValue(args);
            }
        }
    }

    public boolean isCleanValueValid() { return !CollectionUtil.isEmptyValuesOrInvalid(this._cleanedValue); }
    public boolean isValueValid() { return !CollectionUtil.isEmptyValuesOrInvalid(this.originalValue); }

    public void setOriginalValue(String value) { this.originalValue = Collections.singletonList(value); }
    public void setOriginalValue(String[] value) { this.originalValue = new ArrayList<>(Arrays.asList(value)); }
    public void setOriginalValue(List<String> value) { this.originalValue = new ArrayList<>(value); }
    public String getOriginalValue() { if(this._originalValueLine == null && this.originalValue != null) this._originalValueLine = TextUtils.join(" ", this.originalValue).trim(); return this._originalValueLine; }
    public List<String> getOriginalValueAsList() { return this.originalValue; }

    public boolean isMalicious() { return this.malicious; }
    public void setIsMalicious(boolean isMalicious) { this.malicious = isMalicious; }

    public String getNewValue() { return this.newValue; }
    public List<String> getNewValueAsList() { return Collections.singletonList(this.newValue); }
    public void setNewValue(String newValue) {this.newValue = newValue; }

    public void setSanitizedList(List<String> value) { this._cleanedValue = value; }
    public List<String> customSanitizedList(boolean useContains, String... badStrings) { return CollectionUtil.getVerifiedStrings(getSanitizedList(), useContains, badStrings); }
    public List<String> getSanitizedList() {
        if(this.originalValue == null) return new ArrayList<>();
        if(this._cleanedValue == null) this._cleanedValue = StringUtil.breakStringListExtreme(this.originalValue);
        return this._cleanedValue;
    }

    public Process getEchoProcess() { return ShellUtils.echo(this.getNewValue()); }
    public ProcessBuilder getEchoProcessBuilder() { return ShellUtils.getEchoProcessBuilder(this.getNewValue()); }
    public void setEchoForProcessBuilder(ProcessBuilder pb) {
        String[] cmdline = { "sh", "-c", "echo " + this.getNewValue() };
        pb.command(cmdline);
    }

    @NonNull
    @Override
    public String toString() {
        return new StringBuilder()
                .append("Is Malicious: ").append(this.isMalicious()).append("\n")
                .append("Original Value: ").append(this.getOriginalValue()).append("\n")
                .append("New Value: ").append(this.getNewValue()).append("\n")
                .toString();
    }
}
