package eu.faircode.xlua.interceptors.shell;

import androidx.annotation.NonNull;

import java.util.List;

import eu.faircode.xlua.api.standard.interfaces.ICommandIntercept;
import eu.faircode.xlua.interceptors.UserContextMaps;
import eu.faircode.xlua.utilities.StringUtil;

public abstract class CommandInterceptor implements ICommandIntercept {
    protected String command;
    public CommandInterceptor() { }

    @Override
    public  boolean keepGoing(UserContextMaps maps, String key) {  return StringUtil.toBoolean(maps.getSetting(key, "true"), true); }
    @Override
    public boolean interceptCommand(ShellInterceptionResult result) { return false; }
    @Override
    public String getCommand() { return this.command; }
    @Override
    public boolean containsCommand(String input) { return input != null && input.toLowerCase().contains(command.toLowerCase()); }
    @Override
    public boolean containsCommand(List<String> commands) {
        for(String s : commands) {
            String sLow = s.toLowerCase();
            if(sLow.contains(this.command))
                return true;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() { return this.command; }
}
