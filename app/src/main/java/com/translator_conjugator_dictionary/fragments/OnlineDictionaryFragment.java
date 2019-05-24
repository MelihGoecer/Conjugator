package com.translator_conjugator_dictionary.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.annotations.NotNull;
import com.translator_conjugator_dictionary.APIs.SynonymsDefinitionsApi;
import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.UI.DictionaryActivity;
import com.translator_conjugator_dictionary.adaptersDictionary.DictionaryContentAdapter;
import com.translator_conjugator_dictionary.modelsConj.RecentSearch;
import com.translator_conjugator_dictionary.modelsDictionary.DictionaryContentItem;
import com.translator_conjugator_dictionary.modelsDictionary.DictionaryListItem;
import com.translator_conjugator_dictionary.modelsSynonyms.ListObject;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_de;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_es;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_fr;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_ru;
import com.translator_conjugator_dictionary.modelsSynonyms.Meaning_tr;
import com.translator_conjugator_dictionary.utils.DatabaseHelper;
import com.translator_conjugator_dictionary.utils.DictionaryHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OnlineDictionaryFragment extends Fragment implements DictionaryContentAdapter.OnDictionaryLongClickListener
        , DictionaryContentAdapter.OnCheckBoxClickListener {

    private DictionaryContentAdapter dictionaryContentAdapter;
    public ConstraintLayout constraintLayout;
    //vars
    private SynonymsDefinitionsApi synonymsDefinitionsApi;
    public static Boolean inActionMode = false;
    private ActionMode mActionMode;
    public static List<Integer> positionCheckBoxes = new ArrayList<>();
    private DatabaseHelper mDb;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDb = new DatabaseHelper(getContext());
    }

    public static OnlineDictionaryFragment newInstance() {
        return new OnlineDictionaryFragment();
    }

    public OnlineDictionaryFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit1 = new Retrofit.Builder()
                .baseUrl("https://googledictionaryapi.eu-gb.mybluemix.net")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        synonymsDefinitionsApi = retrofit1.create(SynonymsDefinitionsApi.class);
        dictionaryContentAdapter = new DictionaryContentAdapter(getContext(), this, this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dictionary, container, false);
        constraintLayout = v.findViewById(R.id.constraintLayout_dictionary_fragment);
        //widgets
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView_dictionary);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setAdapter(dictionaryContentAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NotNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void sendRequest(String searchTerm, final String languageShort) {
        switch (languageShort) {
            case "en":
                Call<List<ListObject<Meaning, String>>> call = synonymsDefinitionsApi.getDefinitionsSynonyms(searchTerm, languageShort);
                call.enqueue(new Callback<List<ListObject<Meaning, String>>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<ListObject<Meaning, String>>> call, @NotNull Response<List<ListObject<Meaning, String>>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            ((DictionaryActivity) Objects.requireNonNull(getActivity())).progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                        List<ListObject<Meaning, String>> results = new ArrayList<>(Objects.requireNonNull(response.body()));
                        mDb.addDataConj(new RecentSearch(results.get(0).getWord(), getResources().getStringArray(R.array.languagesDictionary)[0].toLowerCase()), "recent_dictionary_searches");
                        dictionaryContentAdapter.setDictionaryItemsList((DictionaryHelper.getList(results)));
                        ((DictionaryActivity) Objects.requireNonNull(getActivity())).progressBar.setVisibility(View.INVISIBLE);

                    }

                    @Override
                    public void onFailure(@NotNull Call<List<ListObject<Meaning, String>>> call, @NotNull Throwable t) {
                        ((DictionaryActivity) Objects.requireNonNull(getActivity())).progressBar.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable()) {
                            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "de":
                Call<ListObject<Meaning_de, String[]>> call1 = synonymsDefinitionsApi.getDefinitionsSynonymsDe(searchTerm, languageShort);
                call1.enqueue(new Callback<ListObject<Meaning_de, String[]>>() {
                    @Override
                    public void onResponse(@NotNull Call<ListObject<Meaning_de, String[]>> call, @NotNull Response<ListObject<Meaning_de, String[]>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            ((DictionaryActivity) Objects.requireNonNull(getActivity())).progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                        ListObject<Meaning_de, String[]> results = response.body();
                        mDb.addDataConj(new RecentSearch(Objects.requireNonNull(results).getWord(), getResources().getStringArray(R.array.languagesDictionary)[3].toLowerCase()), "recent_dictionary_searches");
                        dictionaryContentAdapter.setDictionaryItemsList(DictionaryHelper.getListDe(((results))));
                        ((DictionaryActivity) Objects.requireNonNull(getActivity())).progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(@NotNull Call<ListObject<Meaning_de, String[]>> call, @NotNull Throwable t) {
                        ((DictionaryActivity) Objects.requireNonNull(getActivity())).progressBar.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable()) {
                            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "fr":
                Call<ListObject<Meaning_fr, String[]>> call2 = synonymsDefinitionsApi.getDefinitionsSynonymsFr(searchTerm, languageShort);
                call2.enqueue(new Callback<ListObject<Meaning_fr, String[]>>() {
                    @Override
                    public void onResponse(Call<ListObject<Meaning_fr, String[]>> call, Response<ListObject<Meaning_fr, String[]>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                        ListObject<Meaning_fr, String[]> results = response.body();
                        mDb.addDataConj(new RecentSearch(results.getWord(), getResources().getStringArray(R.array.languagesDictionary)[1].toLowerCase()), "recent_dictionary_searches");
                        dictionaryContentAdapter.setDictionaryItemsList(DictionaryHelper.getListFr(((results))));
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ListObject<Meaning_fr, String[]>> call, Throwable t) {
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable()) {
                            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "ru":
                Call<ListObject<Meaning_ru, String[]>> call3 = synonymsDefinitionsApi.getDefinitionsSynonymsRu(searchTerm, languageShort);
                call3.enqueue(new Callback<ListObject<Meaning_ru, String[]>>() {
                    @Override
                    public void onResponse(Call<ListObject<Meaning_ru, String[]>> call, Response<ListObject<Meaning_ru, String[]>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                        ListObject<Meaning_ru, String[]> results = response.body();
                        mDb.addDataConj(new RecentSearch(results.getWord(), getResources().getStringArray(R.array.languagesDictionary)[2].toLowerCase()), "recent_dictionary_searches");
                        dictionaryContentAdapter.setDictionaryItemsList(DictionaryHelper.getListRu(((results))));
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ListObject<Meaning_ru, String[]>> call, Throwable t) {
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable()) {
                            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "es":
                Call<ListObject<Meaning_es, String[]>> call4 =
                        synonymsDefinitionsApi.getDefinitionsSynonymsEs(searchTerm, languageShort);
                call4.enqueue(new Callback<ListObject<Meaning_es, String[]>>() {
                    @Override
                    public void onResponse(Call<ListObject<Meaning_es, String[]>> call,
                                           Response<ListObject<Meaning_es, String[]>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                        ListObject<Meaning_es, String[]> results = response.body();
                        mDb.addDataConj(new RecentSearch(results.getWord(),
                                getResources().getStringArray(R.array.languagesDictionary)[4].toLowerCase()), "recent_dictionary_searches");
                        dictionaryContentAdapter.setDictionaryItemsList(DictionaryHelper.getListEs(((results))));
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ListObject<Meaning_es, String[]>> call,
                                          Throwable t) {
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable()) {
                            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            case "tr":
                Call<ListObject<Meaning_tr, String[]>> call5 =
                        synonymsDefinitionsApi.getDefinitionsSynonymsTr(searchTerm, languageShort);
                call5.enqueue(new Callback<ListObject<Meaning_tr, String[]>>() {
                    @Override
                    public void onResponse(Call<ListObject<Meaning_tr, String[]>> call,
                                           Response<ListObject<Meaning_tr, String[]>> response) {
                        if (!response.isSuccessful()) {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                            return;
                        }

                        ListObject<Meaning_tr, String[]> results = response.body();
                        mDb.addDataConj(new RecentSearch(results.getWord(),
                                getResources().getStringArray(R.array.languagesDictionary)[5].toLowerCase()), "recent_dictionary_searches");
                        dictionaryContentAdapter.setDictionaryItemsList(DictionaryHelper.getListTr(((results))));
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ListObject<Meaning_tr, String[]>> call,
                                          Throwable t) {
                        ((DictionaryActivity) getActivity()).progressBar.setVisibility(View.INVISIBLE);
                        if (!isNetworkAvailable()) {
                            Toast.makeText(getContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onLongClick(int position) {

        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(25, 80));
        } else {
            v.vibrate(25);
        }
        if (mActionMode != null) {
            return;
        }
        dictionaryContentAdapter.notifyDataSetChanged();
        inActionMode = true;
        mActionMode = ((DictionaryActivity) Objects.requireNonNull(getActivity())).startSupportActionMode(mActionModeCallback);
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.conjugator_action_mode_menu, menu);
            mode.setTitle(R.string.select);
            mode.setSubtitle("0 Items");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_marked_conjugation_menu_item:
                    Snackbar.make(((DictionaryActivity) getActivity()).navigation, Html.fromHtml("<font color=\"#ffffff\">"+getString(R.string.saving)+"</font>"), Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.AccentPurple))
                            .setAction(getString(R.string.undo), new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                }
                            })
                            .addCallback(new Snackbar.Callback() {

                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    switch (event) {
                                        case 1:
                                            return;
                                        case 2:
                                            SimpleDateFormat dateFormat = (SimpleDateFormat) DateFormat.getDateTimeInstance();
                                            dateFormat.applyPattern("yyyyMMdHms");
                                            String s = dateFormat.format(new Date());
                                            /* mDb.addSavedDItems(s, ((DictionaryActivity) getActivity()).searchView.getQuery().toString(), getDictionaryItemsViaCBPos());*/
                                            mActionMode.finish();
                                    }
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {

                                }
                            }).show();
                    return true;

                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            positionCheckBoxes.clear();
            dictionaryContentAdapter.notifyDataSetChanged();
            inActionMode = false;
            mActionMode = null;
        }
    };

    private List<DictionaryContentItem> getDictionaryItemsViaCBPos() {
        List<DictionaryListItem> dictionaryListItems = new ArrayList<>(dictionaryContentAdapter.getmDictionaryListItems());

        List<DictionaryContentItem> ckeckedBlocks = new ArrayList<>();
        for (Integer k : positionCheckBoxes) {
            ckeckedBlocks.add((DictionaryContentItem) dictionaryListItems.get(k));
        }
        return ckeckedBlocks;
    }


    @Override
    public void onCheckBoxClick(int position, Boolean i) {
        if (i) {
            positionCheckBoxes.add(position);
        } else {
            positionCheckBoxes.remove((Integer) position);
            for (int z = 0; z < positionCheckBoxes.size(); z++) {
                if (positionCheckBoxes.get(z).equals(position)) {
                    positionCheckBoxes.remove(z);
                }
            }
        }
        mActionMode.setSubtitle(getString(R.string.selected_item_indicator, positionCheckBoxes.size()));
    }


}
