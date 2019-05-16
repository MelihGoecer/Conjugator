package com.translator_conjugator_dictionary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.translator_conjugator_dictionary.models.Translation;
import com.translator_conjugator_dictionary.modelsConj.ConjBlock;
import com.translator_conjugator_dictionary.modelsConj.IRecentSearch;
import com.translator_conjugator_dictionary.modelsConj.RecentSearch;
import com.translator_conjugator_dictionary.modelsConj.SaveTenseBlock;
import com.translator_conjugator_dictionary.modelsConj.TenseBlock;
import com.translator_conjugator_dictionary.modelsDictionary.DictionaryContentItem;
import com.translator_conjugator_dictionary.modelsDictionary.SaveDictionaryContentItem;
import com.translator_conjugator_dictionary.modelsDictionary.SaveItem;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "translationSave.db";
    private static final int DB_VERSION = 1;
    private static final int CONJ_TABLE_MAX = 40;
    //saved translation table
    private static final String TABLE_NAME = "translations";
    private static final String COL_HEADER = "header";
    private static final String COL_RESULT = "results";
    private static final String COL_THIRD_RESULT = "third_results";
    private static final String COL_SOURCE = "source";
    private static final String COL_TARGET = "target";
    private static final String COL_THIRD_LANG = "third_language";
    private static final String COL_TIMESTAMP = "timestamp";
    //recent searches conjugation
    private static final String TABLE_NAME_CONJUGATION = "recent_conjugations";
    private static final String COL_SEARCH_TERM = "search_term";
    private static final String COL_SOURCE_LANGUAGE = "source_language";
    private static final String COL_TIMESTAMP_CONJ = "timestamp";
    //recent searches dictionary
    private static final String TABLE_NAME_DICTIONARY = "recent_dictionary_searches";
    //saved conjugation items
    private static final String TABLE_NAME_SAVED_CONJUGATIONS = "saved_conjugations";
    private static final String COL_SAVED_CONJ_TIMESTAMP = "time";
    private static final String COL_SAVED_VERB = "verbs";
    private static final String COL_SAVED_HEADER = "headers";
    private static final String COL_SAVED_PRONOUN = "pronouns";
    private static final String COL_SAVED_CONJUGATION = "conjugations";
    private static final String COL_SAVED_CONJUGATION_LANGUAGE = "language";
    //saved dictionary items
    private static final String TABLE_NAME_SAVED_DICTIONARY_ITEMS = "saved_dictionary_items";
    private static final String COL_SAVED_D_TIMESTAMP = "timestamp";
    private static final String COL_SAVED_D_WORD = "words";
    private static final String COL_SAVED_D_DEFINITION = "definitions";
    private static final String COL_SAVED_D_EXAMPLE = "examples";
    private static final String COL_SAVED_D_SYNONYM = "synonyms";

    private static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_NAME +
                    "(" + COL_HEADER + " TEXT NOT NULL, " +
                    COL_RESULT + " TEXT NOT NULL, " +
                    COL_THIRD_RESULT + " TEXT NOT NULL, " +
                    COL_SOURCE + " TEXT NOT NULL, " +
                    COL_TARGET + " TEXT NOT NULL, " +
                    COL_THIRD_LANG + " TEXT NOT NULL, " +
                    COL_TIMESTAMP + " TEXT NOT NULL);";


    private static final String SQL_CREATE_CONJUGATION_TABLE =
            "CREATE TABLE " + TABLE_NAME_CONJUGATION + "(" + COL_SEARCH_TERM + " TEXT NOT NULL, "
                    + COL_SOURCE_LANGUAGE + " TEXT NOT NULL, "
                    + COL_TIMESTAMP_CONJ + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private static final String SQL_CREATE_DICTIONARY_TABLE = "CREATE TABLE " + TABLE_NAME_DICTIONARY + "(" + COL_SEARCH_TERM + " TEXT NOT NULL, "
            + COL_SOURCE_LANGUAGE + " TEXT NOT NULL, "
            + COL_TIMESTAMP_CONJ + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

    private static final String SQL_CREATE_SAVED_CONJ_TABLE = "CREATE TABLE " + TABLE_NAME_SAVED_CONJUGATIONS
            + "(" + COL_SAVED_CONJ_TIMESTAMP + " TEXT NOT NULL," +
            COL_SAVED_VERB + " TEXT NOT NULL, " +
            COL_SAVED_HEADER + " TEXT NOT NULL, " +
            COL_SAVED_PRONOUN + " TEXT NOT NULL, " +
            COL_SAVED_CONJUGATION + " TEXT NOT NULL, " +
            COL_SAVED_CONJUGATION_LANGUAGE + " TEXT NOT NULL);";

    private static final String SQL_CREATE_SAVED_D_TABLE = "CREATE TABLE " + TABLE_NAME_SAVED_DICTIONARY_ITEMS
            + "(" + COL_SAVED_D_TIMESTAMP + " TEXT NOT NULL, " +
            COL_SAVED_D_WORD + " TEXT NOT NULL, " +
            COL_SAVED_D_DEFINITION + " TEXT, " +
            COL_SAVED_D_EXAMPLE + " TEXT, " +
            COL_SAVED_D_SYNONYM + " TEXT);";

    public void deleteTable() {
    this.getWritableDatabase().execSQL(" DROP TABLE IF EXISTS saved_conjugations");
    }
    public void addTable() {
        this.getWritableDatabase().execSQL(SQL_CREATE_SAVED_CONJ_TABLE);
    }


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONJUGATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DICTIONARY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SAVED_CONJ_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SAVED_D_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DROP);
        onCreate(sqLiteDatabase);
    }

    public boolean addSavedItemsConj(String time, String verb, TenseBlock t, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = -1;
            for (ConjBlock c : t.getConjBlocks()) {
                contentValues.put(COL_SAVED_CONJ_TIMESTAMP, time);
                contentValues.put(COL_SAVED_VERB, verb);
                contentValues.put(COL_SAVED_HEADER, t.getHeader());
                contentValues.put(COL_SAVED_PRONOUN, c.getPerson());
                contentValues.put(COL_SAVED_CONJUGATION, c.getResult());
                contentValues.put(COL_SAVED_CONJUGATION_LANGUAGE, language);
                result = db.insert(TABLE_NAME_SAVED_CONJUGATIONS, null, contentValues);
        }


        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public boolean addSavedDItems(String time, String word, List<DictionaryContentItem> dictionaryContentItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = -1;
        for (DictionaryContentItem dc : dictionaryContentItems) {
            if (dc.getSynonyms() != null){
                for (String s : dc.getSynonyms()) {
                    contentValues.put(COL_SAVED_D_TIMESTAMP, time);
                    contentValues.put(COL_SAVED_D_WORD, word);
                    contentValues.put(COL_SAVED_D_DEFINITION, dc.getDefinition());
                    contentValues.put(COL_SAVED_D_EXAMPLE, dc.getExample());
                    contentValues.put(COL_SAVED_D_SYNONYM, s);
                    result = db.insert(TABLE_NAME_SAVED_DICTIONARY_ITEMS, null, contentValues);
                }
            } else {
                contentValues.put(COL_SAVED_D_TIMESTAMP, time);
                contentValues.put(COL_SAVED_D_WORD, word);
                contentValues.put(COL_SAVED_D_DEFINITION, dc.getDefinition());
                contentValues.put(COL_SAVED_D_EXAMPLE, dc.getExample());
                contentValues.put(COL_SAVED_D_SYNONYM, "");
                result = db.insert(TABLE_NAME_SAVED_DICTIONARY_ITEMS, null, contentValues);
            }

        }



        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addDataTranslation(String tableName, String header, String result1, String thirdResult
            , String source, String target, String thirdLanguage) {
        if (getAllRecentTranslations().size() > 1000) {
            removeLastSearch(tableName);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = -1;
        contentValues.put(COL_HEADER, header);
        contentValues.put(COL_RESULT, result1);
        contentValues.put(COL_THIRD_RESULT, thirdResult);
        contentValues.put(COL_SOURCE, source);
        contentValues.put(COL_TARGET, target);
        contentValues.put(COL_THIRD_LANG, thirdLanguage);
        contentValues.put(COL_TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<SaveItem> getAllSavedItems() {
        List<SaveItem> saveItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor1 = db.rawQuery(" SELECT DISTINCT time,verbs,headers FROM saved_conjugations ORDER BY verbs DESC", null);
        cursor1.moveToFirst();
        while (!cursor1.isAfterLast()) {
            String time = cursor1.getString(0);
            String foundVerb = cursor1.getString(1);
            String header = cursor1.getString(2);
            Cursor cursor2 = db.rawQuery(" SELECT time,verbs,headers,pronouns,conjugations," +
                    "language" +
                    " FROM saved_conjugations WHERE time = ?" +
                    " AND verbs = ?" +
                    " AND headers = ? ORDER BY verbs DESC", new String[] {time,foundVerb,header});
            cursor2.moveToFirst();
            String v = cursor2.getString(1);
            String h = cursor2.getString(2);
            String l = cursor2.getString(5);

            List<ConjBlock> conjBlocks = new ArrayList<>();
            while (!cursor2.isAfterLast()) {
                conjBlocks.add(new ConjBlock(cursor2.getString(3), cursor2.getString(4)));
                cursor2.moveToNext();
            }
            saveItems.add(new SaveTenseBlock(v, h, conjBlocks, l));
            cursor2.close();
            cursor1.moveToNext();
        }
        cursor1.close();

        Cursor cursor4 = db.rawQuery(" SELECT DISTINCT timestamp,words,definitions FROM saved_dictionary_items ORDER BY words DESC", null);
        cursor4.moveToFirst();
        while (!cursor4.isAfterLast()) {
            String time = cursor4.getString(0);
            String word = cursor4.getString(1);
            String definition = cursor4.getString(2);
            Cursor cursor5 = db.rawQuery(" SELECT timestamp,words,definitions,examples,synonyms FROM saved_dictionary_items " +
                    "WHERE timestamp = ? AND words = ? AND definitions = ? ORDER BY words DESC", new String[] {time, word, definition});
            cursor5.moveToFirst();
            String w = cursor5.getString(1);
            String d = cursor5.getString(2);
            String e = cursor5.getString(3);

            List<String> s = new ArrayList<>();
            while (!cursor5.isAfterLast()) {
                s.add(cursor5.getString(4));
                cursor5.moveToNext();
            }
            saveItems.add(new SaveDictionaryContentItem(w, d, e, s));
            cursor5.close();
            cursor4.moveToNext();
        }
        return saveItems;

    }

    public List<Translation> getFirstTenRecentTranslations() {
        List<Translation> recentSearches = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT header,results,third_results,source,target,third_language,timestamp"
                + " FROM " + TABLE_NAME
                + " ORDER BY timestamp DESC", null);
        cursor.moveToFirst();
        int i = 0;
        if (cursor.getCount() != 0) {
            if (cursor.getCount() < 20) {
                while (i < cursor.getCount()) {
                    Translation recentSearch = new Translation(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                            , cursor.getString(4), cursor.getString(5), cursor.getString(6));
                    recentSearches.add(recentSearch);
                    cursor.moveToNext();
                    i++;
                }
            } else {
                while (i < 20) {
                    Translation recentSearch = new Translation(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                            , cursor.getString(4), cursor.getString(5), cursor.getString(6));
                    recentSearches.add(recentSearch);
                    cursor.moveToNext();
                    i++;
                }

            }
        }
        cursor.close();
        return recentSearches;
    }

    public List<Translation> getAllRecentTranslations() {
        List<Translation> recentSearches = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT header,results,third_results,source,target,third_language,timestamp"
                + " FROM " + TABLE_NAME
                + " ORDER BY timestamp DESC", null);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            while (!cursor.isAfterLast()) {
                Translation recentSearch = new Translation(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                        , cursor.getString(4), cursor.getString(5), cursor.getString(6));
                recentSearches.add(recentSearch);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return recentSearches;
    }


    public boolean addDataConj(RecentSearch recentSearch, String tableName) {
        if (getAllRecentSearches(tableName).size() > CONJ_TABLE_MAX) {
            removeLastSearch(tableName);
        }
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long result = -1;
        contentValues.put(COL_SEARCH_TERM, recentSearch.getSearchTerm());
        contentValues.put(COL_SOURCE_LANGUAGE, recentSearch.getSourceLanguage());
        result = db.insert(tableName, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<IRecentSearch> getAllRecentSearches(String tableName) {
        List<IRecentSearch> recentSearches = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT search_term,source_language,timestamp"
                + " FROM " + tableName
                + " ORDER BY timestamp DESC", null);
        cursor.moveToFirst();

        if (cursor.getCount() != 0) {
            while (!cursor.isAfterLast()) {
                RecentSearch recentSearch = new RecentSearch(cursor.getString(0), cursor.getString(1));
                recentSearches.add(recentSearch);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return recentSearches;
    }

    public Integer removeLastSearch(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT timestamp FROM " + tableName + " ORDER BY timestamp ASC ", null);
        cursor.moveToFirst();
        String t = cursor.getString(0);
        cursor.close();
        return db.delete(tableName, "timestamp = ?", new String[]{t});
    }

    public Integer deleteDataConj(int p, String tableName) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT timestamp FROM " + tableName + " ORDER BY timestamp ASC ", null);
        cursor.moveToPosition(p);
        return database.delete(tableName, "timestamp = ?", new String[]{cursor.getString(0)});
    }

    public List<Translation> filterTranslations(String s) {
        List<Translation> filteredList = new ArrayList<>();

        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT " +
                " header,results,third_results,source,target,third_language,timestamp" +
                " FROM translations WHERE header LIKE " + "'%" + s + "%'" + " ORDER BY timestamp DESC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Translation translation = new Translation(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                    , cursor.getString(4), cursor.getString(5), cursor.getString(6));
            filteredList.add(translation);
            cursor.moveToNext();
        }
        cursor.close();
        return filteredList;
    }

    public Translation getRequestedItem(String timestamp) {
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT header,results,third_results,source,target,third_language,timestamp"
                + " FROM " + TABLE_NAME
                + " WHERE timestamp = " + timestamp, null);
        cursor.moveToFirst();
        Translation translation = new Translation(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)
                , cursor.getString(4), cursor.getString(5), cursor.getString(6));
        cursor.close();
        return translation;

    }

    public void clearTable(String name) {
        this.getWritableDatabase().execSQL("DELETE FROM " + name);
    }

}
