package eu.faircode.xlua.randomizers;

import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomIMEI implements IRandomizer {
    @Override
    public String generateString() {
        return RandomStringGenerator.generateRandomNumberString(15);
    }

    @Override
    public int generateInteger() { return 0; }
}
