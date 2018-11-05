package com.varma.hemanshu.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    View mProgressbar;
    TextView mEmptyTextView;

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int NEWS_LOADER_ID = 1;
    private static final String NEWS_BASE_URL = "https://content.guardianapis.com/search";

    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mProgressbar = findViewById(R.id.loading_indicator);
        mEmptyTextView = findViewById(R.id.empty_text_view);
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        ListView list = findViewById(R.id.list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        list.setAdapter(mAdapter);

        Log.i(LOG_TAG, "Invoked onCreate");
        LoaderManager loaderManager = getLoaderManager();
        if (isConnected) {
            Log.i(LOG_TAG, "Loader Manager Initiated");
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mEmptyTextView.setText(R.string.no_internet);
            mProgressbar.setVisibility(View.GONE);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News currentData = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentData.getUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                if (webIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(webIntent);

                }
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Invoked onCreateLoader");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String queryString = sharedPref.getString(getString(R.string.settings_query_key),
                getString(R.string.settings_query_value));
        String pageFeedSize = sharedPref.getString(getString(R.string.settings_page_feed_key),
                getString(R.string.settings_page_feed_value));

        Uri baseUrl = Uri.parse(NEWS_BASE_URL);
        Uri.Builder urlBuilder = baseUrl.buildUpon();
        urlBuilder.appendQueryParameter("q", queryString);
        urlBuilder.appendQueryParameter("format", "json");
        urlBuilder.appendQueryParameter("page-size", pageFeedSize);
        urlBuilder.appendQueryParameter("show-fields", "byline");
        urlBuilder.appendQueryParameter("api-key", getString(R.string.api_key));
        return new NewsLoader(this, urlBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsData) {
        Log.i(LOG_TAG, "Invoked onLoadFinished");
        mProgressbar.setVisibility(View.GONE);

        //Set this data if no results were found.
        mEmptyTextView.setText(R.string.no_news);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();
        if (newsData != null && !newsData.isEmpty()) {
            mAdapter.addAll(newsData);
            mEmptyTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        Log.i(LOG_TAG, "Invoked onLoaderReset");
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_action) {
            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
