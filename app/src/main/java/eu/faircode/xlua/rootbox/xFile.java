package eu.faircode.xlua.rootbox;

import java.io.File;
import java.io.FileDescriptor;

public class xFile {
    private File file;

    public String getPath() { return file.getPath(); }
    public String getName() { return file.getName(); }
    public String getAbsolutePath() { return file.getAbsolutePath(); }

    public int getDescriptorRaw() {
        return 0;
    }

    public FileDescriptor getDescriptor() {

        return null;
    }

    public boolean isOpen() {
        return false;
    }
}
