package me.lensferno.dogename.utils;

import java.security.SecureRandom;

public final class Random {

    private final java.util.Random random = new java.util.Random();
    private final java.util.Random secRandom = new SecureRandom();

    private boolean useSecureRandom = true;

    public void setUseSecureRandom(boolean useSecureRandom) {
        this.useSecureRandom = useSecureRandom;
    }

    public int getRandomNumber(int minNumber, int maxNumber) {
        if (useSecureRandom) {
            return minNumber + secRandom.nextInt(maxNumber - minNumber + 1);
        } else {
            return minNumber + random.nextInt(maxNumber - minNumber + 1);
        }
    }

    public int getRandomNumber(int maxNumber) {
        if (useSecureRandom) {
            return secRandom.nextInt(maxNumber + 1);
        } else {
            return random.nextInt(maxNumber + 1);
        }
    }

}
