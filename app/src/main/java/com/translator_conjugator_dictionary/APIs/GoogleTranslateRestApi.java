package com.translator_conjugator_dictionary.APIs;

import com.translator_conjugator_dictionary.models.GoogleTranslation;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleTranslateRestApi {
    String KEY = "AIzaSyA4k8OAaAK44eLMH8DIai2WDVoLSgKWDao";

    @GET("language/translate/v2?key=" + KEY)
    Call<GoogleTranslation> translate(@Query("q") String textToTranslate,
                                      @Query("source") String source,
                                      @Query("target") String targetLanguage,
                                      @Query("model") String model
    );

}
