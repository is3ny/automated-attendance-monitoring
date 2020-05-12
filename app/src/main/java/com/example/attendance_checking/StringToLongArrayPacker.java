package com.example.attendance_checking;

import android.util.Log;

import java.util.Arrays;

public final class StringToLongArrayPacker {
    static Long[] toLongArray(String text) {
        char[] chars = text.toCharArray();
        Long[] pack = new Long[chars.length];

        for (int i = 0; i < chars.length; i++) {
            pack[i] = (long)Character.getNumericValue(chars[i]);
        }

        return pack;
    }

    static String toString(Long[] pack) {
        char[] chars = new char[pack.length];

        for (int i = 0; i < pack.length; i++) {
            chars[i] = (char) pack[i].intValue();
        }

        String ret = null;
        try {
            ret = Arrays.toString(chars);
        } catch (Exception e) {
            Log.e("String conv", e.toString());
        }

        return ret;
    }
}
