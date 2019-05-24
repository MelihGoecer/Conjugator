package com.translator_conjugator_dictionary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.models.Translation;
import com.translator_conjugator_dictionary.modelsConj.IRecentSearch;
import com.translator_conjugator_dictionary.modelsConj.RecentSearch;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";
    private static final int DB_VERSION = 2;

    private static final String DB_NAME = "translationSave.db";
    //autocompletion for conjugator
    private static final String TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE =
            "autocompletion_conjugator_de";
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
    //saved dictionary items
    private static final String TABLE_NAME_SAVED_DICTIONARY_ITEMS = "saved_dictionary_items";
    private static final String TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_EN =
            "autocompletion_conjugator_en";
    private static final String SQL_CREATE_AUTOCOMPLETION_TABLE_DE =
            "CREATE TABLE " + TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE + "("
                    + "verb" + " TEXT NOT NULL, "
                    + "length" + " INTEGER NOT NULL, "
                    + "first_character" + " TEXT NOT NULL);";


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
    private static final String SQL_CREATE_AUTOCOMPLETION_TABLE_EN =
            "CREATE TABLE " + TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_EN + "("
                    + "verb" + " TEXT NOT NULL, "
                    + "length" + " INTEGER NOT NULL, "
                    + "first_character" + " TEXT NOT NULL);";
    private Context context;
    private String[] verbsList;


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE);
        sqLiteDatabase.execSQL(SQL_CREATE_CONJUGATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DICTIONARY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_AUTOCOMPLETION_TABLE_DE);
        sqLiteDatabase.execSQL(SQL_CREATE_AUTOCOMPLETION_TABLE_EN);
        readFileAndAddToTable();
    }

    private void readFileAndAddToTable() {
        ReadFile readFile = new ReadFile();
        readFile.start();
    }

    private void addVerbsToTable() {
        WriteToTable writeToTable1 = new WriteToTable(0, 1584, R.raw.verbs_list_de, TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE);
        WriteToTable writeToTable2 = new WriteToTable(1585, 3169, R.raw.verbs_list_de, TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE);
        WriteToTable writeToTable3 = new WriteToTable(3170, 4754, R.raw.verbs_list_de, TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE);
        WriteToTable writeToTable4 = new WriteToTable(4755, 6339, R.raw.verbs_list_de, TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE);
        WriteToTable writeToTable5 = new WriteToTable(6340, 7923, R.raw.verbs_list_de, TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE);

        WriteToTable writeToTable6 = new WriteToTable(0, 2710, R.raw.verbs_list_en,
                TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_EN);

        writeToTable1.start();
        writeToTable2.start();
        writeToTable3.start();
        writeToTable4.start();
        writeToTable5.start();

        writeToTable6.start();
    }

    public void queryVerbsTable(String query, OnQueryListener onQueryListener, String lang) {
        QueryTableForAutoCompletion autoCompletion;
        switch (lang) {
            case "de":
                autoCompletion = new QueryTableForAutoCompletion(TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_DE,
                        query,
                        onQueryListener);
                autoCompletion.start();
                break;
            case "en":
                autoCompletion = new QueryTableForAutoCompletion(TABLE_NAME_AUTOCOMPLETION_CONJUGATOR_EN,
                        query,
                        onQueryListener);
                autoCompletion.start();
                break;
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

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        switch (i) {
            case 1:
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SAVED_CONJUGATIONS);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_SAVED_DICTIONARY_ITEMS);
                sqLiteDatabase.execSQL(SQL_CREATE_AUTOCOMPLETION_TABLE_DE);
                sqLiteDatabase.execSQL(SQL_CREATE_AUTOCOMPLETION_TABLE_EN);
                readFileAndAddToTable();
                Log.d(TAG, "onUpgrade: old version: " + i + " new version: " + i1);
            case 2:
        }


    }

    public void clearTable(String name) {
        this.getWritableDatabase().execSQL("DELETE FROM " + name);


    }

    public interface OnQueryListener {
        void onQueryFinished(List<String> results);
    }

    public class ReadFile extends Thread {

        @Override
        public void run() {
            InputStreamReader isr;

            InputStream ips = context.getResources().openRawResource(R.raw.verbs_list_de);
            try {
                isr = new InputStreamReader(ips, StandardCharsets.UTF_8.name());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                isr = null;
            }
            StringBuilder list = new StringBuilder();

            int d = 0;
            try {
                d = isr.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (d != -1) {
                list.append(((char) d));
                try {
                    d = isr.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            verbsList = list.toString().split(",");

            addVerbsToTable();

        }
    }

    public class WriteToTable extends Thread {
        private int startIndex;
        private int endIndex;
        private int rawResId;
        private String language;

        WriteToTable(int startIndex, int endIndex, int rawResId, String language) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.rawResId = rawResId;
            this.language = language;
        }

        @Override
        public void run() {
            ContentValues contentValues = new ContentValues();
            SQLiteDatabase db = DatabaseHelper.this.getWritableDatabase();
            InputStreamReader isr;

            InputStream ips = context.getResources().openRawResource(rawResId);
            try {
                isr = new InputStreamReader(ips, StandardCharsets.UTF_8.name());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                isr = null;
            }
            StringBuilder list = new StringBuilder();

            int d = 0;
            try {
                d = isr.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (d != -1) {
                list.append(((char) d));
                try {
                    d = isr.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            verbsList = list.toString().split(",");


            ArrayList<String> subList = new ArrayList<>(Arrays.asList(verbsList).subList(startIndex,
                    endIndex));


            Log.d(TAG, "run: " + verbsList.length);

            for (String verb : subList) {
                Log.d(TAG, "run: " + verb);
                contentValues.put("verb", verb);
                contentValues.put("length", verb.length());
                contentValues.put("first_character", String.valueOf(verb.charAt(0)));
                db.insert(language, null, contentValues);
                contentValues.clear();
            }

        }
    }

    class QueryTableForAutoCompletion extends Thread {
        private String table;
        private String query;
        private OnQueryListener onQueryListener;

        public QueryTableForAutoCompletion(String language, String query, OnQueryListener onQueryListener) {
            this.table = language;
            this.query = query;
            this.onQueryListener = onQueryListener;
        }

        @Override
        public void run() {
            SQLiteDatabase db = DatabaseHelper.this.getReadableDatabase();

            int length = query.length();

            if (length != 0) {
                String firstCharacter = String.valueOf(query.charAt(0));

                Cursor cursor =
                        db.rawQuery("SELECT verb, length, first_character FROM "
                                        + table +
                                        " WHERE first_character = ? AND length >= ? AND verb LIKE" +
                                        " '%" + query + "%' ORDER BY verb DESC, length ASC",
                                new String[]{firstCharacter, String.valueOf(length)});

                cursor.moveToFirst();
                int totalNumber = cursor.getCount();
                List<String> suggestions = new ArrayList<>();

                while (!cursor.isAfterLast()) {
                    suggestions.add(cursor.getString(0));
                    cursor.moveToNext();
                }
                cursor.close();
                onQueryListener.onQueryFinished(suggestions);
            }


        }
    }

}
