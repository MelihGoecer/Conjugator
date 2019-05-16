package com.translator_conjugator_dictionary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;

public class ConjugatorHelper {


    public static String getLangLong(String languageShort, String[] allLangsLong, String[] allLangsShort) {
        for (int i = 0; i < allLangsLong.length; i++) {
            if (allLangsShort[i].equals(languageShort))
                return allLangsLong[i];
        }
        return null;
    }

    public static String getLangShort(String languageLong, String[] allLangsLong, String[] allLangsShort) {
        for (int i = 0; i < allLangsShort.length; i++) {
            if (allLangsLong[i].toLowerCase().equals(languageLong.toLowerCase()))
                return allLangsShort[i];
        }
        return null;
    }

    public static int findPosition(String s, String []langs) {
        for(int i=0;i<langs.length;i++){
            if (s.equals(langs[i].toLowerCase())){
                return i;
            }
        }
        return -1;
    }

    public static String getLangInGerman(String languageOther, String[] allLangsLong) {
        int index = 0;
        for (int i = 0; i < allLangsLong.length; i++) {
            if (languageOther.toLowerCase().equals(allLangsLong[i].toLowerCase())) {
                index = i;
            }
        }
        switch (index) {
            case 0:
                return "englisch";
            case 1:
                return "deutsch";
            case 2:
                return "franzoesisch";
            case 3:
                return "spanisch";
            case 4:
                return "italienisch";
            case 5:
                return "russisch";
            case 6:
                return "daenisch";
            case 7:
                return "finnisch";
            case 8:
                return "niederlaendisch";
            case 9:
                return "polnisch";
            case 10:
                return "schwedisch";
            case 11:
                return "portugiesisch";
            default:
                return "englisch";
        }
    }

    public static final String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m"
            , "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
