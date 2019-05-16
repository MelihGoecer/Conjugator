package com.translator_conjugator_dictionary.utils;

import com.translator_conjugator_dictionary.modelsDictionary.DictionaryContentItem;
import com.translator_conjugator_dictionary.modelsDictionary.DictionaryListItem;
import com.translator_conjugator_dictionary.modelsDictionary.WordTypeManager;
import com.translator_conjugator_dictionary.modelsSynonyms.ListObject;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_de;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_es;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_fr;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_ru;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_tr;
import com.translator_conjugator_dictionary.modelsSynonyms.WordType;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DictionaryHelper {


    public static List<DictionaryListItem> getList(List<ListObject<Meaning, String>> results) {
        List<DictionaryListItem> dictionaryListItems = new ArrayList<>();
        for (ListObject result : results) {
            for (WordTypeManager wordTypeManager : checkAvailableMethods(((Meaning) result.getMeaning()))) {
                dictionaryListItems.add(wordTypeManager);
                for (WordType wordType : wordTypeManager.getWordTypes()) {
                    try {
                        dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), Arrays.asList(wordType.getSynonyms())));
                    } catch (NullPointerException e) {
                        if (wordType.getSynonyms() == null) {
                            dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), null));
                        }
                    }

                }
            }
        }
        return dictionaryListItems;
    }

    public static List<DictionaryListItem> getListDe(ListObject<Meaning_de, String[]> results) {
        List<DictionaryListItem> dictionaryListItems = new ArrayList<>();
        for (WordTypeManager wordTypeManager : checkAvailableMethodsDe((results.getMeaning()))) {
            dictionaryListItems.add(wordTypeManager);
            for (WordType wordType : wordTypeManager.getWordTypes()) {
                try {
                    dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), Arrays.asList(wordType.getSynonyms())));
                } catch (NullPointerException e) {
                    if (wordType.getSynonyms() == null) {
                        dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), null));
                    }
                }

            }
        }
        return dictionaryListItems;
    }

    public static List<DictionaryListItem> getListFr(ListObject<Meaning_fr, String[]> results) {
        List<DictionaryListItem> dictionaryListItems = new ArrayList<>();
        for (WordTypeManager wordTypeManager : checkAvailableMethodsFr((results.getMeaning()))) {
            dictionaryListItems.add(wordTypeManager);
            for (WordType wordType : wordTypeManager.getWordTypes()) {
                try {
                    dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), Arrays.asList(wordType.getSynonyms())));
                } catch (NullPointerException e) {
                    if (wordType.getSynonyms() == null) {
                        dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), null));
                    }
                }

            }
        }
        return dictionaryListItems;
    }

    public static List<DictionaryListItem> getListRu(ListObject<Meaning_ru, String[]> results) {
        List<DictionaryListItem> dictionaryListItems = new ArrayList<>();
        for (WordTypeManager wordTypeManager : checkAvailableMethodsRu((results.getMeaning()))) {
            dictionaryListItems.add(wordTypeManager);
            for (WordType wordType : wordTypeManager.getWordTypes()) {
                try {
                    dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), Arrays.asList(wordType.getSynonyms())));
                } catch (NullPointerException e) {
                    if (wordType.getSynonyms() == null) {
                        dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), null));
                    }
                }

            }
        }
        return dictionaryListItems;
    }

    public static List<DictionaryListItem> getListEs(ListObject<Meaning_es, String[]> results) {
        List<DictionaryListItem> dictionaryListItems = new ArrayList<>();
        for (WordTypeManager wordTypeManager : checkAvailableMethodsEs((results.getMeaning()))) {
            dictionaryListItems.add(wordTypeManager);
            for (WordType wordType : wordTypeManager.getWordTypes()) {
                try {
                    dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), Arrays.asList(wordType.getSynonyms())));
                } catch (NullPointerException e) {
                    if (wordType.getSynonyms() == null) {
                        dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), null));
                    }
                }

            }
        }
        return dictionaryListItems;
    }

    public static List<DictionaryListItem> getListTr(ListObject<Meaning_tr, String[]> results) {
        List<DictionaryListItem> dictionaryListItems = new ArrayList<>();
        for (WordTypeManager wordTypeManager : checkAvailableMethodsTr((results.getMeaning()))) {
            dictionaryListItems.add(wordTypeManager);
            for (WordType wordType : wordTypeManager.getWordTypes()) {
                try {
                    dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), Arrays.asList(wordType.getSynonyms())));
                } catch (NullPointerException e) {
                    if (wordType.getSynonyms() == null) {
                        dictionaryListItems.add(new DictionaryContentItem(wordType.getDefinition(), wordType.getExample(), null));
                    }
                }

            }
        }
        return dictionaryListItems;
    }

    public static List<WordTypeManager> checkAvailableMethods(Meaning meaning) {
        List<WordTypeManager> results = new ArrayList<>();
        Method[] methods = meaning.getClass().getDeclaredMethods();
        methods[0] = null;
        List<Method> filteredMethods = new ArrayList<>();
        for (Method currentMethod : methods) {
            Object o = null;
            try {
                o = currentMethod.invoke(meaning);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NullPointerException e) {
            } catch (IllegalArgumentException e) {
            } catch (Exception e) {
            }

            if (o != null) {
                filteredMethods.add(currentMethod);
            }
        }
        for (Method method : filteredMethods) {
            try {
                WordType[] wordTypes1 = ((WordType[]) method.invoke(meaning));
                String s = method.getName().replace("get", "");
                results.add(new WordTypeManager(s, wordTypes1));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return results;
    }

    public static List<WordTypeManager> checkAvailableMethodsDe(Meaning_de meaning) {
        List<WordTypeManager> results = new ArrayList<>();
        Method[] methods = meaning.getClass().getDeclaredMethods();
        methods[0] = null;
        List<Method> filteredMethods = new ArrayList<>();
        for (Method currentMethod : methods) {
            Object o = null;
            try {
                o = currentMethod.invoke(meaning);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NullPointerException e) {
            } catch (IllegalArgumentException e) {
            } catch (Exception e) {
            }
            if (o != null) {
                filteredMethods.add(currentMethod);
            }
        }
        for (Method method : filteredMethods) {
            try {
                WordType[] wordTypes1 = ((WordType[]) method.invoke(meaning));
                String s = method.getName().replace("get", "");
                results.add(new WordTypeManager(s, wordTypes1));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return results;
    }

    public static List<WordTypeManager> checkAvailableMethodsFr(Meaning_fr meaning) {
        List<WordTypeManager> results = new ArrayList<>();
        Method[] methods = meaning.getClass().getDeclaredMethods();
        methods[0] = null;
        List<Method> filteredMethods = new ArrayList<>();
        for (Method currentMethod : methods) {
            Object o = null;
            try {
                o = currentMethod.invoke(meaning);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NullPointerException e) {
            } catch (IllegalArgumentException e) {
            } catch (Exception e) {
            }
            if (o != null) {
                filteredMethods.add(currentMethod);
            }
        }
        for (Method method : filteredMethods) {
            try {
                WordType[] wordTypes1 = ((WordType[]) method.invoke(meaning));
                String s = method.getName().replace("get", "");
                results.add(new WordTypeManager(s, wordTypes1));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return results;
    }

    public static List<WordTypeManager> checkAvailableMethodsRu(Meaning_ru meaning) {
        List<WordTypeManager> results = new ArrayList<>();
        Method[] methods = meaning.getClass().getDeclaredMethods();
        methods[0] = null;
        List<Method> filteredMethods = new ArrayList<>();
        for (Method currentMethod : methods) {
            Object o = null;
            try {
                o = currentMethod.invoke(meaning);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NullPointerException e) {
            } catch (IllegalArgumentException e) {
            } catch (Exception e) {
            }
            if (o != null) {
                filteredMethods.add(currentMethod);
            }
        }
        for (Method method : filteredMethods) {
            try {
                WordType[] wordTypes1 = ((WordType[]) method.invoke(meaning));
                String s = method.getName().replace("get", "");
                results.add(new WordTypeManager(s, wordTypes1));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return results;
    }

    public static List<WordTypeManager> checkAvailableMethodsEs(Meaning_es meaning) {
        List<WordTypeManager> results = new ArrayList<>();
        Method[] methods = meaning.getClass().getDeclaredMethods();
        methods[0] = null;
        List<Method> filteredMethods = new ArrayList<>();
        for (Method currentMethod : methods) {
            Object o = null;
            try {
                o = currentMethod.invoke(meaning);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NullPointerException e) {
            } catch (IllegalArgumentException e) {
            } catch (Exception e) {
            }
            if (o != null) {
                filteredMethods.add(currentMethod);
            }
        }
        for (Method method : filteredMethods) {
            try {
                WordType[] wordTypes1 = ((WordType[]) method.invoke(meaning));
                String s = method.getName().replace("get", "");
                results.add(new WordTypeManager(s, wordTypes1));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return results;
    }

    public static List<WordTypeManager> checkAvailableMethodsTr(Meaning_tr meaning) {
        List<WordTypeManager> results = new ArrayList<>();
        Method[] methods = meaning.getClass().getDeclaredMethods();
        methods[0] = null;
        List<Method> filteredMethods = new ArrayList<>();
        for (Method currentMethod : methods) {
            Object o = null;
            try {
                o = currentMethod.invoke(meaning);
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            } catch (NullPointerException e) { }catch (IllegalArgumentException e) {
            } catch (Exception e) {
            }
            if (o != null) {
                filteredMethods.add(currentMethod);
            }
        }
        for (Method method : filteredMethods) {
            try {
                WordType[] wordTypes1 = ((WordType[]) method.invoke(meaning));
                String s = method.getName().replace("get", "");
                results.add(new WordTypeManager(s, wordTypes1));
            } catch (IllegalAccessException e) {
            } catch (InvocationTargetException e) {
            }
        }
        return results;
    }


}
