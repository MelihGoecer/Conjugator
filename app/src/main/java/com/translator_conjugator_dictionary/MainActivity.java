package com.translator_conjugator_dictionary;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.translator_conjugator_dictionary.UI.AllRecentTransActivity;
import com.translator_conjugator_dictionary.UI.ConjugationActivity;
import com.translator_conjugator_dictionary.UI.DictionaryActivity;
import com.translator_conjugator_dictionary.UI.SavedItemActivity;
import com.translator_conjugator_dictionary.ViewModels.MainActivityViewModel;
import com.translator_conjugator_dictionary.adapters.SpinnerAdapter;
import com.translator_conjugator_dictionary.adapters.TranslationRecentSearchAdapter;
import com.translator_conjugator_dictionary.models.SpinnerItem;
import com.translator_conjugator_dictionary.models.Translation;
import com.translator_conjugator_dictionary.utils.Constants;
import com.translator_conjugator_dictionary.utils.DatabaseHelper;
import com.translator_conjugator_dictionary.utils.TranslationUtils;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnClickListener, TranslationRecentSearchAdapter.OnRecentTransClickListener {
    //constants
    private static final String CURRENTITEM1 = "currentItem1";
    private static final String CURRENTITEM2 = "currentItem2";
    private static final String CURRENTPOS = "currentPosition";
    private static final String ALTLAYOUT = "altLayout";
    //widgets;
    private Spinner firstLanguage;
    private Spinner secondLanguage;
    private Spinner thirdLanguage;
    private TextInputEditText textInputEditText;
    private TextInputEditText textOutputEditText;
    private TextInputEditText textThirdEditText;
    private BottomNavigationView bottomNavigationView;
    private ConstraintLayout layout;
    private ImageView imageButtonSwapLangs;
    private ImageView imageButtonSwapLangs2;
    private MenuItem item;
    private ProgressBar progressBar;
    private TextView textViewRecognizer;
    private ImageView imageViewRecognizeSpeech;
    private ExpandableLayout el;
    private ImageView imageViewDropDown;
    private TextView textViewRecentTransTitle;
    private View separatorView;
    private InterstitialAd mInterstitialAd;
    private ViewStub stubImageButton;
    private ViewStub stubThirdSpinner;
    //vars
    private TranslationUtils translationUtils = new TranslationUtils();
    private String prevText;
    private String[] langArray;
    private TextWatcher textWatcher;
    private List<TextInputEditText> textInputEditTextList;
    private List<TextInputLayout> textInputLayoutList;
    private List<Spinner> spinnerList;
    private ArrayList<SpinnerItem> mList1;
    private ArrayList<SpinnerItem> mList2;
    private ArrayList<SpinnerItem> mList3;
    private byte currentPos = 0;
    private byte numberOfViews;
    private ConstraintSet constraintSetOld = new ConstraintSet();
    private MainActivityViewModel viewModel;
    private Boolean altLayout = false;
    private Boolean focus2 = false;
    private SpeechRecognizer mSpeechRecognizer;
    private Intent mSpeechRecognizerIntent;
    private Boolean popUpRecognizer = false;
    private DatabaseHelper mDb = new DatabaseHelper(this);
    private Boolean firstTimeDeleted = true;
    private String deletedText = "";
    private String deletedText1 = "";
    private String deletedText2 = "";
    private boolean stubInflated = false;
    private ConstraintSet constraintSet = new ConstraintSet();
    private int[] inflatedIds = new int[2];

    public void closePopUp(View v) {
        mSpeechRecognizer.stopListening();
        openOrClosePopUp(false);
        textViewRecognizer.setText(getString(R.string.speak_translator));
        textViewRecognizer.setClickable(false);
    }

    public void tryAgain(View v) {
        closeKeyboard();
        textViewRecognizer.setText(getString(R.string.speak_translator));
        textViewRecognizer.setClickable(false);
        mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButton_swap_langs) {
            SpinnerItem spinnerItem1 = (SpinnerItem) firstLanguage.getSelectedItem();
            SpinnerItem spinnerItem2 = (SpinnerItem) secondLanguage.getSelectedItem();
            firstLanguage.setSelection(findPosOfSpinnerItemInFirstSpinner(spinnerItem2));
            secondLanguage.setSelection(findPosOfSpinnerItemInSecondSpinner(spinnerItem1));
            Drawable d = imageButtonSwapLangs.getDrawable();

            if (d instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
                avd.start();
            } else if (d instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
                avd.start();
            }
            if (currentPos == 0 || currentPos == 1) {
                textInputEditTextList.get(getCurrentPos()).setText(textInputEditTextList.get(getOtherViewsPos().get(0)).getText().toString());
            } else if (currentPos == 2) {
                String s2 = textInputEditTextList.get(getOtherViewsPos().get(0)).getText().toString();
                textInputEditTextList.get(getOtherViewsPos().get(0)).setText(textInputEditTextList.get(getOtherViewsPos().get(1)).getText().toString());
                textInputEditTextList.get(getOtherViewsPos().get(1)).setText(s2);
            }
        } else if (view.getId() == inflatedIds[0]) {
            SpinnerItem spinnerItem3 = (SpinnerItem) secondLanguage.getSelectedItem();
            SpinnerItem spinnerItem4 = (SpinnerItem) thirdLanguage.getSelectedItem();
            secondLanguage.setSelection(findPosOfSpinnerItemInSecondSpinner(spinnerItem4));
            thirdLanguage.setSelection(findPosOfSpinnerItemInThirdSpinner(spinnerItem3));
            Drawable d = imageButtonSwapLangs2.getDrawable();
            if (d instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
                avd.start();
            } else if (d instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
                avd.start();
            }
            if (currentPos == 0) {
                String s2 = textInputEditTextList.get(getOtherViewsPos().get(0)).getText().toString();
                textInputEditTextList.get(getOtherViewsPos().get(0)).setText(textInputEditTextList.get(getOtherViewsPos().get(1)).getText().toString());
                textInputEditTextList.get(getOtherViewsPos().get(1)).setText(s2);
            } else if (currentPos == 1 || currentPos == 2) {
                textInputEditTextList.get(getCurrentPos()).setText(textInputEditTextList.get(getOtherViewsPos().get(1)).getText().toString());
            }
        }

    }

    private byte findPosOfSpinnerItemInFirstSpinner(SpinnerItem item) {
        for (byte i = 0; i < firstLanguage.getCount(); i++) {
            if (((SpinnerItem) firstLanguage.getItemAtPosition(i)).getLanguage().equals(item.getLanguage())) {
                return i;
            }
        }
        return -1;
    }

    private byte findPosOfSpinnerItemInSecondSpinner(SpinnerItem item) {
        for (byte i = 0; i < secondLanguage.getCount(); i++) {
            if (((SpinnerItem) secondLanguage.getItemAtPosition(i)).getLanguage().equals(item.getLanguage())) {
                return i;
            }
        }
        return -1;
    }

    private byte findPosOfSpinnerItemInThirdSpinner(SpinnerItem item) {
        for (byte i = 0; i < thirdLanguage.getCount(); i++) {
            if (((SpinnerItem) thirdLanguage.getItemAtPosition(i)).getLanguage().equals(item.getLanguage())) {
                return i;
            }
        }
        return -1;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_online_dictionary:
                    Intent intent1 = new Intent(MainActivity.this, DictionaryActivity.class);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    return true;
                case R.id.navigation_translator:

                    return true;
                case R.id.navigation_conjugation:
                    Intent intent2 = new Intent(MainActivity.this, ConjugationActivity.class);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;

                case R.id.navigation_marked:
                    Intent intent3 = new Intent(MainActivity.this, SavedItemActivity.class);
                    startActivity(intent3);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stubImageButton = findViewById(R.id.stub_imageButton_swapLangs);
        stubThirdSpinner = findViewById(R.id.stub_thirdSpinner);
        stubImageButton.setOnInflateListener(new ViewStub.OnInflateListener() {
            @Override
            public void onInflate(ViewStub viewStub, View view) {
                stubInflated = true;
                thirdLanguage = (Spinner) stubThirdSpinner.inflate();
                inflatedIds[1] = thirdLanguage.getId();
                spinnerList.add(thirdLanguage);
                SpinnerAdapter mAdapter3 = new SpinnerAdapter(MainActivity.this, mList3);
                thirdLanguage.setAdapter(mAdapter3);
                thirdLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        setLanguages(2);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
        });
        final ImageView i1 = findViewById(R.id.imageView_goto_conjugator);
        i1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConjugationActivity.class);
                intent.putExtra("MainActivity", "fromMainActivity");
                intent.putExtra("translatedText", textOutputEditText.getText().toString());
                startActivity(intent);
            }
        });

        final ImageView i2 = findViewById(R.id.imageView_goto_dictionary);
        i2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DictionaryActivity.class);
                intent.putExtra("MainActivity", "fromMainActivity");
                intent.putExtra("languageTrans", ((SpinnerItem) firstLanguage.getSelectedItem()).getLanguage());
                intent.putExtra("translatedText", textInputEditText.getText().toString());
                startActivity(intent);
            }
        });

        separatorView = findViewById(R.id.viewSeparator);
        RelativeLayout relativeLayoutShowAll = findViewById(R.id.relativeLayout_show_all_trans);
        relativeLayoutShowAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAd();
                Intent intent = new Intent(MainActivity.this, AllRecentTransActivity.class);
                startActivity(intent);
            }
        });
        imageViewDropDown = findViewById(R.id.imageView_dropdown_recent_translations);
        el = findViewById(R.id.expandable_layout_trans);
        final RecyclerView linearLayoutEL = findViewById(R.id.linearLayout_expandableLayout_translation);
        final List<Translation> translations = mDb.getFirstTenRecentTranslations();
        linearLayoutEL.setHasFixedSize(true);
        linearLayoutEL.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setInitialPrefetchItemCount(3);
        linearLayoutEL.setLayoutManager(linearLayoutManager);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                linearLayoutEL.setAdapter(new TranslationRecentSearchAdapter(translations,
                        MainActivity.this));
            }
        }, 2000);


        imageViewRecognizeSpeech = findViewById(R.id.imageView_recognize_speech);
        imageViewRecognizeSpeech.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    el.setExpanded(false);
                    textViewRecentTransTitle.setTextColor(getResources().getColor(R.color.black));
                    separatorView.setVisibility(View.VISIBLE);
                    imageViewDropDown.setRotation(0);
                    SpeechRecognitionListener listener = new SpeechRecognitionListener();
                    mSpeechRecognizer.setRecognitionListener(listener);
                    mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getAppropriateLocale());
                    ActivityCompat.requestPermissions
                            (MainActivity.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO},
                                    REQUEST_RECORD_PERMISSION);
                    if (popUpRecognizer && textViewRecognizer.getText().equals(getString(R.string.try_again))) {
                        textViewRecognizer.setText(getString(R.string.speak_translator));
                    }
                }
                return true;
            }
        });
        textViewRecognizer = findViewById(R.id.textView_recognized_text);
        textViewRecognizer.setClickable(false);

        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mSpeechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());

        progressBar = findViewById(R.id.progressBar_recognized_text);

        MobileAds.initialize(this, getString(R.string.ADMOB_APP_ID));

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.AD_UNIT_RECENT_TRANS));
        if (getSharedPreferences("sharedPrefsForAds", MODE_PRIVATE).getInt("counter", 0) == Constants.numberForPreparingAd) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.PrimaryDarkDarkBlue));
        }

        textViewRecentTransTitle = ((TextView) findViewById(R.id.textView_recent_translation_title));
        textViewRecentTransTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecentTrans();

            }
        });
        layout = findViewById(R.id.main_content);
        constraintSetOld.clone(layout);

        imageButtonSwapLangs = findViewById(R.id.imageButton_swap_langs);
        textInputEditText = findViewById(R.id.editText_input);
        textOutputEditText = findViewById(R.id.editText_output);
        textThirdEditText = findViewById(R.id.editText_third);
        textInputEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        textInputEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        textOutputEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        textOutputEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        textThirdEditText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        textThirdEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);
        TextInputLayout textOutputLayout = findViewById(R.id.textOutputLayout);
        TextInputLayout textThirdLayout = findViewById(R.id.textLayout_third);
        Toolbar toolbar = findViewById(R.id.MainActivity_toolbar);
        firstLanguage = findViewById(R.id.choose_spinner);
        secondLanguage = findViewById(R.id.target_spinner);
        bottomNavigationView = findViewById(R.id.navigation_translator);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        langArray = getResources().getStringArray(R.array.languages_array);
        viewModel = ViewModelProviders.of(MainActivity.this).get(MainActivityViewModel.class);
        viewModel.getmFirstTranslation().observe(MainActivity.this, observerTrans1);
        viewModel.getmSecondTranslation().observe(MainActivity.this, observerTrans2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        textInputEditTextList = new ArrayList<>();
        textInputLayoutList = new ArrayList<>();
        spinnerList = new ArrayList<>();

        textInputEditTextList.add(textInputEditText);
        textInputEditTextList.add(textOutputEditText);
        textInputEditTextList.add(textThirdEditText);

        textInputLayoutList.add(textInputLayout);
        textInputLayoutList.add(textOutputLayout);
        textInputLayoutList.add(textThirdLayout);

        spinnerList.add(firstLanguage);
        spinnerList.add(secondLanguage);

        initList();

        ArrayList<SpinnerItem> spinnerItemsInitial = new ArrayList<>();
        spinnerItemsInitial.add(new SpinnerItem(getString(R.string.language_german),
                R.drawable.flag_of_germany));
        SpinnerAdapter mAdapter1Initial = new SpinnerAdapter(MainActivity.this, spinnerItemsInitial);
        firstLanguage.setAdapter(mAdapter1Initial);

        textInputLayout.setHint(getString(R.string.language_german));
        textOutputLayout.setHint(getString(R.string.language_english));

        final SpinnerAdapter mAdapter1 = new SpinnerAdapter(MainActivity.this, mList1);
        final SpinnerAdapter mAdapter2 = new SpinnerAdapter(MainActivity.this, mList2);

        firstLanguage.setAdapter(mAdapter1);
        firstLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setLanguages(0);
                String s = "";
                for (TextInputEditText inputEditText : textInputEditTextList) {
                    if (inputEditText.getText().toString() != null) {
                        s = inputEditText.getText().toString();
                        break;
                    }
                }
                translate(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        secondLanguage.setAdapter(mAdapter2);
        secondLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                setLanguages(1);
                if (!focus2) {
                    focus2 = true;
                } else {
                    SpinnerItem spinnerItem = (SpinnerItem) spinnerList.get(getCurrentPos()).getSelectedItem();
                    SpinnerItem spinnerItem1 = (SpinnerItem) spinnerList.get(getOtherViewsPos().get(0)).getSelectedItem();
                    viewModel.sendRequest(textInputEditText.getText().toString(),
                            convertLanguage(spinnerItem.getLanguage()),
                            convertLanguage(spinnerItem1.getLanguage()),
                            "nmt", 0, MainActivity.this);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if (!focus2) {
                    focus2 = true;
                } else {
                    SpinnerItem spinnerItem = (SpinnerItem) spinnerList.get(getCurrentPos()).getSelectedItem();
                    SpinnerItem spinnerItem1 = (SpinnerItem) spinnerList.get(getOtherViewsPos().get(0)).getSelectedItem();
                    viewModel.sendRequest(textInputEditText.getText().toString(),
                            convertLanguage(spinnerItem.getLanguage()),
                            convertLanguage(spinnerItem1.getLanguage()),
                            "nmt", 0, MainActivity.this);
                }
            }
        });


        imageButtonSwapLangs.setOnClickListener(this);
        numberOfViews = 2;


        if (savedInstanceState != null) {
            currentPos = savedInstanceState.getByte("currentPosition");
            setTextListener(textInputEditTextList.get(currentPos));
            altLayout = savedInstanceState.getBoolean("altLayout");

            if (altLayout) {
                inflateStubView();
                prepareConstraintSets(constraintSet);
                constraintSet.applyTo(layout);
            } else {
                constraintSetOld.applyTo(layout);
            }

            openOrClosePopUp(savedInstanceState.getBoolean("popup"));
        }

    }

    private ObjectAnimator changeRotate(ImageView v, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    public void expandView(View v) {
        showRecentTrans();
    }

    private void showRecentTrans() {
        el.toggle();
        if (el.isExpanded()) {
            textViewRecentTransTitle.setTextColor(getResources().getColor(R.color.darkGrey));
            changeRotate(imageViewDropDown, 0, 180).start();
            separatorView.setVisibility(View.GONE);
        } else {
            textViewRecentTransTitle.setTextColor(getResources().getColor(R.color.black));
            changeRotate(imageViewDropDown, 180, 0).start();
            separatorView.setVisibility(View.VISIBLE);
        }
    }

    View.OnKeyListener onKeyListener3 = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        closeKeyboard();
                        String s = textThirdEditText.getText().toString().trim();
                        if (!s.equals("")) {
                            int spaceCount = 0;
                            for (int k = 0; k < s.length(); k++) {
                                if (String.valueOf(s.charAt(k)).equals(" ")) {
                                    spaceCount++;
                                }
                            }
                            if (spaceCount < 3) {
                                if (altLayout) {
                                    mDb.addDataTranslation("translations", s
                                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                            , textInputEditTextList.get(getOtherViewsPos().get(1)).getEditableText().toString()
                                            , getResources().getStringArray(R.array.languages_array_reverse2)[thirdLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase());
                                } else {
                                    mDb.addDataTranslation("translations", s
                                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                            , ""
                                            , getResources().getStringArray(R.array.languages_array_reverse2)[thirdLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                            , "");

                                }


                            }
                        }
                        return true;
                    default:
                        return true;
                }
            }
            return false;
        }
    };

    View.OnKeyListener onKeyListener1 = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        closeKeyboard();
                        String s = textInputEditText.getText().toString().trim();
                        if (!s.equals("")) {
                            int spaceCount = 0;
                            for (int k = 0; k < s.length(); k++) {
                                if (String.valueOf(s.charAt(k)).equals(" ")) {
                                    spaceCount++;
                                }
                            }
                            if (spaceCount < 3) {
                                if (altLayout) {
                                    mDb.addDataTranslation("translations", s
                                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                            , textInputEditTextList.get(getOtherViewsPos().get(1)).getEditableText().toString()
                                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array_reverse2)[thirdLanguage.getSelectedItemPosition()].toLowerCase());
                                } else {
                                    mDb.addDataTranslation("translations", s
                                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                            , ""
                                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                                            , "");
                                }
                            }
                        }
                        return true;
                    default:
                        return true;
                }
            }
            return false;
        }
    };

    View.OnKeyListener onKeyListener2 = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            if (keyEvent.getAction() == KeyEvent.ACTION_UP) {
                switch (i) {
                    case KeyEvent.KEYCODE_ENTER:
                        closeKeyboard();
                        String s = textOutputEditText.getText().toString().trim();
                        if (!s.equals("")) {
                            int spaceCount = 0;
                            for (int k = 0; k < s.length(); k++) {
                                if (String.valueOf(s.charAt(k)).equals(" ")) {
                                    spaceCount++;
                                }
                            }
                            if (spaceCount < 3) {
                                if (altLayout) {
                                    mDb.addDataTranslation("translations", s
                                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                            , textInputEditTextList.get(getOtherViewsPos().get(1)).getEditableText().toString()
                                            , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array_reverse2)[thirdLanguage.getSelectedItemPosition()].toLowerCase());
                                } else {
                                    mDb.addDataTranslation("translations", s
                                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                            , ""
                                            , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                            , "");
                                }

                            }
                        }
                }
            }
            return false;
        }
    };

    public void clearText(View v) {
        String s = textInputEditText.getText().toString().trim();
        if (!s.equals("")) {
            int spaceCount = 0;
            for (int k = 0; k < s.length(); k++) {
                if (String.valueOf(s.charAt(k)).equals(" ")) {
                    spaceCount++;
                }
            }
            if (spaceCount < 3) {
                if (altLayout) {
                    mDb.addDataTranslation("translations", s
                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                            , textInputEditTextList.get(getOtherViewsPos().get(1)).getEditableText().toString()
                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                            , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                            , getResources().getStringArray(R.array.languages_array_reverse2)[thirdLanguage.getSelectedItemPosition()].toLowerCase());
                } else {
                    mDb.addDataTranslation("translations", s
                            , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                            , ""
                            , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                            , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                            , "");
                }
            }
            textInputEditText.setText("");
            textOutputEditText.setText("");
            if (altLayout) {
                textThirdEditText.setText("");
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
    }

    private void sendRequestAfterConjLeft() {
        textInputEditText.setText(getIntent().getStringExtra("conj"));
        String[] langs = getResources().getStringArray(R.array.languages_array);
        for (int i = 0; i < langs.length; i++) {
            if (langs[i].toLowerCase().equals(getIntent().getStringExtra("languageConj"))) {
                firstLanguage.setSelection(i);
                break;
            }
        }
        secondLanguage.performClick();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        textInputEditText.setOnFocusChangeListener(MainActivity.this);
        textOutputEditText.setOnFocusChangeListener(MainActivity.this);
        textThirdEditText.setOnFocusChangeListener(MainActivity.this);
    }

    Observer<String> observerTrans1 = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if (s.contains("&#39;")) {
                s = s.replace("&#39;", "'");
            }
            textInputEditTextList.get(getOtherViewsPos().get(0)).setText(s);
        }
    };
    Observer<String> observerTrans2 = new Observer<String>() {
        @Override
        public void onChanged(String s) {
            if (s.contains("&#39;")) {
                s = s.replace("&#39;", "'");
            }
            textInputEditTextList.get(getOtherViewsPos().get(1)).setText(s);
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if ("fromConjugator".equals(getIntent().getStringExtra("ConjugationActivity"))) {
                sendRequestAfterConjLeft();
                getIntent().removeExtra("ConjugationActivity");
            }
            if ("fromAllRecentTrans".equals(getIntent().getStringExtra("AllRecentTransActivity"))) {
                substituteTranslationFields(mDb.getRequestedItem(getIntent().getStringExtra("timestampOfClickedItem")));
                getIntent().removeExtra("AllRecentTransActivity");
            }
        }
    }


    private void initList() {
        mList1 = new ArrayList<>(translationUtils.fillSpinner(getResources().getStringArray(R.array.languages_array),
                getResources().getStringArray(R.array.languages_array_reverse),
                getResources().getStringArray(R.array.languages_array_reverse2)).get(0));
        mList2 = new ArrayList<>(translationUtils.fillSpinner(getResources().getStringArray(R.array.languages_array),
                getResources().getStringArray(R.array.languages_array_reverse),
                getResources().getStringArray(R.array.languages_array_reverse2)).get(1));
        mList3 = new ArrayList<>(translationUtils.fillSpinner(getResources().getStringArray(R.array.languages_array),
                getResources().getStringArray(R.array.languages_array_reverse),
                getResources().getStringArray(R.array.languages_array_reverse2)).get(2));
    }

    private void setLanguages(int whichSpinner) {
        SpinnerItem spinnerItem = (SpinnerItem) spinnerList.get(whichSpinner).getSelectedItem();
        textInputLayoutList.get(whichSpinner).setHint(spinnerItem.getLanguage());
    }


    private void setTextListener(TextInputEditText e) {

        e.addTextChangedListener(textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                prevText = charSequence.toString().trim();
                if (i1 > i2 && firstTimeDeleted) {
                    deletedText = charSequence.toString();
                    deletedText1 = textInputEditTextList.get(getOtherViewsPos().get(0)).getText().toString();
                    deletedText2 = textInputEditTextList.get(getOtherViewsPos().get(1)).getText().toString();
                    firstTimeDeleted = false;
                } else if (i1 > i2) {
                    if (i2 == 0 && i == 0 && !deletedText.trim().equals("")) {
                        if (altLayout) {
                            mDb.addDataTranslation("translations", deletedText
                                    , deletedText1
                                    , deletedText2
                                    , getStringLanguagesArray(getCurrentPos())[spinnerList.get(getCurrentPos()).getSelectedItemPosition()].toLowerCase()
                                    , getStringLanguagesArray(getOtherViewsPos().get(0))[spinnerList.get(getOtherViewsPos().get(0)).getSelectedItemPosition()].toLowerCase()
                                    , getStringLanguagesArray(getOtherViewsPos().get(1))[spinnerList.get(getOtherViewsPos().get(1)).getSelectedItemPosition()].toLowerCase());
                        } else {
                            mDb.addDataTranslation("translations", deletedText
                                    , deletedText1
                                    , ""
                                    , getStringLanguagesArray(getCurrentPos())[spinnerList.get(getCurrentPos()).getSelectedItemPosition()].toLowerCase()
                                    , getStringLanguagesArray(getOtherViewsPos().get(0))[spinnerList.get(getOtherViewsPos().get(0)).getSelectedItemPosition()].toLowerCase()
                                    , "");

                        }

                    }
                    if (i1 < i2) {
                        firstTimeDeleted = true;
                    }

                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().equals("") && !charSequence.toString().trim().equals(prevText)) {
                    translate(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (textInputEditTextList.get(getCurrentPos()).getText().toString().trim().equals("")) {
                    textInputEditTextList.get(getOtherViewsPos().get(0)).setText("");
                    textInputEditTextList.get(getOtherViewsPos().get(1)).setText("");
                }
            }
        });

    }

    private void translate(CharSequence charSequence) {
        SpinnerItem spinnerItem = (SpinnerItem) spinnerList.get(getCurrentPos()).getSelectedItem();
        SpinnerItem spinnerItem1 = (SpinnerItem) spinnerList.get(getOtherViewsPos().get(0)).getSelectedItem();
        viewModel.sendRequest(charSequence.toString(),
                convertLanguage(spinnerItem.getLanguage()),
                convertLanguage(spinnerItem1.getLanguage()),
                "nmt", 0, MainActivity.this);
        if (altLayout) {
            SpinnerItem spinnerItem2 = (SpinnerItem) spinnerList.get(getOtherViewsPos().get(1)).getSelectedItem();
            viewModel.sendRequest(charSequence.toString(),
                    convertLanguage(spinnerItem.getLanguage()),
                    convertLanguage(spinnerItem2.getLanguage()),
                    "nmt", 1, MainActivity.this);
        }
    }

    private byte getCurrentPos() {
        return currentPos;
    }


    private String convertLanguage(String pLang) {
        String langShort = "";
        for (int i = 0; i < TranslationUtils.LANGUAGES2.length; i++) {
            if (pLang.equals(langArray[i])) {
                langShort = TranslationUtils.LANGUAGES2[i];
                break;
            }

        }
        return langShort;
    }

    private List<Byte> getOtherViewsPos() {
        List<Byte> posOfOtherViews = new ArrayList<>();

        for (byte i = 0; i <= numberOfViews; i++) {
            if (getCurrentPos() != i) {
                posOfOtherViews.add(i);
            }
        }

        return posOfOtherViews;
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        switch (view.getId()) {
            case R.id.editText_input:
                setLanguages(0);
                currentPos = 0;
                if (b) {
                    setTextListener(textInputEditText);
                    textInputEditText.setOnKeyListener(onKeyListener1);

                } else {
                    textInputEditText.removeTextChangedListener(textWatcher);
                    textInputEditText.setOnKeyListener(null);
                }

                break;

            case R.id.editText_output:

                currentPos = 1;
                if (b) {
                    setTextListener(textOutputEditText);
                    textOutputEditText.setOnKeyListener(onKeyListener2);
                } else {
                    textOutputEditText.removeTextChangedListener(textWatcher);
                    textOutputEditText.setOnKeyListener(null);
                }

                break;

            case R.id.editText_third:

                currentPos = 2;
                if (b) {
                    setTextListener(textThirdEditText);
                    textThirdEditText.setOnKeyListener(onKeyListener3);
                } else {
                    textThirdEditText.removeTextChangedListener(textWatcher);
                    textThirdEditText.setOnKeyListener(null);
                }

                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        textInputEditText.removeTextChangedListener(textWatcher);
        textOutputEditText.removeTextChangedListener(textWatcher);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.translator_menu, menu);
        this.item = menu.findItem(R.id.add_field);
        if (altLayout) {
            item.setIcon(R.drawable.ic_remove_black_24dp);
        } else {
            item.setIcon(R.drawable.ic_add_black_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        openThirdTranslationField();
        return true;
    }


    private void prepareConstraintSets(ConstraintSet constraintSet) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            //imageButton
            constraintSet.setVisibility(R.id.textLayout_third, ConstraintSet.VISIBLE);
            constraintSet.connect(R.id.target_spinner, ConstraintSet.END, inflatedIds[0], ConstraintSet.START);
            constraintSet.connect(inflatedIds[0], ConstraintSet.END, inflatedIds[1], ConstraintSet.START);
            constraintSet.connect(inflatedIds[0], ConstraintSet.START, R.id.target_spinner,
                    ConstraintSet.END);
            constraintSet.connect(inflatedIds[0], ConstraintSet.TOP, R.id.main_content,
                    ConstraintSet.TOP);
            constraintSet.setMargin(inflatedIds[0], ConstraintSet.END,
                    (int) getResources().getDimension(R.dimen.swapLangImageBtnMarg));
            constraintSet.setMargin(inflatedIds[0], ConstraintSet.START,
                    (int) getResources().getDimension(R.dimen.swapLangImageBtnMarg));
            constraintSet.setMargin(inflatedIds[0], ConstraintSet.TOP, (int) getResources().getDimension(R.dimen.padd_10));
            constraintSet.constrainHeight(textInputLayoutList.get(0).getId(),
                    (int) getResources().getDimension(R.dimen.textLayout));
            constraintSet.constrainHeight(textInputLayoutList.get(1).getId(),
                    (int) getResources().getDimension(R.dimen.textLayout));
            constraintSet.constrainHeight(textInputLayoutList.get(2).getId(),
                    (int) getResources().getDimension(R.dimen.textLayoutThird));
            constraintSet.constrainWidth(inflatedIds[0], (int) getResources().getDimension(R.dimen.swapLangImageBtn));
            constraintSet.constrainHeight(inflatedIds[0],
                    (int) getResources().getDimension(R.dimen.swapLangImageBtn));
//thirdSpinner
            constraintSet.connect(inflatedIds[0], ConstraintSet.END, inflatedIds[1], ConstraintSet.START);
            constraintSet.connect(inflatedIds[1], ConstraintSet.START, inflatedIds[0], ConstraintSet.END);
            constraintSet.connect(inflatedIds[1], ConstraintSet.END, R.id.main_content, ConstraintSet.END);
            constraintSet.connect(inflatedIds[1], ConstraintSet.TOP, R.id.main_content, ConstraintSet.TOP);
            constraintSet.setMargin(inflatedIds[1], ConstraintSet.TOP,
                    (int) getResources().getDimension(R.dimen.appbar_padding_top));
            constraintSet.setHorizontalWeight(inflatedIds[0], 1);
            constraintSet.setHorizontalWeight(inflatedIds[1], 1);
            constraintSet.constrainWidth(inflatedIds[1],
                    (int) getResources().getDimension(R.dimen.no));
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //imageButton
            constraintSet.setVisibility(R.id.textLayout_third, ConstraintSet.VISIBLE);
            constraintSet.connect(R.id.target_spinner, ConstraintSet.END, inflatedIds[0], ConstraintSet.START);
            constraintSet.connect(inflatedIds[0], ConstraintSet.END, inflatedIds[1], ConstraintSet.START);
            constraintSet.connect(inflatedIds[0], ConstraintSet.START, R.id.target_spinner,
                    ConstraintSet.END);
            constraintSet.connect(inflatedIds[0], ConstraintSet.TOP, R.id.main_content,
                    ConstraintSet.TOP);
            constraintSet.connect(R.id.textLayout_third, ConstraintSet.TOP, inflatedIds[1], ConstraintSet.BOTTOM);
            constraintSet.setMargin(inflatedIds[0], ConstraintSet.END,
                    (int) getResources().getDimension(R.dimen.swapLangImageBtnMarg));
            constraintSet.setMargin(inflatedIds[0], ConstraintSet.START,
                    (int) getResources().getDimension(R.dimen.swapLangImageBtnMarg));
            constraintSet.setMargin(inflatedIds[0], ConstraintSet.TOP, (int) getResources().getDimension(R.dimen.padd_10));
            constraintSet.constrainHeight(textInputLayoutList.get(2).getId(),
                    (int) getResources().getDimension(R.dimen.textLayout));
            constraintSet.constrainWidth(inflatedIds[0], (int) getResources().getDimension(R.dimen.swapLangImageBtn));
            constraintSet.constrainHeight(inflatedIds[0],
                    (int) getResources().getDimension(R.dimen.swapLangImageBtn));
//thirdSpinner
            constraintSet.connect(inflatedIds[0], ConstraintSet.END, inflatedIds[1], ConstraintSet.START);
            constraintSet.connect(inflatedIds[1], ConstraintSet.START, inflatedIds[0], ConstraintSet.END);
            constraintSet.connect(inflatedIds[1], ConstraintSet.END, R.id.main_content, ConstraintSet.END);
            constraintSet.connect(inflatedIds[1], ConstraintSet.TOP, R.id.main_content, ConstraintSet.TOP);
            constraintSet.setMargin(inflatedIds[1], ConstraintSet.TOP,
                    (int) getResources().getDimension(R.dimen.appbar_padding_top));
            constraintSet.setHorizontalWeight(inflatedIds[0], 1);
            constraintSet.setHorizontalWeight(inflatedIds[1], 1);
            constraintSet.constrainWidth(inflatedIds[1],
                    (int) getResources().getDimension(R.dimen.no));
        }

    }

    private void inflateStubView() {
        imageButtonSwapLangs2 = (ImageView) stubImageButton.inflate();
        imageButtonSwapLangs2.setOnClickListener(this);
        inflatedIds[0] = imageButtonSwapLangs2.getId();
        constraintSet.clone(layout);
    }

    private void openThirdTranslationField() {
        Transition changeBounds = new ChangeBounds();
        changeBounds.setInterpolator(new OvershootInterpolator());

        TransitionManager.beginDelayedTransition(layout, changeBounds);
        if (!altLayout) {
            if (!stubInflated) {
                inflateStubView();
            }
            prepareConstraintSets(constraintSet);

            if (popUpRecognizer) {
                constraintSet.setVisibility(R.id.cardView_popup, ConstraintSet.VISIBLE);
                constraintSet.setHorizontalWeight(R.id.cardView_popup, 1);
                constraintSet.setHorizontalWeight(R.id.recyclerView_recent_searches, 0);
                constraintSet.setVisibility(R.id.recyclerView_recent_searches, ConstraintSet.GONE);
            } else {
                constraintSet.setVisibility(R.id.cardView_popup, ConstraintSet.INVISIBLE);
                constraintSet.setHorizontalWeight(R.id.cardView_popup, 0);
                constraintSet.setHorizontalWeight(R.id.recyclerView_recent_searches, 1);
                constraintSet.setVisibility(R.id.recyclerView_recent_searches, ConstraintSet.VISIBLE);
            }

            constraintSet.applyTo(layout);
            startAnimVectorDraw(altLayout);
            altLayout = true;
        } else {
            startAnimVectorDraw(altLayout);
            if (popUpRecognizer) {
                constraintSetOld.setVisibility(R.id.cardView_popup, ConstraintSet.VISIBLE);
                constraintSetOld.setHorizontalWeight(R.id.cardView_popup, 1);
                constraintSetOld.setHorizontalWeight(R.id.recyclerView_recent_searches, 0);
                constraintSetOld.setVisibility(R.id.recyclerView_recent_searches, ConstraintSet.GONE);
            } else {
                constraintSetOld.setVisibility(R.id.cardView_popup, ConstraintSet.INVISIBLE);
                constraintSetOld.setHorizontalWeight(R.id.cardView_popup, 0);
                constraintSetOld.setHorizontalWeight(R.id.recyclerView_recent_searches, 1);
                constraintSetOld.setVisibility(R.id.recyclerView_recent_searches, ConstraintSet.VISIBLE);
            }
            constraintSetOld.applyTo(layout);
            altLayout = false;
        }
    }

    public void startAnimVectorDraw(boolean b) {
        if (!b) {
            item.setEnabled(false);
            item.setIcon(R.drawable.from_plus_to_minus);
            Drawable d = item.getIcon();
            if (d instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
                avd.start();
            } else if (d instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
                avd.start();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    item.setIcon(R.drawable.ic_remove_black_24dp);
                    item.setEnabled(true);
                }
            }, 700);


        } else {
            item.setEnabled(false);
            item.setIcon(R.drawable.from_minus_to_plus);
            Drawable d = item.getIcon();
            if (d instanceof AnimatedVectorDrawableCompat) {
                AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) d;
                avd.start();
            } else if (d instanceof AnimatedVectorDrawable) {
                AnimatedVectorDrawable avd = (AnimatedVectorDrawable) d;
                avd.start();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    item.setIcon(R.drawable.ic_add_black_24dp);
                    item.setEnabled(true);
                }
            }, 700);

        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putByte(CURRENTPOS, currentPos);
        outState.putString(CURRENTITEM1, ((SpinnerItem) firstLanguage.getSelectedItem()).getLanguage());
        outState.putString(CURRENTITEM2, ((SpinnerItem) secondLanguage.getSelectedItem()).getLanguage());
        outState.putBoolean(ALTLAYOUT, altLayout);
        outState.putBoolean("popup", popUpRecognizer);

    }

    private static final int REQUEST_RECORD_PERMISSION = 100;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openOrClosePopUp(true);
                    closeKeyboard();
                    mSpeechRecognizer.startListening(mSpeechRecognizerIntent);

                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.permission_denied), Toast
                            .LENGTH_SHORT).show();
                }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private String getAppropriateLocale() {
        int i = firstLanguage.getSelectedItemPosition();
        switch (i) {
            case 0:
                return "de-DE";
            case 1:
                return "en-US";
            case 2:
                return "fr-FR";
            case 3:
                return "tr-TR";
            case 4:
                return "ru-RU";
            case 5:
                return "es-ES";
            case 6:
                return "it-IT";
            case 7:
                return "pl-PL";
            case 8:
                return "fi-FI";
            case 9:
                return "da-DK";
            case 10:
                return "el-GR";
            case 11:
                return "nn-NO";
            case 12:
                return "nl-NL";
            case 13:
                return "hr-HR";
            case 14:
                return "ar-SA";
            case 15:
                return "mn-MN";
            case 16:
                return "pt-PT";
            case 17:
                return "zh-CN";
            default:
                return "en-US";
        }

    }

    private void openOrClosePopUp(Boolean i) {
        ConstraintSet constraintSetOld = new ConstraintSet();
        ConstraintSet constraintSetNew = new ConstraintSet();
        constraintSetOld.clone(layout);
        constraintSetOld.setVisibility(R.id.cardView_popup, ConstraintSet.INVISIBLE);
        constraintSetOld.setHorizontalWeight(R.id.cardView_popup, 0);
        constraintSetOld.setHorizontalWeight(R.id.recyclerView_recent_searches, 1);
        constraintSetOld.setVisibility(R.id.recyclerView_recent_searches, ConstraintSet.VISIBLE);
        constraintSetNew.clone(layout);
        constraintSetNew.setVisibility(R.id.cardView_popup, ConstraintSet.VISIBLE);
        constraintSetNew.setHorizontalWeight(R.id.cardView_popup, 1);
        constraintSetNew.setHorizontalWeight(R.id.recyclerView_recent_searches, 0);
        constraintSetNew.setVisibility(R.id.recyclerView_recent_searches, ConstraintSet.GONE);
        Transition changeBounds = new ChangeBounds();
        changeBounds.setInterpolator(new LinearInterpolator());

        TransitionManager.beginDelayedTransition(layout, changeBounds);
        if (i) {
            popUpRecognizer = true;
            imageViewRecognizeSpeech.setImageResource(R.drawable.ic_mic_filled);
            constraintSetNew.applyTo(layout);
        } else {
            popUpRecognizer = false;
            imageViewRecognizeSpeech.setImageResource(R.drawable.ic_mic_none);
            constraintSetOld.applyTo(layout);
        }


    }

    private String[] getStringLanguagesArray(int p) {
        switch (p) {
            case 0:
                return getResources().getStringArray(R.array.languages_array);
            case 1:
                return getResources().getStringArray(R.array.languages_array_reverse);
            case 2:
                return getResources().getStringArray(R.array.languages_array_reverse2);
        }
        return null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer.destroy();
        }
    }

    @Override
    public void onRecentTransClick(int position) {
        List<Translation> translationList = new ArrayList<>(mDb.getFirstTenRecentTranslations());
        Translation clickedItem = translationList.get(position);
        for (TextInputEditText t : textInputEditTextList) {
            t.clearFocus();
        }
        substituteTranslationFields(clickedItem);

    }

    private void substituteTranslationFields(Translation clickedItem) {
        if (clickedItem.getThirdResult().equals("")) {
            textInputEditText.setText(clickedItem.getHeader());
            textOutputEditText.setText(clickedItem.getResult());
            firstLanguage.setSelection(TranslationUtils.findPosOfLang(clickedItem.getSource(), getResources().getStringArray(R.array.languages_array)));
            secondLanguage.setSelection(TranslationUtils.findPosOfLang(clickedItem.getTarget(), getResources().getStringArray(R.array.languages_array_reverse)));
        } else {
            if (!altLayout) {
                openThirdTranslationField();
            }
            textInputEditText.setText(clickedItem.getHeader());
            textOutputEditText.setText(clickedItem.getResult());
            textThirdEditText.setText(clickedItem.getThirdResult());
            firstLanguage.setSelection(TranslationUtils.findPosOfLang(clickedItem.getSource(), getResources().getStringArray(R.array.languages_array)));
            secondLanguage.setSelection(TranslationUtils.findPosOfLang(clickedItem.getTarget(), getResources().getStringArray(R.array.languages_array_reverse)));
            thirdLanguage.setSelection(TranslationUtils.findPosOfLang(clickedItem.getThirdLanguage(), getResources().getStringArray(R.array.languages_array_reverse2)));
        }
    }

    class SpeechRecognitionListener implements RecognitionListener {

        public SpeechRecognitionListener() {
        }

        @Override
        public void onBeginningOfSpeech() {
            progressBar.setIndeterminate(false);
            progressBar.setMax(10);
            textInputEditText.clearFocus();
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
        }

        @Override
        public void onEndOfSpeech() {
            progressBar.setIndeterminate(true);
        }

        @Override
        public void onError(int errorCode) {
        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
        }

        @Override
        public void onPartialResults(Bundle arg0) {
            ArrayList<String> matches = arg0.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = matches.get(0);

            textInputEditText.setText(text);
            translate(text);
        }

        @Override
        public void onReadyForSpeech(Bundle arg0) {
        }

        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = matches.get(0);

            textInputEditText.setFocusableInTouchMode(true);
            textInputEditText.requestFocus();
            textInputEditText.setText(text);
            textViewRecognizer.setText(getString(R.string.try_again));
            textViewRecognizer.setClickable(true);
            String s = text.trim();
            if (!s.equals("")) {
                int spaceCount = 0;
                for (int k = 0; k < s.length(); k++) {
                    if (String.valueOf(s.charAt(k)).equals(" ")) {
                        spaceCount++;
                    }
                }
                if (spaceCount < 3) {
                    if (altLayout) {
                        mDb.addDataTranslation("translations", text
                                , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                , textInputEditTextList.get(getOtherViewsPos().get(1)).getEditableText().toString()
                                , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                                ,
                                getResources().getStringArray(R.array.languages_array_reverse2)[thirdLanguage.getSelectedItemPosition()].toLowerCase());
                    } else {

                        mDb.addDataTranslation("translations", text
                                , textInputEditTextList.get(getOtherViewsPos().get(0)).getEditableText().toString()
                                , ""
                                , getResources().getStringArray(R.array.languages_array)[firstLanguage.getSelectedItemPosition()].toLowerCase()
                                , getResources().getStringArray(R.array.languages_array_reverse)[secondLanguage.getSelectedItemPosition()].toLowerCase()
                                , "");
                    }

                }
            }

        }

        @Override
        public void onRmsChanged(float rmsdB) {
            progressBar.setProgress((int) rmsdB);
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
}


