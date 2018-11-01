package com.varma.hemanshu.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    private static final String REQUEST_URL = "https://content.guardianapis.com/search?format=json&page-size=20&show-fields=byline&api-key=55cad110-56bb-418b-92d5-49dbf9cfad2b";

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
        return new NewsLoader(this, REQUEST_URL);
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
}
