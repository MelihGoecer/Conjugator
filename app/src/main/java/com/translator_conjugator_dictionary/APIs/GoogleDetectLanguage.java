package com.translator_conjugator_dictionary.APIs;

import com.translator_conjugator_dictionary.modelsConj.DetectedLanguage;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleDetectLanguage {

    String KEY = "AIzaSyA4k8OAaAK44eLMH8DIai2WDVoLSgKWDao";

    @GET("language/translate/v2/detect?key=" + KEY)
    Call<DetectedLanguage> detectLanguage(@Query("q") String textToTranslate);

}
