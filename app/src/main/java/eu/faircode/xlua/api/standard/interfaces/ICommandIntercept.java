package eu.faircode.xlua.api.standard.interfaces;

import java.util.List;

import eu.faircode.xlua.interceptors.UserContextMaps;
import eu.faircode.xlua.interceptors.shell.ShellInterceptionResult;

public interface ICommandIntercept {
    boolean interceptCommand(ShellInterceptionResult result);
    String getCommand();
    boolean containsCommand(String input);
    boolean containsCommand(List<String> commands);
    boolean keepGoing(UserContextMaps maps, String key);
}
