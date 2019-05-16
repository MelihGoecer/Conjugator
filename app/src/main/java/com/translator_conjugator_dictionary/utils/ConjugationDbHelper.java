package com.translator_conjugator_dictionary.utils;

import com.translator_conjugator_dictionary.modelsConj.ConjBlock;
import com.translator_conjugator_dictionary.modelsConj.ResultBlock;
import com.translator_conjugator_dictionary.modelsConj.TenseBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConjugationDbHelper {

    public static List<ResultBlock> filterResult(String result) {
        List<String> patterns = new ArrayList<>();
        patterns.add("<div id=\"conjFull\" name=\"conjugationsFull\" class=\"content\">");
        patterns.add("<div id=\"conjTrans\" name=\"conjugationTrans\" class=\"content\">");
        patterns.add("    <div class=\"conj-tense-wrapper\">");
        patterns.add("    </div>" +
                "  </div>" +
                "</div>");
        String mResult1 = "";

        Pattern p1 = Pattern.compile(Pattern.quote(patterns.get(0)) + "(.*?)" + Pattern.quote(patterns.get(1)));
        Matcher m1 = p1.matcher(result.replaceAll("\\n|\\r", ""));
        while (m1.find()) {
            mResult1 = m1.group(1);
        }

        List<String> mResult2 = new ArrayList<>();

        Pattern p2 = Pattern.compile(Pattern.quote(patterns.get(2)) + "(.*?)" + Pattern.quote(patterns.get(3)));
        Matcher m2 = p2.matcher(mResult1);
        while (m2.find()) {
            mResult2.add(m2.group(1));
        }

        List<ResultBlock> resultBlockList = new ArrayList<>();

        for (String currentItem : mResult2) {

            String[] tenseBlocksArr = currentItem.split("<div class=\"conj-tense-block\">");
            List<String> tenseBlocks = new ArrayList<>(Arrays.asList(tenseBlocksArr));
            String resultBlockHeader = tenseBlocks.get(0).replaceAll("      <div class=\"conj-block container result-block\"><h3>|</h3></div>", "");
            tenseBlocks.remove(0);

            List<TenseBlock> tenseBlockList = new ArrayList<>();

            for (String currentTenseBlock : tenseBlocks) {


                String[] conj = currentTenseBlock.split("  <h3 class=\"conj-tense-block-header\">|</h3>" +
                        "|  <div class=\"conj-item\">" +
                        "    <div class=\"conj-person\">|</div>" +
                        "    <div class=\"conj-result\">|</div>" +
                        "  </div>");
                List<String> conjAsList = new ArrayList<>(Arrays.asList(conj));

                for (int i = 0; i < conjAsList.size(); i++) {
                    if (conjAsList.get(i).equals("")) {
                        conjAsList.remove(i);
                    }
                }

                String tenseBlockHeader = conjAsList.get(0);
                conjAsList.remove(0);
                List<ConjBlock> conjBlocks = new ArrayList<>();
                String a = "";

                for (int i = 0; i < conjAsList.size(); i++) {
                    if (i % 2 == 0) {
                        a = conjAsList.get(i);

                    } else {
                        conjBlocks.add(new ConjBlock(a, conjAsList.get(i)));
                    }
                }

                tenseBlockList.add(new TenseBlock(tenseBlockHeader, conjBlocks));

            }

            ResultBlock resultBlock = new ResultBlock(resultBlockHeader, tenseBlockList);
            if (resultBlock != null) {
                resultBlockList.add(resultBlock);
            }

        }

        return resultBlockList;
    }


    public static int getIndex(String s) {
        switch (s.charAt(0)) {
            case 'a':
            case 'ä':
                return 3;
            case 'b':
                return 2;
            case 'c':
                return 1;
            case 'd':
                return 5;
            case 'e':
                return 3;
            case 'f':
                return 4;
            case 'g':
                return 2;
            case 'h':
                return 6;
            case 'i':
                return 1;
            case 'j':
                return 1;
            case 'k':
                return 1;
            case 'l':
                return 3;
            case 'm':
                return 3;
            case 'n':
                return 6;
            case 'o':
            case 'ö':
                return 1;
            case 'p':
                return 1;
            case 'q':
                return 1;
            case 'r':
                return 1;
            case 's':
                return 3;
            case 't':
                return 1;
            case 'u':
            case 'ü':
                return 5;
            case 'v':
                return 3;
            case 'w':
                return 6;
            case 'x':
                return 1;
            case 'y':
                return 1;
            case 'z':
                return 6;
            default:
                return 0;
        }

    }

    public static int getCountOfVowels(String s) {
        int countVowels = 0;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == 'a' || ch == 'e' || ch == 'i'
                    || ch == 'o' || ch == 'u' || ch == 'ä'
                    || ch == 'ö' || ch == 'ü') {
                countVowels++;
            }

        }
        return countVowels;
    }


}
