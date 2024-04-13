package eu.faircode.xlua.random.randomizers;

import androidx.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import eu.faircode.xlua.random.IRandomizer;
import eu.faircode.xlua.random.elements.ISpinnerElement;
import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomKernelSysName implements IRandomizer {
    public static final String[] SYS_NAMES = new String[] { "Linux", "Unix", "BSD", "XNU", "random" };

    @Override
    public boolean isSetting(String setting) { return setting.equalsIgnoreCase(getSettingName()); }

    @Override
    public String getSettingName() {  return "android.kernel.sys.name"; }

    @Override
    public String getName() {
        return "Kernel Sys Name";
    }

    @Override
    public String getID() { return "%kernel_sys_name%"; }

    @Override
    public String generateString() {
        String nodeName = SYS_NAMES[ThreadLocalRandom.current().nextInt(0, SYS_NAMES.length)];
        if ("random".equals(nodeName))
            return RandomStringGenerator.generateRandomAlphanumericString(ThreadLocalRandom.current().nextInt(5, 15), RandomStringGenerator.LOWER_LETTERS);
        return nodeName;
    }

    @Override
    public int generateInteger() { return 0; }

    @Override
    public List<ISpinnerElement> getOptions() { return null; }

    @NonNull
    @Override
    public String toString() { return getName(); }
}
