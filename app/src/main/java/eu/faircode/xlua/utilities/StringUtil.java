package eu.faircode.xlua.utilities;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class StringUtil {

    public static boolean isValidString(String str) {
        return str != null && !str.equals(" ") && !str.isEmpty();
    }

    public static String random(int minLen, int maxLen) {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        //int randomLength = generator.nextInt(minLen, maxLen);
        int randomLength = ThreadLocalRandom.current().nextInt(minLen, maxLen);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
