package com.translator_conjugator_dictionary.UI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.translator_conjugator_dictionary.MainActivity;
import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.fragments.OnlineDictionaryFragment;
import com.translator_conjugator_dictionary.fragments.RecentDictionaryFragment;
import com.translator_conjugator_dictionary.utils.Constants;

import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;
import androidx.viewpager.widget.ViewPager;

import de.hdodenhof.circleimageview.CircleImageView;


public class DictionaryActivity extends AppCompatActivity {
    private static final String SEARCHTERM = "searchTerm";
    public static final String STATE_KEY = "instanceState";
    //widgets
    public ViewPager mViewPager;
    private Toolbar toolbar;
    public BottomNavigationView navigation;
    public MaterialSpinner spinnerLanguage;
    private TabLayout tabLayout;
    private ImageView arrowDropDownList;
    public CircleImageView spinnerFlag;
    private InterstitialAd mInterstitialAd;
    public ProgressBar progressBar;
    public SearchView searchView;
    public TextView textViewTitle;
    //vars
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final String colorPage0 = "#008577";
    private static final String colorPage1 = "#546e7a";
    private static final String colorPage2 = "#3F51B5";
    private String c1 = colorPage0;
    private String c2 = colorPage1;
    private Integer previousOffset = 0;
    private Integer offsetLimit1 = Math.abs(Integer.parseInt(colorPage1.replace("#", ""), 16) - Integer.parseInt(colorPage0.replace("#", ""), 16));
    private Integer offsetLimit2 = Math.abs(Integer.parseInt(colorPage2.replace("#", ""), 16) - Integer.parseInt(colorPage1.replace("#", ""), 16));
    private Integer offsetLimit = Math.abs(Integer.parseInt(c2.replace("#", ""), 16) - Integer.parseInt(c1.replace("#", ""), 16));
    private Integer lim = Math.abs(Integer.parseInt(c2.replace("#", ""), 16) - Integer.parseInt(c1.replace("#", ""), 16));
    private Integer velocity;
    private StringBuilder currentColor = new StringBuilder(c1);
    private Integer r1;
    private Integer r2;
    private Integer r3;
    private String searchTerm;
    public OnlineDictionaryFragment onlineDictionaryFragment = OnlineDictionaryFragment.newInstance();
    private Boolean spinnerInDropDown = false;

