package com.translator_conjugator_dictionary.UI;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.azoft.carousellayoutmanager.CenterScrollListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.translator_conjugator_dictionary.MainActivity;
import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.adapters.SavedItemAdapter;
import com.translator_conjugator_dictionary.modelsConj.ConjugationFullAlternative;
import com.translator_conjugator_dictionary.utils.DatabaseHelper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SavedItemActivity extends AppCompatActivity implements View.OnClickListener, MenuItem.OnMenuItemClickListener {

    //widgets
    private BottomNavigationView navigation;
    private Toolbar toolbar;
    private int currentItemPos;
    private MenuItem menuItem;
    private SubMenu subMenu;
    private RecyclerView recyclerView;
    private DatabaseHelper mDb = new DatabaseHelper(this);
    private SavedItemAdapter savedItemAdapter;
    private CenterScrollListener centerScrollListener = new CenterScrollListener();
    private FrameLayout frameLayout;

    public static final String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m"
            , "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_item);

        AdView mAdView = findViewById(R.id.adView_favourites);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        assignVars();
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
    }

    private static final String TAG = "SavedItemActivity";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        addAccordingRV(item.getItemId());
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_saved_item, menu);
        return true;
    }

    private void addAccordingRV(final int i) {
        switch (i) {
            case R.id.menu_column:
            case 0:
                currentItemPos = 0;
                menuItem.setIcon(subMenu.getItem(0).getIcon());
                frameLayout.removeAllViews();
                recyclerView = new RecyclerView(this);
                frameLayout.addView(recyclerView, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(savedItemAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                break;
            case R.id.menu_grid:
            case 1:
                currentItemPos = 1;
                menuItem.setIcon(subMenu.getItem(1).getIcon());
                frameLayout.removeAllViews();
                recyclerView = new RecyclerView(this);
                frameLayout.addView(recyclerView, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(savedItemAdapter);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case R.id.menu_staggered:
            case 2:
                currentItemPos = 2;
                menuItem.setIcon(subMenu.getItem(2).getIcon());
                frameLayout.removeAllViews();
                recyclerView = new RecyclerView(this);
                frameLayout.addView(recyclerView, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(savedItemAdapter);
                StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                break;
            case R.id.menu_carousel:
            case 3:
                currentItemPos = 3;
                menuItem.setIcon(subMenu.getItem(3).getIcon());
                final CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, true);
                layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
                frameLayout.removeAllViews();
                recyclerView = new RecyclerView(this);
                frameLayout.addView(recyclerView, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(savedItemAdapter);
                recyclerView.addOnScrollListener(centerScrollListener);
                recyclerView.setLayoutManager(layoutManager);
                break;
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sp = getSharedPreferences("sharedPrefsSavedItem", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("currentItemPos", currentItemPos);
        editor.apply();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        addAccordingRV(menuItem.getItemId());
        return true;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_online_dictionary:

                    Intent intent1 = new Intent(SavedItemActivity.this, DictionaryActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent1, ActivityOptions.makeSceneTransitionAnimation(SavedItemActivity.this).toBundle());
                    } else {
                        startActivity(intent1);
                    }
                    return true;
                case R.id.navigation_translator:

                    Intent intent2 = new Intent(SavedItemActivity.this, MainActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent2, ActivityOptions.makeSceneTransitionAnimation(SavedItemActivity.this).toBundle());
                    } else {
                        startActivity(intent2);
                    }
                    return true;
                case R.id.navigation_conjugation:

                    Intent intent3 = new Intent(SavedItemActivity.this, ConjugationActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent3, ActivityOptions.makeSceneTransitionAnimation(SavedItemActivity.this).toBundle());
                    } else {
                        startActivity(intent3);
                    }
                    return true;
            }
            return false;
        }
    };
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private void assignVars() {
        frameLayout = findViewById(R.id.container_frame);
        toolbar = findViewById(R.id.toolbar_saved_item);
        recyclerView = new RecyclerView(this);
        frameLayout.addView(recyclerView, new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        recyclerView.setHasFixedSize(true);
        /*savedItemAdapter = new SavedItemAdapter(mDb.getAllSavedItems(), this);*/
        recyclerView.setAdapter(savedItemAdapter);
        currentItemPos = getSharedPreferences("sharedPrefsSavedItem", MODE_PRIVATE).getInt("currentItemPos", 0);
        menuItem = toolbar.getMenu().getItem(0);
        subMenu = menuItem.getSubMenu();
        for (int i = 0; i < subMenu.size(); i++) {
            subMenu.getItem(i).setOnMenuItemClickListener(this);
        }
        addAccordingRV(0);

    }

    public void readAllDocuments(View v) {
       /* db.collection("Translator").document("Words").collection("Conjugation")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult().getDocuments()) {
                                readAlphabeticallyOrderedDocument(document.getId());
                            }

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/


    }

    private void readAlphabeticallyOrderedDocument(String id) {
        for (String letter : alphabet) {
            db.collection("Translator").document("Words").collection("Conjugation-en")
                    .document(id).collection(letter).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            final ConjugationFullAlternative cfa =
                                    document.toObject(ConjugationFullAlternative.class);
                            final String text = cfa.getVerb() + ",";


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    writeToFile(text);
                                    Log.d(TAG, "onComplete: " + cfa.getVerb());
                                }
                            }, 400);


                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }

                }
            });

        }
    }

    private void writeToFile(String text) {
        try (FileWriter fw = new FileWriter(getFilesDir() + "/verbs_list_en.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.print(text);
        } catch (IOException e) {

        }
    }

}

