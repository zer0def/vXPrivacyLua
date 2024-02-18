package eu.faircode.xlua.utilities;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomStringGenerator {
    // Define character sets for numbers and letters
    public static final String NUMBERS = "0123456789";
    public static final String UPPER_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_LETTERS = "abcdefghijklmnopqrstuvwxyz";

    // Generate a random string with only numbers
    public static String generateRandomNumberString(int length) {
        return generateRandomString(NUMBERS, length);
    }

    // Generate a random string with numbers and letters
    public static String generateRandomAlphanumericString(int length) {
        String characters = NUMBERS + UPPER_LETTERS + LOWER_LETTERS;
        return generateRandomString(characters, length);
    }

    public static String generateRandomAlphanumericString(int length, String letters) {
        String characters = NUMBERS + letters;
        return generateRandomString(characters, length);
    }

    // Generate a random string with only letters
    public static String generateRandomLetterString(int length, String letters) {
        return generateRandomString(letters, length);
    }

    // Helper method to generate a random string from a given character set
    private static String generateRandomString(String characters, int length) {
        //            String randomName = allMapNames.get(ThreadLocalRandom.current().nextInt(0, allMapNames.size()));
        if (length <= 0)
            throw new IllegalArgumentException("Length must be greater than 0");

        //Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            //int index = random.nextInt(characters.length());
            int index = ThreadLocalRandom.current().nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }
}
