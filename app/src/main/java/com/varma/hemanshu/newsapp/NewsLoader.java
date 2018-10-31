package com.varma.hemanshu.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String LOG_TAG = NewsLoader.class.getSimpleName();
    private String url;

    public NewsLoader(Context context, String stringUrl) {
        super(context);
        url = stringUrl;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG,"Invoked onStartLoading");
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.i(LOG_TAG,"Invoked loadInBackground");
        if (url == null) {
            return null;
        }
        List<News> myListNews = QueryUtils.newsData(url);
        return myListNews;
    }
}
