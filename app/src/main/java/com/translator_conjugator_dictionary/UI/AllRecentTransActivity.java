package com.translator_conjugator_dictionary.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.translator_conjugator_dictionary.MainActivity;
import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.adapters.TranslationRecentSearchAdapter;
import com.translator_conjugator_dictionary.models.Translation;
import com.translator_conjugator_dictionary.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllRecentTransActivity extends AppCompatActivity implements TranslationRecentSearchAdapter.OnRecentTransClickListener {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseHelper mDb = new DatabaseHelper(this);
    private TranslationRecentSearchAdapter translationRecentSearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_recent_trans);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.PrimaryDarkDarkBlue));
        }


        toolbar = findViewById(R.id.toolbar_all_recent_trans);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        recyclerView = findViewById(R.id.recyclerView_all_recent_trans);
        List<Translation> translations = mDb.getAllRecentTranslations();
        recyclerView.setHasFixedSize(true);
        recyclerView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        translationRecentSearchAdapter = new TranslationRecentSearchAdapter(translations, this);
        recyclerView.setAdapter(translationRecentSearchAdapter);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                mDb.clearTable("translations");
                translationRecentSearchAdapter.setmTranslations(new ArrayList<Translation>());
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recent_trans, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_search));
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setIconified(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                translationRecentSearchAdapter.setmTranslations(mDb.filterTranslations(s));
                return true;
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

        return true;

    }

    @Override
    public void onRecentTransClick(int position) {
        Intent intent = new Intent(AllRecentTransActivity.this, MainActivity.class);
        Translation translation = translationRecentSearchAdapter.getmTranslations().get(position);
        intent.putExtra("AllRecentTransActivity", "fromAllRecentTrans");
        intent.putExtra("timestampOfClickedItem", translation.getTimestamp());
        startActivity(intent);
    }


}
