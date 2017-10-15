package com.wangwenjun.guava.io;

import com.google.common.base.Preconditions;

/***************************************
 * @author:Alex Wang
 * @Date:2017/10/15
 * @QQ: 532500648
 ***************************************/
public final class Base64 {

    private final static String CODE_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

    private final static char[] CODE_DICT = CODE_STRING.toCharArray();

    private Base64() {
    }

    public static String encode(String plain) {
        Preconditions.checkNotNull(plain);
        StringBuilder result = new StringBuilder();
        String binaryString = toBinary(plain);

        int delta = 6 - binaryString.length() % 6;//should append

        for (int i = 0; i < delta && delta != 6; i++) {
            binaryString += "0";
        }

        for (int index = 0; index < binaryString.length() / 6; index++) {
            int begin = index * 6;
            String encodeString = binaryString.substring(begin, begin + 6);
            char encodeChar = CODE_DICT[Integer.valueOf(encodeString, 2)];
            result.append(encodeChar);
        }

        if (delta != 6) {
            for (int i = 0; i < delta / 2; i++) {
                result.append("=");
            }
        }

        return result.toString();
    }

    private static String toBinary(final String source) {
        final StringBuilder binaryResult = new StringBuilder();
        for (int index = 0; index < source.length(); index++) {
            String charBin = Integer.toBinaryString(source.charAt(index));
            int delta = 8 - charBin.length();
            for (int d = 0; d < delta; d++) {
                charBin = "0" + charBin;
            }

            binaryResult.append(charBin);
        }

        return binaryResult.toString();
    }


}
