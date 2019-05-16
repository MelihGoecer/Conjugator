package com.translator_conjugator_dictionary.utils;

import android.app.Application;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.models.SpinnerItem;

import java.util.ArrayList;
import java.util.List;

public class TranslationUtils extends Application {

    public static final String[] LANGUAGES2 = {"de", "en", "fr", "tr", "ru", "es", "it", "pl",
            "fi", "da", "el", "no", "nl", "hr", "ar", "mn", "pt", "zh-CN"
    };


    public List<List<SpinnerItem>> fillSpinner(String[] lang1, String[] lang2, String[] lang3) {
        List<List<SpinnerItem>> listList = new ArrayList<>();

        List<SpinnerItem> list1 = new ArrayList<>();
        String[] langArray1 = lang1;
        list1.add(new SpinnerItem(langArray1[0], R.drawable.flag_of_germany));
        list1.add(new SpinnerItem(langArray1[1], R.drawable.grossbritannien_flagge));
        list1.add(new SpinnerItem(langArray1[2], R.drawable.flag_of_france));
        list1.add(new SpinnerItem(langArray1[3], R.drawable.flag_of_turkey));
        list1.add(new SpinnerItem(langArray1[4], R.drawable.flag_of_russia));
        list1.add(new SpinnerItem(langArray1[5], R.drawable.flag_of_spain));
        list1.add(new SpinnerItem(langArray1[6], R.drawable.flag_of_italy));
        list1.add(new SpinnerItem(langArray1[7], R.drawable.flag_of_poland));
        list1.add(new SpinnerItem(langArray1[8], R.drawable.flagge_finnland));
        list1.add(new SpinnerItem(langArray1[9], R.drawable.flag_of_denmark));
        list1.add(new SpinnerItem(langArray1[10], R.drawable.flag_of_greece));
        list1.add(new SpinnerItem(langArray1[11], R.drawable.flag_of_norway));
        list1.add(new SpinnerItem(langArray1[12], R.drawable.flag_of_the_netherlands));
        list1.add(new SpinnerItem(langArray1[13], R.drawable.flag_of_croatia));
        list1.add(new SpinnerItem(langArray1[14], R.drawable.flag_of_arabia));
        list1.add(new SpinnerItem(langArray1[15], R.drawable.flag_of_mongolia));
        list1.add(new SpinnerItem(langArray1[16], R.drawable.flag_of_portugal));
        list1.add(new SpinnerItem(langArray1[17], R.drawable.flag_of_china));


        List<SpinnerItem> list2 = new ArrayList<>();
        String[] langArray2 = lang2;
        list2.add(new SpinnerItem(langArray2[0], R.drawable.grossbritannien_flagge));
        list2.add(new SpinnerItem(langArray2[1], R.drawable.flag_of_norway));
        list2.add(new SpinnerItem(langArray2[2], R.drawable.flag_of_greece));
        list2.add(new SpinnerItem(langArray2[3], R.drawable.flag_of_denmark));
        list2.add(new SpinnerItem(langArray2[4], R.drawable.flagge_finnland));
        list2.add(new SpinnerItem(langArray2[5], R.drawable.flag_of_poland));
        list2.add(new SpinnerItem(langArray2[6], R.drawable.flag_of_italy));
        list2.add(new SpinnerItem(langArray2[7], R.drawable.flag_of_spain));
        list2.add(new SpinnerItem(langArray2[8], R.drawable.flag_of_russia));
        list2.add(new SpinnerItem(langArray2[9], R.drawable.flag_of_turkey));
        list2.add(new SpinnerItem(langArray2[10], R.drawable.flag_of_france));
        list2.add(new SpinnerItem(langArray2[11], R.drawable.flag_of_germany));
        list2.add(new SpinnerItem(langArray2[12], R.drawable.flag_of_china));
        list2.add(new SpinnerItem(langArray2[13], R.drawable.flag_of_the_netherlands));
        list2.add(new SpinnerItem(langArray2[14], R.drawable.flag_of_croatia));
        list2.add(new SpinnerItem(langArray2[15], R.drawable.flag_of_arabia));
        list2.add(new SpinnerItem(langArray2[16], R.drawable.flag_of_mongolia));
        list2.add(new SpinnerItem(langArray2[17], R.drawable.flag_of_portugal));

        List<SpinnerItem> list3 = new ArrayList<>();
        String[] langArray3 = lang3;
        list3.add(new SpinnerItem(langArray3[0], R.drawable.flag_of_france));
        list3.add(new SpinnerItem(langArray3[1], R.drawable.flag_of_italy));
        list3.add(new SpinnerItem(langArray3[2], R.drawable.flag_of_norway));
        list3.add(new SpinnerItem(langArray3[3], R.drawable.flag_of_russia));
        list3.add(new SpinnerItem(langArray3[4], R.drawable.flag_of_turkey));
        list3.add(new SpinnerItem(langArray3[5], R.drawable.flag_of_greece));
        list3.add(new SpinnerItem(langArray3[6], R.drawable.flag_of_denmark));
        list3.add(new SpinnerItem(langArray3[7], R.drawable.flag_of_germany));
        list3.add(new SpinnerItem(langArray3[8], R.drawable.flagge_finnland));
        list3.add(new SpinnerItem(langArray3[9], R.drawable.flag_of_poland));
        list3.add(new SpinnerItem(langArray3[10], R.drawable.flag_of_spain));
        list3.add(new SpinnerItem(langArray3[11], R.drawable.grossbritannien_flagge));
        list3.add(new SpinnerItem(langArray3[12], R.drawable.flag_of_arabia));
        list3.add(new SpinnerItem(langArray3[13], R.drawable.flag_of_the_netherlands));
        list3.add(new SpinnerItem(langArray3[14], R.drawable.flag_of_croatia));
        list3.add(new SpinnerItem(langArray3[15], R.drawable.flag_of_mongolia));
        list3.add(new SpinnerItem(langArray3[16], R.drawable.flag_of_china));
        list3.add(new SpinnerItem(langArray3[17], R.drawable.flag_of_portugal));

        listList.add(list1);
        listList.add(list2);
        listList.add(list3);
        return listList;
    }

    public static int findPosOfLang(String language, String[] accordingArray) {
        for (int i = 0; i < accordingArray.length; i++) {
            if (accordingArray[i].toLowerCase().equals(language)) {
                return i;
            }
        }
        return -1;

    }

}