    private void startAnim() {
        arrowDropDownList.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.down_up));
        Drawable d = arrowDropDownList.getDrawable();
        if(d instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
            avd.start();
        } else if(d instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
            avd.start();
        }
    }

    private void endAnim() {
        arrowDropDownList.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.up_down));
        Drawable d = arrowDropDownList.getDrawable();
        if(d instanceof AnimatedVectorDrawableCompat) {
            AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
            avd.start();
        } else if(d instanceof AnimatedVectorDrawable) {
            AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
            avd.start();
        }
    }

    public Integer setLim(Integer lim) {
        this.lim = lim;
        return this.lim;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_online_dictionary:

                    return true;
                case R.id.navigation_translator:
                    Intent intent1 = new Intent(DictionaryActivity.this, MainActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
                case R.id.navigation_conjugation:
                    Intent intent2 = new Intent(DictionaryActivity.this, ConjugationActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;

                case R.id.navigation_marked:
                    Intent intent3 = new Intent(DictionaryActivity.this, SavedItemActivity.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;

            }
            return false;
        }
    };

    @Override
    protected void onRestart() {
        super.onRestart();
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_dictionary);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        textViewTitle = findViewById(R.id.textView_title_dictionary);
        progressBar = findViewById(R.id.progressBar_dictionary);

        mInterstitialAd = new InterstitialAd(DictionaryActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.AD_UNIT_DICTIONARY));
        if (getSharedPreferences("sharedPrefsForAds", MODE_PRIVATE).getInt("counter", 0) == Constants.numberForPreparingAd) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        arrowDropDownList = findViewById(R.id.imageButton_drop_down);
        spinnerFlag = findViewById(R.id.imageView_spinner_flag);
        spinnerLanguage = findViewById(R.id.spinner_language_dictionary);
        spinnerLanguage.setItems(getResources().getStringArray(R.array.languagesDictionary));
        Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.grossbritannien_flagge)).into(spinnerFlag);

        spinnerLanguage.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                spinnerInDropDown = false;
                spinnerFlag.setImageResource(getImageResourceFlag());
                endAnim();
            }
        });

        arrowDropDownList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerInDropDown = true;
                spinnerLanguage.expand();
                startAnim();

            }
        });
        spinnerFlag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerInDropDown = true;
                spinnerLanguage.expand();
                startAnim();

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navigation = (BottomNavigationView) findViewById(R.id.navigation_online_dictionary);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                ColorCalculatingThread colorCalculatingThread = new ColorCalculatingThread(position, positionOffset);
                new Thread(colorCalculatingThread).start();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (savedInstanceState != null) {
            this.searchTerm = savedInstanceState.getString("searchTerm");
            mSectionsPagerAdapter.restoreState(savedInstanceState.getParcelable("instanceState"), this.getClassLoader());
            onlineDictionaryFragment = ((OnlineDictionaryFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 0));
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (spinnerInDropDown) {
                endAnim();
            }
            if ("fromMainActivity".equals(getIntent().getStringExtra("MainActivity"))) {
                getIntent().removeExtra("MainActivity");
                if (getIntent().getStringExtra("translatedText").trim() != "") {
                    try {
                        spinnerLanguage.setSelectedIndex(findPosForLang(getIntent().getStringExtra("languageTrans")));
                        spinnerFlag.setImageResource(getImageResourceFlag());
                        onlineDictionaryFragment.constraintLayout.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        searchView.setQuery(getIntent().getStringExtra("translatedText"), false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendRequest(getIntent().getStringExtra("translatedText"));
                            }
                        }, 1000);
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(this, getResources().getText(R.string.unsupportedLanguage),
                                Toast.LENGTH_SHORT).show();
                    }



                } else {
                    Toast.makeText(DictionaryActivity.this, getString(R.string.type_more_letters), Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

    public void sendRequest(String searchTerm) {
        loadAd();
        onlineDictionaryFragment.sendRequest(searchTerm, findLanguage());
    }

    public int findPosForLang(String langLong) {
        String[] langs = getResources().getStringArray(R.array.languagesDictionary);
        for (int i = 0; i < langs.length; i++) {
            if (langs[i].toLowerCase().equals(langLong.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }


    private String findLanguage() {
        switch (spinnerLanguage.getSelectedIndex()) {
            case 0:
                return "en";
            case 1:
                return "fr";
            case 2:
                return "ru";
            case 3:
                return "de";
            case 4:
                return "es";
            case 5:
                return "tr";
            default:
                return "en";

        }
    }

    public Integer getImageResourceFlag() {
        switch (spinnerLanguage.getSelectedIndex()) {
            case 0:
                return R.drawable.grossbritannien_flagge;
            case 1:
                return R.drawable.flag_of_france;
            case 2:
                return R.drawable.flag_of_russia;
            case 3:
                return R.drawable.flag_of_germany;
            case 4:
                return R.drawable.flag_of_spain;
            case 5:
                return R.drawable.flag_of_turkey;
            default:
                return R.drawable.grossbritannien_flagge;

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.trim() != "") {
                    onlineDictionaryFragment.constraintLayout.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    sendRequest(query);
                } else {
                    Toast.makeText(DictionaryActivity.this, getString(R.string.type_more_letters), Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                closeKeyboard();
                return true;
            }
        });

        searchView.setIconified(false);
        searchView.setQuery(searchTerm, false);
        return true;
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(SEARCHTERM, searchTerm);
        outState.putParcelable(STATE_KEY, mSectionsPagerAdapter.saveState());
        super.onSaveInstanceState(outState);
    }

    private class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return onlineDictionaryFragment;
                case 1:
                    return RecentDictionaryFragment.newInstance();
                default:
                    return onlineDictionaryFragment;
            }

        }

        @Override
        public int getCount() {
            return 2;
        }
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

    class ColorCalculatingThread implements Runnable {
        private int position;
        private float positionOffset;

        ColorCalculatingThread(int position, float positionOffset) {
            this.position = position;
            this.positionOffset = positionOffset;
        }

        @Override
        public void run() {
            BigDecimal ratio;

            if (position == 0) {
                Integer u = offsetLimit1 / 2;
                BigDecimal bb = new BigDecimal(0.5 / u);
                ratio = new BigDecimal(1).divide(bb.setScale(16, RoundingMode.HALF_UP), 16, RoundingMode.HALF_UP);
            } else {
                Integer u = offsetLimit2 / 2;
                BigDecimal bb = new BigDecimal(0.5 / u);
                ratio = new BigDecimal(1).divide(bb.setScale(16, RoundingMode.HALF_UP), 16, RoundingMode.HALF_UP);
            }
            BigDecimal value = new BigDecimal(positionOffset).multiply(ratio);
            BigDecimal scaled = value.setScale(0, RoundingMode.HALF_UP);
            velocity = scaled.intValue() - previousOffset;
            if (velocity != 0) {
                if (velocity > 0 && position == 0) {
                    c1 = colorPage0;
                    c2 = colorPage1;
                    offsetLimit = setLim(Math.abs(
                            Integer.parseInt(
                                    c2.replace("#", ""), 16) - Integer.parseInt(c1.replace("#", ""), 16)));
                } else if (velocity < 0 && position == 0) {
                    c1 = colorPage1;
                    c2 = colorPage0;
                    offsetLimit = 0;
                } else if (velocity > 0 && position == 1) {
                    c1 = colorPage1;
                    c2 = colorPage2;
                    offsetLimit = setLim(Math.abs(
                            Integer.parseInt(
                                    c2.replace("#", ""), 16) - Integer.parseInt(c1.replace("#", ""), 16)));

                } else if (velocity < 0 && position == 1) {
                    c1 = colorPage2;
                    c2 = colorPage1;
                    offsetLimit = 0;
                }
                computeRatio(scaled.intValue(), velocity, offsetLimit);
                setTbColor();
            }
            previousOffset = scaled.intValue();
        }


        private void computeRatio(int currentOffset, int velocity, int offsetLimit) {
            Integer remainingOffset;
            if (velocity > 0) {
                remainingOffset = offsetLimit - currentOffset;
            } else {
                remainingOffset = offsetLimit + currentOffset;
            }
            Integer nR = remainingOffset / velocity;


            if (nR != 0) {
                r1 = (Integer.parseInt(c2.substring(1, 3), 16) - Integer.parseInt(currentColor.substring(1, 3), 16)) / Math.abs(nR);
                r2 = (Integer.parseInt(c2.substring(3, 5), 16) - Integer.parseInt(currentColor.substring(3, 5), 16)) / Math.abs(nR);
                r3 = (Integer.parseInt(c2.substring(5, 7), 16) - Integer.parseInt(currentColor.substring(5, 7), 16)) / Math.abs(nR);
            }
        }

        private void setTbColor() {
            String p1 = "#" + addSomeChars(Integer.toHexString(Integer.parseInt(currentColor.substring(1, 3), 16) + r1));
            String p2 = addSomeChars(Integer.toHexString(Integer.parseInt(currentColor.substring(3, 5), 16) + r2));
            String p3 = addSomeChars(Integer.toHexString(Integer.parseInt(currentColor.substring(5, 7), 16) + r3));
            final String color = (p1 + p2 + p3).substring(0, 7);
            currentColor.replace(0, 7, color);
            if (Integer.parseInt(c1.replace("#", ""), 16) < Integer.parseInt(c2.replace("#", ""), 16)) {
                if (Integer.parseInt(color.replace("#", ""), 16) > Integer.parseInt(c2.replace("#", ""), 16)) {
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.setBackgroundColor(Color.parseColor(color));
                            toolbar.setBackgroundColor(Color.parseColor(color));
                        }
                    });

                }
            } else if (Integer.parseInt(c1.replace("#", ""), 16) > Integer.parseInt(c2.replace("#", ""), 16)) {
                if (Integer.parseInt(color.replace("#", ""), 16) < Integer.parseInt(c2.replace("#", ""), 16)) {
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tabLayout.setBackgroundColor(Color.parseColor(color));
                            toolbar.setBackgroundColor(Color.parseColor(color));
                        }
                    });

                }
            }

        }


        private String addSomeChars(String s) {
            if (s.length() == 1) {
                return "0" + s;
            }
            return s;
        }
    }
}
