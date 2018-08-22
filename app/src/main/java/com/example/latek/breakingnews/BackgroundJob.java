package com.example.latek.breakingnews;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BackgroundJob extends AsyncTaskLoader<List<NewsKeys>> {
    private String newsUrl;
    public BackgroundJob(Context context, String url) {
        super(context);
        newsUrl = url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Override
    public List<NewsKeys> loadInBackground() {
        if (newsUrl == null) {
            return null;
        }
        List<NewsKeys> news = Queries.fetchNewsData(newsUrl);
        return news;
    }
}

