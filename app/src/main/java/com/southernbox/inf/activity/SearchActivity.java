package com.southernbox.inf.activity;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.southernbox.inf.R;

public class SearchActivity extends BaseActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initToolbar();
    }

    private void initToolbar() {
//        mToolbar = (Toolbar) findViewById(R.id.search_toolbar);
//        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//        }
//        mToolbar.post(new Runnable() {
//            @Override
//            public void run() {
//
//                mToolbar.setTitle("");
//
//                MenuItem searchItem = mToolbar.getMenu().getItem(0);
//                SearchView searchView = (SearchView) searchItem.getActionView();
//                searchView.setIconifiedByDefault(false);
//            }
//        });
        SearchView searchView = (SearchView) findViewById(R.id.search_view);

        EditText etSearch = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

        Resources.Theme theme = getTheme();
        TypedValue colorPrimary = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimary, colorPrimary, true);
        TypedValue colorPrimaryDark = new TypedValue();
        theme.resolveAttribute(R.attr.colorPrimaryDark, colorPrimaryDark, true);
        TypedValue colorAccent = new TypedValue();
        theme.resolveAttribute(R.attr.colorAccent, colorAccent, true);
        TypedValue colorBackground = new TypedValue();
        theme.resolveAttribute(R.attr.colorBackground, colorBackground, true);
        TypedValue darkTextColor = new TypedValue();
        theme.resolveAttribute(R.attr.darkTextColor, darkTextColor, true);
        TypedValue lightTextColor = new TypedValue();
        theme.resolveAttribute(R.attr.lightTextColor, lightTextColor, true);

        //设置提示文字及输入文字颜色
        etSearch.setHintTextColor(ContextCompat.getColor(this, darkTextColor.resourceId));
        etSearch.setTextColor(ContextCompat.getColor(this, lightTextColor.resourceId));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.action_search) {
//            startActivity(new Intent(this, SearchActivity.class));
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

}
