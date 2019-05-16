package com.translator_conjugator_dictionary.ViewModels;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.translator_conjugator_dictionary.APIs.GoogleTranslateRestApi;
import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.UI.ConjugationActivity;
import com.translator_conjugator_dictionary.models.GoogleTranslation;
import com.translator_conjugator_dictionary.models.Translations;
import com.translator_conjugator_dictionary.modelsConj.Conjugation;
import com.translator_conjugator_dictionary.modelsConj.ConjugationFull;
import com.translator_conjugator_dictionary.modelsConj.ConjugationFullAlternative;
import com.translator_conjugator_dictionary.modelsConj.RecentSearch;
import com.translator_conjugator_dictionary.modelsConj.ResultBlock;
import com.translator_conjugator_dictionary.modelsConj.ResultBlockAlternative;
import com.translator_conjugator_dictionary.modelsConj.TenseBlock;
import com.translator_conjugator_dictionary.modelsConj.TenseBlockAlternative;
import com.translator_conjugator_dictionary.utils.ConjugationDbHelper;
import com.translator_conjugator_dictionary.utils.ConjugatorHelper;
import com.translator_conjugator_dictionary.utils.DatabaseHelper;
import com.translator_conjugator_dictionary.utils.MySingleton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Repository {
    private PublishTranslations publishTranslations;
    private FirebaseFirestore mDb;
    private static final Repository ourInstance = new Repository();

    public Repository() {
    }

    public static Repository getInstance() {
        return ourInstance;
    }


    public void sendRequest(final String searchTerm, final String language, final IConjugation p, final Context context) {
        String sourceLanguage = language;
        mDb = FirebaseFirestore.getInstance();

        String langShort = ConjugatorHelper.getLangShort(sourceLanguage
                , context.getResources().getStringArray(R.array.allLanguagesLong)
                , context.getResources().getStringArray(R.array.allLanguagesShort));

        String i;
        if (!language.equals(context.getResources().getStringArray(R.array.allLanguagesLong)[1].toLowerCase())) {
            sourceLanguage = "-" + langShort;
            i = String.valueOf(searchTerm.charAt(1));
        } else {
            sourceLanguage = "";
            try {
                i = String.valueOf(searchTerm.charAt(ConjugationDbHelper.getIndex(searchTerm)));
            } catch (StringIndexOutOfBoundsException e) {
                i = "a";
            }
        }
        searchTerm.toLowerCase().trim();

        CollectionReference verbsRef = mDb.collection("Translator").document("Words")
                .collection("Conjugation" + sourceLanguage).document(String.valueOf(searchTerm.charAt(0)))
                .collection(i);

        Query query = verbsRef
                .whereEqualTo("verbsLength", searchTerm.length()).limit(5)
                .whereEqualTo("countVowels", ConjugationDbHelper.getCountOfVowels(searchTerm)).limit(5)
                .whereEqualTo("ending", searchTerm.substring(searchTerm.length() - 2)).limit(5)
                .whereEqualTo("verb", searchTerm).limit(1);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot document = task.getResult();
                    if (!document.isEmpty()) {
                        List<DocumentSnapshot> documentSnapshots = document.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            ConjugationFull conjugationFull = documentSnapshot.toObject(ConjugationFull.class);
                            Conjugation conjugation = new Conjugation(conjugationFull.getResultBlocks());
                            ConjugationActivity.tenseBlocksInRV = true;
                            List<String> wordTypes = new ArrayList<>();
                            for (ResultBlock resultBlock : conjugation.getResultBlocks()) {
                                wordTypes.add(resultBlock.getHeader());
                            }
                            p.getResults(conjugation, wordTypes);
                            RecentSearch recentSearch = new RecentSearch(searchTerm, language.toLowerCase());
                            new DatabaseHelper(context).addDataConj(recentSearch, "recent_conjugations");

                        }
                    } else {
                        sendRequestToWebservice(searchTerm.toLowerCase().trim(), language,
                                context, p);
                    }
                } else {
                    ((ConjugationActivity) context).progressBar.setVisibility(View.INVISIBLE);
                    if (!isNetworkAvailable(context)) {
                        Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
    }

    public void queryDatabase(final String searchTerm, final String language, final Context context) {


    }

    private void sendRequestToWebservice(final String searchTerm, final String language,
                                         final Context context, final IConjugation publishResults) {
        RequestQueue queue = MySingleton.getInstance(context)
                .getRequestQueue();
        String l = ConjugatorHelper.getLangInGerman(language, context.getResources().getStringArray(R.array.allLanguagesLong));

        String url = "https://de.bab.la/konjugieren/" + l + "/" + searchTerm;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<ResultBlock> resultBlockList = new ArrayList<>(ConjugationDbHelper.filterResult(response));
                        if (resultBlockList.size() == 0) {
                            Toast.makeText(context, context.getString(R.string.no_results_found),
                                    Toast.LENGTH_SHORT).show();
                            publishResults.getResults(null, null);
                        } else {
                            ConjugationActivity.tenseBlocksInRV = true;
                            List<String> wordTypes = new ArrayList<>();
                            for (ResultBlock resultBlock : resultBlockList) {
                                wordTypes.add(resultBlock.getHeader());
                            }
                            publishResults.getResults(new Conjugation(resultBlockList), wordTypes
                            );
                            RecentSearch recentSearch = new RecentSearch(searchTerm, language.toLowerCase());
                            new DatabaseHelper(context).addDataConj(recentSearch, "recent_conjugations");

                            List<ResultBlockAlternative> alternatives = new ArrayList<>();
                            for (ResultBlock resultBlock : resultBlockList) {
                                List<TenseBlockAlternative> alternatives1 = new ArrayList<>();
                                for (TenseBlock t : resultBlock.getTenseBlocks()) {
                                    TenseBlockAlternative tt = new TenseBlockAlternative(t.getHeader(), t.getConjBlocks());
                                    alternatives1.add(tt);
                                }
                                alternatives.add(new ResultBlockAlternative(resultBlock.getHeader(), alternatives1));
                            }
                            mDb.collection("Translator").document("Words")
                                    .collection("Conjugation-" + ConjugatorHelper.getLangShort(language
                                            , context.getResources().getStringArray(R.array.allLanguagesLong), context.getResources().getStringArray(R.array.allLanguagesShort)))
                                    .document(String.valueOf(searchTerm.charAt(0))).collection(String.valueOf(searchTerm.charAt(1)))
                                    .add(new ConjugationFullAlternative(searchTerm, alternatives, searchTerm.length(), searchTerm.substring(searchTerm.length() - 2)
                                            , ConjugationDbHelper.getCountOfVowels(searchTerm)))
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                        }
                                    });
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        ((ConjugationActivity) context).progressBar.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable(context)) {
                            Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public void sendRequestForTranslation(String textToTranslate, String source, String target, String model, final PublishTranslations publishTranslations
            , final int i, final Context context) {
        if (source.equals(target)) {
            Toast.makeText(context, context.getString(R.string.change_language), Toast.LENGTH_SHORT).show();
            return;

        }
        this.publishTranslations = publishTranslations;
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("https://translation.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleTranslateRestApi googleTranslateRestApi = retrofit1.create(GoogleTranslateRestApi.class);

        Call<GoogleTranslation> call = googleTranslateRestApi.translate(textToTranslate, source, target, model);

        call.enqueue(new Callback<GoogleTranslation>() {
            @Override
            public void onResponse(Call<GoogleTranslation> call, retrofit2.Response<GoogleTranslation> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    return;
                }

                GoogleTranslation googleTranslation = response.body();
                Translations[] translations = googleTranslation.getData().getTranslations();
                String translatedText = translations[0].getTranslatedText();
                Repository.this.publishTranslations.getResults(translatedText, i);

            }

            @Override
            public void onFailure(Call<GoogleTranslation> call, Throwable t) {
                if (!isNetworkAvailable(context)) {
                    Toast.makeText(context, context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, context.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public interface IConjugation {
        void getResults(Conjugation conjugation, List<String> tense);
    }

    public interface PublishTranslations {
        void getResults(String translation, int i);
    }

}

