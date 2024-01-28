package eu.faircode.xlua.randomizers;

import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomDRM implements IRandomizer {
    @Override
    public String generateString() {
        //64
        return RandomStringGenerator.generateRandomAlphanumericString(64, RandomStringGenerator.LOWER_LETTERS);
    }

    @Override
    public int generateInteger() { return 0; }
}
