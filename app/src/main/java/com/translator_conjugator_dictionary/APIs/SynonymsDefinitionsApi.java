package com.translator_conjugator_dictionary.APIs;

import com.translator_conjugator_dictionary.modelsSynonyms.ListObject;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_de;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_es;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_fr;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_ru;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_tr;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SynonymsDefinitionsApi {
    @GET("/")
    Call<List<ListObject<Meaning, String>>> getDefinitionsSynonyms(@Query("define") String searchTerm,
                                                                   @Query("lang") String language);

    @GET("/")
    Call<ListObject<Meaning_de, String[]>> getDefinitionsSynonymsDe(@Query("define") String searchTerm,
                                                                    @Query("lang") String language);

    @GET("/")
    Call<ListObject<Meaning_fr, String[]>> getDefinitionsSynonymsFr(@Query("define") String searchTerm,
                                                                    @Query("lang") String language);

    @GET("/")
    Call<ListObject<Meaning_ru, String[]>> getDefinitionsSynonymsRu(@Query("define") String searchTerm,
                                                                    @Query("lang") String language);

    @GET("/")
    Call<ListObject<Meaning_es, String[]>> getDefinitionsSynonymsEs(@Query("define") String searchTerm,
                                                                    @Query("lang") String language);

    @GET("/")
    Call<ListObject<Meaning_tr, String[]>> getDefinitionsSynonymsTr(@Query("define") String searchTerm,
                                                                    @Query("lang") String language);
}
