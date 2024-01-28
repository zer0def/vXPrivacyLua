package eu.faircode.xlua.randomizers;

import eu.faircode.xlua.utilities.RandomStringGenerator;

public class RandomGSF implements IRandomizer {
    @Override
    public String generateString() {
        return RandomStringGenerator.generateRandomAlphanumericString(16, RandomStringGenerator.LOWER_LETTERS);
    }

    @Override
    public int generateInteger() { return 0; }
}
