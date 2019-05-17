package com.translator_conjugator_dictionary.UI;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.FadingCircle;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.annotations.NotNull;
import com.translator_conjugator_dictionary.APIs.GoogleDetectLanguage;
import com.translator_conjugator_dictionary.MainActivity;
import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.ViewModels.Repository;
import com.translator_conjugator_dictionary.fragments.ConjugationFragment;
import com.translator_conjugator_dictionary.fragments.RecentlySearchedItemsFragment;
import com.translator_conjugator_dictionary.modelsConj.Conjugation;
import com.translator_conjugator_dictionary.modelsConj.DetectedLanguage;
import com.translator_conjugator_dictionary.modelsConj.LanguageItem;
import com.translator_conjugator_dictionary.utils.ConjugatorHelper;
import com.translator_conjugator_dictionary.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConjugationActivity extends AppCompatActivity implements Repository.IConjugation,
        RecentlySearchedItemsFragment.OnRecentSearchItemClickListener {
    private static final String TAG = "ConjugationActivity";
    //widgets
    public AutoCompleteTextView editTextSearch;
    private BottomNavigationView navigation;
    public ProgressBar progressBar;
    public ConstraintLayout layout;
    private InterstitialAd mInterstitialAd;
    private Toolbar toolbar;
    private CircleImageView civLanguage;
    public AppBarLayout appBarLayout;
    private RecentlySearchedItemsFragment rsif;
    private SearchView searchView;
    private SearchView.SearchAutoComplete searchAutoComplete;
    //vars
    private GoogleDetectLanguage googleDetectLanguage;
    private static String[] allLangsLong;
    private static String[] allLangsShort;
    public static Boolean tenseBlocksInRV = false;
    public static boolean inAuditionMode = false;
    public String currentLanguage;
    public String queriedVerb;
    private boolean inConjugationMode = false;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_online_dictionary:
                    Intent intent1 = new Intent(ConjugationActivity.this, DictionaryActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                case R.id.navigation_translator:
                    Intent intent2 = new Intent(ConjugationActivity.this, MainActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                case R.id.navigation_conjugation:
                    return true;

                case R.id.navigation_marked:
                    Intent intent3 = new Intent(ConjugationActivity.this, SavedItemActivity.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;

            }
            return false;
        }
    };

    private Locale getLocaleForTTS() {
        int p = ConjugatorHelper.findPosition(currentLanguage.toLowerCase(),
                getResources().getStringArray(R.array.languages_array_conjugation));
        switch (p) {
            case 1:
                return Locale.ENGLISH;
            case 2:
                return Locale.GERMAN;
            case 3:
                return Locale.FRENCH;
            case 4:
                return new Locale("es", "ES");
            case 5:
                return Locale.ITALIAN;
            case 6:
                return new Locale("ru", "RU");
            case 7:
                return new Locale("da", "DK");
            case 8:
                return new Locale("fi", "FI");
            case 9:
                return new Locale("nl", "NL");
            case 10:
                return new Locale("pl", "PL");
            case 11:
                return new Locale("sv", "SE");
            case 12:
                return new Locale("pt", "PT");

        }
        return null;
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conjugation_sample);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        mInterstitialAd = new InterstitialAd(ConjugationActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.AD_UNIT_CONJUGATION));
        if (getSharedPreferences("sharedPrefsForAds", MODE_PRIVATE).getInt("counter", 0) == Constants.numberForPreparingAd) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        /*Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.DarkBlue));
        }*/

        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        rsif = RecentlySearchedItemsFragment.newInstance();
        transaction.replace(R.id.conjugator_view_container, rsif, "rsif");
        transaction.commit();

        ImageView imageViewSettingsConjugation =
                findViewById(R.id.menu_conjugator_settings_conjugation);
        imageViewSettingsConjugation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        appBarLayout = findViewById(R.id.appBarLayout);

        final ImageView imageViewArrowUp = findViewById(R.id.imageView_arrow_up_conjugator);
        imageViewArrowUp.setImageAlpha(0);
        imageViewArrowUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rsif.recyclerView.smoothScrollToPosition(0);
                appBarLayout.setExpanded(true);

            }
        });

        final int[] currentAlphaRecentSearch = {0};
        final int[] previousI = {0};
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                int velocity = Math.abs(i) - Math.abs(previousI[0]);

                try {


                    if (velocity > 0) {
                        int offsetLimit = 224;
                        int maxAlpha = 0;
                        Log.d(TAG, "onOffsetChanged: -");
                        int remainingOffset = offsetLimit - Math.abs(i);
                        int remainingSteps = remainingOffset / velocity;
                        int ratio = maxAlpha - (currentAlphaRecentSearch[0] / Math.abs(remainingSteps));
                        currentAlphaRecentSearch[0] = currentAlphaRecentSearch[0] - Math.abs(ratio);
                        imageViewArrowUp.setImageAlpha(255 - currentAlphaRecentSearch[0]);

                    } else {
                        int offsetLimit = 0;
                        int maxAlpha = 255;
                        Log.d(TAG, "onOffsetChanged: +");
                        int remainingOffset = offsetLimit + Math.abs(i);
                        int remainingSteps = remainingOffset / velocity;
                        int ratio = ((maxAlpha - Math.abs(currentAlphaRecentSearch[0])) / Math.abs(remainingSteps));
                        currentAlphaRecentSearch[0] = currentAlphaRecentSearch[0] + Math.abs(ratio);
                        imageViewArrowUp.setImageAlpha(255 - currentAlphaRecentSearch[0]);

                    }

                    previousI[0] = i;

                } catch (ArithmeticException e) {

                }

                if (velocity < 0 && appBarLayout.getElevation() != 0) {
                    appBarLayout.setElevation(0);
                }

            }
        });


        allLangsLong = getResources().getStringArray(R.array.allLanguagesLong);
        allLangsShort = getResources().getStringArray(R.array.allLanguagesShort);
        currentLanguage = allLangsLong[1];
        toolbar = findViewById(R.id.toolbar_conjugator);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://translation.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        googleDetectLanguage = retrofit2.create(GoogleDetectLanguage.class);

        navigation = findViewById(R.id.navigation_conjugation);
        progressBar = findViewById(R.id.progressBar_conjugation);
        Sprite doubleBounce = new FadingCircle();
        doubleBounce.setColor(getResources().getColor(R.color.colorAccent));
        progressBar.setIndeterminateDrawable(doubleBounce);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_conjugation);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        final TextView textView = findViewById(R.id.textView_conjugator_title);

        searchView = findViewById(R.id.menu_conjugator_search);

        searchAutoComplete =
                searchView.findViewById(R.id.search_src_text);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                textView.setVisibility(View.VISIBLE);
                if (!searchAutoComplete.isFocusableInTouchMode()) {
                    searchAutoComplete.setFocusableInTouchMode(true);
                    searchAutoComplete.setOnClickListener(null);
                }
                return false;
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setVisibility(View.INVISIBLE);
            }
        });
        searchView.setIconified(false);

        searchAutoComplete.setFocusableInTouchMode(false);

        searchAutoComplete.setBackgroundResource(R.drawable.search_view_bg);

        // Create a new ArrayAdapter and add data to search auto complete object.
        String dataArr[] = {"Apple", "Amazon", "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, dataArr);
        searchAutoComplete.setAdapter(newsAdapter);

        // Listen to search view item on click event.
        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString = (String) adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText(queryString);
                Toast.makeText(ConjugationActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
            }
        });

        searchAutoComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setFocusableInTouchMode(true);
                v.requestFocus();
                v.setOnClickListener(null);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queriedVerb = query;
                sendRequest();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        civLanguage = findViewById(R.id.menu_conjugator_language);
        civLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupWindow popupWindow = createPopupForCIV();
                popupWindow.setFocusable(true);
                popupWindow.showAsDropDown(v, popupWindow.getWidth() * -1 / 2, 0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                        layoutParams.alpha = 1;
                        getWindow().setAttributes(layoutParams);
                    }
                });

                WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
                layoutParams.alpha = 0.9f;
                getWindow().setAttributes(layoutParams);


            }
        });

    }

    private void circularReveal(View view, int... pos) {
        int cx = pos[0];
        int cy = pos[1];

        float finalRadius = (float) Math.hypot(cx, cy);

        Animator anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
        view.setVisibility(View.VISIBLE);

        anim.start();

    }

    private PopupWindow createPopupForCIV() {
        View content = getLayoutInflater().inflate(R.layout.popupwindow_language, null);
        LinearLayout linearLayout = content.findViewById(R.id.linearLayout_languages);
        final PopupWindow popUp = new PopupWindow(this);

        final List<LanguageItem> languageItemsObj = getLanguageItems();

        for (int i = 0; i < getResources().getStringArray(R.array.languages_array_conjugation).length; i++) {
            View v = getLayoutInflater().inflate(R.layout.spinner_item, null);
            TextView textView = v.findViewById(R.id.textView_language);
            textView.setText(languageItemsObj.get(i).getLanguage());
            final CircleImageView civ = v.findViewById(R.id.circular_imageView);
            civ.setImageResource(languageItemsObj.get(i).getFlagResId());
            final int finalI = i;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    civLanguage.setImageResource(languageItemsObj.get(finalI).getFlagResId());
                    popUp.dismiss();
                    currentLanguage = languageItemsObj.get(finalI).getLanguage();
                    Log.d(TAG, "onClick: " + currentLanguage);
                }
            });
            linearLayout.addView(v);
        }

        popUp.setContentView(content);
        popUp.setOutsideTouchable(true);
        popUp.setBackgroundDrawable(null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popUp.setElevation(getResources().getDimension(R.dimen.elevation_popupwindow));
        }
        popUp.setWidth((int) getResources().getDimension(R.dimen.lanuage_width_popup));
        return popUp;
    }

    private List<LanguageItem> getLanguageItems() {
        List<LanguageItem> languageItems = new ArrayList<>();
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[0], R.drawable.grossbritannien_flagge));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[1], R.drawable.flag_of_germany));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[2], R.drawable.flag_of_france));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[3], R.drawable.flag_of_spain));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[4], R.drawable.flag_of_italy));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[5], R.drawable.flag_of_russia));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[6], R.drawable.flag_of_denmark));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[7], R.drawable.flagge_finnland));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[8], R.drawable.flag_of_the_netherlands));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[9], R.drawable.flag_of_poland));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[10], R.drawable.flag_of_sweden));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[11], R.drawable.flag_of_portugal));
        languageItems.add(new LanguageItem(getResources().getStringArray(R.array.languages_array_conjugation)[12], R.drawable.swap_lang));
        return languageItems;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void clearEditText(View v) {
        editTextSearch.setText("");
    }

    private void sendRequest() {
        closeKeyboard();
        inConjugationMode = true;
        progressBar.setVisibility(View.VISIBLE);
        if (currentLanguage.equals(getString(R.string.Automatic_String))) {
            loadAd();
            detectLang();
        } else {
            loadAd();
            Repository.getInstance().sendRequest(queriedVerb,
                    currentLanguage.toLowerCase(),
                    this, this);
        }
    }

    private void fitDesign(boolean conjugation) {
        final RelativeLayout relativeLayoutToolbar = findViewById(R.id.relativeLayout_toolbar);
        final RelativeLayout relativeLayoutRecentTitle = findViewById(R.id.relativeLayout_recent_title);
        if (conjugation) {
            RelativeLayout relativeLayoutBg =
                    findViewById(R.id.relativeLayout_toolbar_bg);
            relativeLayoutBg.setVisibility(View.VISIBLE);
            relativeLayoutBg.setBackground(new DottedLayout());
            relativeLayoutToolbar.setVisibility(View.INVISIBLE);
            relativeLayoutRecentTitle.setVisibility(View.INVISIBLE);
            final ImageView imageViewSearch = findViewById(R.id.menu_conjugator_search_conjugation);
            imageViewSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    circularReveal(relativeLayoutToolbar,
                            (int) imageViewSearch.getX() + imageViewSearch.getWidth() / 2,
                            (int) imageViewSearch.getY() + imageViewSearch.getHeight() / 2);
                    searchView.setIconified(false);
                }
            });


            final ImageView imageViewAddFavorite = findViewById(R.id.imageView_add_to_favorite);
            try {
                if (((boolean) imageViewAddFavorite.getTag())) {
                    imageViewAddFavorite.setImageResource(R.drawable.ic_star_outline);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            imageViewAddFavorite.setTag(false);
            imageViewAddFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!((boolean) v.getTag())) {
                        imageViewAddFavorite.setImageResource(R.drawable.ic_star_filled);
                        v.setTag(true);
                    } else {
                        imageViewAddFavorite.setImageResource(R.drawable.ic_star_outline);
                        v.setTag(false);
                    }

                }
            });

        } else {
            findViewById(R.id.relativeLayout_toolbar_bg).setVisibility(View.GONE);
            relativeLayoutToolbar.setVisibility(View.VISIBLE);
            relativeLayoutRecentTitle.setVisibility(View.VISIBLE);
        }

    }

    private void detectLang() {
        Call<DetectedLanguage> call = googleDetectLanguage.detectLanguage(editTextSearch.getText().toString());
        call.enqueue(new Callback<DetectedLanguage>() {
            @Override
            public void onResponse(@NotNull Call<DetectedLanguage> call, @NotNull Response<DetectedLanguage> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ConjugationActivity.this, ConjugationActivity.this.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                final DetectedLanguage detectedLanguage = response.body();
                String detectedLang;
                if (detectedLanguage != null) {
                    detectedLang = detectedLanguage.getData().getDetections()[0][0].getLanguage();
                    String s = editTextSearch.getText().toString();
                    String lang = ConjugatorHelper.getLangLong(detectedLang, allLangsLong, allLangsShort);
                    if (lang != null) {
                        currentLanguage = lang;
                        Repository.getInstance().sendRequest(s, lang, ConjugationActivity
                                .this, ConjugationActivity.this);
                    }
                }

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(@NotNull Call<DetectedLanguage> call, @NotNull Throwable t) {
                if (!ConjugatorHelper.isNetworkAvailable(ConjugationActivity.this)) {
                    Toast.makeText(ConjugationActivity.this, ConjugationActivity.this.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    Toast.makeText(ConjugationActivity.this, ConjugationActivity.this.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void loadAd() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefsForAds", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int counter = sharedPreferences.getInt("counter", 0);
        counter++;
        editor.putInt("counter", counter);
        editor.apply();
        if (counter == Constants.numberForPreparingAd) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }
        if (counter == Constants.totalNumberOfAds) {
            if (mInterstitialAd.isLoaded()) {
                counter = 0;
                editor.putInt("counter", counter);
                editor.apply();
                mInterstitialAd.show();
            } else {
                counter = Constants.numberForUnLoadedAd;
                editor.putInt("counter", counter);
                editor.apply();
            }
        }
    }


    private int getFlagResId() {
        String[] l = getResources().getStringArray(R.array.languages_array_conjugation);
        if (currentLanguage.toLowerCase().equals(l[0].toLowerCase())) {
            return R.drawable.grossbritannien_flagge;
        } else if (currentLanguage.toLowerCase().equals(l[1].toLowerCase())) {
            return R.drawable.flag_of_germany;
        } else if (currentLanguage.toLowerCase().equals(l[2].toLowerCase())) {
            return R.drawable.flag_of_france;
        } else if (currentLanguage.toLowerCase().equals(l[3].toLowerCase())) {
            return R.drawable.flag_of_spain;
        } else if (currentLanguage.toLowerCase().equals(l[4].toLowerCase())) {
            return R.drawable.flag_of_italy;
        } else if (currentLanguage.toLowerCase().equals(l[5].toLowerCase())) {
            return R.drawable.flag_of_russia;
        } else if (currentLanguage.toLowerCase().equals(l[6].toLowerCase())) {
            return R.drawable.flag_of_denmark;
        } else if (currentLanguage.toLowerCase().equals(l[7].toLowerCase())) {
            return R.drawable.flagge_finnland;
        } else if (currentLanguage.toLowerCase().equals(l[8].toLowerCase())) {
            return R.drawable.flag_of_the_netherlands;
        } else if (currentLanguage.toLowerCase().equals(l[9].toLowerCase())) {
            return R.drawable.flag_of_poland;
        } else if (currentLanguage.toLowerCase().equals(l[10].toLowerCase())) {
            return R.drawable.flag_of_sweden;
        } else if (currentLanguage.toLowerCase().equals(l[11].toLowerCase())) {
            return R.drawable.flag_of_portugal;
        }
        return -1;
    }


    @Override
    public void getResults(Conjugation conjugation, List<String> tense) {
        progressBar.setVisibility(View.INVISIBLE);
        fitDesign(true);
        if (conjugation != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("conjugation", conjugation);
            bundle.putStringArrayList("tenses", (ArrayList<String>) tense);
            ConjugationFragment conjugationFragment = ConjugationFragment.newInstance(bundle);
            FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
            transaction1.replace(R.id.conjugator_view_container, conjugationFragment,
                    "ConjugationFragment");
            transaction1.commit();
            Log.d(TAG, "getResults: " + getSupportFragmentManager().getFragments().size());
        }
    }

    @Override
    public void onBackPressed() {
        if (inConjugationMode) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.conjugator_view_container, rsif, "rsif");
            transaction.commit();
            fitDesign(false);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRecentSearchClick(CharSequence... strings) {
        currentLanguage = strings[1].toString();
        if (!searchAutoComplete.isFocusableInTouchMode()) {
            searchAutoComplete.setFocusableInTouchMode(true);
            searchAutoComplete.setOnClickListener(null);
        }
        civLanguage.setImageResource(getFlagResId());
        searchView.setQuery(strings[0].toString(), true);
    }

}
