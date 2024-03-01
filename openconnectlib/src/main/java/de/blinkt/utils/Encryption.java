package de.blinkt.utils;

public class Encryption {

    public static String EncryptOrDecrypt(String input,String keys) {
        char[] key = keys.toCharArray();
        StringBuilder output = new StringBuilder();

        for(int i = 0; i < input.length(); i++) {
            output.append((char) (input.charAt(i) ^ key[i % key.length]));
        }
        return output.toString();
    }

}
